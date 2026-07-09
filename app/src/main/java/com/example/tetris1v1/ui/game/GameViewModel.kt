package com.example.tetris1v1.ui.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tetris1v1.game.controller.LocalGameController
import com.example.tetris1v1.game.data.GameResult
import com.example.tetris1v1.game.data.toResult
import com.example.tetris1v1.game.enums.GamePhase
import com.example.tetris1v1.game.interfaces.GameAction
import com.example.tetris1v1.game.interfaces.GameController
import com.example.tetris1v1.ui.game.data.GameUiState
import com.example.tetris1v1.ui.game.data.toUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class GameViewModel(
    controllerFactory: (CoroutineScope) -> GameController = { LocalGameController(it) }
) : ViewModel() {

    private val controller: GameController = controllerFactory(viewModelScope)

    val uiState: StateFlow<GameUiState> = controller.state
        .map { it.toUiState() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = GameUiState()
        )

    val gameResult: StateFlow<GameResult?> = controller.state
        .filter { it.phase == GamePhase.OVER }
        .map { it.toResult() }
        .distinctUntilChanged()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    fun onStart() = controller.dispatch(GameAction.Start)

    fun onTogglePause() {
        when (uiState.value.phase) {
            GamePhase.PLAYING -> controller.dispatch(GameAction.Pause)
            GamePhase.PAUSED -> controller.dispatch(GameAction.Resume)
            else -> Unit
        }
    }

    fun onMoveLeft() = controller.dispatch(GameAction.MoveLeft)
    fun onMoveRight() = controller.dispatch(GameAction.MoveRight)
    fun onRotate() = controller.dispatch(GameAction.RotateClockwise)
    fun onSoftDrop() = controller.dispatch(GameAction.SoftDrop)
    fun onHardDrop() = controller.dispatch(GameAction.HardDrop)

    override fun onCleared() {
        super.onCleared()
        controller.stop()
    }
}