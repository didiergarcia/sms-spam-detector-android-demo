📱 SMS Spam Detector (Android + TFLite)

This Android app classifies incoming SMS messages as Spam or Ham (Not Spam) using a lightweight TensorFlow Lite model deployed entirely on-device. It's built with Jetpack Compose, uses StateFlow for reactive UI updates, and matches training-time tokenization to ensure high accuracy.

🚀 Features

📥 Real-time SMS Detection via system BroadcastReceiver

🤖 TFLite Inference using a BiLSTM neural network

📊 Accurate Predictions matching TensorFlow training

🧠 Tokenizer Matching via exported word_index.json

⚡️ Modern Jetpack Compose UI with live state updates

🪄 Animated SPAM/HAM Indicator that persists until next SMS

🧠 Model Overview

Framework: Keras → TensorFlow Lite

Architecture: Embedding + Bidirectional LSTM + Dense

Accuracy: ~98% on validation set

Size: < 1MB (.tflite)

Input: [1, 100] padded sequence of token IDs (int32)

🛠️ Tech Stack

Layer

Technology

UI

Jetpack Compose + Material 3

State Management

Kotlin StateFlow + ViewModel

ML Inference

TensorFlow Lite

SMS Access

Android BroadcastReceiver

Deployment

Android SDK 26+

📁 Folder Structure

com.github.didiergarcia.smsspamdetector/
├── MainActivity.kt            # Compose entry point
├── SmsReceiver.kt            # Listens for incoming SMS
├── SmsViewModel.kt           # Shared state with StateFlow
├── data/
│   └── tokenizer/
│       └── TokenizerHelper.kt
├── ui/
│   ├── SmsUi.kt              # Jetpack Compose UI
│   └── theme/
│       └── (Material theme files)
└── assets/
├── spam_classifier.tflite
└── word_index.json

🛠️ Setup & Testing

Clone the repo and open in Android Studio (Arctic Fox or newer)

The required model files (.tflite, word_index.json) are already in src/main/assets/

Build & run the app on an emulator or device (API level 26+)

When prompted, grant SMS permissions to the app

Set the app as your default SMS handler

In the Android emulator, use Extended Controls → Phone → SMS to send a test message

🔐 Permissions Required

<uses-permission android:name="android.permission.RECEIVE_SMS" />
<uses-permission android:name="android.permission.READ_SMS" />

Note: App must be set as the default SMS handler to receive messages on Android 10+

✨ Example

When an SMS like:

Congratulations! You've won a free giftcard. Click here!

is received, the app will display:

Sender:  555-1234
Message: Congratulations! You've won...
Result:  SPAM

🧐 Gotchas / Tips

Ensure input text is cleaned + tokenized to match training

Model must be exported with input_dtype=int32

Use StateFlow instead of BroadcastReceiver for modern reactive updates

Set your emulator/device to allow SMS permissions manually if needed

📜 License

MIT License — free to use and modify.