package com.masteryi.yibokgua.ui.screen.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.masteryi.yibokgua.ui.components.AdaptiveScreen
import com.masteryi.yibokgua.viewmodel.DivinationViewModel
import java.text.SimpleDateFormat
import java.util.*

private val ThinBorder: BorderStroke
    @Composable get() = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline)

@Composable
fun HistoryScreen(
    viewModel: DivinationViewModel,
    onNavigateToDetail: (Int) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val results = uiState.historyList

    AdaptiveScreen {
        if (results.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("尚無起卦記錄", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
            items(results) { result ->
                val timeStr = remember(result.timestamp) {
                    SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault())
                        .format(Date(result.timestamp))
                }
                Card(
                    border = ThinBorder,
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigateToDetail(result.originalHexagram.id) }
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                result.divinationType,
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                            Text(timeStr, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Text(
                            "${result.originalHexagram.symbol} ${result.originalHexagram.name}",
                            fontSize = 22.sp
                        )
                    }
                }
            }
        }
    }
    }
}
