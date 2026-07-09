package com.example.tetris1v1.ui.match

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tetris1v1.ui.components.RedButton
import com.example.tetris1v1.ui.components.TopBar
import com.example.tetris1v1.ui.friends.FriendsViewModel
import com.example.tetris1v1.ui.theme.CardBackground
import com.example.tetris1v1.ui.theme.CardBorder
import com.example.tetris1v1.ui.theme.NavyBackground
import com.example.tetris1v1.ui.theme.TextWhite
import com.example.tetris1v1.ui.theme.TetrisRed

@Composable
fun MatchScreen(
    onNavigateBack: () -> Unit,
    onStartGame: () -> Unit,
    viewModel: FriendsViewModel = viewModel()
) {
    val friends by viewModel.friends.collectAsState()
    val friendsLoading by viewModel.friendsLoading.collectAsState()
    var selectedFriendId by remember { mutableStateOf<String?>(null) }
    var selectedFriendName by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        viewModel.loadFriends()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NavyBackground)
    ) {
        TopBar(onNavigateBack, "1v1 Match")

        if (friendsLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = TetrisRed)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
                    .padding(top = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text(
                        text = "Pick a Friend",
                        color = TextWhite,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Select a friend to challenge in a 1v1 match",
                        color = TextWhite.copy(alpha = 0.6f),
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

                if (friends.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 48.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No friends yet. Add some friends first!",
                                color = TextWhite.copy(alpha = 0.5f),
                                fontSize = 14.sp
                            )
                        }
                    }
                } else {
                    items(friends) { friend ->
                        val isSelected = selectedFriendId == friend.id

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    if (isSelected) TetrisRed.copy(alpha = 0.15f)
                                    else CardBackground
                                )
                                .border(
                                    width = if (isSelected) 1.5.dp else 1.dp,
                                    color = if (isSelected) TetrisRed else CardBorder,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .padding(horizontal = 16.dp, vertical = 14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = friend.userName,
                                    color = TextWhite,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "High Score: ${friend.highScore}",
                                    color = TextWhite.copy(alpha = 0.6f),
                                    fontSize = 12.sp
                                )
                            }
                            RedButton(
                                text = if (isSelected) "Selected" else "Select",
                                onClick = {
                                    selectedFriendId = friend.id
                                    selectedFriendName = friend.userName
                                },
                                enabled = !isSelected,
                                modifier = Modifier.width(110.dp)
                            )
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                        if (selectedFriendName != null) {
                            Text(
                                text = "Challenging: $selectedFriendName",
                                color = TetrisRed,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                        RedButton(
                            text = "Start Game",
                            onClick = onStartGame,
                            enabled = selectedFriendId != null
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            }
        }
    }
}