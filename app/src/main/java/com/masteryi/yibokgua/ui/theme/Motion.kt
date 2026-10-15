package com.masteryi.yibokgua.ui.theme

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf

/**
 * 全局微動效 Tokens。
 *
 * 原則：短、輕、靜。動畫只負責給出「觸及回饋」與「方向感」，
 * 不搶內容的注意力，因此時長一律壓在 150～420ms 之間，
 * 位移與縮放幅度也刻意維持細微（2% 縮放、8% 滑動）。
 */
@Immutable
data class ZenMotion(
    /** 按壓等極短回饋 */
    val quickMillis: Int = 140,
    /** 元件狀態切換（淡入淡出、漸層） */
    val standardMillis: Int = 260,
    /** 頁面進出等較長動畫 */
    val emphasizedMillis: Int = 420,
    /** 卡片按壓縮放倍率（0.98 = 內縮 2%） */
    val pressScale: Float = 0.98f,
    /** 頁面切換橫向位移，占畫面寬度比例 */
    val pageSlideFraction: Float = 0.08f,
    /** 卡片背景漸層不透明度（2%） */
    val cardGradientAlpha: Float = 0.02f,
    /** 按壓回饋緩動：回彈要快、收尾要靜 */
    val pressEasing: Easing = FastOutSlowInEasing,
    /** 進場緩動：起步慢、中段穩、收尾淡 */
    val enterEasing: Easing = CubicBezierEasing(0.05f, 0.7f, 0.1f, 1.0f),
    /** 退場緩動：先停後移，讓畫面感覺是「沉下去」而非「被推走」 */
    val exitEasing: Easing = CubicBezierEasing(0.3f, 0.0f, 0.8f, 0.15f)
)

/** 全域動效參數，由 [YibokguaTheme] 提供 */
val LocalMotion = staticCompositionLocalOf { ZenMotion() }