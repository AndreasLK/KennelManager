package com.diarreatracker.ui

import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.diarreatracker.ui.component.StatusItem
import com.example.diarreatracker.R

class CageInfoAdapter(private val items: List<StatusItem>) : RecyclerView.Adapter<CageInfoAdapter.StatusViewHolder>(){

    inner class StatusViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val statusLine: TextView = itemView.findViewById(R.id.tvStatusLine)

    }


    override fun onBindViewHolder(holder: StatusViewHolder, position: Int) {
        val item = items[position]

        holder.statusLine.post {
            val textViewWidth = holder.statusLine.width

            holder.statusLine.text = generateDottedText(
                item.statusName,
                item.status,
                textViewWidth,
                holder.statusLine.textSize
            )
        }



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatusViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cage_status_element, parent, false)
        return StatusViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    private fun generateDottedText(
        leftText: String,
        rightText: String,
        textViewWidth: Int,
        textSize: Float
    ): String {
        val paint = Paint()
        paint.textSize = textSize

        val leftTextWidth = paint.measureText(leftText)
        val rightTextWidth = paint.measureText(rightText)

        val totalWidth = leftTextWidth + rightTextWidth

        Log.d("TOTAL WIDTH", "$totalWidth")
        Log.d("TEXT VIEW WIDTH", "$textViewWidth")

        val dotSize = paint.measureText(".")
        val numDots = ((textViewWidth - totalWidth) / dotSize).toInt().coerceIn(0, Int.MAX_VALUE)

        return "$leftText${".".repeat(numDots)}$rightText"
    }
}