package ru.ytken.sravni.internship.presentation

import android.content.Context
import android.graphics.*
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import ru.ytken.sravni.internship.R
import ru.ytken.sravni.internship.domain.insurersactivity.models.InsurerParam
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
                                 isExpanded: MutableLiveData<Boolean>
    ) {
        layoutCoefficients.removeAllViews()
        val inflater = LayoutInflater.from(context)

        val groupRowView = inflater.inflate(R.layout.exp_list_group_view, layoutCoefficients, false)

        val imageViewIcon = groupRowView.findViewById<ImageView>(R.id.imageViewIconCalculator)
        imageViewIcon.setImageDrawable(context.resources.getDrawable(R.drawable.ic_avatars,null))

        val imageArrow = groupRowView.findViewById<ImageView>(R.id.imageArrow)

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

        layoutCoefficients.addView(groupRowView)

        groupRowView.setOnClickListener {
            val visibility =
                if (layoutCoefficients.
                    findViewWithTag<ConstraintLayout>(coefficientsArray[0].title).
                    visibility == View.VISIBLE) View.GONE else View.VISIBLE

            if (visibility == View.VISIBLE) {
                imageArrow.setImageResource(R.drawable.ic_arrow_up)
                isExpanded.value = true
            }
            else {
                imageArrow.setImageResource(R.drawable.ic_arrow_down)
                isExpanded.value = false
            }


            for (index in 0 until coefficientsArray.size) {
                val childRowView = layoutCoefficients.findViewWithTag<ConstraintLayout>(coefficientsArray[index].title)
                childRowView.visibility = visibility
            }
        }

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

            childRowView.tag = coefficientShow.title

            childRowView.visibility = View.GONE

            layoutCoefficients.addView(childRowView)
        }

        if (isExpanded.value == true)
            groupRowView.callOnClick()
    }

    fun createRowViewInsurer(context: Context,
                             layout: LinearLayout,
                             insurerParam: InsurerParam) : View {
        val rowView = LayoutInflater.from(context)
            .inflate(R.layout.insurers_list_child_view, layout, false)

        val textViewHeading = rowView.findViewById<TextView>(R.id.textViewNameBank)
        textViewHeading.text = insurerParam.name

        val textViewRating = rowView.findViewById<TextView>(R.id.textViewBankRating)
        textViewRating.text = insurerParam.rating.toString()

        val imageViewStar = rowView.findViewById<ImageView>(R.id.imageViewStar)
        imageViewStar.setImageResource(R.drawable.ic_star)

        val textViewCost = rowView.findViewById<TextView>(R.id.textViewInsuranceCost)
        textViewCost.text = "${convertStringToPrice(insurerParam.price)}₽"

        val imageViewIcon = rowView.findViewById<ImageView>(R.id.imageViewIconBank)
        val iconSVGurl = insurerParam.bankLogoUrlSVG
        if (iconSVGurl != null) {
            imageViewIcon.loadSvg(iconSVGurl)
        } else {
            imageViewIcon.setImageBitmap(generateIcon(context, insurerParam))
        }

        return rowView
    }

    private fun ImageView.loadSvg(url: String) {
        val imageLoader = ImageLoader.Builder(this.context)
            .componentRegistry { add(SvgDecoder(this@loadSvg.context)) }
            .build()

        val request = ImageRequest.Builder(this.context)
            .crossfade(false)
            .data(url)
            .target(this)
            .build()

        imageLoader.enqueue(request)
    }

    private fun generateIcon(context: Context, insurerParam: InsurerParam): Bitmap {
        val width = 36
        val height = 36
        val radiusCorners = context.resources.getDimension(R.dimen.iconRoundedCornersRadius)

        val tempBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        val tempCanvas = Canvas(tempBitmap)

        val paint = Paint()
        paint.color = Color.WHITE
        tempCanvas.drawPaint(paint)

        paint.isAntiAlias = true
        paint.color = Color.parseColor("#" + insurerParam.backgroundColor)
        paint.style = Paint.Style.FILL
        val rect = RectF(0F,0F, width.toFloat(), height.toFloat())
        tempCanvas.drawRoundRect(
            rect,
            radiusCorners,
            radiusCorners,
            paint
        )

        paint.color = Color.parseColor("#" + insurerParam.fontColor)
        paint.textSize = 25.0f
        val textRect = Rect()
        val text = insurerParam.iconTitle
        paint.getTextBounds(text, 0, text.length, textRect)
        tempCanvas.drawText(
            text,
            width / 2f - (paint.measureText(text) / 2f),
            height / 2f + (textRect.height() / 2f),
            paint
        )
        return tempBitmap
    }

}