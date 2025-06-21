package com.diarreatracker.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.diarreatracker.MainActivity
import com.diarreatracker.ui.component.CustomTextView
import com.diarreatracker.ui.component.DogItemView
import com.diarreatracker.ui.component.StatusItem
import com.example.diarreatracker.R

class BlockDialogFragment : DialogFragment() {

    private lateinit var actualView: CustomTextView
    private lateinit var viewModel: DogSharedViewModel
    private var blockIndex: Int = 0
    private var dogAmount = 0
    private lateinit var adjuster: AdjustDialog

    companion object {
        fun newInstance(
            blockIndex: Int,
            actualView: CustomTextView,
            viewModel: DogSharedViewModel
        ): BlockDialogFragment {
            val fragment = BlockDialogFragment()
            fragment.blockIndex = blockIndex
            fragment.actualView = actualView
            fragment.viewModel = viewModel
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.FullScreenDialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.manage_cage, container, false)
        adjuster = AdjustDialog(requireActivity() as MainActivity, blockIndex, actualView)

        setupViews(view)
        return view
    }

    private fun setupViews(view: View) {
        view.findViewById<ImageButton>(R.id.dialogCloseButton).setOnClickListener {
            dismiss()
        }

        view.findViewById<TextView>(R.id.editCageGeometryButton).setOnClickListener {
            adjuster.showDialog()
        }

        view.findViewById<TextView>(R.id.addDogButton).setOnClickListener {
            actualView.dogs += DogItemView(
                "Please Fill", true, "Date of Birth", "Castration Text", "Bodyscore", false
            )
            updateViews(view)
        }

        updateViews(view)
    }

    private fun updateViews(view: View) {
        showDogs(view)
        showStatus(view)
    }

    private fun showDogs(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.dogListRecycler)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = dogViewAdapter(actualView, viewModel, dialog!!, requireActivity() as MainActivity)
        dogAmount = recyclerView.adapter?.itemCount ?: 0
    }

    private fun showStatus(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.cageStatusRecycler)
        recyclerView.isNestedScrollingEnabled = false
        val data = listOf(
            StatusItem("HEAT", "YES"),
            StatusItem("Dogs", "$dogAmount"),
            StatusItem("Last Heat", "03.04.2024")
        )
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = CageInfoAdapter(data)
        Log.d("STATUS", "Items: ${recyclerView.adapter?.itemCount}")
    }
}
