package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.VideoCall
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.MaqraahViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class MainActivity : ComponentActivity() {

    private val viewModel: MaqraahViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainAppHost(viewModel)
            }
        }
    }
}

@Composable
fun MainAppHost(viewModel: MaqraahViewModel) {
    var currentUser by remember { mutableStateOf(Firebase.auth.currentUser) }
    val userProfile by viewModel.currentUserProfile.collectAsState()
    var selectedTab by remember { mutableIntStateOf(0) }
    var showRegistration by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        val listener = com.google.firebase.auth.FirebaseAuth.AuthStateListener { auth ->
            currentUser = auth.currentUser
            if (auth.currentUser != null) {
                viewModel.loadInitialData()
            }
        }
        Firebase.auth.addAuthStateListener(listener)
        onDispose {
            Firebase.auth.removeAuthStateListener(listener)
        }
    }

    if (currentUser == null) {
        AuthScreen(viewModel = viewModel) {
            currentUser = Firebase.auth.currentUser
            showRegistration = true
        }
    } else if (showRegistration && userProfile?.city.isNullOrBlank()) {
        RegistrationScreen(viewModel = viewModel) {
            showRegistration = false
        }
    } else {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                NavigationBar {
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                        label = { Text("ዋና ገጽ") },
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Group, contentDescription = "Groups") },
                        label = { Text("ቡድኖች") },
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.VideoCall, contentDescription = "Classes") },
                        label = { Text("ክፍሎች") },
                        selected = selectedTab == 2,
                        onClick = { selectedTab = 2 }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Chat, contentDescription = "Chat") },
                        label = { Text("ቻት") },
                        selected = selectedTab == 3,
                        onClick = { selectedTab = 3 }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Payment, contentDescription = "Credits") },
                        label = { Text("ክሬዲት") },
                        selected = selectedTab == 4,
                        onClick = { selectedTab = 4 }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                        label = { Text("ፕሮፋይል") },
                        selected = selectedTab == 5,
                        onClick = { selectedTab = 5 }
                    )
                    if (userProfile?.role == "admin" || userProfile?.role == "super_admin" || userProfile?.email?.contains("admin") == true) {
                        NavigationBarItem(
                            icon = { Icon(Icons.Default.AdminPanelSettings, contentDescription = "Admin") },
                            label = { Text("አስተዳዳሪ") },
                            selected = selectedTab == 6,
                            onClick = { selectedTab = 6 }
                        )
                    }
                }
            }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
                when (selectedTab) {
                    0 -> HomeScreen(
                        viewModel = viewModel,
                        onNavigateToCredits = { selectedTab = 4 },
                        onNavigateToGroups = { selectedTab = 1 }
                    )
                    1 -> GroupsScreen(viewModel)
                    2 -> ClassesScreen(viewModel)
                    3 -> ChatScreen(viewModel)
                    4 -> CreditsScreen(viewModel)
                    5 -> ProfileScreen(viewModel) { currentUser = null }
                    6 -> AdminDashboardScreen(viewModel)
                }
            }
        }
    }
}
