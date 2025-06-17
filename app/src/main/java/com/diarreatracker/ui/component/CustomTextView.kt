package com.diarreatracker.ui.component
import android.content.Context
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import androidx.core.content.withStyledAttributes
import com.example.diarreatracker.R

class CustomTextView : androidx.appcompat.widget.AppCompatTextView {

    private val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 10f // Adjust the stroke width as needed
    }
    var dogColors: List<Int> = emptyList()
        set(value) {
            field = value
            invalidate() // Request a redraw
        }

    private val arcRct = RectF()

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
        context.withStyledAttributes(attrs, R.styleable.CustomTextView) {

            // Get the value for rowName
            rowName =
                getString(R.styleable.CustomTextView_rowName) ?: "Not Assigned" // Default value

        }
    }

    fun reload(){
        var dognames = ""
        for (dog in dogs) {
            dognames += dog.dogname + "\n"

        }
        val fulltext = "$rowName\n$dognames"
        text = fulltext

    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        if (dogColors.isEmpty()) return

        val halfStroke = borderPaint.strokeWidth / 2f
        arcRct.set(
            halfStroke,
            halfStroke,
            width - halfStroke,
            height - halfStroke)
    }









}
