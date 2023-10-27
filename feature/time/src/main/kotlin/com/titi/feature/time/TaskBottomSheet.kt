package com.titi.feature.time

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.mvrx.compose.collectAsState
import com.airbnb.mvrx.compose.mavericksViewModel
import com.titi.core.designsystem.component.TdsDialog
import com.titi.core.designsystem.component.TdsIconButton
import com.titi.core.designsystem.component.TdsInputTimeTextField
import com.titi.core.designsystem.component.TdsOutlinedInputTextField
import com.titi.core.designsystem.component.TdsTaskListItem
import com.titi.core.designsystem.component.TdsText
import com.titi.core.designsystem.component.TdsTextButton
import com.titi.core.designsystem.model.TdsDialogInfo
import com.titi.core.designsystem.model.TdsTask
import com.titi.core.designsystem.theme.TdsColor
import com.titi.core.designsystem.theme.TdsTextStyle
import com.titi.core.designsystem.theme.TiTiTheme
import com.titi.core.util.getTimeToLong
import com.titi.designsystem.R
import com.titi.domain.task.model.Task
import kotlinx.coroutines.android.awaitFrame

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun TaskBottomSheet(
    modifier: Modifier = Modifier,
    viewModel: TaskViewModel = mavericksViewModel(),
    themeColor: TdsColor,
    onCloseBottomSheet: (Boolean) -> Unit,
) {
    val uiState by viewModel.collectAsState()

    var showAddTaskDialog by remember { mutableStateOf(false) }
    var taskName by remember { mutableStateOf("") }

    var editMode by remember { mutableStateOf(false) }

    if (showAddTaskDialog) {
        taskName = ""
        TdsDialog(
            tdsDialogInfo = TdsDialogInfo.Confirm(
                title = stringResource(id = R.string.add_task_title),
                message = stringResource(id = R.string.add_task_message),
                cancelable = false,
                positiveText = stringResource(id = R.string.Ok),
                onPositive = {
                    viewModel.addTask(taskName)
                },
                negativeText = stringResource(id = R.string.Cancel),
            ),
            onShowDialog = { showAddTaskDialog = it }
        ) {
            val addTaskFocusRequester = remember { FocusRequester() }
            val keyboard = LocalSoftwareKeyboardController.current

            LaunchedEffect(addTaskFocusRequester) {
                addTaskFocusRequester.requestFocus()
                awaitFrame()
                keyboard?.show()
            }

            TdsOutlinedInputTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(26.dp)
                    .padding(horizontal = 15.dp)
                    .focusRequester(addTaskFocusRequester),
                fontSize = 17.sp,
                text = taskName,
                placeHolder = {
                    TdsText(
                        text = stringResource(id = R.string.add_task_title),
                        textStyle = TdsTextStyle.normalTextStyle,
                        fontSize = 17.sp,
                        color = TdsColor.dividerColor
                    )
                },
                onValueChange = {
                    if (it.length <= 12) {
                        taskName = it
                    }
                }
            )
        }
    }

    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(
        modifier = modifier.navigationBarsPadding(),
        onDismissRequest = { onCloseBottomSheet(false) },
        sheetState = bottomSheetState,
        shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
        tonalElevation = 0.dp,
        containerColor = TdsColor.backgroundColor.getColor(),
        contentColor = TdsColor.backgroundColor.getColor(),
        dragHandle = null,
    ) {
        TaskBottomSheet(
            uiState = uiState,
            themeColor = themeColor,
            editMode = editMode,
            onClickEditButton = {
                editMode = !editMode
            },
            onClickAddButton = {
                showAddTaskDialog = true
            },
            onClickTargetTimeEditButton = {
                viewModel.updateTask(it)
            },
            onClickTargetTimeSwitch = {
                viewModel.updateTask(it)
            },
            onDeleteTask = {
                viewModel.updateTask(it)
            }
        )
    }
}


