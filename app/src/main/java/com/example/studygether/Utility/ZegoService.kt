package com.example.studygether.Utility

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import im.zego.zim.ZIM
import im.zego.zim.callback.*
import im.zego.zim.entity.*
import im.zego.zim.enums.*
import im.zego.zegoexpress.ZegoExpressEngine
import im.zego.zegoexpress.constants.ZegoScenario
import im.zego.zegoexpress.entity.ZegoEngineProfile
import im.zego.zegoexpress.entity.ZegoUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.ArrayList

sealed interface ZegoCallState {
    object Idle : ZegoCallState
    data class Incoming(val callId: String, val callerId: String) : ZegoCallState
    data class Outgoing(val callId: String, val inviteeId: String) : ZegoCallState
    data class Active(val callId: String, val roomId: String, val targetUserId: String) : ZegoCallState
}

object ZegoService {
    private const val TAG = "ZegoService"
    private var zim: ZIM? = null
    private var isInitialized = false
    private var currentUserId: String? = null
    
    private val _callState = MutableStateFlow<ZegoCallState>(ZegoCallState.Idle)
    val callState: StateFlow<ZegoCallState> = _callState.asStateFlow()

    private val _blockedUserIds = MutableStateFlow<Set<String>>(emptySet())
    val blockedUserIds: StateFlow<Set<String>> = _blockedUserIds.asStateFlow()

    private var onNewMessageReceived: ((String, ZIMMessage) -> Unit)? = null

    fun setOnNewMessageListener(listener: ((String, ZIMMessage) -> Unit)?) {
        this.onNewMessageReceived = listener
    }

