package jp.matsuura.facediary.ui.calendar

import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import jp.matsuura.facediary.R
import jp.matsuura.facediary.databinding.ItemContentsBinding
import jp.matsuura.facediary.common.enum.Youbi
import jp.matsuura.facediary.extenstion.BaseViewHolder

class ContentsViewHolder(private val binding: ItemContentsBinding, val onItemClick: ((Int) -> Unit)): BaseViewHolder(binding.root) {

    fun bind(item: CalendarViewModel.CalendarItem.Day) {

        val context = binding.root.context
        binding.day.text = item.day.toString()

        @ColorRes
        val color = when (item.youbi) {
            Youbi.SUN -> R.color.red50
            Youbi.SAT -> R.color.blue50
            else ->
                if (item.isCurrentMonth)
                    R.color.black
                else
                    R.color.gray30
        }

        binding.day.setTextColor(ContextCompat.getColor(context, color))

        binding.root.setOnClickListener {
            if (item.isCurrentMonth) {
                onItemClick(item.day)
            }
        }
    }
}