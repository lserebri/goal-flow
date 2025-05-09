package com.example.goalflow.ui.consumable

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.goalflow.ui.consumable.ConsumableUiState.Loading
import com.example.goalflow.ui.consumable.ConsumableUiState.Success
import com.example.goalflow.ui.consumable.ConsumableUiState.Error
import com.example.goalflow.data.consumable.Consumable
import com.example.goalflow.data.consumable.ConsumableRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConsumableViewModel @Inject constructor (
    private val consumableRepository: ConsumableRepository
) : ViewModel() {
    val allConsumables: StateFlow<ConsumableUiState> = consumableRepository
        .allConsumables.map<List<Consumable>, ConsumableUiState>(::Success)
        .catch { emit(Error(it)) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Loading)

    fun addConsumable(consumable: Consumable) {
        viewModelScope.launch {
            consumableRepository.insertConsumable(consumable)
        }
    }

    fun deleteConsumable(consumable: Consumable) {
        viewModelScope.launch {
            consumableRepository.deleteConsumable(consumable)
        }
    }
}

sealed interface ConsumableUiState {
    object Loading : ConsumableUiState
    data class Error(val throwable: Throwable) : ConsumableUiState
    data class Success(val data: List<Consumable>) : ConsumableUiState
}