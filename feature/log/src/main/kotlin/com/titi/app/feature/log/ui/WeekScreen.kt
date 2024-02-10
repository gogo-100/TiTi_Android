package com.titi.app.feature.log.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.titi.app.core.designsystem.component.TdsColorRow
import com.titi.app.core.designsystem.component.TdsStandardWeekGraph
import com.titi.app.core.designsystem.model.TdsTaskData
import com.titi.app.core.designsystem.model.TdsWeekLineChartData
import com.titi.app.core.designsystem.theme.TdsColor
import com.titi.app.core.designsystem.theme.TiTiTheme
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime

@Composable
fun WeekScreen(
    todayDateTime: ZonedDateTime,
    weekLineChardData: List<TdsWeekLineChartData>,
    tdsColors: List<TdsColor>,
    taskData: List<TdsTaskData>,
    onClickGraphColor: (Int) -> Unit,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
    ) {
        CalendarContent(
            modifier = Modifier.fillMaxWidth(),
            themeColor = tdsColors.first(),
        )

        Spacer(modifier = Modifier.height(15.dp))

        TdsColorRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 42.dp),
            onClick = onClickGraphColor,
        )

        Spacer(modifier = Modifier.height(15.dp))

        TdsStandardWeekGraph(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            todayDateTime = todayDateTime,
            weekLineChardData = weekLineChardData,
            tdsColors = tdsColors,
            taskData = taskData,
        )
    }
}

@Preview
@Composable
private fun WeekScreenPreview() {
    val todayDateTime = ZonedDateTime.now(ZoneOffset.UTC)
    val weekLineChardData = listOf(
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
    )

    val taskData = listOf(
        TdsTaskData(
            key = "수업",
            value = "02:00:00",
            progress = 0.2f,
        ),
        TdsTaskData(
            key = "인공지능",
            value = "03:00:00",
            progress = 0.3f,
        ),
        TdsTaskData(
            key = "알고리즘",
            value = "02:00:00",
            progress = 0.2f,
        ),
        TdsTaskData(
            key = "개발",
            value = "03:00:00",
            progress = 0.3f,
        ),
    )

    val tdsColors = listOf(
        TdsColor.D1,
        TdsColor.D2,
        TdsColor.D3,
        TdsColor.D4,
        TdsColor.D5,
        TdsColor.D6,
        TdsColor.D7,
        TdsColor.D8,
        TdsColor.D9,
        TdsColor.D11,
        TdsColor.D12,
    )

    TiTiTheme {
        WeekScreen(
            todayDateTime = todayDateTime,
            weekLineChardData = weekLineChardData,
            tdsColors = tdsColors,
            taskData = taskData,
            onClickGraphColor = {},
        )
    }
}
