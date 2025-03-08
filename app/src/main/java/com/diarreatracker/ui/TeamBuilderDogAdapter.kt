package com.diarreatracker.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.diarreatracker.R

class TeamBuilderDogAdapter(
    private var itemCount: Int
):RecyclerView.Adapter<TeamBuilderDogAdapter.ViewHolder>() {

    inner class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView){

        fun bind(){

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.teambuilder_dog_element, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemCount
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }
}