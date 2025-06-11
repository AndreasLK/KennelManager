package com.diarreatracker.ui.component
import android.content.Context
import android.util.AttributeSet
import com.example.diarreatracker.R

class CustomTextView : androidx.appcompat.widget.AppCompatTextView {

    // Property for rowName
    var rowName: String = "Nan"
        set(value) {
            field = value
            // You can update the TextView's text or do other actions when rowName changes
            var dognames = ""
            for (dog in dogs) {
                dognames += dog.dogname + "\n"

            }
            val fulltext = "$dognames\n$rowName"
            text = fulltext
        }

    var dogs: List<DogItemView> = emptyList()
        set(value) {
            field = value
            // You can update the TextView's text or do other actions when dogs change
            var dognames = ""
            for (dog in value) {
                dognames += dog.dogname + "\n"

            }
            val fulltext = "$dognames\n$rowName"
            text = fulltext
        }


    // Constructor for programmatic creation (in code)
    constructor(context: Context) : super(context)

    // Constructor for XML layout inflation (for use in layouts)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        // Call the method to initialize custom attributes
        initCustomAttributes(context, attrs)
    }

    // Constructor to apply style (for use in layouts)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        // Call the method to initialize custom attributes
        initCustomAttributes(context, attrs)
    }

    // Method to initialize custom attributes
    private fun initCustomAttributes(context: Context, attrs: AttributeSet) {
        // Obtain the custom attributes
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomTextView)

        // Get the value for rowName
        rowName = typedArray.getString(R.styleable.CustomTextView_rowName) ?: "Not Assigned" // Default value

        typedArray.recycle()
    }

    fun reload(){
        var dognames = ""
        for (dog in dogs) {
            dognames += dog.dogname + "\n"

        }
        val fulltext = "$rowName\n$dognames"
        text = fulltext

    }


}
