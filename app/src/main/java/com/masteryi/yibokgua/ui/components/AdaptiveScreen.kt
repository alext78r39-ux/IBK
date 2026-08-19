package com.masteryi.yibokgua.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * 全局自適應容器：在平板／大螢幕上將內容限制於最大寬度並置中，
 * 手機上則佔滿全寬，維持各畫面版式一致。
 */
@Composable
fun AdaptiveScreen(
    modifier: Modifier = Modifier,
    maxWidth: Dp = 720.dp,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .widthIn(max = maxWidth)
                .fillMaxWidth(),
        ) {
            content()
        }
    }
}