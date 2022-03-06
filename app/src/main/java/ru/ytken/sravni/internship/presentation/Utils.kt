package ru.ytken.sravni.internship.presentation

import android.content.Context
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import ru.ytken.sravni.internship.R
import ru.ytken.sravni.internship.domain.mainactivity.models.CoefficientParamMain

object Utils {
    fun convertStringToPrice(price: Float): String {
        var string = price.toInt().toString()
        var result = ""
        while (string.length > 3) {
            result = string.substring(string.length - 3) + " " + result
            string = string.substring(0, string.length - 3)
        }
        return "$string $result"
    }

    fun inflateCoefficientLayout(context: Context,
                                         layoutCoefficients: LinearLayout,
                                         coefficientsArray: ArrayList<CoefficientParamMain>,
                                         isExpanded: Boolean) {
        layoutCoefficients.removeAllViews()
        val inflater = LayoutInflater.from(context)

        val groupRowView = inflater.inflate(R.layout.exp_list_group_view, layoutCoefficients, false)

        val imageViewIcon = groupRowView.findViewById<ImageView>(R.id.imageViewIconCalculator)
        imageViewIcon.setImageDrawable(context.resources.getDrawable(R.drawable.ic_avatars,null))

        val imageArrow = groupRowView.findViewById<ImageView>(R.id.imageArrow)
        if (isExpanded)
            imageArrow.setImageResource(R.drawable.ic_arrow_up)
        else
            imageArrow.setImageResource(R.drawable.ic_arrow_down)

        val listOfTextView = arrayListOf<TextView>(
            groupRowView.findViewById(R.id.textViewBT),
            groupRowView.findViewById(R.id.textViewKM),
            groupRowView.findViewById(R.id.textViewKT),
            groupRowView.findViewById(R.id.textViewKBM),
            groupRowView.findViewById(R.id.textViewKO),
            groupRowView.findViewById(R.id.textViewKVS)
        )

        for (index in 0 until listOfTextView.size)
            listOfTextView[index].text = coefficientsArray[index].headerValue

        groupRowView.setOnClickListener {
            inflateCoefficientLayout(context, layoutCoefficients, coefficientsArray, !isExpanded)
        }

        layoutCoefficients.addView(groupRowView)

        if (isExpanded) {
            for (index in 0 until listOfTextView.size) {
                val childRowView = inflater.inflate(R.layout.exp_list_child_view, layoutCoefficients, false)

                val textViewHeading = childRowView.findViewById<TextView>(R.id.textViewHeading)
                val textViewSubheading = childRowView.findViewById<TextView>(R.id.textViewSubheading)
                val textViewDescription = childRowView.findViewById<TextView>(R.id.textViewDescription)
                val textViewCoefficient = childRowView.findViewById<TextView>(R.id.textViewCoefficient)

                val coefficientShow = coefficientsArray[index]
                textViewHeading.text = coefficientShow.title
                textViewSubheading.text = "(${coefficientShow.name})"
                textViewDescription.text = coefficientShow.detailText
                textViewCoefficient.text = coefficientShow.value

                layoutCoefficients.addView(childRowView)
            }
        }
    }
}