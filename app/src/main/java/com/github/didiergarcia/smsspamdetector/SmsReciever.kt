package com.github.didiergarcia.smsspamdetector

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsMessage
import android.util.Log
import org.json.JSONObject
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import java.util.Arrays

class SmsReciever : BroadcastReceiver() {

    // The Tokenizer Implementation MUST match what you do to create your tokenizer in your full
    // TensorFlow model. This tokenizer took some work to get right because originally it just
    // lowering casing the text instead of following: tf.keras.preprocessing.text.Tokenizer
    //
    // See the Tokenizer for the Full model here:
    // https://github.com/didiergarcia/sms-spam-tflite-model/blob/main/tflite_sms_spam_model.ipynb
    object TokenizerHelper {
        private const val MAX_LEN = 100
        private const val OOV_TOKEN_INDEX = 1 // Keras sets OOV index to 1 if using oov_token

        private var wordIndex: Map<String, Int>? = null

        fun loadWordIndex(context: Context) {
            val json = context.assets.open("word_index.json").bufferedReader().use { it.readText() }
            wordIndex = JSONObject(json).let { obj ->
                obj.keys().asSequence().associateWith { obj.getInt(it) }
            }
        }

        fun cleanAndTokenize(text: String): IntArray {
            if (wordIndex == null) throw IllegalStateException("Call loadWordIndex() first")

            val cleaned = text.lowercase().replace(Regex("[^a-z0-9\\s]"), " ")
            val tokens = cleaned.split("\\s+".toRegex()).map {
                val idx = wordIndex?.get(it) ?: OOV_TOKEN_INDEX
                if (idx >= 10000) OOV_TOKEN_INDEX else idx
            }

            val padded = IntArray(MAX_LEN) { 0 }
            for (i in tokens.indices) {
                if (i < MAX_LEN) padded[i] = tokens[i]
            }
            return padded
        }
    }

    private fun loadModelFile(context: Context, modelName: String): MappedByteBuffer {
        val fileDescriptor = context.assets.openFd(modelName)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, fileDescriptor.startOffset, fileDescriptor.declaredLength)
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null && intent != null ) {
            val bundle = intent.extras
            val pdus = bundle?.get("pdus") as? Array<*>
            val messages = pdus?.mapNotNull {
                SmsMessage.createFromPdu(it as ByteArray, bundle.getString("format"))
            }

            TokenizerHelper.loadWordIndex(context)
            val model = Interpreter(loadModelFile(context, "spam_classifier.tflite"))

            messages?.forEach { sms ->
                val sender = sms.displayOriginatingAddress
                val messageBody = sms.displayMessageBody
                Log.d("SMS", "From: $sender, Message: $messageBody")

                val inputTensorType = model.getInputTensor(0).dataType()
                Log.d("Model", "Expected input type: $inputTensorType")

                val inputTensor: Array<IntArray> = arrayOf(
                    TokenizerHelper.cleanAndTokenize(messageBody)
                )
                Log.d("Model", Arrays.toString(inputTensor[0]))

                val output = Array(1) { FloatArray(1) } // shape [1, 1]

                model.run(inputTensor, output)

                model.run(inputTensor, output)
                val result =  if (output[0][0] > 0.5) "Spam" else "Ham"
                Log.d("SMS", "detected ${result} - ${output[0][0]}")

                SmsViewModel.instance.updateSms(sender, messageBody, result)
                Log.d("SMS", "sending SMS_RESULT")
            }
        }
    }
}