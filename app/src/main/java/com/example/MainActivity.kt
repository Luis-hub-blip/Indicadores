package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.CongregationViewModel
import com.example.ui.components.AdminLoginDialog
import com.example.ui.screens.AdminScreen
import com.example.ui.screens.PublicScreen
import com.example.ui.screens.SignInScreen
import com.example.ui.theme.MyApplicationTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                AppGate()
            }
        }
    }
}

@Composable
fun AppGate(
    viewModel: CongregationViewModel = viewModel()
) {
    var currentUser by remember { mutableStateOf(Firebase.auth.currentUser) }
    var isDirectlyAuthenticated by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            currentUser = auth.currentUser
        }
        Firebase.auth.addAuthStateListener(listener)
        onDispose {
            Firebase.auth.removeAuthStateListener(listener)
        }
    }

    if (currentUser == null && !isDirectlyAuthenticated) {
        SignInScreen(
            onAuthSuccess = {
                currentUser = Firebase.auth.currentUser
            },
            onUsernamePasswordLogin = { username, pass ->
                val success = viewModel.login(username, pass)
                if (success) {
                    isDirectlyAuthenticated = true
                }
                success
            },
            onGuestContinue = {
                isDirectlyAuthenticated = true
                viewModel.logout()
            }
        )
    } else {
        CongregationApp(
            viewModel = viewModel
        )
    }
}

@Composable
fun CongregationApp(
    viewModel: CongregationViewModel = viewModel()
) {
    val isAdminAuthenticated by viewModel.isAdminAuthenticated.collectAsState()
    val userMessage by viewModel.userMessage.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var showLoginDialog by remember { mutableStateOf(false) }

    LaunchedEffect(userMessage) {
        userMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearUserMessage()
        }
    }

    // Handle back button to return to Public screen if in Admin mode
    BackHandler(enabled = isAdminAuthenticated) {
        viewModel.logout()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        AnimatedContent(
            targetState = isAdminAuthenticated,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "screen_transition",
            modifier = Modifier.padding(innerPadding)
        ) { isAuthenticated ->
            if (isAuthenticated) {
                AdminScreen(
                    viewModel = viewModel,
                    onLogout = { viewModel.logout() }
                )
            } else {
                PublicScreen(
                    viewModel = viewModel,
                    onRequestAdminLogin = { showLoginDialog = true }
                )
            }
        }

        if (showLoginDialog) {
            AdminLoginDialog(
                onDismiss = { showLoginDialog = false },
                onLogin = { adminName, pass ->
                    val success = viewModel.login(adminName, pass)
                    if (success) {
                        showLoginDialog = false
                    }
                    success
                }
            )
        }
    }
}
