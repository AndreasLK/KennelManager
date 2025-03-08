package com.diarreatracker.ui


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.diarreatracker.MainActivity
import com.diarreatracker.ui.component.CustomTextView
import com.diarreatracker.ui.component.DogItemView
import com.diarreatracker.ui.component.StatusItem
import com.example.diarreatracker.R

class BlockDialog(
    private val context: MainActivity,
    private val blockIndex: Int,
    private val actualView: CustomTextView,
    private val viewModel: DogSharedViewModel
) {
    private lateinit var dialogView: View
    private lateinit var dialogBlockView: CustomTextView
    private val adjuster = AdjustDialog(context, blockIndex, actualView)
    private var dogAmount = 0
    private lateinit var dialog: AlertDialog

    @SuppressLint("ClickableViewAccessibility")
    fun showDialog() {
        dialogView = LayoutInflater.from(context).inflate(R.layout.manage_cage, null)
        val builder = AlertDialog.Builder(context)
        builder.setView(dialogView)
        dialog = builder.create()
        updateView()
        val dialogTitle = dialogView.findViewById<TextView>(R.id.textView)
        val dialogCloseButton = dialogView.findViewById<ImageButton>(R.id.dialogCloseButton)
        dialogCloseButton.setOnClickListener{
            dialog.dismiss()
        }

        val editGeometryButttons = dialogView.findViewById<TextView>(R.id.editCageGeometryButton)
        editGeometryButttons.setOnClickListener {
            adjuster.showDialog()
        }

        val addDogButton = dialogView.findViewById<TextView>(R.id.addDogButton)
        addDogButton.setOnClickListener {
            actualView.dogs += DogItemView(
                "Please Fill",
                true,
                "Date of Birt",
                "Castration Text",
                "Bodyscore",
                false
            )
            updateView()
        }





        dialog.show()
    }

    private fun showDogs() {
        val recyclerView = dialogView.findViewById<RecyclerView>(R.id.dogListRecycler)

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = dogViewAdapter(actualView, viewModel, dialog, context)
        dogAmount = recyclerView.adapter!!.itemCount
    }

    private fun showStatus() {
        val recyclerView = dialogView.findViewById<RecyclerView>(R.id.cageStatusRecycler)
        recyclerView.isNestedScrollingEnabled = false
        val sampleData = listOf(
            StatusItem("HEAT", "YES"),
            StatusItem("Dogs", "$dogAmount"),
            StatusItem("Last Heat", "03.04.2024")
        )

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = CageInfoAdapter(sampleData)
        Log.d("STATUS AMOUNT", "SHOULD BE 3 BUT IS ACTUALLY ${recyclerView.adapter!!.itemCount}")
    }

    private fun updateView(){
        showDogs()
        showStatus()
    }
}

//        dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_block_options, null)
//        val builder = AlertDialog.Builder(context)
//        builder.setView(dialogView)
//
//        val dialogTitle = dialogView.findViewById<TextView>(R.id.dialog_title)
//        val importantCheckBox = dialogView.findViewById<CheckBox>(R.id.dialog_checkbox)
//        val applyButton = dialogView.findViewById<Button>(R.id.dialog_apply_button)
//        val adjustButton = dialogView.findViewById<Button>(R.id.dialog_change_size_button)
//        val editText = dialogView.findViewById<EditText>(R.id.dialog_edit_text)
//        dialogBlockView = dialogView.findViewById(R.id.dialog_block_view)
//
//        dialogTitle.text = "Options for Block $blockIndex"
//        editText.setText(actualView.rowName)
//
//        importantCheckBox.setOnCheckedChangeListener { _, isChecked ->
//            val message = if (isChecked) {
//                "Marked block $blockIndex as important"
//            } else {
//                "Unmarked block $blockIndex as important"
//            }
//            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
//        }
//
//        applyButton.setOnClickListener {
//            actualView.rowName = editText.text.toString()
//            actualView.text = "View ${actualView.id}\n${actualView.rowName}"
//        }
//
//        adjustButton.setOnClickListener {
//            adjuster.showDialog()
//        }
