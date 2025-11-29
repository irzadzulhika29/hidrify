package ka.mobile.hidrify.ui.tracker

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackerScreen(
    navController: NavController,
    viewModel: TrackerViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Hydrify Tracker") },
                actions = {
                    // Tombol Navigasi ke Route B (Settings)
                    IconButton(onClick = { navController.navigate("reminder_screen") }) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                // Bagian 1: Indikator Progress
                Text(text = "Target Harian: ${uiState.targetIntake} ml", style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(16.dp))

                CircularProgressIndicator(
                    progress = { uiState.progress },
                    modifier = Modifier.size(150.dp),
                    strokeWidth = 12.dp,
                )

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "${uiState.currentIntake} ml",
                    style = MaterialTheme.typography.headlineLarge
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Bagian 2: Tombol Input (Implementasi List/Collection)
                Text("Tambah Minum:", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                val drinkOptions = listOf(100, 250, 500, 600)
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.height(120.dp)
                ) {
                    items(drinkOptions.size) { index ->
                        Button(onClick = { viewModel.addDrink(drinkOptions[index]) }) {
                            Text("+ ${drinkOptions[index]} ml")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Tombol Reset
                OutlinedButton(
                    onClick = { viewModel.resetToday() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Reset Hari Ini")
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Bagian 3: History Section
                Text(
                    "History Hari Ini:",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            // History Cards
            items(uiState.waterLogs) { log ->
                WaterLogCard(
                    log = log,
                    onDelete = { viewModel.deleteLog(log) },
                    onClick = { navController.navigate("history_detail/${log.id}") }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                if (uiState.waterLogs.isEmpty()) {
                    Text(
                        "Belum ada history hari ini",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun WaterLogCard(
    log: ka.mobile.hidrify.data.local.entity.WaterLog,
    onDelete: () -> Unit,
    onClick: () -> Unit
) {
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    val time = timeFormat.format(Date(log.timestamp))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${log.amount} ml",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = time,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Hapus",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}