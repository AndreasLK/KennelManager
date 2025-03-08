package com.diarreatracker.ui

import android.app.AlertDialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.diarreatracker.ui.component.CustomTextView
import com.diarreatracker.ui.component.DogItemView
import com.example.diarreatracker.R

class dogViewAdapter(
    private val view: CustomTextView,
    private val viewModel: DogSharedViewModel,
    private val popup: AlertDialog,
    private val context: Context
    ): RecyclerView.Adapter<dogViewAdapter.ListViewHolder>()  {

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dogName: EditText = itemView.findViewById(R.id.dogNameDisplay)
        val dogAge: EditText = itemView.findViewById(R.id.ageDisplay)
        val castrationText: EditText = itemView.findViewById(R.id.sterilizationTextDisplay)
        val bodyScore: EditText = itemView.findViewById(R.id.bodyScoreDisplay)
        val genderMale: ImageView = itemView.findViewById(R.id.genderSymbolMale)
        val genderFemale: ImageView = itemView.findViewById(R.id.genderSymbolFemale)
        private val editButton: ImageButton = itemView.findViewById(R.id.editDogButton)
        private val moveButton: ImageButton = itemView.findViewById(R.id.moveDogButton)
        val background: ImageView = itemView.findViewById(R.id.dogUnitBackground)
        val heatButton: Button = itemView.findViewById(R.id.dogHeatButton)
        private var isEditing = false

        init {
            setEditTextsEditable(false)
        }


        private fun Boolean.toInt() = if (this) 1 else 0
        private fun setEditTextsEditable(editable: Boolean){
            val colors = listOf(ContextCompat.getColor(this.itemView.context, R.color.white), ContextCompat.getColor(this.itemView.context, R.color.orange))


            dogName.isFocusable = editable
            dogName.isFocusableInTouchMode = editable
            dogName.setTextColor(colors[editable.toInt()])
            dogAge.isFocusable = editable
            dogAge.isFocusableInTouchMode = editable
            dogAge.setTextColor(colors[editable.toInt()])
            castrationText.isFocusable = editable
            castrationText.isFocusableInTouchMode = editable
            castrationText.setTextColor(colors[editable.toInt()])
            bodyScore.isFocusable = editable
            bodyScore.isFocusableInTouchMode = editable
            bodyScore.setTextColor(colors[editable.toInt()])

            //editButton.backgroundTintList = ContextCompat.getColorStateList(this.itemView.context, R.color.orange)
            if (!editable){
                dogName.clearFocus()
                dogAge.clearFocus()
                castrationText.clearFocus()
                bodyScore.clearFocus()
                editButton.backgroundTintList = ColorStateList.valueOf(Color.TRANSPARENT)

            }
        }


        fun bind(currentItem: DogItemView){
            editButton.setOnClickListener{
                Log.d("EDIT", "EDIT WAS CLICKED")
                isEditing = !isEditing
                setEditTextsEditable(isEditing)


                if (!isEditing){
                    currentItem.dogname = dogName.text.toString()
                    currentItem.dateOfBirth = dogAge.text.toString()
                    currentItem.castrationText = castrationText.text.toString()
                    currentItem.bodyScore = bodyScore.text.toString()

                    view.reload()
                }
            }

            moveButton.setOnClickListener{
                Log.d("MOVING DOG", "DOG WAS CLICKED")
                viewModel.addDog(currentItem)
                view.dogs = view.dogs.filter { it != currentItem }
                view.reload()

                popup.dismiss()


            }

            genderFemale.setOnClickListener{
                if (isEditing){
                    currentItem.gender = true
                    genderFemale.visibility = View.INVISIBLE
                    genderMale.visibility = View.VISIBLE
                    heatButton.visibility = View.VISIBLE
                    view.reload()

                }
            }


            genderMale.setOnClickListener{
                if (isEditing){
                    currentItem.gender = false
                    genderFemale.visibility = View.VISIBLE
                    genderMale.visibility = View.INVISIBLE
                    heatButton.visibility = View.INVISIBLE
                    currentItem.heat = false
                    view.reload()

                }
            }

            heatButton.setOnClickListener{
                currentItem.heat = !currentItem.heat
                view.reload()
            }
        }

    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val currentItem = view.dogs[position]
        holder.dogName.setText(currentItem.dogname)
        holder.dogAge.setText(currentItem.dateOfBirth)
        holder.castrationText.setText(currentItem.castrationText)
        holder.bodyScore.setText(currentItem.bodyScore)

        if (currentItem.gender) {
            holder.genderMale.visibility = View.VISIBLE
            holder.genderFemale.visibility = View.INVISIBLE
        } else {
            holder.genderMale.visibility = View.INVISIBLE
            holder.genderFemale.visibility = View.VISIBLE

        }

        var backgroundColor = ContextCompat.getColor(context, R.color.dog_view_standard)
        if (currentItem.heat) {
            backgroundColor = ContextCompat.getColor(context, R.color.dog_view_heat)
        }
        holder.background.imageTintList = ColorStateList.valueOf(backgroundColor)

        holder.bind(currentItem)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.dog_unit, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount() = view.dogs.size
}