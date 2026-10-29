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
import androidx.compose.ui.unit.sp
import com.example.ui.viewmodel.MaqraahViewModel

@Composable
fun RegistrationScreen(
    viewModel: MaqraahViewModel,
    onRegistrationComplete: () -> Unit
) {
    var fullName by remember { mutableStateOf("") }
    var ageText by remember { mutableStateOf("22") }
    var gender by remember { mutableStateOf("ወንድ (Male)") }
    var city by remember { mutableStateOf("አዲስ አበባ") }
    var phone by remember { mutableStateOf("+2519") }
    var quranLevel by remember { mutableStateOf("መሰረታዊ (Beginner)") }
    var programType by remember { mutableStateOf("ሀፍስ ኢጃዛ (Hafs Ijazah)") }
    var shift by remember { mutableStateOf("የጧት ፈረቃ (Morning)") }
    var avatarEmoji by remember { mutableStateOf("🧔🏽‍♂️") }

    val genders = listOf("ወንድ (Male)", "ሴት (Female)")
    val shifts = listOf("የጧት ፈረቃ (Morning) [9:00-12:00]", "የከሰአት ፈረቃ (Afternoon) [2:00-3:30]", "የማታ ፈረቃ (Evening) [9:00-11:00]")
    val emojis = listOf("🧔🏽‍♂️", "🧕🏽", "👳🏽‍♂️", "🧕🏼", "👨🏽‍🎓", "👩🏽‍🎓")
    val programs = listOf(
        "ተልቂን እና ንባብ ማስተካከያ (Talqeen)",
        "ሂፍዝ (Hifz)",
        "ሀፍስ ኢጃዛ (Hafs Ijazah)",
        "ሌሎች ቂራአቶች (Other Qira'at)",
        "ተጅዊድ ቲዎሪ (Tajweed)",
        "አልጀዘሪያ (Al-Jazari)",
        "ቱህፈቱል አጥፋል (Tuhfat al-Atfal)"
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
                .widthIn(max = 600.dp)
                .verticalScroll(rememberScrollState())
                .padding(vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "የኢማም አልጀዘሪ መቅራአ ምዝገባ",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Registration & Automatic Group Assignment",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Avatar Emoji Picker
            Text("የፕሮፋይል ኢስላማዊ ኢሞጂ ይምረጡ:", fontWeight = FontWeight.SemiBold, modifier = Modifier.align(Alignment.Start))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                emojis.forEach { emo ->
                    Button(
                        onClick = { avatarEmoji = emo },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (avatarEmoji == emo) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                        ),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(emo, fontSize = 20.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = { Text("ሙሉ ስም (Full Name)") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = ageText,
                    onValueChange = { ageText = it },
                    label = { Text("ዕድሜ (Age)") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = city,
                    onValueChange = { city = it },
                    label = { Text("ከተማ (City)") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("ስልክ ቁጥር (Phone)") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("ጾታ (Gender):", fontWeight = FontWeight.SemiBold, modifier = Modifier.align(Alignment.Start))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                genders.forEach { g ->
                    Button(
                        onClick = { gender = g },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (gender == g) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                        ),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(g)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("የፈረቃ ሰዓት ምርጫ (Shift Selection):", fontWeight = FontWeight.SemiBold, modifier = Modifier.align(Alignment.Start))
            shifts.forEach { sh ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    RadioButton(
                        selected = shift == sh,
                        onClick = { shift = sh }
                    )
                    Text(sh, modifier = Modifier.padding(start = 8.dp), style = MaterialTheme.typography.bodySmall)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("የሚፈለገው ፕሮግራም (Program):", fontWeight = FontWeight.SemiBold, modifier = Modifier.align(Alignment.Start))
            programs.forEach { prog ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    RadioButton(
                        selected = programType == prog,
                        onClick = { programType = prog }
                    )
                    Text(prog, modifier = Modifier.padding(start = 8.dp), style = MaterialTheme.typography.bodySmall)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val age = ageText.toIntOrNull() ?: 22
                    viewModel.registerUser(
                        name = fullName.ifBlank { "ተማሪ" },
                        age = age,
                        gender = gender,
                        city = city,
                        phone = phone,
                        quranLevel = quranLevel,
                        programType = programType,
                        shift = shift,
                        avatarEmoji = avatarEmoji
                    )
                    onRegistrationComplete()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = fullName.isNotBlank()
            ) {
                Text("ምዝገባ አጠናቅ እና ወደ ቡድን ግባ", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}
