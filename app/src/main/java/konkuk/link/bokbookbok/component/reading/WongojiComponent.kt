package konkuk.link.bokbookbok.component.reading

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import konkuk.link.bokbookbok.ui.theme.bokBookBokColors
import konkuk.link.bokbookbok.ui.theme.defaultBokBookBokTypography

@Composable
private fun WonGoJiCell(
    char: Char?,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(34.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = char?.toString() ?: "",
            textAlign = TextAlign.Center,
            style = defaultBokBookBokTypography.subLogo,
            color = bokBookBokColors.fontDarkBrown
        )
    }
}

@Composable
fun WonGoJiRow(
    text: String,
    modifier: Modifier = Modifier
) {
    val borderColor = bokBookBokColors.second
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.drawBehind() {
                val cellWidth = size.width / 8
                val strokeWidth = 1.dp.toPx()
                for (i in 1 until 8) {
                    val x = cellWidth * i
                    drawLine(
                        color = borderColor,
                        start = Offset(x, 0f),
                        end = Offset(x, size.height),
                        strokeWidth = strokeWidth
                    )
                    drawLine(
                        color = borderColor,
                        start = Offset(0f, size.height),
                        end = Offset(size.width, size.height),
                        strokeWidth = strokeWidth
                    )
                }
            }
        ) {
            for (i in 0 until 8) {
                val char = text.getOrNull(i)
                WonGoJiCell(char = char, modifier = Modifier.weight(1f))
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
fun WonGoJiBoard(
    text: String,
    modifier: Modifier = Modifier
) {
    val processedLines = remember(text) {
        processTextForWonGoJi(text)
    }

    Column(
        modifier = modifier
            .background(bokBookBokColors.white)
            .border(width = 2.dp, color = bokBookBokColors.second)
    ) {
        processedLines.forEachIndexed { index, line ->
            WonGoJiRow(text = line)

            if (index < processedLines.size - 1) {
                HorizontalDivider(
                    thickness = 1.dp,
                    color = bokBookBokColors.second
                )
            }
        }
    }
}

private fun processTextForWonGoJi(text: String): List<String> {
    if (text.isEmpty()) return emptyList()

    val lines = mutableListOf<String>()
    val words = text.split(' ')
    val currentLine = StringBuilder()

    words.forEach { word ->
        if (word.length > 8) {
            if (currentLine.isNotEmpty()) {
                lines.add(currentLine.toString())
                currentLine.clear()
            }
            word.chunked(8).forEach { chunk ->
                lines.add(chunk)
            }
            return@forEach
        }

        val spaceNeeded = if (currentLine.isNotEmpty()) 1 else 0
        if (currentLine.length + word.length + spaceNeeded > 8) {
            lines.add(currentLine.toString())
            currentLine.clear()
            currentLine.append(word)
        } else {
            if (spaceNeeded > 0) {
                currentLine.append(" ")
            }
            currentLine.append(word)
        }
    }

    if (currentLine.isNotEmpty()) {
        lines.add(currentLine.toString())
    }

    return lines
}