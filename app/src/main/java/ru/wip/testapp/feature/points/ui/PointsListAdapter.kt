package ru.wip.testapp.feature.points.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.wip.testapp.databinding.PointItemBinding
import ru.wip.testapp.feature.points.domain.Point

class PointsListAdapter : RecyclerView.Adapter<PointsListAdapter.PointViewHolder>() {

  private var data = emptyList<Point>()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PointViewHolder(
    PointItemBinding.inflate(
      LayoutInflater.from(parent.context), parent, false
    )
  )

  override fun getItemCount() = data.size

  override fun onBindViewHolder(holder: PointViewHolder, position: Int) {
    val item = data[position]

    with(holder.binding) {
      x.text = item.x.toString().fixMinusSign()
      y.text = item.y.toString().fixMinusSign()
    }
  }

  fun setData(data: List<Point>) {
    this.data = data
    notifyDataSetChanged()
  }

  private fun String.fixMinusSign(): String {
    return replace("-", "−")
  }

  class PointViewHolder(val binding: PointItemBinding) : RecyclerView.ViewHolder(binding.root)

}