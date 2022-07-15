package com.github.repo.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.repo.data.datasource.TokenSharedPreference
import com.github.repo.domain.repository.LoginRepository
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginRepository: LoginRepository,
    private val tokenSharedPreference: TokenSharedPreference
) : ViewModel() {

    private val _uiState = MutableLiveData<UiState>()
    val uiState: LiveData<UiState> = _uiState

    fun getAccessToken(code: String) = viewModelScope.launch {
        _uiState.value = UiState.Loading
        loginRepository.getAccessToken(code)
            .onSuccess {
                tokenSharedPreference.saveToken(it.accessToken)
                _uiState.value = UiState.GetToken(it.accessToken)
            }
            .onFailure {
                _uiState.value = UiState.Error
            }
    }
}