package com.example.tetris1v1.ui.leaderboard

import android.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tetris1v1.models.LeaderboardEntry
import com.example.tetris1v1.ui.components.TopBar
import com.example.tetris1v1.ui.theme.CardBackground
import com.example.tetris1v1.ui.theme.CardBorder
import com.example.tetris1v1.ui.theme.NavyBackground
import com.example.tetris1v1.ui.theme.TetrisRed
import com.example.tetris1v1.ui.theme.TextMuted
import com.example.tetris1v1.ui.theme.TextWhite
import com.example.tetris1v1.ui.theme.Transparent


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaderboardScreen(
    onNavigateBack: () -> Unit,
    viewModel: LeaderboardViewModel = viewModel()
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    val globalEntries by viewModel.globalEntries.collectAsState()
    val friendsEntries by viewModel.friendsEntries.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadGlobalLeaderboard()
        viewModel.loadFriendsLeaderboard()
    }

    val currentEntries = if (selectedTab == 0) globalEntries else friendsEntries

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NavyBackground)
    ) {

        TopBar(onNavigateBack = onNavigateBack, text = "Leaderboard")

        Spacer(modifier = Modifier.height(20.dp))

        // Tab selector
        Row(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(CardBackground)
                .border(1.dp, CardBorder, RoundedCornerShape(10.dp))
                .padding(4.dp),
        ) {
            listOf("Global", "Friends").forEachIndexed { index, label ->
                val selected = selectedTab == index
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (selected) TetrisRed else Transparent)
                        .clickable { selectedTab = index }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = label,
                        color = TextWhite,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 14.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Header row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Rank",
                color = TextMuted,
                fontSize = 13.sp,
                modifier = Modifier.width(56.dp)
            )

            Text(
                text = "Player",
                color = TextMuted,
                fontSize = 13.sp,
                modifier = Modifier.weight(1f)
            )

            Text(
                text = "Score",
                color = TextMuted,
                fontSize = 13.sp
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Entries
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            itemsIndexed(currentEntries) { index, entry ->
                LeaderboardRow(rank = index + 1, entry = entry)
                if (index < currentEntries.lastIndex) {
                    HorizontalDivider(color = CardBorder, thickness = 1.dp)
                }
            }
        }
    }
}
@Composable
fun LeaderboardRow(rank: Int, entry: LeaderboardEntry) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "#$rank",
            color = TextWhite,
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.width(56.dp)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = entry.username,
                color = TextWhite,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = entry.country,
                color = TextMuted,
                fontSize = 12.sp
            )
        }
        Text(
            text = "${entry.highScore}",
            color = TextWhite,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}