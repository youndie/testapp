package ru.wip.testapp.feature.points.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PointsViewModel(
  private val repository: PointsRepository,
  private val shareService: ShareService
) : ViewModel() {

  private val uiState = MutableStateFlow(PointsUiState(emptyList()))
  val observe = uiState.asStateFlow()


  init {
    viewModelScope.launch {
      uiState.update {
        PointsUiState(repository.fetch().sortedBy { it.x })
      }
    }
  }

  fun onSaveChartButtonClicked(bitmap: ByteArray) {
    viewModelScope.launch {
      shareService.shareImage(bitmap, "chart-screenshot")
    }
  }
}