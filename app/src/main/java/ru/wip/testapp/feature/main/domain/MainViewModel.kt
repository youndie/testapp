package ru.wip.testapp.feature.main.domain

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.wip.testapp.domain.navigation.Navigator
import ru.wip.testapp.domain.navigation.Screens
import ru.wip.testapp.feature.points.domain.PointsRepository

class MainViewModel(
  private val handle: SavedStateHandle,
  private val repository: PointsRepository,
  private val navigator: Navigator
) : ViewModel() {

  private var number: Int?
    get() = handle[NUMBER_TAG]
    set(value) {
      handle[NUMBER_TAG] = value
    }

  private val uiState = MutableStateFlow(MainUiState(number))

  val uiStateFlow = uiState.asStateFlow()

  fun onRunClicked() {
    viewModelScope.launch {
      uiState.update {
        MainUiState(
          number = number,
          loading = true
        )
      }

      val (points, error) = repository.load(number ?: 0)

      uiState.update {
        MainUiState(
          number = number,
          loading = false,
          error = error?.message
        )
      }

      points?.let {
        navigator.navigateTo(Screens.POINTS)
      }
    }
  }

  fun onNumberTextChanged(numberText: String?) {
    val number = numberText?.toIntOrNull()
    if (this.number != number) {
      this.number = number

      uiState.update {
        it.copy(number = number)
      }
    }
  }

  fun onSnackBarDismissed() {
    uiState.update {
      it.copy(error = null)
    }
  }

  companion object {
    private const val NUMBER_TAG = "number"
  }
}


