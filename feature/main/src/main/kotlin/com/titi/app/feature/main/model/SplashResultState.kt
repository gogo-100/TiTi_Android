package com.titi.app.feature.main.model

import com.titi.app.core.util.getDailyDayWithHour
import com.titi.app.doamin.daily.model.Daily
import com.titi.app.domain.color.model.TimeColor
import com.titi.app.domain.time.model.RecordTimes

data class SplashResultState(
    val recordTimes: RecordTimes = RecordTimes(),
    val timeColor: TimeColor = TimeColor(),
    val daily: Daily = Daily(day = getDailyDayWithHour(6).first),
)

fun SplashResultState.toFeatureTimeModel() = com.titi.app.feature.time.model.SplashResultState(
    recordTimes = recordTimes,
    timeColor = timeColor,
    daily = daily,
)
