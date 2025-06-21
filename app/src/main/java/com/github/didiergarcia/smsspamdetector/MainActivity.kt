package com.github.didiergarcia.smsspamdetector

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.didiergarcia.smsspamdetector.ui.SmsUi
import com.github.didiergarcia.smsspamdetector.ui.theme.SMSSpamDetectorTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel = SmsViewModel.instance

        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {

                Log.d("Receiver", "Got Intent!")

                val sender = intent?.getStringExtra("sender") ?: return
                val message = intent.getStringExtra("message") ?: return
                val result = intent.getStringExtra("result") ?: return
                viewModel.updateSms(sender, message, result)
            }
        }

        Log.d("main", "registering SMS_RESULT receiver")
        registerReceiver(receiver, IntentFilter("SMS_RESULT"), RECEIVER_NOT_EXPORTED)

        setContent {
            SmsUi(viewModel)
        }
    }
}