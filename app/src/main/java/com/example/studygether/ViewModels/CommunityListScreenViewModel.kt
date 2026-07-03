package com.example.studygether.ViewModels

import androidx.lifecycle.ViewModel
import com.example.studygether.App.UserCommunity
import com.example.studygether.Model.Community
import com.example.studygether.Repository.CommunityRepository
import kotlinx.coroutines.flow.StateFlow

class CommunityListScreenViewModel(
    private val communityRepository: CommunityRepository
): ViewModel() {

    val communities: StateFlow<List<Community?>> = UserCommunity.userCommunityList
    val selectedCommunity: StateFlow<Community?> = UserCommunity.currentUserSelectedCommunity



}