package ru.wip.testapp.feature.points.ui

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.PointF
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.wip.testapp.databinding.FragmentPointsBinding
import ru.wip.testapp.feature.points.domain.PointsUiState
import ru.wip.testapp.feature.points.domain.PointsViewModel
import ru.wip.testapp.feature.points.ui.chart.LineChartAdapter
import java.io.ByteArrayOutputStream


class PointsFragment : Fragment() {

    private lateinit var binding: FragmentPointsBinding

    private val viewModel: PointsViewModel by viewModel()

    private val listAdapter = PointsListAdapter()
    private val chartAdapter = LineChartAdapter(emptyList())

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPointsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.chartView.setAdapter(chartAdapter)
        binding.pointsRecyclerView.adapter = listAdapter

        observeUiState()
        setupListeners()
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.observe.collectLatest { state ->
                    dispatchUiState(state)
                }
            }
        }
    }

    private fun setupListeners() {
        binding.saveChartButton.setOnClickListener {
            onSaveChartButtonClicked()
        }
    }

    private fun onSaveChartButtonClicked() {
        val bitmap = getBitmapFromView(binding.chartView)
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
        viewModel.onSaveChartButtonClicked(stream.toByteArray())
    }

    private fun dispatchUiState(state: PointsUiState) {
        chartAdapter.setData(state.points.map { PointF(it.x, it.y) })
        listAdapter.setData(state.points)
    }

    private fun getBitmapFromView(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(
            view.width, view.height, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }
}