package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.viewmodel.MaqraahViewModel

@Composable
fun HomeScreen(
    viewModel: MaqraahViewModel,
    onNavigateToCredits: () -> Unit,
    onNavigateToGroups: () -> Unit
) {
    val userProfile by viewModel.currentUserProfile.collectAsState()
    val announcements by viewModel.announcements.collectAsState()
    val stories by viewModel.stories.collectAsState()

    var showStoryDialog by remember { mutableStateOf(false) }
    var storyTitle by remember { mutableStateOf("") }
    var storyContent by remember { mutableStateOf("") }

    val programsList = listOf(
        Pair("ተልቂን እና ንባብ ማስተካከያ", "በሼሆች ቁጥጥር ስር ትክክለኛ የፊደል አወጣጥ እና ተጅዊድ ማስተካከያ።"),
        Pair("ሂፍዝ (የቁርአን ቁርጥራጭ ማህፈዝ)", "በዘመናዊ ፕሮግራም ከሙቅሪዕ ጋር በሳምንት 3 ቀናት ሂፍዝ።"),
        Pair("ሀፍስ ኢጃዛ (Hafs Ijazah)", "የሀፍስ ሪዋያ በሰነድ ተصل ወደ ረሱል (ሰዐወ) የሚያደርስ ፈቃድ።"),
        Pair("አልጀዘሪያ እና ቱህፈቱል አጥፋል", "የمتون እውቀት ከሸሂዶች ጋር በጥልቀት መማር።")
    )

    val packagesList = listOf(
        Triple("መሰረታዊ ፓኬጅ (100 ደቂቃ)", "500 ETB", 100),
        Triple("መካከለኛ ፓኬጅ (300 ደቂቃ)", "1,470 ETB", 300),
        Triple("የላቀ ፓኬጅ (600 ደቂቃ)", "2,760 ETB", 600),
        Triple("ቪአይፒ ኢጃዛ ፓኬጅ (1000 ደቂቃ)", "4,500 ETB", 1000)
    )

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
            // Stories / Status Bar at the top
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Add Story Button
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.size(64.dp),
                        onClick = { showStoryDialog = true }
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.Add, contentDescription = "Add Story", tint = MaterialTheme.colorScheme.onPrimaryContainer)
                        }
                    }

                    stories.forEach { story ->
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                            modifier = Modifier.width(140.dp).height(80.dp)
                        ) {
                            Column(modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.Center) {
                                Text(story.authorName, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall, maxLines = 1)
                                Text(story.title, style = MaterialTheme.typography.bodyMedium, maxLines = 1)
                            }
                        }
                    }
                }
            }

            // Welcome & Student Code Card
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${userProfile?.avatarEmoji ?: "🧔🏽‍♂️"} ሰላም, ${userProfile?.name ?: "ተማሪ"}",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Surface(
                                color = MaterialTheme.colorScheme.secondary,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = userProfile?.studentCode ?: "IM-0001",
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    color = MaterialTheme.colorScheme.onSecondary,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Divider(color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.2f))
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("የቀሪ ደቂቃ ሂሳብ", style = MaterialTheme.typography.bodySmall)
                                Text(
                                    text = "${userProfile?.minuteBalance ?: 0} ደቂቃ",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            Column {
                                Text("ፈረቃ / ፕሮግራም", style = MaterialTheme.typography.bodySmall)
                                Text(
                                    text = "${userProfile?.shift ?: ""} | ${userProfile?.programType ?: ""}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            // Admin Announcement Alert Dashboard
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Campaign, contentDescription = null, tint = MaterialTheme.colorScheme.onErrorContainer)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("የማስታወቂያ ማንቂያ ዳሽቦርድ (Alert Dashboard)", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onErrorContainer)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        if (announcements.isEmpty()) {
                            Text("እንኳን ወደ ኢማም አልጀዘሪ መቅራአ በደህና መጡ! ጁሙአ ረፍት ሲሆን ጥናት በፈረቃ ይካሄዳል።", color = MaterialTheme.colorScheme.onErrorContainer)
                        } else {
                            Text(announcements.last().body, color = MaterialTheme.colorScheme.onErrorContainer)
                        }
                    }
                }
            }

            // Maqraah Description & Programs
            item {
                Text(
                    text = "ስለ መቅረአው እና የፕሮግራሞች ዝርዝር",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            items(programsList) { prog ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.School, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(prog.first, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(prog.second, style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = { onNavigateToGroups() },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("በዚህ ፕሮግራም ይመዝገቡ (Join Program)")
                        }
                    }
                }
            }

            // Packages & Pricing List
            item {
                Text(
                    text = "የደቂቃ ፓኬጆች እና አከፋፈል ሂደት",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            items(packagesList) { pkg ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(pkg.first, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
                            Text("ዋጋ: ${pkg.second}", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
                        }
                        Button(onClick = { onNavigateToCredits() }) {
                            Text("ይግዙ (Buy)")
                        }
                    }
                }
            }
        }

        if (showStoryDialog) {
            AlertDialog(
                onDismissRequest = { showStoryDialog = false },
                title = { Text("አዲስ ስቶሪ መልቀቅ (Publish Story)") },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = storyTitle,
                            onValueChange = { storyTitle = it },
                            label = { Text("ርዕስ (Title)") }
                        )
                        OutlinedTextField(
                            value = storyContent,
                            onValueChange = { storyContent = it },
                            label = { Text("ይዘት (Content)") }
                        )
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        if (storyTitle.isNotBlank()) {
                            viewModel.publishStory(storyTitle, storyContent)
                            storyTitle = ""
                            storyContent = ""
                            showStoryDialog = false
                        }
                    }) {
                        Text("ለቀቅ")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showStoryDialog = false }) {
                        Text("ሰርዝ")
                    }
                }
            )
        }
    }
}
