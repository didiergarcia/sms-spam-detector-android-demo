package com.github.didiergarcia.smsspamdetector

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsMessage
import android.util.Log

class SmsReciever : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            val bundle = intent.extras
            val pdus = bundle?.get("pdus") as? Array<*>
            val messages = pdus?.mapNotNull {
                SmsMessage.createFromPdu(it as ByteArray, bundle.getString("format"))
            }
            messages?.forEach { sms ->
                val sender = sms.displayOriginatingAddress
                val messageBody = sms.displayMessageBody
                Log.d("SMS", "From: $sender, Message: $messageBody")
                // Call classifyMessage() here
            }
        }
    }
}