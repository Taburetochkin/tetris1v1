package com.example.tetris1v1.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tetris1v1.ui.auth.AuthState
import com.example.tetris1v1.ui.auth.AuthViewModel
import com.example.tetris1v1.ui.components.RedButton
import com.example.tetris1v1.ui.components.StyledTextField
import com.example.tetris1v1.ui.components.TetrisLogo
import com.example.tetris1v1.ui.components.TopBar
import com.example.tetris1v1.ui.theme.FieldBackground
import com.example.tetris1v1.ui.theme.FieldBorder
import com.example.tetris1v1.ui.theme.NavyBackground
import com.example.tetris1v1.ui.theme.TextHint
import com.example.tetris1v1.ui.theme.TextWhite
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(
    viewModel: AuthViewModel,
    onNavigateBack: () -> Unit
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val authState by viewModel.authState.collectAsState()

    var age by remember { mutableStateOf(currentUser?.age?.toString() ?: "") }
    var selectedCountry by remember { mutableStateOf(currentUser?.country ?: "") }
    var countryDropdownExpanded by remember { mutableStateOf(false) }
    var showSuccess by remember { mutableStateOf(false) }
    var previousAuthState by remember { mutableStateOf(authState) }

    val countries = listOf(
        "Afghanistan", "Albania", "Algeria", "Argentina", "Australia", "Austria",
        "Bangladesh", "Belgium", "Bolivia", "Brazil", "Canada", "Chile", "China",
        "Colombia", "Croatia", "Czech Republic", "Denmark", "Ecuador", "Egypt",
        "Ethiopia", "Finland", "France", "Germany", "Ghana", "Greece", "Guatemala",
        "Hungary", "India", "Indonesia", "Iran", "Iraq", "Ireland", "Israel",
        "Italy", "Japan", "Jordan", "Kazakhstan", "Kenya", "South Korea", "Kuwait",
        "Malaysia", "Mexico", "Morocco", "Netherlands", "New Zealand", "Nigeria",
        "Norway", "Pakistan", "Peru", "Philippines", "Poland", "Portugal", "Romania",
        "Russia", "Saudi Arabia", "Serbia", "Singapore", "South Africa", "Spain",
        "Sri Lanka", "Sweden", "Switzerland", "Taiwan", "Thailand", "Turkey",
        "Ukraine", "United Arab Emirates", "United Kingdom", "United States",
        "Uruguay", "Venezuela", "Vietnam"
    )

    LaunchedEffect(Unit) {
        viewModel.resetState()
    }

    LaunchedEffect(authState) {
        if (authState is AuthState.Success && previousAuthState !is AuthState.Success) {
            showSuccess = true
            viewModel.resetState()
        }
        previousAuthState = authState
    }
    if (showSuccess) {
        LaunchedEffect(Unit) {
            delay(2000)
            showSuccess = false
            onNavigateBack()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(NavyBackground)
    )
    {

        TopBar(onNavigateBack = onNavigateBack, text = "Player Settings")

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(36.dp))

            TetrisLogo(modifier = Modifier)

            Spacer(modifier = Modifier.height(36.dp))

            // Non-editable username
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(FieldBackground.copy(alpha = 0.3f))
                    .border(1.dp, FieldBackground, RoundedCornerShape(12.dp))
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                Text(
                    text = currentUser?.userName ?: "",
                    color = TextWhite,
                    fontSize = 15.sp
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Editable age
            StyledTextField(
                value = age,
                onValueChange = { if (it.length <= 3 && it.all { c -> c.isDigit() }) age = it },
                placeholder = "Age",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Editable country dropdown
            ExposedDropdownMenuBox(
                expanded = countryDropdownExpanded,
                onExpandedChange = { countryDropdownExpanded = it },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(FieldBackground)
                        .border(1.dp, FieldBorder, RoundedCornerShape(12.dp))
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                )
                {
                    TextField(
                        value = selectedCountry,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = countryDropdownExpanded)
                        },
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite,
                            disabledTextColor = TextHint,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                        ),
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = countryDropdownExpanded,
                        onDismissRequest = { countryDropdownExpanded = false }
                    ) {
                        countries.forEach { country ->
                            DropdownMenuItem(
                                text = { Text(country) },
                                onClick = {
                                    selectedCountry = country
                                    countryDropdownExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (showSuccess) {
                Text(
                    text = "Profile updated successfully!",
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (authState is AuthState.Error) {
                Text(
                    text = (authState as AuthState.Error).message,
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            RedButton(
                text = "Save Changes",
                onClick = {
                    val ageInt = age.toIntOrNull() ?: return@RedButton
                    viewModel.updateUserProfile(ageInt, selectedCountry)
                },
                modifier = Modifier.fillMaxWidth(),
                isLoading = authState is AuthState.Loading,
                enabled = authState !is AuthState.Loading && !showSuccess
            )
        }
    }
}