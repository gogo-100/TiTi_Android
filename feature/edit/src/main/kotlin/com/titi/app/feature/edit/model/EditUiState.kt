package com.titi.app.feature.edit.model

import android.os.Build
import android.os.Bundle
import com.airbnb.mvrx.Mavericks
import com.airbnb.mvrx.MavericksState
import com.titi.app.core.designsystem.extension.getTimeString
import com.titi.app.core.designsystem.model.TdsTaskData
import com.titi.app.core.designsystem.model.TdsTimeTableData
import com.titi.app.core.designsystem.theme.TdsColor
import com.titi.app.doamin.daily.model.Daily
import com.titi.app.feature.edit.mapper.toFeatureModel
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.UUID

data class EditUiState(
    val currentDate: LocalDate = LocalDate.now(),
    val graphColors: List<TdsColor> = listOf(
        TdsColor.D1,
        TdsColor.D2,
        TdsColor.D3,
        TdsColor.D4,
        TdsColor.D5,
        TdsColor.D6,
        TdsColor.D7,
        TdsColor.D8,
        TdsColor.D9,
        TdsColor.D10,
        TdsColor.D11,
        TdsColor.D12,
    ),
    val currentDaily: Daily = Daily(
        day = currentDate
            .atStartOfDay()
            .atZone(ZoneId.systemDefault())
            .withZoneSameInstant(ZoneOffset.UTC)
            .toString(),
    ),
    val selectedTaskIndex: Int = -1,
    val clickedTaskName: String? = null,
    val saveEnabled: Boolean = false,
    val finishEvent: Boolean = false,
) : MavericksState {
    constructor(args: Bundle) : this(
        currentDate = LocalDate.parse(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                args.getParcelable(
                    Mavericks.KEY_ARG,
                    String::class.java,
                )
            } else {
                args.getParcelable(Mavericks.KEY_ARG)
            } ?: LocalDate.now().toString(),
        ),
    )

    val dailyGraphData: DailyGraphData = currentDaily.toFeatureModel(graphColors)
}

data class DailyGraphData(
    val totalTime: String = 0L.getTimeString(),
    val maxTime: String = 0L.getTimeString(),
    val timeLine: List<Long> = LongArray(24) { 0L }.toList(),
    val taskData: List<TdsTaskData> = emptyList(),
    val tdsTimeTableData: List<TdsTimeTableData> = emptyList(),
)

data class DateTimeTaskHistory(
    val id: String = UUID.randomUUID().toString(),
    val startDateTime: LocalDateTime,
    val endDateTime: LocalDateTime,
) {
    val diffTime = Duration.between(startDateTime, endDateTime).toMillis() / 1000
}
