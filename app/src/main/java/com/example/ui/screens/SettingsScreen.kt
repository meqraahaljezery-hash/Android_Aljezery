package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.viewmodel.MaqraahViewModel

@Composable
fun SettingsScreen(viewModel: MaqraahViewModel) {
    val currentLang by viewModel.currentLanguage.collectAsState()
    val isDarkMode by viewModel.isDarkMode.collectAsState()
    val fontSizeScale by viewModel.fontSizeScale.collectAsState()
    val chatColor by viewModel.chatColorOption.collectAsState()

    val colors = listOf("Emerald", "Gold", "Navy", "Charcoal")

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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "መቼቶች እና ማስተካከያዎች (Settings)",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            // Language Settings
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("ቋንቋ ይምረጡ (Language)", fontWeight = FontWeight.Bold)
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = { viewModel.setLanguage("am") },
                            colors = ButtonDefaults.buttonColors(containerColor = if (currentLang == "am") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant),
                            modifier = Modifier.weight(1f)
                        ) { Text("አማርኛ") }
                        Button(
                            onClick = { viewModel.setLanguage("ar") },
                            colors = ButtonDefaults.buttonColors(containerColor = if (currentLang == "ar") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant),
                            modifier = Modifier.weight(1f)
                        ) { Text("العربية") }
                        Button(
                            onClick = { viewModel.setLanguage("en") },
                            colors = ButtonDefaults.buttonColors(containerColor = if (currentLang == "en") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant),
                            modifier = Modifier.weight(1f)
                        ) { Text("English") }
                    }
                }
            }

            // Dark / Light Mode
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("የጨለማ ሞድ (Dark Mode)", fontWeight = FontWeight.Bold)
                    Switch(checked = isDarkMode, onCheckedChange = { viewModel.setDarkMode(it) })
                }
            }

            // Font Size Adjustment
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("የፎንት ትልቀት ማስተካከያ (Font Scale)", fontWeight = FontWeight.Bold)
                    Slider(
                        value = fontSizeScale,
                        onValueChange = { viewModel.setFontSizeScale(it) },
                        valueRange = 0.8f..1.4f
                    )
                }
            }

            // Chat Color Theme Option
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("የቻት እስክሪን ቀለማት ምርጫ (Chat Color)", fontWeight = FontWeight.Bold)
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        colors.forEach { col ->
                            Button(
                                onClick = { viewModel.setChatColorOption(col) },
                                colors = ButtonDefaults.buttonColors(containerColor = if (chatColor == col) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant),
                                modifier = Modifier.weight(1f)
                            ) { Text(col, style = MaterialTheme.typography.bodySmall) }
                        }
                    }
                }
            }

            // About & Privacy Policy
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("ስለ መተግበሪያው (About)", fontWeight = FontWeight.Bold)
                    Text("ኢማም አልጀዘሪ መቅራአ - الإصدار 1.0", style = MaterialTheme.typography.bodySmall)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("ግላዊነት ፖሊሲ (Privacy Policy):", fontWeight = FontWeight.Bold)
                    Text("ሁሉም የተማሪዎች ድምጽ እና የውይይት መረጃዎች ጥብቅ ምስጢርነት የተጠበቁ ናቸው።", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
