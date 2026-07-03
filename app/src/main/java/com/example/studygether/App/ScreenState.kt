package com.example.studygether.App

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.studygether.Model.Community
import com.example.studygether.ui.theme.Typography
import com.example.studygether.ui.theme.tokens.AppSpacing

sealed interface CommunityScreenState {
    data object Loading : CommunityScreenState
    data object NoCommunity : CommunityScreenState
    data class Loaded(val community: Community) : CommunityScreenState
}

@Composable
fun rememberCommunityScreenState(): CommunityScreenState {
    val hasLoaded by UserCommunity.hasLoaded.collectAsStateWithLifecycle()
    val communities by UserCommunity.userCommunityList.collectAsStateWithLifecycle()
    val selected by UserCommunity.currentUserSelectedCommunity.collectAsStateWithLifecycle()

    return when {
        !hasLoaded -> CommunityScreenState.Loading
        selected != null -> CommunityScreenState.Loaded(selected!!)
        communities.isEmpty() -> CommunityScreenState.NoCommunity
        else -> CommunityScreenState.Loading
    }
}

@Composable
fun LoadingIndicator() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
fun NoCommunityJoinedContent() {
    Column (
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("You haven't joined any community yet.", style = Typography.titleLarge,
            textAlign = TextAlign.Center)
    }
}