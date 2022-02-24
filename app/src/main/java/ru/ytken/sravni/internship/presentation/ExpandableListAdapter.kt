package ru.ytken.sravni.internship.presentation

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ImageView
import android.widget.TextView
import ru.ytken.sravni.internship.R
import ru.ytken.sravni.internship.domain.models.CoefficientParam
import ru.ytken.sravni.internship.domain.models.ListCoefficientsParam

class ExpandableListAdapter(context: Context, vm: MainViewModel)
    : BaseExpandableListAdapter() {

    private val mContext = context
    private val listCoeffs = vm.listCoefficient.value

    override fun getGroupCount(): Int { return 1 }

    override fun getChildrenCount(listPosition: Int): Int { return listCoeffs?.getSize() ?: 0 }

    override fun getGroup(listPosition: Int): ListCoefficientsParam? {
        return listCoeffs
    }

    override fun getChild(listPosition: Int, expListPosition: Int): CoefficientParam {
        return listCoeffs?.getElementById(listPosition) ?: CoefficientParam("title", "header", "name", "detail", "value")
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
        var myConvertView: View

        if (convertView == null) {
            val inflater: LayoutInflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            myConvertView = inflater.inflate(R.layout.exp_list_group_view, null)
        }
        else
            myConvertView = convertView
        val imageViewIcon = myConvertView.findViewById<ImageView>(R.id.imageViewIconCalculator)
        imageViewIcon.setImageDrawable(mContext.resources.getDrawable(R.drawable.ic_avatars,null))

        val imageArrow = myConvertView.findViewById<ImageView>(R.id.imageArrow)
        if (isExpanded)
            imageArrow.setImageResource(R.drawable.ic_arrow_up)
        else
            imageArrow.setImageResource(R.drawable.ic_arrow_down)

        if (listCoeffs != null) {
            val BTtextView = myConvertView.findViewById<TextView>(R.id.textViewBT)
            BTtextView.text = listCoeffs.BT.headerValue

            val KMtextView = myConvertView.findViewById<TextView>(R.id.textViewKM)
            KMtextView.text = listCoeffs.KM.headerValue

            val KTtextView = myConvertView.findViewById<TextView>(R.id.textViewKT)
            KTtextView.text = listCoeffs.KT.headerValue

            val KBMtextView = myConvertView.findViewById<TextView>(R.id.textViewKBM)
            KBMtextView.text = listCoeffs.KBM.headerValue

            val KOtextView = myConvertView.findViewById<TextView>(R.id.textViewKO)
            KOtextView.text = listCoeffs.KO.headerValue

            val KVStextView = myConvertView.findViewById<TextView>(R.id.textViewKVS)
            KVStextView.text = listCoeffs.KVS.headerValue
        }

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

            val coefficientShow = listCoeffs?.getElementById(expandedListPosition)
            if (coefficientShow != null) {
                textViewHeading.text = coefficientShow.title
                textViewSubheading.text = "(${coefficientShow.name})"
                textViewDescription.text = coefficientShow.detailText
                textViewCoefficient.text = coefficientShow.value
            }
        }
        else
            myConvertView = convertView

        return myConvertView
    }

    override fun isChildSelectable(listPosition: Int, expandedListPosition: Int): Boolean {
        return false
    }

}
