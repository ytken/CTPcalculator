package ru.ytken.sravni.internship.presentation

import android.view.ViewGroup
import android.widget.ExpandableListView
import android.widget.ListView
import androidx.core.view.marginBottom
import androidx.core.view.marginTop

object MainScreenUtils {

    var onCollapseHeight: Int = 0

    var onExpandHeight: Int = 0

    var listParametersHeight: Int = 0

    fun setExpandableListViewHeightBasedOnChildren(listView: ExpandableListView) {
        if (listView.isGroupExpanded(0)) {
            if (onExpandHeight == 0)
                onExpandHeight = listView.height
            setListViewHeight(listView, onExpandHeight)
        }
        else {
            if (onCollapseHeight == 0)
                onCollapseHeight = listView.height
            setListViewHeight(listView, onCollapseHeight)
        }
    }

    private fun countListViewHeight(listView: ListView) : Int {
        val adapter = listView.adapter
        var totalHeight = 0
        for (i in 0 until adapter.count) {
            val listItem = adapter.getView(i, null, listView)
            listItem.measure(0, 0)
            totalHeight += listItem.measuredHeight + listItem.marginTop + listItem.marginBottom
        }
        return totalHeight + (listView.dividerHeight * (adapter.count - 1)) + listView.paddingTop + listView.paddingBottom
    }

    private fun setListViewHeight(listView: ListView, height: Int) {
        var params: ViewGroup.LayoutParams = listView.layoutParams
        params.height = height
        listView.layoutParams = params
        listView.requestLayout()
    }

}