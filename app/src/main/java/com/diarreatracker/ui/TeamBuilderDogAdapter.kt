package com.diarreatracker.ui

import android.content.ClipData
import android.content.Context
import android.text.TextUtils
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.diarreatracker.ui.component.DogDragShadowBuilder
import com.diarreatracker.ui.component.DogRunSummary
import com.diarreatracker.ui.component.TeambuilderDogElement
import com.example.diarreatracker.R

class TeamBuilderDogAdapter(
    private val items: MutableList<DogRunSummary>,
    private val recyclerWidth: Int,
    private val columnCount: Int,
    private val context: Context,
    private val days: Int,
    private val onItemRemoved: (DogRunSummary) -> Unit

):RecyclerView.Adapter<TeamBuilderDogAdapter.ViewHolder>() {
    private val cellWidth = recyclerWidth / columnCount
    private val cellHeight = context.dpToPx(50)


    inner class ViewHolder(val textView: TeambuilderDogElement) : RecyclerView.ViewHolder(textView) {
        init {
            textView.setOnLongClickListener{
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION){
                    val item = items[position]
                    val clipData = ClipData.newPlainText("dogData", item.dogID.toString())
                    val dragShadow = DogDragShadowBuilder(it)


                    it.startDragAndDrop(clipData, dragShadow, it, 0)
                    removeItem(position)
                    onItemRemoved(item)
                }
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {



        val textView = TeambuilderDogElement(parent.context).apply {
            layoutParams = RecyclerView.LayoutParams(
                cellWidth,
                cellHeight
            )
            setPadding(0,0,0,0)
            maxLines = 1
            isSingleLine = true
            ellipsize = TextUtils.TruncateAt.END
            gravity = Gravity.CENTER
            textAlignment = TextView.TEXT_ALIGNMENT_CENTER
            setBackgroundResource(R.drawable.textview_background)
        }
        return ViewHolder(
            textView
        )
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.bind(items[position], days)
    }

    fun removeItem(position: Int){
        if (position in items.indices){
            items.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    private fun Context.dpToPx(dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(),
            resources.displayMetrics
        ).toInt()
    }
}