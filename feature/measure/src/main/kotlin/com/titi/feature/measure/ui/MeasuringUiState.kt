package com.titi.feature.measure.ui

import android.os.Build
import android.os.Bundle
import com.airbnb.mvrx.Mavericks
import com.airbnb.mvrx.MavericksState
import com.titi.core.util.addTimeToNow
import com.titi.core.util.getMeasureTime
import com.titi.doamin.daily.model.Daily
import com.titi.domain.color.model.TimeColor
import com.titi.domain.time.model.RecordTimes
import com.titi.feature.measure.SplashResultState
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime

data class MeasuringUiState(
    val measuringRecordTimes: MeasuringRecordTimes,
    val splashResultState: SplashResultState,
    val measureTime: Long,
    val isSleepMode: Boolean = false,
) : MavericksState {

    constructor(args: Bundle) : this(
        measuringRecordTimes = getSplashResultStateFromArgs(args).run {
            recordTimes.toMeasuringRecordTimes(
                isSleepMode = false,
                measureTime = getMeasureTime(
                    recordTimes.recordStartAt ?: ZonedDateTime.now(ZoneOffset.UTC).toString()
                ),
                daily = daily
            )
        },
        splashResultState = getSplashResultStateFromArgs(args),
        measureTime = getMeasureTime(
            getSplashResultStateFromArgs(args).recordTimes.recordStartAt
                ?: ZonedDateTime.now(ZoneOffset.UTC).toString()
        )
    )

    val recordTimes: RecordTimes get() = splashResultState.recordTimes
    val measuringTimeColor: MeasuringTimeColor
        get() = splashResultState.timeColor.toMeasuringTimeColor(
            recordTimes.recordingMode
        )
    val daily: Daily? get() = splashResultState.daily

}

fun getSplashResultStateFromArgs(args: Bundle): SplashResultState =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        args.getParcelable(
            Mavericks.KEY_ARG,
            SplashResultState::class.java
        )
    } else {
        args.getParcelable(Mavericks.KEY_ARG)
    } ?: SplashResultState()

data class MeasuringTimeColor(
    val backgroundColor: Long,
    val isTextBlackColor: Boolean,
)

data class MeasuringRecordTimes(
    val outCircularProgress: Float,
    val inCircularProgress: Float,
    val savedSumTime: Long,
    val savedTime: Long,
    val savedGoalTime: Long,
    val finishGoalTime: String,
    val isTaskTargetTimeOn: Boolean,
)

fun TimeColor.toMeasuringTimeColor(recordingMode: Int) = MeasuringTimeColor(
    backgroundColor = if (recordingMode == 1) timerBackgroundColor else stopwatchBackgroundColor,
    isTextBlackColor = if (recordingMode == 1) isTimerBlackTextColor else isStopwatchBlackTextColor
)

fun RecordTimes.toMeasuringRecordTimes(
    isSleepMode: Boolean,
    measureTime: Long,
    daily: Daily?
): MeasuringRecordTimes {
    val calculateSumTime = savedSumTime + measureTime
    val calculateSavedSumTime = if (isSleepMode) {
        calculateSumTime - calculateSumTime % 60
    } else {
        calculateSumTime
    }

    val calculateTime = if (recordingMode == 1) {
        setTimerTime - (savedTimerTime - measureTime)
    } else {
        savedStopWatchTime + measureTime
    }
    val calculateSavedTime = if (isSleepMode) {
        if (recordingMode == 1) {
            calculateTime - calculateTime % 60 + 60
        } else {
            calculateTime - calculateTime % 60
        }
    } else {
        calculateTime
    }

    val calculateGoalTime = currentTask?.let {
        if (it.isTaskTargetTimeOn) {
            it.taskTargetTime - (daily?.tasks?.get(it.taskName) ?: 0) - measureTime
        } else {
            savedGoalTime - measureTime
        }
    } ?: (savedGoalTime - measureTime)
    val calculateSavedGoalTime = if (isSleepMode) {
        calculateGoalTime - calculateGoalTime % 60
    } else {
        calculateGoalTime
    }

    val finishGoalTime = addTimeToNow(calculateSavedGoalTime)

    val maxOutCircular = if (recordingMode == 1) {
        setTimerTime.toFloat()
    } else {
        3600f
    }
    val outCircularProgress = calculateSavedTime / maxOutCircular

    val maxInCircular = if (currentTask?.isTaskTargetTimeOn == true) {
        currentTask?.taskTargetTime?.toFloat() ?: 0f
    } else {
        setGoalTime.toFloat()
    }
    val inCircularProgress = calculateSavedSumTime / maxInCircular

    return MeasuringRecordTimes(
        outCircularProgress = outCircularProgress,
        inCircularProgress = inCircularProgress,
        savedSumTime = calculateSavedSumTime,
        savedTime = calculateSavedTime,
        savedGoalTime = calculateSavedGoalTime,
        finishGoalTime = finishGoalTime,
        isTaskTargetTimeOn = currentTask?.isTaskTargetTimeOn ?: false
    )

}

