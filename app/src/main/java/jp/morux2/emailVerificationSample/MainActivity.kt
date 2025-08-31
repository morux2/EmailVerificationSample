package jp.morux2.emailVerificationSample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val keyboardController = LocalSoftwareKeyboardController.current
            val focusManager = LocalFocusManager.current
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .imePadding()
                    .clickable(
                        // ripple を無効にする
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = {
                            // 画面外タップでフォーカスを解除しキーボードを閉じる
                            keyboardController?.hide()
                            focusManager.clearFocus()
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                EmailVerificationCodeTextField()
            }
        }
    }
}