package com.example.tetris1v1.ui.auth

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.runtime.*
import com.example.tetris1v1.ui.game.GameScreen
import com.example.tetris1v1.ui.friends.FriendsScreen
import com.example.tetris1v1.ui.game.OnlineGameScreen
import com.example.tetris1v1.ui.home.HomeScreen
import com.example.tetris1v1.ui.leaderboard.LeaderboardScreen
import com.example.tetris1v1.ui.match.MatchScreen
import com.example.tetris1v1.ui.profile.UserProfileScreen
import com.example.tetris1v1.ui.game.GarbageDemoScreen

@Composable
fun AuthNavigation(viewModel: AuthViewModel = viewModel()) {
    val navController = rememberNavController()
    val authState by viewModel.authState.collectAsState()

    LaunchedEffect(authState) {
        val currentRoute = navController.currentBackStackEntry?.destination?.route
        when (authState) {
            is AuthState.Success -> {
                if (currentRoute == "register") {
                    navController.navigate("login") { popUpTo(0) }
                } else if (currentRoute == "login") {
                    navController.navigate("home") { popUpTo(0) }
                }
            }
            is AuthState.LoggedOut -> navController.navigate("login") {
                popUpTo(0)
            }
            else -> Unit
        }
    }

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                viewModel = viewModel,
                onNavigateToRegister = { navController.navigate("register") }
            )
        }
        composable("register") {
            RegisterScreen(
                viewModel = viewModel,
                onNavigateToLogin = { navController.popBackStack() }
            )
        }
        composable("home") {
            HomeScreen(
                authViewModel = viewModel,
                onLogout = { viewModel.logout() },
                onSettings = { navController.navigate("profile") },
                onLeaderboard = { navController.navigate("leaderboard") },
                onClassicGame = { navController.navigate("game") },
                onRequest = { navController.navigate("friends") },
                onMatches = { navController.navigate("garbage_demo") }
            )
        }
        composable("leaderboard") {
            LeaderboardScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable("profile") {
            UserProfileScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable("friends") {
            FriendsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable("match") {
            MatchScreen(
                onNavigateBack = { navController.popBackStack() },
                onStartGame = { navController.navigate("online") }
            )
        }
        composable("online") {
            OnlineGameScreen(
                onExitToMenu = { navController.popBackStack() },
                onGameOver = { result -> viewModel.updateHighScore(result.score) }
            )
        }
        composable("garbage_demo") {
            GarbageDemoScreen(
                onExitToMenu = {
                    navController.popBackStack()
                }
            )
        }
        composable("game") {
            GameScreen(
                onExitToMenu = { navController.popBackStack() },
                onGameOver = { result -> viewModel.updateHighScore(result.score) }
            )
        }
    }
}