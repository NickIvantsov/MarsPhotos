package com.ivantsov.marsphotos.ui.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivantsov.marsphotos.data.reprository.PhotosRepository
import com.ivantsov.marsphotos.ui.main.intent.MainIntent
import com.ivantsov.marsphotos.ui.main.viewstate.MainState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: PhotosRepository
) : ViewModel() {

    val userIntent = Channel<MainIntent>(Channel.UNLIMITED)
    private val _state = MutableStateFlow<MainState>(MainState.Idle)
    val state: StateFlow<MainState>
        get() = _state

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            userIntent.consumeAsFlow().collect {
                when (it) {
                    is MainIntent.FetchPhoto -> fetchPhoto()
                }
            }
        }
    }

    private fun fetchPhoto() {
        viewModelScope.launch {
            _state.value = MainState.Loading
            _state.value = try {
                MainState.Photos(repository.getPhotos())
            } catch (e: Exception) {
                MainState.Error(e)
            }
        }
    }
}