@Composable
fun TaskBottomSheet(
    uiState: TaskUiState,
    themeColor: TdsColor,
    editMode: Boolean,
    onClickEditButton: () -> Unit,
    onClickAddButton: () -> Unit,
    onClickTargetTimeEditButton: (Task) -> Unit,
    onClickTargetTimeSwitch: (Task) -> Unit,
    onDeleteTask: (Task) -> Unit,
) {
    var hour by remember { mutableStateOf("") }
    var minutes by remember { mutableStateOf("") }
    var seconds by remember { mutableStateOf("") }
    var showTaskTargetTimeDialog by remember { mutableStateOf(false) }
    var editTask by remember {
        mutableStateOf(
            Task(
                id = 0,
                position = 0,
                taskName = "",
            )
        )
    }

    if (showTaskTargetTimeDialog) {
        hour = ""
        minutes = ""
        seconds = ""

        TdsDialog(
            tdsDialogInfo = TdsDialogInfo.Confirm(
                title = editTask.taskName,
                message = stringResource(id = R.string.edit_task_target_time),
                cancelable = false,
                positiveText = stringResource(id = R.string.Ok),
                onPositive = {
                    val targetTime = getTimeToLong(hour, minutes, seconds)
                    onClickTargetTimeEditButton(editTask.copy(taskTargetTime = targetTime))
                },
                negativeText = stringResource(id = R.string.Cancel),
            ),
            onShowDialog = {
                showTaskTargetTimeDialog = it
            }
        ) {
            TdsInputTimeTextField(
                hour = hour,
                onHourChange = { hour = it },
                minutes = minutes,
                onMinutesChange = { minutes = it },
                seconds = seconds,
                onSecondsChange = { seconds = it }
            )
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
        ) {
            TdsTextButton(
                modifier = Modifier.align(Alignment.CenterStart),
                text = stringResource(id = R.string.edit),
                textColor = themeColor,
                fontSize = 18.sp,
                onClick = onClickEditButton
            )

            TdsText(
                modifier = Modifier.align(Alignment.Center),
                text = stringResource(id = R.string.tasks),
                textStyle = TdsTextStyle.normalTextStyle,
                fontSize = 20.sp,
                color = TdsColor.textColor
            )

            TdsIconButton(
                modifier = Modifier.align(Alignment.CenterEnd),
                onClick = onClickAddButton
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.add_icon),
                    contentDescription = "add",
                    tint = themeColor.getColor()
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            items(uiState.tasks) { task ->
                TdsTaskListItem(
                    tdsTask = TdsTask(
                        taskTargetTime = task.taskTargetTime,
                        isTaskTargetTimeOn = task.isTaskTargetTimeOn,
                        taskName = task.taskName
                    ),
                    editMode = editMode,
                    themeColor = themeColor,
                    onClickTask = { },
                    onLongClickTask = { },
                    onEdit = {
                        editTask = task
                        showTaskTargetTimeDialog = true
                    },
                    onTargetTimeOn = {
                        onClickTargetTimeSwitch(
                            task.copy(
                                isTaskTargetTimeOn = it
                            )
                        )
                    },
                    onDelete = {
                        onDeleteTask(
                            task.copy(
                                isDelete = true
                            )
                        )
                    },
                    onLongClickMenu = {}
                )
            }
        }
    }
}

@Preview
@Composable
private fun TaskBottomSheetPreview() {
    TiTiTheme {
        TaskBottomSheet(
            uiState = TaskUiState(
                tasks = listOf(
                    Task(
                        id = 0,
                        position = 0,
                        taskName = "국어",
                        taskTargetTime = 3600,
                        isTaskTargetTimeOn = false,
                        savedSumTime = 0,
                        isDelete = false
                    ),
                    Task(
                        id = 1,
                        position = 1,
                        taskName = "영어",
                        taskTargetTime = 3600,
                        isTaskTargetTimeOn = false,
                        savedSumTime = 0,
                        isDelete = false
                    ),
                    Task(
                        id = 2,
                        position = 2,
                        taskName = "수학",
                        taskTargetTime = 3600,
                        isTaskTargetTimeOn = false,
                        savedSumTime = 0,
                        isDelete = false
                    ),
                )
            ),
            themeColor = TdsColor.blueColor,
            editMode = true,
            onClickEditButton = { },
            onClickAddButton = { },
            onClickTargetTimeEditButton = { },
            onClickTargetTimeSwitch = {},
            onDeleteTask = {}
        )
    }
}