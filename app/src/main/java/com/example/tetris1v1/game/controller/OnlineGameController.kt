package com.example.tetris1v1.game.controller

import com.example.tetris1v1.game.calculateSpeed
import com.example.tetris1v1.game.enums.GamePhase
import com.example.tetris1v1.game.interfaces.GameAction
import com.example.tetris1v1.game.interfaces.GameController
import com.example.tetris1v1.game.singletons.GameEngine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.example.tetris1v1.game.data.GameState

class OnlineGameController(
    private val scope: CoroutineScope
) : GameController {

    private val _state = MutableStateFlow(GameState())
    override val state: StateFlow<GameState> = _state.asStateFlow()

    private var gravityJob: Job? = null

    override fun dispatch(action: GameAction) {

        _state.update {
            GameEngine.reduce(it, action)
        }

        when (action) {

            GameAction.Start,
            GameAction.Resume,
            GameAction.SoftDrop,
            GameAction.HardDrop ->
                restartGravity()

            GameAction.Pause ->
                gravityJob?.cancel()

            else -> Unit
        }

        if (_state.value.phase == GamePhase.OVER) {
            gravityJob?.cancel()
        }
    }

    override fun stop() {
        gravityJob?.cancel()
    }

    private fun restartGravity() {

        gravityJob?.cancel()

        gravityJob = scope.launch {

            while (_state.value.phase == GamePhase.PLAYING) {

                delay(calculateSpeed(_state.value.level))

                if (_state.value.phase == GamePhase.PLAYING) {
                    _state.update {
                        GameEngine.reduce(it, GameAction.Tick)
                    }
                }
            }
        }
    }
}