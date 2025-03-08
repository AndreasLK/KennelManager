package com.diarreatracker.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.diarreatracker.ui.component.CustomTextView
import com.example.diarreatracker.R

class GanglineAdapter(
    private var itemCount: Int

): RecyclerView.Adapter<GanglineAdapter.ViewHolder>() {

    private val colors = listOf(R.color.dog1, R.color.dog2, R.color.dog3, R.color.dog4, R.color.dog5, R.color.dog6)
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val dogLeft: CustomTextView = itemView.findViewById(R.id.sledElementDogLeft)
        private val dogRight: CustomTextView = itemView.findViewById(R.id.sledElementDogRight)
        private val dogBackgrondLeft: View = itemView.findViewById(R.id.sledElementDogBackgroundLeft)
        private val dogBackgrondRight: View = itemView.findViewById(R.id.sledElementDogBackgroundRight)


        fun bind(){

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.teambuilder_sled_element, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemCount
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }

    fun updateCount(newCount: Int){
        itemCount = newCount
        notifyDataSetChanged()
    }
}