package jp.matsuura.facediary.ui.calendar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import jp.matsuura.facediary.databinding.ItemContentsBinding
import jp.matsuura.facediary.databinding.ItemHeaderBinding
import jp.matsuura.facediary.extenstion.BaseViewHolder
import jp.matsuura.facediary.extenstion.dpToPx

class CalendarAdapter(val onItemClick: ((Int) -> Unit)): RecyclerView.Adapter<BaseViewHolder>() {

    companion object {
        private const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_DAY = 1
    }

    var dataSource: List<CalendarViewModel.CalendarItem> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val context = parent.context
        val bottomHeight = 100.dpToPx(context)
        val headerHeight = 40.dpToPx(context)
        val height = (parent.height - bottomHeight - headerHeight) / 6
        return when(viewType) {
            VIEW_TYPE_HEADER -> HeaderViewHolder(
                ItemHeaderBinding.inflate(LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            VIEW_TYPE_DAY -> ContentsViewHolder(
                ItemContentsBinding.inflate(LayoutInflater.from(parent.context),
                    parent,
                    false
                ).apply {
                    root.minHeight = height
                },
                onItemClick = onItemClick
            )
            else -> throw IllegalStateException("Bad view type!!")
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        when (val item = dataSource[position]) {
            is CalendarViewModel.CalendarItem.Header -> {
                (holder as HeaderViewHolder).bind(item.youbi)
            }
            is CalendarViewModel.CalendarItem.Day -> {
                (holder as ContentsViewHolder).bind(item)
            }
        }
    }

    override fun getItemCount(): Int {
        return 7 * 7
    }

    override fun getItemViewType(position: Int): Int {
        return when(dataSource[position]) {
            is CalendarViewModel.CalendarItem.Header -> VIEW_TYPE_HEADER
            is CalendarViewModel.CalendarItem.Day -> VIEW_TYPE_DAY
        }
    }

}