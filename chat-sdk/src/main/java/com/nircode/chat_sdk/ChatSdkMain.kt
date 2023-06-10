package com.nircode.chat_sdk

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter

@Composable
fun ChatSdk(
    image: Painter,
    contentDescription: String
){
Image(painter = image, contentDescription = contentDescription)
}