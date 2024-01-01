package com.titi.feature.time.content

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.titi.core.designsystem.component.TdsDialog
import com.titi.core.designsystem.component.TdsInputTimeTextField
import com.titi.core.designsystem.model.TdsDialogInfo
import com.titi.core.util.addTimeToNow
import com.titi.core.util.getTimeToLong
import com.titi.designsystem.R

@Composable
fun TimeTimerDialog(
    onPositive: (Long) -> Unit,
    onShowDialog: (Boolean) -> Unit,
) {
    var hour by remember { mutableStateOf("") }
    var minutes by remember { mutableStateOf("") }
    var seconds by remember { mutableStateOf("") }
    var setTimerTime by remember { mutableLongStateOf(0) }

    TdsDialog(
        tdsDialogInfo = TdsDialogInfo.Confirm(
            title = stringResource(R.string.set_timer_time_title),
            message = stringResource(
                R.string.set_timer_time_message,
                addTimeToNow(setTimerTime)
            ),
            positiveText = stringResource(id = R.string.Ok),
            negativeText = stringResource(id = R.string.Cancel),
            onPositive = {
                onPositive(setTimerTime)
            },
        ),
        onShowDialog = onShowDialog
    ) {
        TdsInputTimeTextField(
            modifier = Modifier.padding(horizontal = 15.dp),
            hour = hour,
            onHourChange = {
                hour = it
                setTimerTime = getTimeToLong(hour, minutes, seconds)
            },
            minutes = minutes,
            onMinutesChange = {
                minutes = it
                setTimerTime = getTimeToLong(hour, minutes, seconds)
            },
            seconds = seconds,
            onSecondsChange = {
                seconds = it
                setTimerTime = getTimeToLong(hour, minutes, seconds)
            }
        )
    }
}