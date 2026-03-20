package com.marianaalra.booklog.ui.feature.statistics

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.marianaalra.booklog.ui.viewmodel.MonthStats
import com.marianaalra.booklog.ui.viewmodel.StatisticsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    userId: Long,
    viewModel: StatisticsViewModel,
    onNavigateBack: () -> Unit
) {
    val monthStats by viewModel.monthStats.collectAsState()
    val totalFinalizadas by viewModel.totalFinalizadas.collectAsState()
    val totalEnProgreso by viewModel.totalEnProgreso.collectAsState()

    LaunchedEffect(userId) { viewModel.loadStats(userId) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Estadísticas") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Regresar")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Tarjetas resumen
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SummaryCard(
                    label = "Finalizadas",
                    value = totalFinalizadas,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )
                SummaryCard(
                    label = "En progreso",
                    value = totalEnProgreso,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.weight(1f)
                )
            }

            Text("Lecturas por mes (últimos 6 meses)",
                style = MaterialTheme.typography.titleMedium)

            // Gráfica de barras simple por mes
            if (monthStats.isEmpty()) {
                Text("Aún no hay datos suficientes.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            } else {
                monthStats.forEach { stat ->
                    MonthBarRow(stat = stat)
                }
            }
        }
    }
}

@Composable
fun SummaryCard(label: String, value: Int, color: androidx.compose.ui.graphics.Color, modifier: Modifier = Modifier) {
    Card(modifier = modifier, colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value.toString(), style = MaterialTheme.typography.headlineLarge, color = color)
            Text(label, style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer)
        }
    }
}

@Composable
fun MonthBarRow(stat: MonthStats) {
    val total = (stat.pendientes + stat.enProgreso + stat.finalizadas).coerceAtLeast(1)
    val maxWidth = 200.dp

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(stat.label, style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onBackground)
        Spacer(Modifier.height(4.dp))

        // Barra finalizadas
        if (stat.finalizadas > 0) {
            Row(verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(
                    modifier = Modifier
                        .height(16.dp)
                        .width(maxWidth * stat.finalizadas / total)
                        .background(MaterialTheme.colorScheme.primary,
                            shape = MaterialTheme.shapes.small)
                )
                Text("${stat.finalizadas} fin.", style = MaterialTheme.typography.bodySmall)
            }
            Spacer(Modifier.height(4.dp))
        }

        // Barra en progreso
        if (stat.enProgreso > 0) {
            Row(verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(
                    modifier = Modifier
                        .height(16.dp)
                        .width(maxWidth * stat.enProgreso / total)
                        .background(MaterialTheme.colorScheme.tertiary,
                            shape = MaterialTheme.shapes.small)
                )
                Text("${stat.enProgreso} prog.", style = MaterialTheme.typography.bodySmall)
            }
            Spacer(Modifier.height(4.dp))
        }

        // Barra pendientes
        if (stat.pendientes > 0) {
            Row(verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(
                    modifier = Modifier
                        .height(16.dp)
                        .width(maxWidth * stat.pendientes / total)
                        .background(MaterialTheme.colorScheme.surfaceVariant,
                            shape = MaterialTheme.shapes.small)
                )
                Text("${stat.pendientes} pend.", style = MaterialTheme.typography.bodySmall)
            }
        }

        HorizontalDivider(modifier = Modifier.padding(top = 8.dp))
    }
}