package com.masteryi.yibokgua

import com.masteryi.core.model.iching.EightPalaceBuilder
import com.masteryi.core.model.iching.PalaceStage
import com.masteryi.core.model.iching.Trigram
import com.masteryi.data.repository.HexagramRepository
import org.junit.Assert.assertEquals
import org.junit.Test

class EightPalaceTest {

    private val allHexagrams = HexagramRepository().getAllHexagrams()

    @Test
    fun build_產出八宮且每宮八卦() {
        val palaces = EightPalaceBuilder.build(allHexagrams)
        assertEquals(8, palaces.size)
        palaces.forEach { palace ->
            assertEquals(8, palace.hexagrams.size)
            assertEquals(palace.palaceTrigram.binaryCode, palace.hexagrams.first().hexagram.lowerTrigram.binaryCode)
            assertEquals(palace.palaceTrigram.binaryCode, palace.hexagrams.first().hexagram.upperTrigram.binaryCode)
        }
    }

    @Test
    fun build_乾宮依變爻順序正確() {
        val qian = EightPalaceBuilder.build(allHexagrams).first { it.palaceTrigram == Trigram.QIAN }
        val expected = listOf(1, 44, 33, 12, 20, 23, 35, 14) // 乾為天..火天大有
        assertEquals(expected, qian.hexagrams.map { it.hexagram.id })
        // stages confirm: 本宮、一世..五世、遊魂、歸魂
        assertEquals(PalaceStage.entries, qian.hexagrams.map { it.stage })
    }

    @Test
    fun build_八宮所有卦序正確() {
        val byPalace = EightPalaceBuilder.build(allHexagrams).associateBy { it.palaceTrigram }
        // 乾  坤  震  巽  坎  離  艮  兌
        val expectedIds = mapOf(
            Trigram.QIAN to listOf(1, 44, 33, 12, 20, 23, 35, 14),
            Trigram.KUN to listOf(2, 24, 19, 11, 34, 43, 5, 8),
            Trigram.ZHEN to listOf(51, 16, 40, 32, 46, 48, 28, 17),
            Trigram.XUN to listOf(57, 9, 37, 42, 25, 21, 27, 18),
            Trigram.KAN to listOf(29, 60, 3, 63, 49, 55, 36, 7),
            Trigram.LI to listOf(30, 56, 50, 64, 4, 59, 6, 13),
            Trigram.GEN to listOf(52, 22, 26, 41, 38, 10, 61, 53),
            Trigram.DUI to listOf(58, 47, 45, 31, 39, 15, 62, 54)
        )
        expectedIds.forEach { (trigram, ids) ->
            assertEquals("${trigram.nameZh}宮卦序錯誤", ids, byPalace[trigram]!!.hexagrams.map { it.hexagram.id })
        }
    }

    @Test
    fun build_世應位正確() {
        val qian = EightPalaceBuilder.build(allHexagrams).first { it.palaceTrigram == Trigram.QIAN }
        val shiPositions = qian.hexagrams.map { it.stage.shiPosition }
        assertEquals(listOf(6, 1, 2, 3, 4, 5, 4, 3), shiPositions)
        // 應位 = 世位 ± 3
        qian.hexagrams.forEach { ph ->
            assertEquals(
                "${ph.stage.label}應位錯誤",
                if (ph.stage.shiPosition <= 3) ph.stage.shiPosition + 3 else ph.stage.shiPosition - 3,
                ph.stage.yingPosition
            )
        }
    }

    @Test
    fun build_遊魂與歸魂規則正確() {
        val byPalace = EightPalaceBuilder.build(allHexagrams).associateBy { it.palaceTrigram }
        // 遊魂：五世卦第四爻返變；歸魂：內卦復歸本宮（乾宮遊魂=火地晉、歸魂=火天大有）
        assertEquals(35, byPalace[Trigram.QIAN]!!.hexagrams[6].hexagram.id)
        assertEquals(14, byPalace[Trigram.QIAN]!!.hexagrams[7].hexagram.id)
        assertEquals(5, byPalace[Trigram.KUN]!!.hexagrams[6].hexagram.id)
        assertEquals(8, byPalace[Trigram.KUN]!!.hexagrams[7].hexagram.id)
    }
}