package ru.ytken.sravni.internship.presentation

import android.content.Context
import android.util.AttributeSet
import android.widget.ExpandableListView

class CoefficientListView(context: Context, attrs: AttributeSet): ExpandableListView(context, attrs) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val expandSpec = MeasureSpec.makeMeasureSpec(
            Int.MAX_VALUE shr 2,
            MeasureSpec.AT_MOST
        )
        super.onMeasure(widthMeasureSpec, expandSpec)
    }

}