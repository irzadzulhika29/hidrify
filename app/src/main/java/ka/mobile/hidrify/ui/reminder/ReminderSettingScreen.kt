package ka.mobile.hidrify.ui.reminder

import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderSettingScreen(
    navController: NavController,
    viewModel: ReminderViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pengaturan Reminder") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) { // KOMUNIKASI: KEMBALI KE ROUTE A
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            // 1. Switch On/Off
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Aktifkan Notifikasi", style = MaterialTheme.typography.bodyLarge)
                Switch(
                    checked = uiState.isEnabled,
                    onCheckedChange = { viewModel.updateState(isEnabled = it) }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
            Divider()
            Spacer(modifier = Modifier.height(24.dp))

            Text("Jadwal Smart Reminder", style = MaterialTheme.typography.titleMedium)
            Text(
                "Notifikasi hanya akan muncul di antara jam bangun dan jam tidur.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 2. Input Jam Bangun
            TimePickerField(
                label = "Jam Bangun",
                timeValue = uiState.wakeTime,
                context = context,
                onTimeSelected = { viewModel.updateState(wakeTime = it) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 3. Input Jam Tidur
            TimePickerField(
                label = "Jam Tidur",
                timeValue = uiState.bedTime,
                context = context,
                onTimeSelected = { viewModel.updateState(bedTime = it) }
            )

            Spacer(modifier = Modifier.weight(1f))

            // 4. Tombol Simpan
            Button(
                onClick = {
                    viewModel.saveSettings()
                    navController.popBackStack() // KOMUNIKASI: Tutup Route B, kembali ke A
                },
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text("Simpan Perubahan")
            }
        }
    }
}

// Komponen Helper untuk menampilkan Jam & Dialog
@Composable
fun TimePickerField(
    label: String,
    timeValue: String,
    context: Context,
    onTimeSelected: (String) -> Unit
) {
    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)

    val timePickerDialog = TimePickerDialog(
        context,
        { _, h, m ->
            val formattedTime = String.format("%02d:%02d", h, m)
            onTimeSelected(formattedTime)
        },
        hour, minute, true
    )

    OutlinedTextField(
        value = timeValue,
        onValueChange = {},
        label = { Text(label) },
        readOnly = true,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { timePickerDialog.show() }, // Klik text field -> Muncul Dialog Jam
        enabled = false, // Supaya keyboard tidak muncul
        colors = OutlinedTextFieldDefaults.colors(
            disabledTextColor = MaterialTheme.colorScheme.onSurface,
            disabledBorderColor = MaterialTheme.colorScheme.outline,
            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    )
}