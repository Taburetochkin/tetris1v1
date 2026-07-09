package com.example.tetris1v1.ui.friends

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tetris1v1.ui.components.RedButton
import com.example.tetris1v1.ui.components.StyledTextField
import com.example.tetris1v1.ui.components.TopBar
import com.example.tetris1v1.ui.theme.AcceptGreen
import com.example.tetris1v1.ui.theme.CardBackground
import com.example.tetris1v1.ui.theme.CardBorder
import com.example.tetris1v1.ui.theme.NavyBackground
import com.example.tetris1v1.ui.theme.TetrisRed
import com.example.tetris1v1.ui.theme.TextWhite


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendsScreen(
    onNavigateBack: () -> Unit,
    viewModel: FriendsViewModel = viewModel()
) {
    var searchUsername by remember { mutableStateOf("") }
    val searchResult by viewModel.searchResult.collectAsState()
    val requestState by viewModel.requestState.collectAsState()
    val friendRequests by viewModel.friendRequests.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadIncomingRequests()
    }

    LaunchedEffect(requestState) {
        if (requestState is RequestState.Success) {
            kotlinx.coroutines.delay(2000)
            viewModel.resetRequestState()
            viewModel.resetSearchState()
            searchUsername = ""
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NavyBackground)
    ) {

        TopBar(onNavigateBack, "Friend Request")

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Send Friend Request",
                    color = TextWhite,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(8.dp))
                StyledTextField(
                    value = searchUsername,
                    onValueChange = {
                        searchUsername = it
                        viewModel.resetSearchState()
                        viewModel.resetRequestState()
                    },
                    placeholder = "Search by username"
                )
                Spacer(modifier = Modifier.height(10.dp))
                RedButton(
                    text = "Search",
                    onClick = { viewModel.searchUserByUsername(searchUsername.trim()) },
                    isLoading = searchResult is SearchState.Loading,
                    enabled = searchUsername.isNotBlank() && searchResult !is SearchState.Loading
                )
            }

            // Search results
            item {
                when (val result = searchResult) {
                    is SearchState.Found -> {
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(CardBackground)
                                .border(1.dp, CardBorder, RoundedCornerShape(12.dp))
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = result.username,
                                color = TextWhite,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Button(
                                onClick = { viewModel.sendFriendRequest(result.userId) },
                                enabled = requestState !is RequestState.Loading,
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A1A2E)),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                if (requestState is RequestState.Loading) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(16.dp),
                                        color = TextWhite,
                                        strokeWidth = 2.dp
                                    )
                                } else {
                                    Text("Add Friend", color = TextWhite, fontSize = 13.sp)
                                }
                            }
                        }
                        if (requestState is RequestState.Success) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Friend request sent!", color = AcceptGreen, fontSize = 13.sp)
                        }
                        if (requestState is RequestState.Error) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = (requestState as RequestState.Error).message,
                                color = TetrisRed,
                                fontSize = 13.sp
                            )
                        }
                    }
                    is SearchState.NotFound -> {
                        Text("No user found with that username.", color = TetrisRed, fontSize = 13.sp)
                    }
                    is SearchState.Error -> {
                        Text(result.message, color = TetrisRed, fontSize = 13.sp)
                    }
                    else -> {}
                }
            }


            if (friendRequests.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider(color = CardBorder)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Pending Requests",
                        color = TextWhite,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                items(friendRequests)
                { request ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(CardBackground)
                            .border(1.dp, CardBorder, RoundedCornerShape(12.dp))
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = request.requesterUsername,
                            color = TextWhite,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Button(
                                onClick = {
                                    viewModel.acceptFriendRequest(request.id, request.requesterId)
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = AcceptGreen)
                            ) {
                                Text("Accept", color = TextWhite, fontWeight = FontWeight.SemiBold)
                            }
                            Button(
                                onClick = { viewModel.declineFriendRequest(request.id) },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = TetrisRed)
                            ) {
                                Text("Reject", color = TextWhite, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }
            }
        }
    }
}