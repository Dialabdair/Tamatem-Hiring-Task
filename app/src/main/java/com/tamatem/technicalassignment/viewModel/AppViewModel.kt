package com.tamatem.technicalassignment.viewModel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow

class AppViewModel:ViewModel() {
    private val mutableSharedFlow:MutableStateFlow<AppUiState> = MutableStateFlow(AppUiState.NONE)
    val uiState = mutableSharedFlow.asSharedFlow()

    fun setUrl(url: String){
        mutableSharedFlow.value = AppUiState.UrlSetSuccessfully(url)
    }


    sealed class AppUiState{
        data class UrlSetSuccessfully(val url: String):AppUiState()
        object NONE:AppUiState()
    }
}