package com.masteryi.yibokgua.ui.screen.reader

import android.app.Application
import android.net.Uri
import android.provider.OpenableColumns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.nio.ByteBuffer
import java.nio.charset.Charset
import java.nio.charset.CodingErrorAction

/** 閱讀器畫面狀態 */
data class ReaderUiState(
    val title: String = "",
    val paragraphs: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
) {
    val hasContent: Boolean get() = paragraphs.isNotEmpty()
}

/**
 * 書籍閱讀器 ViewModel。
 *
 * 負責匯入 .txt（透過 SAF Uri）、解析文字並提供顯示設定。
 */
class ReaderViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ReaderSettingsRepository(application)

    private val _settings = MutableStateFlow(ReaderSettings())
    val settings: StateFlow<ReaderSettings> = _settings.asStateFlow()

    private val _uiState = MutableStateFlow(ReaderUiState())
    val uiState: StateFlow<ReaderUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.settings.collect { stored ->
                // 只在初次載入時覆蓋，避免非同步回寫蓋掉使用者的即時調整
                if (!initialized) {
                    _settings.value = stored
                    initialized = true
                }
            }
        }
    }

    private var initialized = false

    // ---- 顯示設定（即時生效，背景持久化） ----

    fun setFontSizeSp(value: Float) {
        _settings.update { it.copy(fontSizeSp = value.coerceIn(
            ReaderSettings.MIN_FONT_SIZE_SP, ReaderSettings.MAX_FONT_SIZE_SP)) }
        viewModelScope.launch { repository.setFontSizeSp(_settings.value.fontSizeSp) }
    }

    fun setLineHeightMultiplier(value: Float) {
        _settings.update { it.copy(lineHeightMultiplier = value.coerceIn(
            ReaderSettings.MIN_LINE_HEIGHT, ReaderSettings.MAX_LINE_HEIGHT)) }
        viewModelScope.launch { repository.setLineHeightMultiplier(_settings.value.lineHeightMultiplier) }
    }

    fun setParagraphSpacingDp(value: Float) {
        _settings.update { it.copy(paragraphSpacingDp = value) }
        viewModelScope.launch { repository.setParagraphSpacingDp(value) }
    }

    fun setContentPaddingDp(value: Float) {
        _settings.update { it.copy(contentPaddingDp = value) }
        viewModelScope.launch { repository.setContentPaddingDp(value) }
    }

    // ---- 匯入文件 ----

    fun importTxt(uri: Uri) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            val result = withContext(Dispatchers.IO) { runCatching { readDocument(uri) } }
            result
                .onSuccess { document ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            title = document.title,
                            paragraphs = document.text
                                .split('\n')
                                .map { line -> line.trim() }
                                .filter { line -> line.isNotEmpty() },
                            error = null
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(isLoading = false, error = error.message ?: "文件讀取失敗")
                    }
                }
        }
    }

    fun clear() {
        _uiState.value = ReaderUiState()
    }

    private data class Document(val title: String, val text: String)

    private fun readDocument(uri: Uri): Document {
        val context = getApplication<Application>()
        val title = queryDisplayName(uri) ?: "未命名文件"
        val bytes = context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
            ?: error("無法開啟文件")
        return Document(title = title, text = decodeText(bytes))
    }

    private fun queryDisplayName(uri: Uri): String? {
        val context = getApplication<Application>()
        return runCatching {
            context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                val index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (index >= 0 && cursor.moveToFirst()) cursor.getString(index) else null
            }
        }.getOrNull()
    }

    /** 依 BOM 與編碼自動判讀文字，優先 UTF-8，其次 UTF-16，最後以 GB18030 相容中文舊檔。 */
    private fun decodeText(bytes: ByteArray): String {
        if (bytes.size >= 3 &&
            bytes[0] == 0xEF.toByte() && bytes[1] == 0xBB.toByte() && bytes[2] == 0xBF.toByte()
        ) {
            return String(bytes, 3, bytes.size - 3, Charsets.UTF_8)
        }
        if (bytes.size >= 2 && bytes[0] == 0xFF.toByte() && bytes[1] == 0xFE.toByte()) {
            return String(bytes, 2, bytes.size - 2, Charsets.UTF_16LE)
        }
        if (bytes.size >= 2 && bytes[0] == 0xFE.toByte() && bytes[1] == 0xFF.toByte()) {
            return String(bytes, 2, bytes.size - 2, Charsets.UTF_16BE)
        }

        val strictUtf8 = runCatching {
            Charsets.UTF_8.newDecoder()
                .onMalformedInput(CodingErrorAction.REPORT)
                .onUnmappableCharacter(CodingErrorAction.REPORT)
                .decode(ByteBuffer.wrap(bytes))
                .toString()
        }.getOrNull()
        if (strictUtf8 != null) return strictUtf8

        return runCatching {
            String(bytes, Charset.forName("GB18030"))
        }.getOrElse {
            String(bytes, Charsets.UTF_8)
        }
    }
}
