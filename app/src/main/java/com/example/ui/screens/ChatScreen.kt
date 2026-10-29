package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.viewmodel.MaqraahViewModel

@Composable
fun ChatScreen(viewModel: MaqraahViewModel) {
    val userProfile by viewModel.currentUserProfile.collectAsState()
    val allUsers by viewModel.allUsers.collectAsState()
    val friendshipRequests by viewModel.friendshipRequests.collectAsState()

    var selectedTab by remember { mutableStateOf("የግል ውይይት") }
    val tabs = listOf("የግል ውይይት", "ግሩፖች", "ማንቂያዎች/ኖቲፊኬሽን")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "ውይይቶች (Telegram Style Chat)",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Telegram-style Header Tabs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                tabs.forEach { tab ->
                    Button(
                        onClick = { selectedTab = tab },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedTab == tab) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                        ),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text(tab, color = if (selectedTab == tab) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            when (selectedTab) {
                "የግል ውይይት" -> {
                    Column(modifier = Modifier.fillMaxSize()) {
                        if (friendshipRequests.isNotEmpty()) {
                            Text("የጓደኝነት ጥያቄዎች (Friendship Requests):", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
                            Spacer(modifier = Modifier.height(8.dp))
                            LazyColumn(modifier = Modifier.height(120.dp)) {
                                items(friendshipRequests) { req ->
                                    Card(
                                        modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth().padding(12.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text("${req.senderName} (${req.senderCode}) ጓደኝነት ጠይቆዎታል", style = MaterialTheme.typography.bodySmall)
                                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                                IconButton(onClick = { viewModel.updateFriendshipStatus(req.id, "Accepted") }) {
                                                    Icon(Icons.Default.Check, contentDescription = "Accept", tint = MaterialTheme.colorScheme.primary)
                                                }
                                                IconButton(onClick = { viewModel.updateFriendshipStatus(req.id, "Rejected") }) {
                                                    Icon(Icons.Default.Close, contentDescription = "Reject", tint = MaterialTheme.colorScheme.error)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        Text("ተጠቃሚዎች እና ኡስታዞች (Contacts):", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(allUsers.filter { it.id != userProfile?.id }) { contact ->
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(contact.avatarEmoji, fontSize = 28.sp)
                                            Spacer(modifier = Modifier.width(12.dp))
                                            Column {
                                                Text(contact.name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
                                                Text("ኮድ: ${contact.studentCode} | ሚና: ${contact.role}", style = MaterialTheme.typography.bodySmall)
                                            }
                                        }
                                        Button(onClick = {
                                            viewModel.sendFriendshipRequest(contact.id, contact.name, contact.studentCode)
                                        }) {
                                            Text("ውይይት ጠይቅ")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                "ግሩፖች" -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("እባክዎ ከታች ካለው 'ቡድኖች' ማውጫ ግሩፖቹን ይምረጡ።")
                    }
                }
                "ማንቂያዎች/ኖቲፊኬሽን" -> {
                    LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        item {
                            Card(modifier = Modifier.fillMaxWidth()) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text("የመቅረአው ኦፊሻል ኖቲፊኬሽን", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("እንኳን ደስ አለዎት! አዲስ የኢጃዛ ሰሰን በነገው ዕለት ይጀምራል።")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
