package com.titi.domain.alarm.usecase

import com.titi.data.alarm.api.AlarmRepository
import javax.inject.Inject

class CanSetAlarmUseCase @Inject constructor(
    private val alarmRepository: AlarmRepository
) {

    operator fun invoke() = alarmRepository.canScheduleExactAlarms()

}