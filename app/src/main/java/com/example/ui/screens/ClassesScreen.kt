package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VideoCall
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.viewmodel.MaqraahViewModel

@Composable
fun ClassesScreen(viewModel: MaqraahViewModel) {
    val classes by viewModel.classes.collectAsState()
    val userProfile by viewModel.currentUserProfile.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var classTitle by remember { mutableStateOf("") }
    var classTime by remember { mutableStateOf("ዛሬ 10፡00 ሰዓት") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "የቀጥታ የቁርአን ትምህርት ክፍሎች",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                if (userProfile?.role == "teacher" || userProfile?.role == "admin" || userProfile?.role == "super_admin") {
                    Button(onClick = { showDialog = true }) {
                        Text("ክፍል ፍጠር")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (classes.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("ምንም የቀጥታ ትምህርት ክፍል የለም።", color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f))
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(classes) { cls ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(cls.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("ሙቅሪዕ: ${cls.teacherName}", style = MaterialTheme.typography.bodyMedium)
                                Text("ሰዓት: ${cls.scheduleTime}", style = MaterialTheme.typography.bodySmall)
                                Spacer(modifier = Modifier.height(12.dp))
                                Button(
                                    onClick = { /* Join LiveKit Session */ },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(Icons.Default.VideoCall, contentDescription = null)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("ወደ ክፍል ግባ (Join Live Session)")
                                }
                            }
                        }
                    }
                }
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("አዲስ የትምህርት ክፍል መፍጠር") },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = classTitle,
                            onValueChange = { classTitle = it },
                            label = { Text("የክፍል ርዕስ (Title)") }
                        )
                        OutlinedTextField(
                            value = classTime,
                            onValueChange = { classTime = it },
                            label = { Text("ሰዓት (Time)") }
                        )
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        if (classTitle.isNotBlank()) {
                            viewModel.scheduleClass(
                                title = classTitle,
                                teacherName = userProfile?.name ?: "ሙቅሪዕ",
                                time = classTime
                            )
                            showDialog = false
                            classTitle = ""
                        }
                    }) {
                        Text("ፍጠር")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("ሰርዝ")
                    }
                }
            )
        }
    }
}
