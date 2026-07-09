package com.example.tetris1v1.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tetris1v1.ui.auth.AuthViewModel
import com.example.tetris1v1.ui.theme.CardBackground
import com.example.tetris1v1.ui.theme.CardBorder
import com.example.tetris1v1.ui.theme.NavyBackground
import com.example.tetris1v1.ui.theme.TetrisRed
import com.example.tetris1v1.ui.theme.TextWhite

@Composable
fun CardButton(
    label: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(15.dp))
            .background(CardBackground)
            .border(1.dp, CardBorder, RoundedCornerShape(15.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = TextWhite,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = label,
                color = TextWhite,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onLogout: () -> Unit,
    onSettings: () -> Unit = {},
    onClassicGame: () -> Unit = {},
    onRequest: () -> Unit = {},
    onLeaderboard: () -> Unit = {},
    onMatches: () -> Unit = {},
    authViewModel: AuthViewModel
) {
    val currentUser by authViewModel.currentUser.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(NavyBackground)
            .padding(horizontal = 24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp)
                .align(Alignment.TopCenter),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = { authViewModel.logout(); onLogout() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = "Logout",
                    tint = TextWhite,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(TetrisRed)
                        .padding(6.dp)
                )
            }

            IconButton(onClick = onSettings) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings",
                    tint = TextWhite,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(TetrisRed)
                        .padding(6.dp)
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Hello",
                color = TextWhite,
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = currentUser?.userName ?: "Player",
                color = TetrisRed,
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(20.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(CardBackground)
                    .border(1.dp, CardBorder, RoundedCornerShape(16.dp))
                    .padding(vertical = 20.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "High Score",
                        color = TextWhite,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "%,.0f".format((currentUser?.highScore ?: 0).toDouble())
                            .replace(",", "."),
                        color = TetrisRed,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            val gap = 16.dp
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(gap)
            ) {
                CardButton(
                    label = "Classic",
                    icon = Icons.Default.PlayArrow,
                    onClick = onClassicGame,
                    modifier = Modifier.weight(1f)
                )
                CardButton(
                    label = "1v1",
                    icon = Icons.Default.Person,
                    onClick = onMatches,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(gap))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(gap)
            ) {
                CardButton(
                    label = "Leaderboard",
                    icon = Icons.Default.Star,
                    onClick = onLeaderboard,
                    modifier = Modifier.weight(1f)
                )
                CardButton(
                    label = "Friends",
                    icon = Icons.Default.Add,
                    onClick = onRequest,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}