package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.viewmodel.MaqraahViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun ProfileScreen(
    viewModel: MaqraahViewModel,
    onLoggedOut: () -> Unit
) {
    val userProfile by viewModel.currentUserProfile.collectAsState()

    var showProgramChange by remember { mutableStateOf(false) }
    var requestedProgram by remember { mutableStateOf("ሂፍዝ (Hifz)") }
    var changeReason by remember { mutableStateOf("") }

    val programs = listOf(
        "ተልቂን እና ንባብ ማስተካከያ (Talqeen)",
        "ሂፍዝ (Hifz)",
        "ሀፍስ ኢጃዛ (Hafs Ijazah)",
        "ሌሎች ቂራአቶች (Other Qira'at)",
        "ተጅዊድ ቲዎሪ (Tajweed)",
        "አልጀዘሪያ (Al-Jazari)"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 500.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = userProfile?.avatarEmoji ?: "🧔🏽‍♂️",
                fontSize = 64.sp
            )

            Text(
                text = userProfile?.name ?: "ተማሪ",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Surface(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "የተማሪ ኮድ: ${userProfile?.studentCode ?: "IM-0001"}",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontWeight = FontWeight.Bold
                )
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("ኢሜይል: ${userProfile?.email ?: ""}", fontWeight = FontWeight.Bold)
                    Text("ስልክ: ${userProfile?.phone ?: ""}")
                    Text("አድራሻ / ከተማ: ${userProfile?.city ?: ""}")
                    Text("ጾታ: ${userProfile?.gender ?: ""}")
                    Text("የፈረቃ ሰዓት: ${userProfile?.shift ?: ""}")
                    Text("የቂርአት ፕሮግራም: ${userProfile?.programType ?: ""}")
                    Text("የደቂቃ ቀሪ ሂሳብ: ${userProfile?.minuteBalance ?: 0} ደቂቃ", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                }
            }

            // Program Change Request Trigger
            Button(
                onClick = { showProgramChange = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("የፕሮግራም ቅየራ ጥያቄ ማቅረብ (Request Program Change)")
            }

            if (showProgramChange) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("አዲስ የሚፈልጉት ፕሮግራም:", fontWeight = FontWeight.Bold)
                        programs.forEach { p ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(selected = requestedProgram == p, onClick = { requestedProgram = p })
                                Text(p, style = MaterialTheme.typography.bodySmall)
                            }
                        }
                        OutlinedTextField(
                            value = changeReason,
                            onValueChange = { changeReason = it },
                            label = { Text("ምክንያት (Reason for change)") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Button(onClick = {
                            viewModel.submitProgramChangeRequest(userProfile?.programType ?: "", requestedProgram, changeReason)
                            showProgramChange = false
                            changeReason = ""
                        }) {
                            Text("ጥያቄ ላክ")
                        }
                    }
                }
            }

            // Quranic Statistics Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("የቂርአት ስታትፊክስ (Quranic Statistics)", fontWeight = FontWeight.Bold)
                    Text("• የተሳተፉባቸው ክፍሎች: 14")
                    Text("• የተነበቡ አያቶች: 340")
                    Text("• ያገለገሉበት ጠቅላላ ሰዓት: 12 ሰዓት")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    Firebase.auth.signOut()
                    onLoggedOut()
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.ExitToApp, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("ውጣ (Log Out)")
            }
        }
    }
}
