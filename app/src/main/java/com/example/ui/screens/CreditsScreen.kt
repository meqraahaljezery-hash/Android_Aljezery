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
import com.example.ui.viewmodel.MaqraahViewModel

data class CreditPackage(val name: String, val minutes: Int, val priceEtb: Int)

@Composable
fun CreditsScreen(viewModel: MaqraahViewModel) {
    val userProfile by viewModel.currentUserProfile.collectAsState()

    var selectedMethod by remember { mutableStateOf("Telebirr") }
    val methods = listOf("Telebirr", "CBE (ንግድ ባንክ)", "BOA (አቢሲኒያ)", "M-Pesa")

    val packages = listOf(
        CreditPackage("መሰረታዊ ፓኬጅ", 100, 500),
        CreditPackage("መካከለኛ ፓኬጅ", 200, 1000),
        CreditPackage("የላቀ ፓኬጅ", 300, 1470),
        CreditPackage("ፕሮ ፓኬጅ", 400, 1920),
        CreditPackage("ወርቃማ ፓኬጅ", 500, 2350),
        CreditPackage("ታላቅ ፓኬጅ", 600, 2760),
        CreditPackage("ቪአይፒ ኢጃዛ ፓኬጅ", 1000, 4500)
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
            item {
                Text(
                    text = "የደቂቃ ሂሳብ እና ፓኬጆች መሙያ",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text("1 ደቂቃ = 5 የኢትዮጵያ ብር (ከፍተኛ ቅናሽ ፓኬጆች)", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("የቀሪ ሂሳብዎ (Current Balance):", style = MaterialTheme.typography.bodySmall)
                        Text(
                            text = "${userProfile?.minuteBalance ?: 0} ደቂቃ",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }

            item {
                Text("የክፍያ ዘዴ ይምረጡ (Payment Method):", fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    methods.forEach { m ->
                        Button(
                            onClick = { selectedMethod = m },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedMethod == m) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                            ),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(m, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }

            item {
                Text("የጥቅሎች ዝርዝር እና መለያ ስም (Packages & Pricing):", fontWeight = FontWeight.SemiBold)
            }

            items(packages) { pkg ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(pkg.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Text("${pkg.minutes} ደቂቃ - ${pkg.priceEtb} ETB", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
                        }
                        Button(onClick = {
                            viewModel.submitPayment(pkg.priceEtb, pkg.minutes, selectedMethod)
                        }) {
                            Text("ይግዙ (Buy)")
                        }
                    }
                }
            }
        }
    }
}
