package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.VideoCall
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.VideocamOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.model.StudyGroup
import com.example.data.model.UserProfile
import com.example.ui.viewmodel.MaqraahViewModel

@Composable
fun GroupsScreen(viewModel: MaqraahViewModel) {
    val userProfile by viewModel.currentUserProfile.collectAsState()
    val studyGroups by viewModel.studyGroups.collectAsState()
    val allUsers by viewModel.allUsers.collectAsState()

    var selectedGroup by remember { mutableStateOf<StudyGroup?>(null) }
    var inLiveCall by remember { mutableStateOf(false) }
    var showMemberProfile by remember { mutableStateOf<UserProfile?>(null) }

    val isPrivileged = userProfile?.role == "teacher" || userProfile?.role == "admin" || userProfile?.role == "super_admin"

    if (inLiveCall && selectedGroup != null) {
        LiveKitCallScreen(group = selectedGroup!!) {
            inLiveCall = false
        }
    } else if (selectedGroup != null) {
        // Group Detail & Chat Screen
        GroupDetailScreen(
            group = selectedGroup!!,
            userProfile = userProfile,
            allUsers = allUsers,
            isPrivileged = isPrivileged,
            onStartCall = { inLiveCall = true },
            onBack = { selectedGroup = null },
            onSendFriendReq = { member ->
                viewModel.sendFriendshipRequest(member.id, member.name, member.studentCode)
            }
        )
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = "የቂርአት ቡድኖች (7 ፈረቃ እና ጾታ ቡድኖች)",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text("በምዝገባ ወቅት በመረጡት ፈረቃ እና ጾታ መሰረት የተደራጁ ግሩፖች", style = MaterialTheme.typography.bodyMedium)

                Spacer(modifier = Modifier.height(16.dp))

                if (studyGroups.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(studyGroups) { group ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(group.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                        Surface(
                                            color = MaterialTheme.colorScheme.primaryContainer,
                                            shape = RoundedCornerShape(6.dp)
                                        ) {
                                            Text(
                                                text = group.nameAr,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                                style = MaterialTheme.typography.bodySmall
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(group.description, style = MaterialTheme.typography.bodyMedium)
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text("ኃላፊ/ሙቅሪዕ: ${group.leaderName}", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold)
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Button(
                                        onClick = { selectedGroup = group },
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Icon(Icons.Default.Group, contentDescription = null)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("ወደ ቡድኑ ግባ (Open Group & Chat)")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GroupDetailScreen(
    group: StudyGroup,
    userProfile: UserProfile?,
    allUsers: List<UserProfile>,
    isPrivileged: Boolean,
    onStartCall: () -> Unit,
    onBack: () -> Unit,
    onSendFriendReq: (UserProfile) -> Unit
) {
    var tabIndex by remember { mutableIntStateOf(0) }
    var chatText by remember { mutableStateOf("") }
    val chatMessages = remember { mutableStateListOf(Pair("አስተማሪ ሼህ አህመድ", "እንኳን ወደ ${group.name} በደህና መጡ!")) }
    var showMembers by remember { mutableStateOf(false) }

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
                TextButton(onClick = onBack) {
                    Text("← ተመለስ")
                }
                Text(group.name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                TextButton(onClick = { showMembers = !showMembers }) {
                    Text("ሜንበሮች (${allUsers.size})")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (isPrivileged) {
                Button(
                    onClick = onStartCall,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) {
                    Icon(Icons.Default.VideoCall, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("ቀጥታ ስርጭት/ድምጽ መደወያ ጀምር (Start Live Session)")
                }
                Spacer(modifier = Modifier.height(8.dp))
            } else {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "ተማሪዎች የቀጥታ ስርጭት መጀመር አይችሉም። ሙቅሪዕ ወይም አድሚን ሲጀምር መሳተፍ ይችላሉ።",
                        modifier = Modifier.padding(12.dp),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (showMembers) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    LazyColumn(modifier = Modifier.padding(8.dp)) {
                        items(allUsers) { member ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("${member.avatarEmoji} ${member.name} (${member.studentCode})", style = MaterialTheme.typography.bodyMedium)
                                IconButton(onClick = { onSendFriendReq(member) }) {
                                    Icon(Icons.Default.PersonAdd, contentDescription = "Add Friend", tint = MaterialTheme.colorScheme.primary)
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Group Chat Stream
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(chatMessages) { msg ->
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(msg.first, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
                            Text(msg.second, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = chatText,
                    onValueChange = { chatText = it },
                    placeholder = { Text("መልዕክት ይጻፉ...") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = {
                    if (chatText.isNotBlank()) {
                        chatMessages.add(Pair(userProfile?.name ?: "ተማሪ", chatText))
                        chatText = ""
                    }
                }) {
                    Text("ላክ")
                }
            }
        }
    }
}

@Composable
fun LiveKitCallScreen(group: StudyGroup, onLeave: () -> Unit) {
    var isMuted by remember { mutableStateOf(false) }
    var isVideoEnabled by remember { mutableStateOf(true) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(group.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("● LiveKit Active", modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), style = MaterialTheme.typography.bodySmall)
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(vertical = 16.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = if (isVideoEnabled) Icons.Default.Group else Icons.Default.VideocamOff,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("የቡድን የቀጥታ ስርጭት (Live Room)", style = MaterialTheme.typography.titleMedium)
                        Text("ኃላፊ: ${group.leaderName}", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { isMuted = !isMuted }) {
                    Icon(
                        imageVector = if (isMuted) Icons.Default.MicOff else Icons.Default.Mic,
                        contentDescription = "Mute",
                        tint = if (isMuted) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                    )
                }

                Button(
                    onClick = onLeave,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("ክፍል ውጣ")
                }

                IconButton(onClick = { isVideoEnabled = !isVideoEnabled }) {
                    Icon(
                        imageVector = if (isVideoEnabled) Icons.Default.Videocam else Icons.Default.VideocamOff,
                        contentDescription = "Video",
                        tint = if (isVideoEnabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}
