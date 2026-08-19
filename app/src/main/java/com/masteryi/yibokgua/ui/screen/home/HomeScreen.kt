package com.masteryi.yibokgua.ui.screen.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.masteryi.core.model.iching.LunarDate
import com.masteryi.core.model.iching.Trigram
import com.masteryi.yibokgua.viewmodel.DivinationViewModel
import com.masteryi.yibokgua.ui.components.AdaptiveScreen
import java.time.LocalDate

private val ThinBorder: BorderStroke
    @Composable get() = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline)

/** 全屏自適應參數：依可用高度決定是否進入緊湊模式 */
private data class HomeSpace(
    val compact: Boolean,
    val titleFontSize: Int,
    val quotePadding: Dp,
    val taijiSize: Dp,
    val sectionSpacing: Dp,
    val trigramSpacing: Dp,
    val palaceSymbolSize: Int,
    val palaceNameSize: Int,
    val palacePaddingV: Dp
)

@Composable
private fun HomeScreenSpace(maxHeight: Dp): HomeSpace {
    val compact = maxHeight < 720.dp
    return HomeSpace(
        compact = compact,
        titleFontSize = if (compact) 28 else 36,
        quotePadding = if (compact) 8.dp else 14.dp,
        taijiSize = if (compact) 84.dp else 110.dp,
        sectionSpacing = if (compact) 4.dp else 8.dp,
        trigramSpacing = if (compact) 5.dp else 8.dp,
        palaceSymbolSize = if (compact) 17 else 20,
        palaceNameSize = if (compact) 10 else 12,
        palacePaddingV = if (compact) 5.dp else 8.dp
    )
}

@Composable
fun HomeScreen(
    viewModel: DivinationViewModel,
    onStartManualDivination: () -> Unit
) {
    var selectedTrigram by remember { mutableStateOf(Trigram.QIAN) }
    val lunarToday = remember { LunarDate.of(LocalDate.now()) }

    AdaptiveScreen {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val space = HomeScreenSpace(maxHeight)
            val horizontalPad = if (space.compact) 12.dp else 16.dp

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = horizontalPad)
            ) {
                Spacer(Modifier.statusBarsPadding())

                HomeHeader(lunarLabel = lunarToday.text, space = space)

                Spacer(Modifier.height(space.sectionSpacing))
                DivinationQuote(space = space)

                Spacer(Modifier.height(space.sectionSpacing))
                TaijiAction(space = space, onClick = onStartManualDivination)

                Spacer(Modifier.height(space.sectionSpacing))
                SectionTitle("八卦", compact = space.compact)

                // 八卦橫排：佔滿剩餘空間並垂直置中
                PalaceSelector(
                    selected = selectedTrigram,
                    onSelect = { selectedTrigram = it },
                    space = space,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
                Spacer(Modifier.height(4.dp))
            }
        }
    }
}

@Composable
private fun HomeHeader(lunarLabel: String, space: HomeSpace) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 6.dp, bottom = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "易卜",
            fontSize = space.titleFontSize.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif,
            color = MaterialTheme.colorScheme.primary,
            letterSpacing = 4.sp
        )
        Spacer(Modifier.height(if (space.compact) 1.dp else 4.dp))
        Text(
            lunarLabel,
            style = if (space.compact) MaterialTheme.typography.titleSmall
            else MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.secondary,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.height(1.dp))
        Text(
            "一事一卦 · 心誠則靈",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            letterSpacing = 1.sp
        )
        Spacer(Modifier.height(if (space.compact) 4.dp else 8.dp))
        HorizontalDivider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f),
            modifier = Modifier.fillMaxWidth(0.6f)
        )
    }
}

@Composable
private fun DivinationQuote(space: HomeSpace) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        border = ThinBorder,
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = space.quotePadding),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("「", fontSize = (if (space.compact) 16 else 20).sp, fontFamily = FontFamily.Serif,
                color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Bold)
            Text(
                "禍福莫逃乎數，吉凶皆有其機。",
                fontFamily = FontFamily.Serif,
                fontSize = (if (space.compact) 14 else 17).sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = 3.dp)
            )
            Text("」", fontSize = (if (space.compact) 16 else 20).sp, fontFamily = FontFamily.Serif,
                color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun TaijiAction(space: HomeSpace, onClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(space.taijiSize)
                .shadow(3.dp, CircleShape)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surface)
                .border(2.dp, MaterialTheme.colorScheme.secondary, CircleShape)
                .clickable(onClick = onClick)
        ) {
            TaijiArtwork(Modifier.fillMaxSize(0.78f))
        }
        Spacer(Modifier.height(if (space.compact) 2.dp else 4.dp))
        Text(
            "手動起卦",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun TaijiArtwork(modifier: Modifier = Modifier) {
    Canvas(modifier) {
        val r = size.minDimension / 2
        drawArc(Color.Black, 90f, 180f, true)
        drawArc(Color.White, -90f, 180f, true)
        drawCircle(Color.White, radius = r / 2, center = Offset(center.x, center.y - r / 2))
        drawCircle(Color.Black, radius = r / 2, center = Offset(center.x, center.y + r / 2))
        drawCircle(Color.Black, radius = r / 8, center = Offset(center.x, center.y - r / 2))
        drawCircle(Color.White, radius = r / 8, center = Offset(center.x, center.y + r / 2))
    }
}

@Composable
private fun SectionTitle(text: String, compact: Boolean) {
    Text(
        text,
        style = if (compact) MaterialTheme.typography.titleSmall else MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(top = 2.dp, bottom = 2.dp)
    )
}

/** 八卦橫排：八卦全部單行排列，依剩餘空間等比分配寬度 */
@Composable
private fun PalaceSelector(
    selected: Trigram,
    onSelect: (Trigram) -> Unit,
    space: HomeSpace,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(space.trigramSpacing),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Trigram.entries.forEach { trigram ->
            PalaceButton(
                trigram = trigram,
                selected = trigram == selected,
                onClick = { onSelect(trigram) },
                space = space,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            )
        }
    }
}

@Composable
private fun PalaceButton(
    trigram: Trigram,
    selected: Boolean,
    onClick: () -> Unit,
    space: HomeSpace,
    modifier: Modifier = Modifier
) {
    val container = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
    val content = if (selected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = container, contentColor = content),
        border = if (selected) BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary) else ThinBorder,
        elevation = CardDefaults.cardElevation(defaultElevation = if (selected) 2.dp else 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = space.palacePaddingV),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(trigram.symbol, fontSize = space.palaceSymbolSize.sp)
            Spacer(Modifier.height(if (space.compact) 1.dp else 3.dp))
            Text(
                trigram.nameZh,
                fontSize = space.palaceNameSize.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
        }
    }
}