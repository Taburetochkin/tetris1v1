package com.example.tetris1v1.ui.auth

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import java.util.Locale
import android.location.Geocoder
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.example.tetris1v1.ui.components.RedButton
import com.example.tetris1v1.ui.components.StyledTextField
import com.example.tetris1v1.ui.components.TetrisLogo
import com.example.tetris1v1.ui.theme.FieldBackground
import com.example.tetris1v1.ui.theme.FieldBorder
import com.example.tetris1v1.ui.theme.NavyBackground
import com.example.tetris1v1.ui.theme.TextHint
import com.example.tetris1v1.ui.theme.TextWhite
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    onNavigateToLogin: () -> Unit
) {
    val context = LocalContext.current

    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var selectedCountry by remember { mutableStateOf("") }
    var countryDropdownExpanded by remember { mutableStateOf(false) }
    var isDetectingLocation by remember { mutableStateOf(false) }
    var locationError by remember { mutableStateOf<String?>(null) }
    val authState by viewModel.authState.collectAsState()

    var showSuccess by remember { mutableStateOf(false) }

    LaunchedEffect(authState) {
        if (authState is AuthState.Success) {
            showSuccess = true
        }
    }

    if (showSuccess) {
        LaunchedEffect(Unit) {
            delay(2000)
            viewModel.resetState()
            onNavigateToLogin()
        }
    }

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

    // Fetches the current location and reverse-geocodes it to a country name
    fun detectCountryFromLocation() {
        isDetectingLocation = true
        locationError = null
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        val cancellationToken = CancellationTokenSource()
        try {
            fusedLocationClient
                .getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, cancellationToken.token)
                .addOnSuccessListener { location ->
                    isDetectingLocation = false
                    if (location != null) {
                        try {
                            val geocoder = Geocoder(context, Locale.getDefault())
                            val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                            val countryName = addresses?.firstOrNull()?.countryName
                            if (countryName != null) {
                                selectedCountry = countries.find {
                                    it.equals(countryName, ignoreCase = true)
                                } ?: countryName
                            } else {
                                locationError = "Could not determine country"
                            }
                        } catch (e: Exception) {
                            locationError = "Geocoding failed: ${e.localizedMessage}"
                        }
                    } else {
                        locationError = "Location unavailable — please select manually"
                    }
                }
                .addOnFailureListener { e ->
                    isDetectingLocation = false
                    locationError = "Location error: ${e.localizedMessage}"
                }
        } catch (e: SecurityException) {
            isDetectingLocation = false
            locationError = "Location permission denied"
        }
    }

    // Permission launcher — triggers location detection once granted
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            detectCountryFromLocation()
        } else {
            locationError = "Location permission denied — please select country manually"
        }
    }

    // Auto-request location permission on first composition
    LaunchedEffect(Unit) {
        locationPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(NavyBackground)
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            TetrisLogo(modifier = Modifier)

            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "Create Account",
                style = MaterialTheme.typography.headlineLarge,
                color = TextWhite
            )

            Spacer(modifier = Modifier.height(32.dp))

            StyledTextField(
                value = username,
                onValueChange = { username = it },
                placeholder = "Username",
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            StyledTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = "Email",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            StyledTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = "Password",
                modifier = Modifier.fillMaxWidth(),
                isPassword = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            StyledTextField(
                value = age,
                onValueChange = { if (it.length <= 3 && it.all { c -> c.isDigit() }) age = it },
                placeholder = "Age",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Country field — pre-filled from geolocation, user can still override
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
                        value = if (isDetectingLocation) "Detecting location…" else selectedCountry,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            if (isDetectingLocation) {
                                CircularProgressIndicator(modifier = Modifier.size(20.dp))
                            } else {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = countryDropdownExpanded)
                            }
                        },
                        enabled = !isDetectingLocation,
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

            // Location error with a retry option
            if (locationError != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = locationError!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.weight(1f)
                    )
                    TextButton(onClick = {
                        locationError = null
                        locationPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
                    }) {
                        Text("Retry", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (showSuccess) {
                Text(
                    text = "User created successfully!",
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
                text = "Register",
                onClick = {
                    val ageInt = age.toIntOrNull() ?: return@RedButton
                    viewModel.register(username, email, password, ageInt, selectedCountry)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = authState !is AuthState.Loading
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = onNavigateToLogin) {
                Text("Already have an account? Login")
            }
        }
    }
}