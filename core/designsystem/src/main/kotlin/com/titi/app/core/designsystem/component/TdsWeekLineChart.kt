package com.titi.app.core.designsystem.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.titi.app.core.designsystem.extension.toTimeString
import com.titi.app.core.designsystem.model.TdsWeekLineChartData
import com.titi.app.core.designsystem.theme.TdsColor
import com.titi.app.core.designsystem.theme.TdsTextStyle
import com.titi.app.core.designsystem.theme.TiTiTheme

@Composable
fun TdsWeekLineChart(
    modifier: Modifier = Modifier,
    weekLineChardData: List<TdsWeekLineChartData>,
    startColor: Color,
    endColor: Color,
) {
    require(weekLineChardData.size == 7) {
        "The TdsWeekLineChartDataList must be 7 in size"
    }

    val maxTime = weekLineChardData.maxBy { it.time }.time.toFloat()

    BoxWithConstraints(modifier = modifier) {
        val itemWidth = maxWidth / weekLineChardData.size

        Row(modifier = Modifier.fillMaxSize()) {
            weekLineChardData.forEach {
                TdsWeekLineBar(
                    modifier = Modifier
                        .width(itemWidth)
                        .fillMaxHeight(),
                    time = it.time.toTimeString(),
                    progress = if (maxTime == 0f) {
                        0f
                    } else {
                        it.time / maxTime
                    },
                    date = it.date,
                    brush = Brush.verticalGradient(
                        listOf(
                            startColor,
                            endColor,
                        ),
                    ),
                )
            }
        }
    }
}

@Composable
private fun TdsWeekLineBar(
    modifier: Modifier = Modifier,
    time: String,
    progress: Float,
    date: String,
    brush: Brush,
) {
    val textStyle = TdsTextStyle
        .NORMAL_TEXT_STYLE
        .getTextStyle(fontSize = 10.sp)
        .copy(color = TdsColor.TEXT.getColor())
    val timeTextMeasurer = rememberTextMeasurer()
    val dateTextMeasurer = rememberTextMeasurer()
    val timeTextLayoutResult = remember(time) {
        timeTextMeasurer.measure(time, textStyle)
    }
    val dateTextLayoutResult = remember(date) {
        dateTextMeasurer.measure(date, textStyle)
    }

    Canvas(modifier = modifier) {
        val spacing = 4.dp.toPx()

        val radius = size.width * 0.2f
        val barWidth = size.width * 0.9f
        val allTextHeight = timeTextLayoutResult.size.height + dateTextLayoutResult.size.height
        val barMaxHeight = size.height - 2 * spacing - allTextHeight
        val barHeight = barMaxHeight * progress

        val startX = center.x - barWidth / 2
        val startY = size.height - barHeight - spacing - dateTextLayoutResult.size.height
        val cornerRadius = CornerRadius(radius, radius)

        val path = Path().apply {
            addRoundRect(
                RoundRect(
                    rect = Rect(
                        offset = Offset(
                            x = startX,
                            y = startY,
                        ),
                        size = Size(barWidth, barHeight),
                    ),
                    topLeft = cornerRadius,
                    topRight = cornerRadius,
                ),
            )
        }

        drawText(
            textMeasurer = timeTextMeasurer,
            text = time,
            style = textStyle,
            topLeft = Offset(
                x = center.x - dateTextLayoutResult.size.width / 2,
                y = startY - dateTextLayoutResult.size.height - spacing,
            ),
        )

        drawPath(
            path = path,
            brush = brush,
        )

        drawText(
            textMeasurer = dateTextMeasurer,
            text = date,
            style = textStyle,
            topLeft = Offset(
                x = center.x - dateTextLayoutResult.size.width / 2,
                y = size.height - dateTextLayoutResult.size.height,
            ),
        )
    }
}

@Preview
@Composable
private fun TdsWeekLineBarPreview() {
    TiTiTheme {
        TdsWeekLineBar(
            modifier = Modifier
                .width(30.dp)
                .height(100.dp)
                .background(Color.White),
            time = "1:19",
            progress = 0.5f,
            date = "1/28",
            brush = Brush.verticalGradient(
                listOf(
                    TdsColor.D1.getColor(),
                    TdsColor.D2.getColor(),
                ),
            ),
        )
    }
}

@Preview
@Composable
private fun TdsWeekLineChartPreview() {
    TiTiTheme {
        TdsWeekLineChart(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White),
            weekLineChardData = listOf(
                TdsWeekLineChartData(
                    time = 6200,
                    date = "1/12",
                ),
                TdsWeekLineChartData(
                    time = 3700,
                    date = "1/13",
                ),
                TdsWeekLineChartData(
                    time = 5200,
                    date = "1/14",
                ),
                TdsWeekLineChartData(
                    time = 1042,
                    date = "1/15",
                ),
                TdsWeekLineChartData(
                    time = 4536,
                    date = "1/16",
                ),
                TdsWeekLineChartData(
                    time = 3700,
                    date = "1/17",
                ),
                TdsWeekLineChartData(
                    time = 2455,
                    date = "1/18",
                ),
            ),
            startColor = TdsColor.D2.getColor(),
            endColor = TdsColor.D3.getColor(),
        )
    }
}
