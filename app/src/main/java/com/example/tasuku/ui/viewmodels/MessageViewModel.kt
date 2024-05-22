package com.example.tasuku.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class MessageViewModel(
    savedStateHandle: SavedStateHandle,
    ): ViewModel() {
    val channelId:String = checkNotNull(savedStateHandle["channelId"])
}