package com.masteryi.yibokgua.ui.screen.reader

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.masteryi.yibokgua.ui.theme.YibokguaTheme
import kotlin.math.roundToInt

/**
 * 書籍閱讀器畫面。
 *
 * - 可匯入 .txt 檔案並顯示（透過 SAF，無需儲存權限）
 * - 點擊畫面中央切換上下工具列
 * - 字級調整即時生效
 * - Material 3，全 Compose，未使用 XML layout 或 SharedPreferences
 */
@Composable
fun ReaderScreen(
    viewModel: ReaderViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val settings by viewModel.settings.collectAsStateWithLifecycle()

    var toolbarVisible by rememberSaveable { mutableStateOf(true) }

    val importLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let(viewModel::importTxt)
    }
    val openFilePicker: () -> Unit = { importLauncher.launch(arrayOf("text/plain")) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        when {
            uiState.isLoading -> ReaderLoading()
            uiState.error != null -> ReaderError(
                message = uiState.error.orEmpty(),
                onRetry = openFilePicker
            )
            !uiState.hasContent -> ReaderEmpty(onImport = openFilePicker)
            else -> ReaderText(
                paragraphs = uiState.paragraphs,
                settings = settings,
                onCenterTap = { toolbarVisible = !toolbarVisible }
            )
        }

        AnimatedVisibility(
            visible = toolbarVisible,
            enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
            modifier = Modifier.align(Alignment.TopCenter)
        ) {
            ReaderTopBar(
                title = uiState.title.ifBlank { "書籍" },
                onImport = openFilePicker,
                onCollapse = { toolbarVisible = false }
            )
        }

        AnimatedVisibility(
            visible = toolbarVisible,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            ReaderBottomBar(
                settings = settings,
                onFontSizeChange = viewModel::setFontSizeSp,
                onLineHeightChange = viewModel::setLineHeightMultiplier
            )
        }
    }
}

@Composable
private fun ReaderTopBar(
    title: String,
    onImport: () -> Unit,
    onCollapse: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceVariant,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        tonalElevation = 3.dp,
        shadowElevation = 3.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .height(56.dp)
                .padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onCollapse) {
                Icon(Icons.Default.Menu, contentDescription = "隱藏工具列")
            }
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            TextButton(onClick = onImport) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(4.dp))
                Text("匯入")
            }
        }
    }
}

@Composable
private fun ReaderBottomBar(
    settings: ReaderSettings,
    onFontSizeChange: (Float) -> Unit,
    onLineHeightChange: (Float) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceVariant,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        tonalElevation = 3.dp,
        shadowElevation = 3.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            SettingSliderRow(
                label = "字級",
                valueLabel = "${settings.fontSizeSp.roundToInt()}",
                value = settings.fontSizeSp,
                valueRange = ReaderSettings.MIN_FONT_SIZE_SP..ReaderSettings.MAX_FONT_SIZE_SP,
                steps = 23,
                onValueChange = onFontSizeChange
            )
            SettingSliderRow(
                label = "行距",
                valueLabel = String.format("%.1f", settings.lineHeightMultiplier),
                value = settings.lineHeightMultiplier,
                valueRange = ReaderSettings.MIN_LINE_HEIGHT..ReaderSettings.MAX_LINE_HEIGHT,
                steps = 11,
                onValueChange = onLineHeightChange
            )
        }
    }
}

@Composable
private fun SettingSliderRow(
    label: String,
    valueLabel: String,
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int,
    onValueChange: (Float) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.width(36.dp)
        )
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange,
            steps = steps,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = valueLabel,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.End,
            modifier = Modifier.width(36.dp)
        )
    }
}

@Composable
private fun ReaderText(
    paragraphs: List<String>,
    settings: ReaderSettings,
    onCenterTap: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    val width = size.width.toFloat()
                    val height = size.height.toFloat()
                    val inCenterX = offset.x >= width * 0.25f && offset.x <= width * 0.75f
                    val inCenterY = offset.y >= height * 0.25f && offset.y <= height * 0.75f
                    if (inCenterX && inCenterY) onCenterTap()
                }
            },
        contentPadding = PaddingValues(
            start = settings.contentPaddingDp.dp,
            end = settings.contentPaddingDp.dp,
            top = settings.contentPaddingDp.dp + 72.dp,
            bottom = settings.contentPaddingDp.dp + 128.dp
        ),
        verticalArrangement = Arrangement.spacedBy(settings.paragraphSpacingDp.dp)
    ) {
        items(paragraphs) { paragraph ->
            Text(
                text = paragraph,
                fontSize = settings.fontSizeSp.sp,
                lineHeight = (settings.fontSizeSp * settings.lineHeightMultiplier).sp,
                fontFamily = FontFamily.Serif,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun ReaderEmpty(onImport: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "書架尚無內容",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "匯入 .txt 純文字檔即可開始閱讀。",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(20.dp))
        Button(onClick = onImport) {
            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(6.dp))
            Text("匯入文字檔")
        }
    }
}

@Composable
private fun ReaderLoading() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ReaderError(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "讀取失敗",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.error
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(20.dp))
        Button(onClick = onRetry) { Text("重新匯入") }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
private fun ReaderTextPreview() {
    YibokguaTheme {
        ReaderText(
            paragraphs = listOf(
                "道可道，非常道。名可名，非常名。",
                "無名天地之始，有名萬物之母。",
                "故常無欲以觀其妙，常有欲以觀其徼。"
            ),
            settings = ReaderSettings(),
            onCenterTap = {}
        )
    }
}
