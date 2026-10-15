package com.masteryi.yibokgua.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.masteryi.yibokgua.ui.theme.LocalBorderWidth
import com.masteryi.yibokgua.ui.theme.LocalMotion

/** 全局統一細框線樣式（隨主題與設定自適應） */
@Composable
fun zenThinBorder(color: Color = MaterialTheme.colorScheme.outline): BorderStroke =
    BorderStroke(LocalBorderWidth.current.dp, color)

/**
 * 卡片色暈漸層：僅以 2% 透明度把主色與輔色暈染在卡片上，
 * 對比低到近乎不覺察，卻讓每張卡片保有各自的色溫。
 */
@Composable
fun zenCardTintBrush(
    start: Color = MaterialTheme.colorScheme.primary,
    end: Color = MaterialTheme.colorScheme.secondary,
    alpha: Float = LocalMotion.current.cardGradientAlpha
): Brush = Brush.linearGradient(
    colorStops = arrayOf(
        0f to start.copy(alpha = alpha),
        0.55f to Color.Transparent,
        1f to end.copy(alpha = alpha)
    )
)

/**
 * 可重用的按壓縮放（預設 0.98）。
 * 因按下狀態需與點擊共用同一個 interactionSource，呼叫端寫法為：
 * ```
 * val press = remember { MutableInteractionSource() }
 * Box(Modifier.zenPressScale(press).clickable(interactionSource = press) { … })
 * ```
 */
@Composable
fun Modifier.zenPressScale(interactionSource: MutableInteractionSource): Modifier = composed {
    val pressed by interactionSource.collectIsPressedAsState()
    val motion = LocalMotion.current
    val scale by animateFloatAsState(
        targetValue = if (pressed) motion.pressScale else 1f,
        animationSpec = tween(durationMillis = motion.quickMillis, easing = motion.pressEasing),
        label = "zenPressScale"
    )
    graphicsLayer {
        scaleX = scale
        scaleY = scale
    }
}

/**
 * 全域卡片：24dp 圓角、2% 透明度漸層背景、細框線，並內建 0.98 按壓縮放微動效。
 *
 * 以 [MaterialTheme.shapes.extraLarge]（24dp）為預設圓角，
 * 傳入 onClick 時自動接上按壓回饋，取代原本到處散落的 Modifier.clickable。
 */
@Composable
fun ZenCard(
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    contentColor: Color = contentColorFor(containerColor),
    shape: Shape = MaterialTheme.shapes.extraLarge,
    border: BorderStroke? = zenThinBorder(),
    gradient: Boolean = true,
    enabled: Boolean = true,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val motion = LocalMotion.current
    val scale by animateFloatAsState(
        targetValue = if (pressed && onClick != null) motion.pressScale else 1f,
        animationSpec = tween(durationMillis = motion.quickMillis, easing = motion.pressEasing),
        label = "zenCardScale"
    )
    val scaledModifier = modifier.graphicsLayer {
        scaleX = scale
        scaleY = scale
    }

    if (onClick != null) {
        Surface(
            onClick = onClick,
            modifier = scaledModifier,
            enabled = enabled,
            shape = shape,
            color = containerColor,
            contentColor = contentColor,
            tonalElevation = 0.dp,
            shadowElevation = 0.dp,
            border = null,
            interactionSource = interactionSource
        ) {
            ZenCardBody(shape = shape, border = border, gradient = gradient, content = content)
        }
    } else {
        Surface(
            modifier = scaledModifier,
            shape = shape,
            color = containerColor,
            contentColor = contentColor,
            tonalElevation = 0.dp,
            shadowElevation = 0.dp,
            border = null
        ) {
            ZenCardBody(shape = shape, border = border, gradient = gradient, content = content)
        }
    }
}

/** 卡片內容層：漸層色暈鋪在底色之上、框線之上，並提供 [ColumnScope] 給呼叫端 */
@Composable
private fun ZenCardBody(
    shape: Shape,
    border: BorderStroke?,
    gradient: Boolean,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (gradient) Modifier.background(zenCardTintBrush(), shape) else Modifier)
            .then(border?.let { Modifier.border(it, shape) } ?: Modifier),
        content = content
    )
}