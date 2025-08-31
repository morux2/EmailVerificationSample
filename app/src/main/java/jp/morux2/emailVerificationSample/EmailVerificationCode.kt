package jp.morux2.emailVerificationSample

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

private const val VERIFICATION_CODE_LENGTH = 6
private val focusColor = Color.Green

@Composable
internal fun EmailVerificationCodeTextField() {
    var emailCode by rememberSaveable { mutableStateOf("") }
    // カーソルのハンドルおよびテキスト範囲選択時の背景色を非表示にする
    val noHandleColors = TextSelectionColors(
        handleColor = Color.Transparent,
        backgroundColor = Color.Transparent,
    )
    var focusState: FocusState? by remember { mutableStateOf(null) }
    CompositionLocalProvider(LocalTextSelectionColors provides noHandleColors) {
        BasicTextField(
            modifier = Modifier
                .onFocusChanged {
                    focusState = it
                },
            value = TextFieldValue(
                text = emailCode,
                // カーソルは常に末尾に配置する
                selection = TextRange(emailCode.length)
            ),
            onValueChange = {
                val filteredInput = it.text.filter { char -> char.isDigit() }
                emailCode = filteredInput.take(VERIFICATION_CODE_LENGTH)
            },
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.NumberPassword
            ),
            decorationBox = { innerTextField ->
                Box {
                    // コンテキストメニューが装飾の側で表示されるように重ねる
                    Box(
                        // 透明度を0にして、innerTextField が見えないようにする
                        modifier = Modifier.alpha(0f)
                    ) {
                        innerTextField()
                    }
                    EmailVerificationCodeDecorationBox(
                        emailCode = emailCode,
                        focusState = focusState,
                    )
                }
            }
        )
    }
}

@Composable
private fun EmailVerificationCodeDecorationBox(
    emailCode: String,
    focusState: FocusState?,
) {
    val cursorAnimationAlpha by rememberInfiniteTransition().animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(500),
            repeatMode = RepeatMode.Reverse
        )
    )
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        repeat(VERIFICATION_CODE_LENGTH) { index ->
            val digit = emailCode.getOrNull(index)?.toString() ?: ""
            val targeted = (index == emailCode.length.coerceAtMost(VERIFICATION_CODE_LENGTH - 1))
            val focused = focusState?.isFocused == true
            EmailVerificationCodeDigitBox(
                modifier = Modifier.size(48.dp),
                digit = digit,
                focusedBox = targeted && focused,
                showCursor = targeted && focused && cursorAnimationAlpha > 0.5f,
            )
        }
    }
}

@Composable
private fun EmailVerificationCodeDigitBox(
    digit: String,
    focusedBox: Boolean,
    showCursor: Boolean,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .border(
                width = 1.dp,
                color = when {
                    focusedBox -> focusColor
                    else -> Color.Gray
                },
                shape = RoundedCornerShape(8.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Row {
            Text(text = digit)
            if (showCursor) {
                VerticalDivider(
                    modifier = Modifier
                        .height(16.dp)
                        .padding(start = 2.dp),
                    thickness = 1.dp,
                    color = focusColor
                )
            }
        }
    }
}

@Preview
@Composable
private fun EmailVerificationCodeTextFieldPreview() {
    EmailVerificationCodeTextField()
}