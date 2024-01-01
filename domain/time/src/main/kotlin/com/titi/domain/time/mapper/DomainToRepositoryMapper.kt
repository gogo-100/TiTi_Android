package com.titi.domain.time.mapper

import com.titi.data.time.api.model.CurrentTaskRepositoryModel
import com.titi.data.time.api.model.RecordTimesRepositoryModel
import com.titi.domain.time.model.CurrentTask
import com.titi.domain.time.model.RecordTimes

internal fun RecordTimes.toRepositoryModel() = RecordTimesRepositoryModel(
    recordingMode = recordingMode,
    recording = recording,
    recordStartAt = recordStartAt ?: "",
    setGoalTime = setGoalTime,
    setTimerTime = setTimerTime,
    savedSumTime = savedSumTime,
    savedTimerTime = savedTimerTime,
    savedStopWatchTime = savedStopWatchTime,
    savedGoalTime = savedGoalTime,
    currentTaskRepositoryModel = currentTask?.toRepositoryModel()
)

internal fun CurrentTask.toRepositoryModel() = CurrentTaskRepositoryModel(
    taskName = taskName,
    taskTargetTime = taskTargetTime,
    isTaskTargetTimeOn = isTaskTargetTimeOn
)