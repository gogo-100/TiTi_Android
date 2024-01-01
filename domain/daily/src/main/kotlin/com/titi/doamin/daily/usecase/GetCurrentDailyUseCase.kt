package com.titi.doamin.daily.usecase

import com.titi.data.daily.api.DailyRepository
import com.titi.doamin.daily.mapper.toDomain
import javax.inject.Inject

class GetCurrentDailyUseCase @Inject constructor(
    private val dailyRepository: DailyRepository
) {

    suspend operator fun invoke() =
        dailyRepository.getCurrentDaily()?.toDomain()

}