package konkuk.link.bokbookbok.screen.admin

import android.R.attr.onClick
import android.R.attr.text
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import konkuk.link.bokbookbok.component.common.ButtonComponent
import konkuk.link.bokbookbok.component.common.ButtonTypeEnum
import konkuk.link.bokbookbok.ui.theme.bokBookBokColors
import konkuk.link.bokbookbok.ui.theme.defaultBokBookBokTypography
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    navController: NavController,
    factory: AdminViewModelFactory,
) {
    val viewModel: AdminViewModel = viewModel(factory = factory)
    val uiState by viewModel.uiState.collectAsState()

    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDateRangePickerState()

    val imagePickerLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickVisualMedia(),
            onResult = { uri -> viewModel.onImageSelected(uri) },
        )

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text("이번 주 도서 선정", style = defaultBokBookBokTypography.body)

        OutlinedButton(onClick = { showDatePicker = true }, modifier = Modifier.fillMaxWidth()) {
            val dateFormat = SimpleDateFormat("yy.MM.dd", Locale.getDefault())
            val start = viewModel.startDate?.let { dateFormat.format(it) }
            val end = viewModel.endDate?.let { dateFormat.format(it) }
            Text(text = if (start != null && end != null) "$start ~ $end" else "이번 주 날짜 선택", style = defaultBokBookBokTypography.subBody, color = bokBookBokColors.fontDarkBrown)
        }

        OutlinedTextField(value = viewModel.title, onValueChange = viewModel::onTitleChanged, label = { Text("이번 주 책 제목", style = defaultBokBookBokTypography.subBody) })
        OutlinedTextField(value = viewModel.author, onValueChange = viewModel::onAuthorChanged, label = { Text("저자", style = defaultBokBookBokTypography.subBody) })
        OutlinedTextField(value = viewModel.description, onValueChange = viewModel::onDescriptionChanged, label = { Text("이번 주 책 소개", style = defaultBokBookBokTypography.subBody) }, minLines = 4)

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text("금주 도서로 바로 지정", style = defaultBokBookBokTypography.subBody, color = bokBookBokColors.fontDarkBrown)
            Switch(
                checked = viewModel.isCurrent,
                onCheckedChange = viewModel::onIsCurrentChanged,
                colors =
                    SwitchDefaults.colors(
                        checkedThumbColor = bokBookBokColors.main,
                        checkedTrackColor = bokBookBokColors.main.copy(alpha = 0.5f),
                        uncheckedThumbColor = bokBookBokColors.fontLightGray,
                        uncheckedTrackColor = bokBookBokColors.borderLightGray,
                        uncheckedBorderColor = bokBookBokColors.borderDarkGray,
                    ),
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            OutlinedButton(
                onClick = {
                    imagePickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                },
            ) {
                Text("이번 주 책 사진", style = defaultBokBookBokTypography.subBody, color = bokBookBokColors.fontDarkBrown)
            }
            AsyncImage(model = viewModel.imageUri, contentDescription = "선택된 이미지", modifier = Modifier.size(100.dp))
        }

        Spacer(modifier = Modifier.weight(1f))

        when (val state = uiState) {
            is AdminUiState.Loading -> CircularProgressIndicator()
            is AdminUiState.Success -> Text(state.message, color = MaterialTheme.colorScheme.primary)
            is AdminUiState.Error -> Text(state.message, color = MaterialTheme.colorScheme.error)
            else -> {}
        }

        ButtonComponent(
            buttonText = "도서 선정 완료",
            buttonType = ButtonTypeEnum.FILL,
            onClick = viewModel::registerBook,
            enabled = uiState !is AdminUiState.Loading,
        )
    }

    if (showDatePicker) {
        DateRangePickerDialog(
            datePickerState = datePickerState,
            onConfirm = {
                showDatePicker = false
                val startMillis = datePickerState.selectedStartDateMillis
                val endMillis = datePickerState.selectedEndDateMillis
                viewModel.onDateRangeSelected(
                    start = startMillis?.let { Date(it) },
                    end = endMillis?.let { Date(it) },
                )
            },
            onCancel = { showDatePicker = false },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangePickerDialog(
    datePickerState: DateRangePickerState,
    onConfirm: () -> Unit,
    onCancel: () -> Unit,
) {
    DatePickerDialog(
        onDismissRequest = onCancel,
        confirmButton = { Button(onClick = onConfirm) { Text("확인") } },
        dismissButton = { TextButton(onClick = onCancel) { Text("취소") } },
    ) {
        DateRangePicker(state = datePickerState)
    }
}
