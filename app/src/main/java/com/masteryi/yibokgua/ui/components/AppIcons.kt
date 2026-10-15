package com.masteryi.yibokgua.ui.components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.unit.dp

/**
 * 書籍圖示（Material「book」）。
 *
 * 以 [ImageVector] 自建，避免為單一圖示引入 `material-icons-extended`。
 */
val BookIcon: ImageVector by lazy {
    ImageVector.Builder(
        name = "Book",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).addPath(
        pathData = PathParser().parsePathString(
            "M18 2H6c-1.1 0-2 .9-2 2v16c0 1.1.9 2 2 2h12c1.1 0 2-.9 " +
                "2-2V4c0-1.1-.9-2-2-2zM6 4h5v8l-2.5-1.5L6 12V4z"
        ).toNodes(),
        fill = SolidColor(Color.Black)
    ).build()
}
