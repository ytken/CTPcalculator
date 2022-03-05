package ru.ytken.sravni.internship.presentation

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ImageView
import android.widget.TextView
import ru.ytken.sravni.internship.R
import ru.ytken.sravni.internship.domain.mainactivity.models.CoefficientParamMain

class ExpandableListAdapter(context: Context, listCoefficientsParam: ArrayList<CoefficientParamMain>)
    : BaseExpandableListAdapter() {

    private val mContext = context
    private val listCoeffs = listCoefficientsParam

    override fun getGroupCount(): Int {
        return 1 }

    override fun getChildrenCount(listPosition: Int): Int {
        return listCoeffs.size }

    override fun getGroup(listPosition: Int): List<CoefficientParamMain> {
        return listCoeffs
    }

    override fun getChild(listPosition: Int, expListPosition: Int): CoefficientParamMain {
        return listCoeffs[listPosition]
    }

    override fun getGroupId(listPosition: Int): Long {
        return 0L
    }

    override fun getChildId(listPosition: Int, expandedListPosition: Int): Long {
        return expandedListPosition.toLong()
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun getGroupView(listPosition: Int, isExpanded: Boolean,
                              convertView: View?, parent: ViewGroup?): View {

        var myConvertView: View = if (convertView == null) {
            val inflater: LayoutInflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            inflater.inflate(R.layout.exp_list_group_view, null)
        } else
            convertView
        val imageViewIcon = myConvertView.findViewById<ImageView>(R.id.imageViewIconCalculator)
        imageViewIcon.setImageDrawable(mContext.resources.getDrawable(R.drawable.ic_avatars,null))

        val imageArrow = myConvertView.findViewById<ImageView>(R.id.imageArrow)
        if (isExpanded)
            imageArrow.setImageResource(R.drawable.ic_arrow_up)
        else
            imageArrow.setImageResource(R.drawable.ic_arrow_down)

        val listOfTextView = arrayListOf<TextView>(
            myConvertView.findViewById(R.id.textViewBT),
            myConvertView.findViewById(R.id.textViewKM),
            myConvertView.findViewById(R.id.textViewKT),
            myConvertView.findViewById(R.id.textViewKBM),
            myConvertView.findViewById(R.id.textViewKO),
            myConvertView.findViewById(R.id.textViewKVS)
        )

        for (index in 0 until listOfTextView.size)
            listOfTextView[index].text = listCoeffs[index].headerValue

        return myConvertView
    }

    override fun getChildView(listPosition: Int, expandedListPosition: Int,
                              isLastChild: Boolean, convertView: View?, parent: ViewGroup?): View {

        var myConvertView: View

        if (convertView == null) {
            val inflater: LayoutInflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            myConvertView = inflater.inflate(R.layout.exp_list_child_view, null)

            val textViewHeading = myConvertView.findViewById<TextView>(R.id.textViewHeading)
            val textViewSubheading = myConvertView.findViewById<TextView>(R.id.textViewSubheading)
            val textViewDescription = myConvertView.findViewById<TextView>(R.id.textViewDescription)
            val textViewCoefficient = myConvertView.findViewById<TextView>(R.id.textViewCoefficient)

            val coefficientShow = listCoeffs[expandedListPosition]
            textViewHeading.text = coefficientShow.title
            textViewSubheading.text = "(${coefficientShow.name})"
            textViewDescription.text = coefficientShow.detailText
            textViewCoefficient.text = coefficientShow.value
        }
        else
            myConvertView = convertView

        return myConvertView
    }

    override fun isChildSelectable(listPosition: Int, expandedListPosition: Int): Boolean {
        return false
    }

}
