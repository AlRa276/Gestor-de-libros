package com.marianaalra.book.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marianaalra.book.domain.model.Book
import com.marianaalra.book.domain.usecase.book.GetBooksUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

data class MonthStats(
    val label: String,       // "Ene 2025"
    val pendientes: Int,
    val enProgreso: Int,
    val finalizadas: Int
)

class StatisticsViewModel(
    private val getBooksUseCase: GetBooksUseCase
) : ViewModel() {

    private val _monthStats = MutableStateFlow<List<MonthStats>>(emptyList())
    val monthStats: StateFlow<List<MonthStats>> = _monthStats

    private val _totalFinalizadas = MutableStateFlow(0)
    val totalFinalizadas: StateFlow<Int> = _totalFinalizadas

    private val _totalEnProgreso = MutableStateFlow(0)
    val totalEnProgreso: StateFlow<Int> = _totalEnProgreso

    fun loadStats(userId: Long) {
        viewModelScope.launch {
            getBooksUseCase(userId).collect { books ->
                _totalFinalizadas.value = books.count { it.status == "FINALIZADA" }
                _totalEnProgreso.value = books.count { it.status == "EN_PROGRESO" }
                _monthStats.value = calcularEstadisticasMensuales(books)
            }
        }
    }

    private fun calcularEstadisticasMensuales(books: List<Book>): List<MonthStats> {
        val cal = Calendar.getInstance()
        val resultado = mutableListOf<MonthStats>()
        val meses = listOf("Ene","Feb","Mar","Abr","May","Jun",
            "Jul","Ago","Sep","Oct","Nov","Dic")

        // Últimos 6 meses
        repeat(6) { i ->
            cal.time = Date()
            cal.add(Calendar.MONTH, -i)
            val mes = cal.get(Calendar.MONTH)
            val anio = cal.get(Calendar.YEAR)

            val librosDelMes = books.filter { book ->
                val c = Calendar.getInstance()
                c.timeInMillis = book.fechaCreacion
                c.get(Calendar.MONTH) == mes && c.get(Calendar.YEAR) == anio
            }

            resultado.add(MonthStats(
                label = "${meses[mes]} $anio",
                pendientes = librosDelMes.count { it.status == "PENDIENTE" },
                enProgreso = librosDelMes.count { it.status == "EN_PROGRESO" },
                finalizadas = librosDelMes.count { it.status == "FINALIZADA" }
            ))
        }
        return resultado.reversed()
    }
}