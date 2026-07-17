package com.example.studygether.App

import com.example.studygether.Model.Community
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

object UserCommunity {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val _userCommunityList = MutableStateFlow<List<Community?>>(emptyList())
    val userCommunityList: StateFlow<List<Community?>> = _userCommunityList.asStateFlow()

    private val _selectedCommunityId = MutableStateFlow<String?>(null)
    private val _currentUserSelectedCommunity = MutableStateFlow<Community?>(null)

    private val _hasLoaded = MutableStateFlow(false) 
    val hasLoaded: StateFlow<Boolean> = _hasLoaded.asStateFlow()

    val currentUserSelectedCommunity: StateFlow<Community?> =
        combine(_userCommunityList, _selectedCommunityId) { list, id ->
            list.firstOrNull { it?.id == id }
        }.stateIn(
            scope = scope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    fun update(list: List<Community?>) {
        _userCommunityList.value = list
        _hasLoaded.value = true
    }

    fun selectCommunity(id: String?) {
        _selectedCommunityId.value = id
    }
    fun reset() {
        _userCommunityList.value = emptyList()
        _selectedCommunityId.value = null
        _hasLoaded.value = false
    }
}