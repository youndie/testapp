package ru.wip.testapp.feature.main.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_SHORT
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.wip.testapp.R
import ru.wip.testapp.databinding.FragmentMainBinding
import ru.wip.testapp.feature.main.domain.MainViewModel

class MainFragment : Fragment() {

  private val viewModel: MainViewModel by viewModel()

  private lateinit var binding: FragmentMainBinding

  private val numberTextWatcher = object : TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    override fun afterTextChanged(s: Editable?) {
      viewModel.onNumberTextChanged(s?.toString())
    }

  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
  ): View {
    val view = inflater.inflate(R.layout.fragment_main, container, false)
    binding = FragmentMainBinding.bind(view)
    return view
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    observeUiState()
    setupListeners()
  }

  private fun observeUiState() {
    viewLifecycleOwner.lifecycleScope.launch {
      viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
        viewModel.uiStateFlow.collectLatest { state ->
          showProgress(state.loading)

          state.number?.toString()?.let {
            showText(it)
          }

          state.error?.let(::showError)
        }
      }
    }
  }

  private fun setupListeners() {
    binding.runButton.setOnClickListener {
      viewModel.onRunClicked()
    }
    binding.numberEditText.addTextChangedListener(numberTextWatcher)
  }

  private fun showText(numberText: String) {
    with(binding.numberEditText) {
      if (this.text?.toString() == numberText) return
      removeTextChangedListener(numberTextWatcher)
      setText(numberText)
      addTextChangedListener(numberTextWatcher)
    }
  }

  private fun showProgress(loading: Boolean) {
    with(binding) {
      numberTextInputLayout.isEnabled = !loading
      runButton.isEnabled = !loading
    }
  }

  private fun showError(error: String) {
    Snackbar.make(binding.root, error, LENGTH_SHORT).apply {
      addCallback(object : Snackbar.Callback() {
        override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
          super.onDismissed(transientBottomBar, event)
          viewModel.onSnackBarDismissed()
        }
      })
    }.show()
  }
}