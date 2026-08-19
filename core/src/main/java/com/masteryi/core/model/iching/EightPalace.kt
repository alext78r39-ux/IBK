package com.masteryi.core.model.iching

/**
 * 京房八宮卦・階段（由本宮依變爻規則推演）
 *
 * 世位（世爻位置）：1 = 初爻 ... 6 = 上爻。
 * 應位（應爻位置）：與世位相距三位（1↔4、2↔5、3↔6）。
 */
enum class PalaceStage(val label: String, val shiPosition: Int) {
    BEN_GONG("本宮", 6),
    YI_SHI("一世", 1),
    ER_SHI("二世", 2),
    SAN_SHI("三世", 3),
    SI_SHI("四世", 4),
    WU_SHI("五世", 5),
    YOU_HUN("遊魂", 4),
    GUI_HUN("歸魂", 3);

    /** 應爻位置（世與應相距三位） */
    val yingPosition: Int
        get() = if (shiPosition <= 3) shiPosition + 3 else shiPosition - 3

    /**
     * 由本宮卦需翻動的爻位（0 = 初爻 ... 5 = 上爻）：
     * 一世變初爻、二世變初・二爻、三世變初・二・三爻、四世變初~四爻、五世變初~五爻、
     * 遊魂由五世返變第四爻、歸魂內卦復歸本宮（僅第四爻保持變爻）。
     */
    val flipPositions: List<Int>
        get() = when (this) {
            BEN_GONG -> emptyList()
            YI_SHI -> listOf(0)
            ER_SHI -> listOf(0, 1)
            SAN_SHI -> listOf(0, 1, 2)
            SI_SHI -> listOf(0, 1, 2, 3)
            WU_SHI -> listOf(0, 1, 2, 3, 4)
            YOU_HUN -> listOf(0, 1, 2, 4)
            GUI_HUN -> listOf(4)
        }

    companion object {
        /** 爻位中文標註：1 = 初爻 ... 6 = 上爻 */
        fun yaoPositionName(position: Int): String = when (position) {
            1 -> "初爻"
            2 -> "二爻"
            3 -> "三爻"
            4 -> "四爻"
            5 -> "五爻"
            6 -> "上爻"
            else -> ""
        }
    }
}

/** 單一宮卦：宮內一個卦及其階段 */
data class PalaceHexagram(
    val hexagram: Hexagram,
    val stage: PalaceStage,
    val palaceTrigram: Trigram
)

/** 一宮八個卦：本宮、一世~五世、遊魂、歸魂 */
data class EightPalaceGroup(
    val palaceTrigram: Trigram,
    val hexagrams: List<PalaceHexagram>
)

/**
 * 八宮卦建構器。
 * 由八純卦（上卦＝下卦＝宮卦）出發，依各階段變爻規則推演出八個卦，
 * 再以 binaryCode 對應到 64 卦資料庫。
 */
object EightPalaceBuilder {

    fun build(allHexagrams: List<Hexagram>): List<EightPalaceGroup> {
        val byBinary = allHexagrams.associateBy { it.binaryCode }
        return Trigram.entries.map { trigram ->
            val baseBinary = trigram.binaryCode + trigram.binaryCode
            val hexagramsInPalace = PalaceStage.entries.map { stage ->
                val binary = flip(baseBinary, stage.flipPositions)
                PalaceHexagram(
                    hexagram = byBinary[binary]
                        ?: error("八宮卦建構失敗：找不到對應卦象 binary=$binary（${trigram.nameZh}宮 ${stage.label}）"),
                    stage = stage,
                    palaceTrigram = trigram
                )
            }
            EightPalaceGroup(palaceTrigram = trigram, hexagrams = hexagramsInPalace)
        }
    }

    /** 依爻位（0~5，初爻至上爻）翻動 binaryCode 的陰陽 */
    private fun flip(binary: String, positions: List<Int>): String {
        val chars = binary.toCharArray()
        positions.forEach { i -> chars[i] = if (chars[i] == '0') '1' else '0' }
        return chars.concatToString()
    }
}