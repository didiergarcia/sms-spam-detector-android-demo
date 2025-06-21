package com.github.didiergarcia.smsspamdetector.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.didiergarcia.smsspamdetector.SmsViewModel

@Composable
fun SmsUi(viewModel: SmsViewModel) {
    val sender by viewModel.sender.collectAsState()
    val message by viewModel.message.collectAsState()
    val result by viewModel.result.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Sender:", fontSize = 20.sp)
            Text(sender, fontSize = 24.sp, textAlign = TextAlign.Center)

            Spacer(modifier = Modifier.height(16.dp))

            Text("Message:", fontSize = 20.sp)
            Text(message, fontSize = 20.sp, textAlign = TextAlign.Center)

            Spacer(modifier = Modifier.height(32.dp))

            AnimatedVisibility(
                visible = result != null,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Text(
                    text = result?.uppercase() ?: "",
                    fontSize = 36.sp,
                    color = if (result == "Spam") MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
