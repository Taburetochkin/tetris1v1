package com.example.tetris1v1.ui.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tetris1v1.game.controller.LocalGameController
import com.example.tetris1v1.game.enums.GamePhase
import com.example.tetris1v1.game.interfaces.GameAction
import com.example.tetris1v1.game.interfaces.GameController
import com.example.tetris1v1.ui.game.data.GameUiState
import com.example.tetris1v1.ui.game.data.toUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.random.Random

class GarbageDemoViewModel(
    controllerFactory: (CoroutineScope) -> GameController = { LocalGameController(it) }
) : ViewModel() {

    private val controller = controllerFactory(viewModelScope)

    private var lastEventId = 0L

    val uiState = controller.state
        .map { it.toUiState() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = GameUiState()
        )

    init {
        viewModelScope.launch {
            controller.state.collectLatest { state ->

                if (state.clearEventId == lastEventId)
                    return@collectLatest

                lastEventId = state.clearEventId

                val garbageLines = when (state.lastClearedLines) {
                    2 -> 1
                    3 -> 2
                    4 -> 3
                    else -> 0
                }

                if (garbageLines > 0) {

                    controller.dispatch(
                        GameAction.AddGarbage(
                            count = garbageLines,
                            gapColumn = Random.nextInt(10)
                        )
                    )
                }
            }
        }
    }

    fun onStart() =
        controller.dispatch(GameAction.Start)

    fun onTogglePause() {
        when (uiState.value.phase) {

            GamePhase.PLAYING ->
                controller.dispatch(GameAction.Pause)

            GamePhase.PAUSED ->
                controller.dispatch(GameAction.Resume)

            else -> {}
        }
    }

    fun onMoveLeft() =
        controller.dispatch(GameAction.MoveLeft)

    fun onMoveRight() =
        controller.dispatch(GameAction.MoveRight)

    fun onRotate() =
        controller.dispatch(GameAction.RotateClockwise)

    fun onSoftDrop() =
        controller.dispatch(GameAction.SoftDrop)

    fun onHardDrop() =
        controller.dispatch(GameAction.HardDrop)

    override fun onCleared() {
        controller.stop()
    }
}