    fun init(context: Context) {
        if (isInitialized) return
        if (ZegoConfig.isPlaceholder()) {
            Log.w(TAG, "Zego credentials are placeholders, skipping initialization.")
            return
        }
        try {
            
            val appConfig = ZIMAppConfig().apply {
                appID = ZegoConfig.APP_ID
                appSign = ZegoConfig.APP_SIGN
            }
            zim = ZIM.create(appConfig, context.applicationContext as Application)
            
            
            zim?.setEventHandler(object : ZIMEventHandler() {
                override fun onReceivePeerMessage(zim: ZIM?, messageList: ArrayList<ZIMMessage>?, fromUserID: String?) {
                    messageList?.forEach { msg ->
                        onNewMessageReceived?.invoke(fromUserID ?: "", msg)
                    }
                }

                override fun onPeerMessageReceived(zim: ZIM?, messageList: ArrayList<ZIMMessage>?, info: ZIMMessageReceivedInfo?, fromUserID: String?) {
                    messageList?.forEach { msg ->
                        onNewMessageReceived?.invoke(fromUserID ?: "", msg)
                    }
                }



                override fun onCallInvitationReceived(zim: ZIM?, info: ZIMCallInvitationReceivedInfo?, callID: String?) {
                    if (callID != null && info != null) {
                        _callState.value = ZegoCallState.Incoming(callID, info.caller)
                    }
                }

                override fun onCallInvitationCancelled(zim: ZIM?, info: ZIMCallInvitationCancelledInfo?, callID: String?) {
                    if (_callState.value is ZegoCallState.Incoming) {
                        _callState.value = ZegoCallState.Idle
                    }
                }

                override fun onCallInvitationAccepted(zim: ZIM?, info: ZIMCallInvitationAcceptedInfo?, callID: String?) {
                    val current = _callState.value
                    if (current is ZegoCallState.Outgoing && callID == current.callId) {
                        startRtcCall(context, current.callId, current.inviteeId)
                    }
                }

                override fun onCallInvitationRejected(zim: ZIM?, info: ZIMCallInvitationRejectedInfo?, callID: String?) {
                    val current = _callState.value
                    if (current is ZegoCallState.Outgoing && callID == current.callId) {
                        _callState.value = ZegoCallState.Idle
                        Toast.makeText(context, "Call rejected", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCallInvitationTimeout(zim: ZIM?, info: ZIMCallInvitationTimeoutInfo?, callID: String?) {
                    _callState.value = ZegoCallState.Idle
                }
            })

            
            val profile = ZegoEngineProfile().apply {
                appID = ZegoConfig.APP_ID
                appSign = ZegoConfig.APP_SIGN
                scenario = ZegoScenario.DEFAULT
                application = context.applicationContext as Application
            }
            ZegoExpressEngine.createEngine(profile, null)

            isInitialized = true
            Log.d(TAG, "Zego SDKs initialized successfully.")
        } catch (e: Exception) {
            Log.e(TAG, "Error initializing Zego SDKs", e)
        }
    }

    fun login(userId: String, username: String, callback: (Boolean) -> Unit) {
        val zimInstance = zim ?: return callback(false)
        currentUserId = userId
        val userInfo = ZIMUserInfo().apply {
            userID = userId
            userName = username
        }
        zimInstance.login(userInfo, object : ZIMLoggedInCallback {
            override fun onLoggedIn(errorInfo: ZIMError?) {
                if (errorInfo?.code == ZIMErrorCode.SUCCESS) {
                    Log.d(TAG, "ZIM Logged In successfully.")
                    fetchBlacklist()
                    callback(true)
                } else {
                    Log.e(TAG, "ZIM Login failed: ${errorInfo?.message}")
                    callback(false)
                }
            }
        })
    }

    fun logout() {
        zim?.logout()
        currentUserId = null
        _blockedUserIds.value = emptySet()
        _callState.value = ZegoCallState.Idle
    }

    fun fetchBlacklist() {
        val zimInstance = zim ?: return
        val config = ZIMBlacklistQueryConfig().apply {
            count = 100
            nextFlag = 0
        }
        zimInstance.queryBlacklist(config, object : ZIMBlacklistQueriedCallback {
            override fun onBlacklistQueried(blacklist: ArrayList<ZIMUserInfo>?, nextFlag: Int, errorInfo: ZIMError?) {
                if (errorInfo?.code == ZIMErrorCode.SUCCESS && blacklist != null) {
                    val ids = blacklist.mapNotNull { it.userID }.toSet()
                    _blockedUserIds.value = ids
                }
            }
        })
    }

    fun blockUser(userId: String, callback: (Boolean) -> Unit) {
        val zimInstance = zim ?: return callback(false)
        val list = arrayListOf(userId)
        zimInstance.addUsersToBlacklist(list, object : ZIMBlacklistUsersAddedCallback {
            override fun onBlacklistUsersAdded(errorUserList: java.util.ArrayList<ZIMErrorUserInfo>?, errorInfo: ZIMError?) {
                if (errorInfo?.code == ZIMErrorCode.SUCCESS) {
                    _blockedUserIds.value = _blockedUserIds.value + userId
                    callback(true)
                } else {
                    callback(false)
                }
            }
        })
    }

    fun unblockUser(userId: String, callback: (Boolean) -> Unit) {
        val zimInstance = zim ?: return callback(false)
        val list = arrayListOf(userId)
        zimInstance.removeUsersFromBlacklist(list, object : ZIMBlacklistUsersRemovedCallback {
            override fun onBlacklistUsersRemoved(errorUserList: ArrayList<ZIMErrorUserInfo>?, errorInfo: ZIMError?) {
                if (errorInfo?.code == ZIMErrorCode.SUCCESS) {
                    _blockedUserIds.value = _blockedUserIds.value - userId
                    callback(true)
                } else {
                    callback(false)
                }
            }
        })
    }

    fun startCall(inviteeId: String, callback: (String?) -> Unit) {
        val zimInstance = zim ?: return callback(null)
        val invitees = arrayListOf(inviteeId)
        val config = ZIMCallInviteConfig().apply {
            timeout = 30
        }
        zimInstance.callInvite(invitees, config, object : ZIMCallInvitationSentCallback {
            override fun onCallInvitationSent(callID: String?, info: ZIMCallInvitationSentInfo?, errorInfo: ZIMError?) {
                if (errorInfo?.code == ZIMErrorCode.SUCCESS && callID != null) {
                    _callState.value = ZegoCallState.Outgoing(callID, inviteeId)
                    callback(callID)
                } else {
                    callback(null)
                }
            }
        })
    }

    fun acceptCall(callId: String, callerId: String, context: Context) {
        val zimInstance = zim ?: return
        val config = ZIMCallAcceptConfig()
        zimInstance.callAccept(callId, config, object : ZIMCallAcceptanceSentCallback {
            override fun onCallAcceptanceSent(id: String?, errorInfo: ZIMError?) {
                if (errorInfo?.code == ZIMErrorCode.SUCCESS) {
                    startRtcCall(context, callId, callerId)
                }
            }
        })
    }

    fun rejectCall(callId: String) {
        val zimInstance = zim ?: return
        val config = ZIMCallRejectConfig()
        zimInstance.callReject(callId, config, object : ZIMCallRejectionSentCallback {
            override fun onCallRejectionSent(id: String?, errorInfo: ZIMError?) {
                _callState.value = ZegoCallState.Idle
            }
        })
    }

    fun cancelCall(callId: String) {
        val zimInstance = zim ?: return
        val config = ZIMCallCancelConfig()
        val invitees = arrayListOf<String>()
        zimInstance.callCancel(invitees, callId, config, object : ZIMCallCancelSentCallback {
            override fun onCallCancelSent(id: String?, errorInvitees: java.util.ArrayList<String>?, errorInfo: ZIMError?) {
                _callState.value = ZegoCallState.Idle
            }
        })
    }

    private fun startRtcCall(context: Context, callId: String, targetUserId: String) {
        val uid = currentUserId ?: return
        _callState.value = ZegoCallState.Active(callId, callId, targetUserId)
        try {
            val express = ZegoExpressEngine.getEngine()
            express.loginRoom(callId, ZegoUser(uid, uid))
            express.startPublishingStream(uid)
        } catch (e: Exception) {
            Log.e(TAG, "RTC start call failed", e)
        }
    }

    fun endRtcCall() {
        val current = _callState.value
        if (current is ZegoCallState.Active) {
            try {
                val express = ZegoExpressEngine.getEngine()
                express.stopPublishingStream()
                express.stopPreview()
                express.stopPlayingStream(current.targetUserId)
                express.logoutRoom()
            } catch (e: Exception) {}
            zim?.callReject(current.callId, ZIMCallRejectConfig(), null)
        }
        _callState.value = ZegoCallState.Idle
    }

    fun queryConversations(callback: (List<ZIMConversation>?, ZIMError?) -> Unit) {
        val zimInstance = zim ?: return callback(null, null)
        val config = ZIMConversationQueryConfig().apply {
            count = 50
            nextConversation = null
        }
        zimInstance.queryConversationList(config, object : ZIMConversationListQueriedCallback {
            override fun onConversationListQueried(conversationList: ArrayList<ZIMConversation>?, errorInfo: ZIMError?) {
                callback(conversationList, errorInfo)
            }
        })
    }

    fun sendMessage(targetUserId: String, text: String, callback: (Boolean, ZIMMessage?) -> Unit) {
        val zimInstance = zim ?: return callback(false, null)
        val textMessage = ZIMTextMessage(text)
        val sendConfig = ZIMMessageSendConfig()
        zimInstance.sendMessage(textMessage, targetUserId, ZIMConversationType.PEER, sendConfig, object : ZIMMessageSentCallback {
            override fun onMessageAttached(message: ZIMMessage?) {
                
            }
            override fun onMessageSent(message: ZIMMessage?, errorInfo: ZIMError?) {
                if (errorInfo?.code == ZIMErrorCode.SUCCESS) {
                    callback(true, message)
                } else {
                    callback(false, null)
                }
            }
        })
    }

    fun queryHistory(targetUserId: String, lastMessage: ZIMMessage?, count: Int, callback: (List<ZIMMessage>?, Boolean) -> Unit) {
        val zimInstance = zim ?: return callback(null, false)
        val config = ZIMMessageQueryConfig().apply {
            this.count = count
            this.nextMessage = lastMessage
            this.reverse = true
        }
        zimInstance.queryHistoryMessage(targetUserId, ZIMConversationType.PEER, config, object : ZIMMessageQueriedCallback {
            override fun onMessageQueried(conversationID: String?, conversationType: ZIMConversationType?, messageList: ArrayList<ZIMMessage>?, errorInfo: ZIMError?) {
                if (errorInfo?.code == ZIMErrorCode.SUCCESS) {
                    val hasMore = messageList != null && messageList.size >= count
                    callback(messageList, hasMore)
                } else {
                    callback(null, false)
                }
            }
        })
    }
}
