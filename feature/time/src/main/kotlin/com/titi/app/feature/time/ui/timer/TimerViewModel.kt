package com.titi.app.feature.time.ui.timer

import android.util.Log
import com.airbnb.mvrx.MavericksViewModel
import com.airbnb.mvrx.MavericksViewModelFactory
import com.airbnb.mvrx.hilt.AssistedViewModelFactory
import com.airbnb.mvrx.hilt.hiltMavericksViewModelFactory
import com.titi.app.core.util.isAfterSixAM
import com.titi.app.core.util.toJson
import com.titi.app.doamin.daily.model.Daily
import com.titi.app.doamin.daily.usecase.GetTodayDailyFlowUseCase
import com.titi.app.doamin.daily.usecase.UpsertDailyUseCase
import com.titi.app.domain.color.model.TimeColor
import com.titi.app.domain.color.usecase.GetTimeColorFlowUseCase
import com.titi.app.domain.color.usecase.UpdateColorUseCase
import com.titi.app.domain.time.model.RecordTimes
import com.titi.app.domain.time.usecase.GetRecordTimesFlowUseCase
import com.titi.app.domain.time.usecase.UpdateMeasuringStateUseCase
import com.titi.app.domain.time.usecase.UpdateRecordingModeUseCase
import com.titi.app.domain.time.usecase.UpdateSetGoalTimeUseCase
import com.titi.app.domain.time.usecase.UpdateSetTimerTimeUseCase
import com.titi.app.feature.time.model.SplashResultState
import com.titi.app.feature.time.model.TimerColor
import com.titi.app.feature.time.model.TimerUiState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime

class TimerViewModel @AssistedInject constructor(
    @Assisted initialState: TimerUiState,
    getRecordTimesFlowUseCase: GetRecordTimesFlowUseCase,
    getTimeColorFlowUseCase: GetTimeColorFlowUseCase,
    getTodayDailyFlowUseCase: GetTodayDailyFlowUseCase,
    private val updateRecordingModeUseCase: UpdateRecordingModeUseCase,
    private val updateColorUseCase: UpdateColorUseCase,
    private val updateSetGoalTimeUseCase: UpdateSetGoalTimeUseCase,
    private val upsertDailyUseCase: UpsertDailyUseCase,
    private val updateMeasuringStateUseCase: UpdateMeasuringStateUseCase,
    private val updateSetTimerTimeUseCase: UpdateSetTimerTimeUseCase,
) : MavericksViewModel<TimerUiState>(initialState) {
    init {
        getRecordTimesFlowUseCase().catch {
            Log.e("TimeViewModel", it.message.toString())
        }.setOnEach {
            copy(recordTimes = it)
        }

        getTimeColorFlowUseCase().catch {
            Log.e("TimeViewModel", it.message.toString())
        }.setOnEach {
            copy(timeColor = it)
        }

        getTodayDailyFlowUseCase().catch {
            Log.e("TimeViewModel", it.message.toString())
        }.setOnEach {
            copy(daily = it)
        }
    }

    private lateinit var prevTimerColor: TimerColor

    fun updateRecordingMode() {
        viewModelScope.launch {
            updateRecordingModeUseCase(1)
        }
    }

    fun updateColor(isTextBlackColor: Boolean = false) {
        viewModelScope.launch {
            updateColorUseCase(
                recordingMode = 1,
                isTextColorBlack = isTextBlackColor,
            )
        }
    }

    fun rollBackTimerColor() {
        viewModelScope.launch {
            if (::prevTimerColor.isInitialized) {
                updateColorUseCase(
                    recordingMode = 1,
                    backgroundColor = prevTimerColor.backgroundColor,
                    isTextColorBlack = prevTimerColor.isTextColorBlack,
                )
            }
        }
    }

    fun savePrevTimerColor(timerColor: TimerColor) {
        prevTimerColor = timerColor
    }

    fun updateSetGoalTime(recordTimes: RecordTimes, setGoalTime: Long) {
        viewModelScope.launch {
            updateSetGoalTimeUseCase(
                recordTimes,
                setGoalTime,
            )
        }
    }

    fun startRecording(recordTimes: RecordTimes, daily: Daily, timeColor: TimeColor): String {
        val updateRecordTimes = if (isAfterSixAM(daily.day)) {
            if (recordTimes.savedTimerTime <= 0) {
                recordTimes.copy(
                    recording = true,
                    recordStartAt = ZonedDateTime.now(ZoneOffset.UTC).toString(),
                    savedTimerTime = recordTimes.setTimerTime,
                )
            } else {
                recordTimes.copy(
                    recording = true,
                    recordStartAt = ZonedDateTime.now(ZoneOffset.UTC).toString(),
                )
            }
        } else {
            recordTimes.copy(
                recording = true,
                recordStartAt = ZonedDateTime.now(ZoneOffset.UTC).toString(),
                savedSumTime = 0,
                savedTimerTime = recordTimes.setTimerTime,
                savedStopWatchTime = 0,
                savedGoalTime = recordTimes.setGoalTime,
            )
        }

        val updateDaily = if (isAfterSixAM(daily.day)) {
            daily
        } else {
            Daily()
        }

        viewModelScope.launch {
            updateMeasuringStateUseCase(updateRecordTimes)
            upsertDailyUseCase(updateDaily)
        }

        return SplashResultState(
            recordTimes = updateRecordTimes,
            daily = updateDaily,
            timeColor = timeColor,
        ).toJson()
    }

    fun updateSetTimerTime(recordTimes: RecordTimes, timerTime: Long) {
        viewModelScope.launch {
            updateSetTimerTimeUseCase(
                recordTimes,
                timerTime,
            )
        }
    }

    @AssistedFactory
    interface Factory : AssistedViewModelFactory<TimerViewModel, TimerUiState> {
        override fun create(state: TimerUiState): TimerViewModel
    }

    companion object :
        MavericksViewModelFactory<TimerViewModel, TimerUiState> by hiltMavericksViewModelFactory()
}
