package com.masteryi.engine.iching

import java.lang.Character.UnicodeScript

/**
 * 簡化版中文字筆畫數計算
 * 注意：此為簡化實作，可後續擴充完整筆畫表
 */
object ChineseStrokeCounter {
    // 常用字簡易筆畫對照（最小必要集）
    private val commonStrokes = mapOf(
        '一' to 1, '丨' to 1, '丶' to 1, '丿' to 1, '乙' to 1, '亅' to 1,
        '二' to 2, '十' to 2, '丁' to 2, '人' to 2, '力' to 2, '又' to 2,
        '三' to 3, '干' to 3, '工' to 3, '土' to 3, '士' to 3, '才' to 3, '下' to 3, '大' to 3, '寸' to 3, '小' to 3, '山' to 3, '川' to 3, '工' to 3, '己' to 3, '巳' to 3,
        '四' to 4, '五' to 4, '六' to 4, '日' to 4, '月' to 4, '木' to 4, '水' to 4, '火' to 4, '牛' to 4, '犬' to 4, '方' to 4, '心' to 4, '手' to 4, '天' to 4,
        '五' to 4, '且' to 5, '田' to 5, '目' to 5, '白' to 5, '石' to 5, '立' to 5,
        '耳' to 6, '自' to 6, '血' to 6, '行' to 6, '衣' to 6, '西' to 6, '地' to 6,
        '言' to 7, '里' to 7, '車' to 7, '金' to 8, '長' to 8, '門' to 8, '雨' to 8,
        '頁' to 9, '風' to 9, '飛' to 9, '食' to 9,
        '馬' to 10, '骨' to 10, '高' to 10,
        '鼎' to 12, '鼓' to 13, '龜' to 16
    )

    fun getStrokes(codePoint: Int): Int {
        if (Character.UnicodeScript.of(codePoint) != UnicodeScript.HAN) return 0
        val char = Character.toChars(codePoint)[0]
        // 常用字表優先
        commonStrokes[char]?.let { return it }
        // 基於 Unihan 常見範圍的簡易估算（保守 fallback）
        return 6
    }

    fun totalStrokes(text: String): Int =
        text.codePoints()
            .map { getStrokes(it) }
            .sum()
}
