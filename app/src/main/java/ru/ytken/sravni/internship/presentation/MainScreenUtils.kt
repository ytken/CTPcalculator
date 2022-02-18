package ru.ytken.sravni.internship.presentation

import android.view.ViewGroup
import android.widget.ExpandableListView

object MainScreenUtils {

    var onCollapseHeight: Int = 0

    var onExpandHeight: Int = 0

    fun setListViewHeightBasedOnChildren(listView: ExpandableListView) {
        if (listView.isGroupExpanded(0)) {
            if (onExpandHeight == 0)
                onExpandHeight = countListViewHeight(listView)
            setListViewHeight(listView, onExpandHeight)
        }
        else {
            if (onCollapseHeight == 0)
                onCollapseHeight = countListViewHeight(listView)
            setListViewHeight(listView, onCollapseHeight)
        }
    }

    private fun countListViewHeight(listView: ExpandableListView) : Int {
        val adapter = listView.adapter
        var totalHeight = 0
        for (i in 0..adapter.count - 1) {
            val listItem = adapter.getView(i, null, listView)
            listItem.measure(0, 0)
            totalHeight += listItem.measuredHeight
        }

        return totalHeight + (listView.dividerHeight * (adapter.count - 1))
    }

    private fun setListViewHeight(listView: ExpandableListView, height: Int) {
        var params: ViewGroup.LayoutParams = listView.layoutParams
        params.height = height
        listView.layoutParams = params
        listView.requestLayout()
    }

}