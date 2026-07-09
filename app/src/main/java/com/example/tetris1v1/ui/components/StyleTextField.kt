package com.example.tetris1v1.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tetris1v1.ui.theme.FieldBackground
import com.example.tetris1v1.ui.theme.FieldBorder
import com.example.tetris1v1.ui.theme.TetrisRed
import com.example.tetris1v1.ui.theme.TextHint
import com.example.tetris1v1.ui.theme.TextWhite

@Composable
fun StyledTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    isPassword: Boolean = false,
    keyboardOptions : KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
){
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(FieldBackground)
            .border(1.dp, FieldBorder, RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        if (isPassword) {
            TextField(
                value = value,
                onValueChange = onValueChange,
                placeholder = { Text(placeholder, color = TextHint, fontSize = 15.sp) },
                visualTransformation = PasswordVisualTransformation(),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = TextWhite,
                    unfocusedTextColor = TextWhite,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = TetrisRed
                ),
                keyboardOptions = keyboardOptions,
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            TextField(
                value = value,
                onValueChange = onValueChange,
                placeholder = { Text(placeholder, color = TextHint, fontSize = 15.sp) },
                colors = TextFieldDefaults.colors(
                    focusedTextColor = TextWhite,
                    unfocusedTextColor = TextWhite,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = TetrisRed
                ),
                keyboardOptions = keyboardOptions,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}