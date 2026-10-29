package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.model.UserProfile
import com.example.ui.viewmodel.MaqraahViewModel

@Composable
fun AdminDashboardScreen(viewModel: MaqraahViewModel) {
    val paymentRequests by viewModel.paymentRequests.collectAsState()
    val programChangeRequests by viewModel.programChangeRequests.collectAsState()
    val allUsers by viewModel.allUsers.collectAsState()

    var selectedUserForAction by remember { mutableStateOf<UserProfile?>(null) }
    var bonusMinutesInput by remember { mutableStateOf("50") }
    var announcementTitle by remember { mutableStateOf("") }
    var announcementBody by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "የአስተዳዳሪ ፓኔል (Super Admin & Admin Panel)",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // Statistics Dashboard
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("የገቢ እና ደቂቃዎች ስታቲስቲክስ (Analytics)", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
                        Text("• ጠቅላላ ገቢ (Revenue): 24,500 ETB")
                        Text("• የተሸጡ ደቂቃዎች (Sold Minutes): 4,900 ደቂቃ")
                        Text("• የተቀሩ ተጠቃሚዎች ደቂቃዎች: 12,400 ደቂቃ")
                        Text("• ዕለታዊ/ሳምንታዊ/ወርሃዊ ንጽጽር: የተረጋጋ ዕድገት አሳይቷል")
                    }
                }
            }

            // Post Announcement
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("የማስታወቂያ ማንቂያ አስተካካይ (Alert Dashboard)", fontWeight = FontWeight.Bold)
                        OutlinedTextField(
                            value = announcementTitle,
                            onValueChange = { announcementTitle = it },
                            label = { Text("ርዕስ (Title)") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = announcementBody,
                            onValueChange = { announcementBody = it },
                            label = { Text("የማንቂያ መልዕክት (Alert Body)") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Button(
                            onClick = {
                                if (announcementTitle.isNotBlank() && announcementBody.isNotBlank()) {
                                    viewModel.postAnnouncement(announcementTitle, announcementBody)
                                    announcementTitle = ""
                                    announcementBody = ""
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("ማንቂያውን ለሁሉም ተማሪዎች ለቀቅ")
                        }
                    }
                }
            }

            // Program Change Requests
            item {
                Text("የፕሮግራም ቅየራ ጥያቄዎች (Program Requests)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }

            if (programChangeRequests.isEmpty()) {
                item { Text("ምንም የፕሮግራም ቅየራ ጥያቄ የለም።") }
            } else {
                items(programChangeRequests) { req ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("ተማሪ: ${req.userName}", fontWeight = FontWeight.Bold)
                            Text("ከ '${req.currentProgram}' ወደ '${req.requestedProgram}'")
                            Text("ምክንያት: ${req.reason}")
                            Text("ሁኔታ: ${req.status}")
                            Spacer(modifier = Modifier.height(8.dp))
                            if (req.status == "Pending") {
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Button(onClick = {
                                        viewModel.updateProgramChangeStatus(req.id, req.userId, req.requestedProgram, "Approved")
                                    }) {
                                        Text("አጽድቅ")
                                    }
                                    Button(
                                        onClick = {
                                            viewModel.updateProgramChangeStatus(req.id, req.userId, req.currentProgram, "Rejected")
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                                    ) {
                                        Text("ውድቅ አድርግ")
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Payment Requests
            item {
                Text("የክፍያ ጥያቄዎች እና ማረጋገጫ (Payment Requests)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }

            if (paymentRequests.isEmpty()) {
                item { Text("ምንም የክፍያ ጥያቄ የለም።") }
            } else {
                items(paymentRequests) { req ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("ተማሪ: ${req.userName}", fontWeight = FontWeight.Bold)
                            Text("መጠን: ${req.amountEtb} ETB (${req.minutesRequested} ደቂቃ)")
                            Text("ዘዴ: ${req.paymentMethod}")
                            Text("ሁኔታ: ${req.status}")
                            Spacer(modifier = Modifier.height(8.dp))
                            if (req.status == "Pending") {
                                Button(onClick = {
                                    viewModel.approvePayment(req.id, req.userId, req.minutesRequested)
                                }) {
                                    Text("ክፍያ አረጋግጥ እና ደቂቃ ጨምር")
                                }
                            }
                        }
                    }
                }
            }

            // User Management List
            item {
                Text("የተጠቃሚዎች አስተዳደር (User Management)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }

            items(allUsers) { user ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("${user.avatarEmoji} ${user.name} (${user.studentCode})", fontWeight = FontWeight.Bold)
                            Text("ሚና: ${user.role} | ሚዛን: ${user.minuteBalance} ደቂቃ", style = MaterialTheme.typography.bodySmall)
                            Text("ኢሜይል: ${user.email}", style = MaterialTheme.typography.bodySmall)
                        }
                        Button(onClick = { selectedUserForAction = user }) {
                            Text("አስተዳድር")
                        }
                    }
                }
            }
        }

        if (selectedUserForAction != null) {
            val u = selectedUserForAction!!
            AlertDialog(
                onDismissRequest = { selectedUserForAction = null },
                title = { Text("ተጠቃሚ ማስተዳደር: ${u.name}") },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("ኢሜይል: ${u.email}")
                        Text("አሁን ያለው ሚና: ${u.role}")
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("ለዚህ ተጠቃሚ ተጨማሪ ደቂቃ ሽልማት ላክ:")
                        OutlinedTextField(
                            value = bonusMinutesInput,
                            onValueChange = { bonusMinutesInput = it },
                            label = { Text("የደቂቃ መጠን") }
                        )
                        Button(onClick = {
                            val mins = bonusMinutesInput.toIntOrNull() ?: 50
                            viewModel.addBonusMinutes(u.id, mins)
                            selectedUserForAction = null
                        }) {
                            Text("ደቂቃ ጨምር (Send Bonus Minutes)")
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(onClick = {
                                val nextRole = if (u.role == "teacher") "student" else "teacher"
                                viewModel.updateUserRole(u.id, nextRole)
                                selectedUserForAction = null
                            }) {
                                Text(if (u.role == "teacher") "እንደ ተማሪ አድርግ" else "እንደ ኡስታዝ/ሙቅሪዕ አድርግ")
                            }
                            Button(
                                onClick = {
                                    viewModel.blockUser(u.id, !u.isBlocked)
                                    selectedUserForAction = null
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                            ) {
                                Text(if (u.isBlocked) "እግዳ አንሳ (Unblock)" else "አግድ (Block)")
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { selectedUserForAction = null }) {
                        Text("ዝጋ")
                    }
                }
            )
        }
    }
}
