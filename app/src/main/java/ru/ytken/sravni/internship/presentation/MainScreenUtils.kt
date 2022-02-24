package ru.ytken.sravni.internship.presentation

import android.util.Log
import android.view.ViewGroup
import android.widget.ExpandableListView
import android.widget.ListView

object MainScreenUtils {

    var onCollapseHeight: Int = 0

    var onExpandHeight: Int = 0

    var listParametersHeight: Int = 0

    fun setExpandableListViewHeightBasedOnChildren(listView: ExpandableListView) {
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

    fun setListViewHeightBasedOnChildren(listView: ListView) {
        if (listParametersHeight == 0)
            listParametersHeight = countListViewHeight(listView)
        setListViewHeight(listView, listParametersHeight)
    }

    private fun countListViewHeight(listView: ListView) : Int {
        val adapter = listView.adapter
        var totalHeight = 0
        for (i in 0 until adapter.count) {
            val listItem = adapter.getView(i, null, listView)
            listItem.measure(0, 0)
            totalHeight += listItem.measuredHeight
        }
        return totalHeight + (listView.dividerHeight * (adapter.count -1)) + listView.paddingTop + listView.paddingBottom
    }

    private fun setListViewHeight(listView: ListView, height: Int) {
        var params: ViewGroup.LayoutParams = listView.layoutParams
        params.height = height
        listView.layoutParams = params
        listView.requestLayout()
    }

    fun callApi(vm: MainViewModel, LOGTAG: String) {
        Log.d(LOGTAG, "Call Api")
        vm.save()
    }

}