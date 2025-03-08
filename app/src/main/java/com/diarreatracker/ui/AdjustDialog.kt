package com.diarreatracker.ui

import android.app.AlertDialog
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import com.diarreatracker.MainActivity
import com.diarreatracker.ui.component.CustomTextView
import com.example.diarreatracker.R

class AdjustDialog(
    private val context: MainActivity,
    private val blockIndex: Int,
    private val actualView: CustomTextView
) {
    private lateinit var dialogView: View
    private lateinit var dialog: AlertDialog
    private lateinit var BlockView: CustomTextView

    fun showDialog(){
        dialogView = LayoutInflater.from(context).inflate(R.layout.size_changer_customtextview, null)
        val builder = AlertDialog.Builder(context)
        builder.setView(dialogView)

        dialog = builder.create()

        val applyAndExitButton = dialogView.findViewById<Button>(R.id.applyAndExitButton)
        val seekBarHeight = dialogView.findViewById<SeekBar>(R.id.seekBarHeight)
        val seekBarWidth = dialogView.findViewById<SeekBar>(R.id.seekBarWidth)
        val exitButton = dialogView.findViewById<ImageButton>(R.id.exitButton)
        val resetButton = dialogView.findViewById<Button>(R.id.resetButton)
        val rownameEdit = dialogView.findViewById<TextView>(R.id.rowNameEditText)


        BlockView = dialogView.findViewById(R.id.dialog_block_view)


        rownameEdit.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                BlockView.rowName = "Preview \n$s"
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        })

        exitButton.setOnClickListener {
            dialog.dismiss()
        }
        resetButton.setOnClickListener {
            resetGUI(seekBarHeight, seekBarWidth, rownameEdit)
        }

        applyAndExitButton.setOnClickListener {
            val layoutParams = actualView.layoutParams
            layoutParams.height = BlockView.height
            layoutParams.width = BlockView.width

            actualView.layoutParams = layoutParams
            actualView.rowName = rownameEdit.text.toString()
            actualView.reload()
            dialog.dismiss()
        }


        seekBarHeight.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

                val snappedProgress = progress / 25 * 25
                seekBar?.progress = snappedProgress

                val layoutParams = BlockView.layoutParams
                layoutParams.height = snappedProgress
                BlockView.layoutParams = layoutParams

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                val snappedProgress = seekBarHeight.progress / 25 * 25
                seekBar?.progress = snappedProgress
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                val snappedProgress = seekBarHeight.progress / 25 * 25
                seekBar?.progress = snappedProgress
            }
        })

        seekBarWidth.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

                val snappedProgress = progress / 25 * 25
                seekBar?.progress = snappedProgress

                val layoutParams = BlockView.layoutParams
                layoutParams.width = snappedProgress
                BlockView.layoutParams = layoutParams

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                val snappedProgress = seekBarWidth.progress / 25 * 25
                seekBar?.progress = snappedProgress
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                val snappedProgress = seekBarWidth.progress / 25 * 25
                seekBar?.progress = snappedProgress
            }
        })

        resetGUI(seekBarHeight, seekBarWidth, rownameEdit)
        dialog.show()
    }

    private fun resetGUI(seekBarHeight: SeekBar, seekBarWidth: SeekBar, rownameEdit: TextView){
        BlockView.height = actualView.height
        BlockView.width = actualView.width
        BlockView.rowName = actualView.rowName

        rownameEdit.text = BlockView.rowName

        seekBarHeight.progress = actualView.height
        seekBarWidth.progress = actualView.width
    }
}