package com.example.studygether.App

import android.content.Context
import android.content.SharedPreferences

class LastSelectedCommunityStore(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("last_selected_community", Context.MODE_PRIVATE)

    fun save(uid: String, communityId: String?) {
        prefs.edit().putString(uid, communityId).apply()
    }

    fun get(uid: String): String? = prefs.getString(uid, null)

    fun clear(uid: String) {
        prefs.edit().remove(uid).apply()
    }
}