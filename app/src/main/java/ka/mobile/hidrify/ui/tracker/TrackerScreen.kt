package ka.mobile.hidrify.ui.tracker

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrackerScreen(
    navController: NavController,
    viewModel: TrackerViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()
    val context = LocalContext.current
    var showAddDialog by rememberSaveable { mutableStateOf(false) }
    var pendingAmount by rememberSaveable { mutableStateOf<Int?>(null) }
    var selectedPhotoUri by rememberSaveable { mutableStateOf<String?>(null) }
    val cameraUriState = remember { mutableStateOf<Uri?>(null) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        selectedPhotoUri = uri?.toString()
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            selectedPhotoUri = cameraUriState.value?.toString()
        }
    }

    LaunchedEffect(uiState.waterLogs.size) {
        if (uiState.waterLogs.isNotEmpty()) {
            listState.animateScrollToItem(0)
        }
    }

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
            state = listState,
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
                        Button(onClick = {
                            pendingAmount = drinkOptions[index]
                            selectedPhotoUri = null
                            showAddDialog = true
                        }) {
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

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Tambah Minum") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text(
                        text = "Jumlah: ${pendingAmount ?: 0} ml",
                        style = MaterialTheme.typography.titleMedium
                    )

                    // Preview Foto
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            selectedPhotoUri?.let { uri ->
                                AsyncImage(
                                    model = uri,
                                    contentDescription = "Foto dokumentasi minum",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            } ?: Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.AddCircle,
                                    contentDescription = null,
                                    modifier = Modifier.size(64.dp),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Belum ada foto",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }

                    Text(
                        text = "Pilih Foto:",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    // Tombol Pilih Foto yang Diperbaiki
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        ElevatedButton(
                            modifier = Modifier.weight(1f),
                            onClick = {
                                galleryLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                            colors = ButtonDefaults.elevatedButtonColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(vertical = 8.dp)
                            ) {
                                Text(
                                    text = "🖼️",
                                    style = MaterialTheme.typography.headlineMedium
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Galeri",
                                    style = MaterialTheme.typography.labelMedium
                                )
                            }
                        }

                        ElevatedButton(
                            modifier = Modifier.weight(1f),
                            onClick = {
                                val uri = createImageUri(context)
                                cameraUriState.value = uri
                                uri?.let { cameraLauncher.launch(it) }
                            },
                            colors = ButtonDefaults.elevatedButtonColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(vertical = 8.dp)
                            ) {
                                Text(
                                    text = "📷",
                                    style = MaterialTheme.typography.headlineMedium
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Kamera",
                                    style = MaterialTheme.typography.labelMedium
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        pendingAmount?.let { amount ->
                            viewModel.addDrink(amount, selectedPhotoUri)
                        }
                        showAddDialog = false
                    },
                    enabled = pendingAmount != null
                ) {
                    Text("Simpan")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("Batal")
                }
            }
        )
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
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (log.photoUri != null) {
                AsyncImage(
                    model = log.photoUri,
                    contentDescription = "Foto dokumentasi minum",
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(12.dp))
            }
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

private fun createImageUri(context: android.content.Context): Uri? {
    return try {
        val image = File.createTempFile(
            "hydrify_${System.currentTimeMillis()}",
            ".jpg",
            context.cacheDir
        )
        FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", image)
    } catch (_: Exception) {
        null
    }
}