package com.masteryi.core.model.iching

/**
 * 八卦資料模型：單一八卦（三爻經卦）的基本屬性與萬類象資料。
 * 欄位對應資料表：基本象／卦德、人物、性格、人體、疾病、天象、動物、物象、場所／建築物、《說卦傳》原文類象。
 */
data class TrigramInfo(
    val trigram: Trigram,
    val basicImagery: String = "",   // 基本象／卦德
    val persons: String = "",        // 人物
    val personality: String = "",    // 性格
    val bodyParts: String = "",      // 人體
    val illnesses: String = "",      // 疾病
    val weather: String = "",        // 天象
    val animals: String = "",        // 動物
    val objects: String = "",        // 物象
    val places: String = "",         // 場所／建築物
    val shuoGuaText: String = ""     // 《說卦傳》原文類象
) {
    val nameZh: String get() = trigram.nameZh
    val symbol: String get() = trigram.symbol

    /** 萬類象列表：僅回傳已填寫的項目（欄位標題 to 內容），依資料表欄位順序 */
    fun attributeEntries(): List<Pair<String, String>> = buildList {
        if (basicImagery.isNotEmpty()) add("基本象／卦德" to basicImagery)
        if (persons.isNotEmpty()) add("人物" to persons)
        if (personality.isNotEmpty()) add("性格" to personality)
        if (bodyParts.isNotEmpty()) add("人體" to bodyParts)
        if (illnesses.isNotEmpty()) add("疾病" to illnesses)
        if (weather.isNotEmpty()) add("天象" to weather)
        if (animals.isNotEmpty()) add("動物" to animals)
        if (objects.isNotEmpty()) add("物象" to objects)
        if (places.isNotEmpty()) add("場所／建築物" to places)
        if (shuoGuaText.isNotEmpty()) add("《說卦傳》原文類象" to shuoGuaText)
    }

    companion object {
        /** 說明文字尚未提供時的佔位顯示 */
        const val PENDING_TEXT = "資料建置中..."
    }
}
