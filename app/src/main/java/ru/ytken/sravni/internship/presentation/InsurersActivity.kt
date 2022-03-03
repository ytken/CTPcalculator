package ru.ytken.sravni.internship.presentation

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.Observer
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.w3c.dom.Text
import ru.ytken.sravni.internship.R
import ru.ytken.sravni.internship.domain.insurersactivity.models.CoefficientParam
import ru.ytken.sravni.internship.domain.insurersactivity.models.InsurerParam
import ru.ytken.sravni.internship.domain.mainactivity.models.ListCoefficientsParam

class InsurersActivity : AppCompatActivity() {

    private val vm by viewModel<InsurersViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insurers)

        val buttonBack = findViewById<LinearLayout>(R.id.buttonInsurersBack)
        buttonBack.setOnClickListener {
            this.finish()
        }

        val buttonCount = findViewById<Button>(R.id.buttonCountExactPrice)
        buttonCount.isEnabled = false

        val expandableListView = findViewById<ExpandableListView>(R.id.listViewCoefficientsInsurers)
        val listCoeffs = intent.getParcelableExtra<ListCoefficientsParam>(getString(R.string.TAG_send_coeffs))
        listCoeffs?.let {
            val expandableListAdapter: ExpandableListAdapter =
                ExpandableListAdapter(applicationContext, it)
            expandableListView.setAdapter(expandableListAdapter)
        }

        expandableListView.setOnGroupCollapseListener {
            MainScreenUtils.setExpandableListViewHeightBasedOnChildren(expandableListView)
        }
        expandableListView.setOnGroupExpandListener {
            MainScreenUtils.setExpandableListViewHeightBasedOnChildren(expandableListView)
        }

        val layoutInsurers = findViewById<LinearLayout>(R.id.listViewInsurers)

        vm.listInsurers.observe(this) {
            layoutInsurers.removeAllViews()
            inflateInsusersLayout(this, layoutInsurers, it.toTypedArray())
        }

        listCoeffs?.let { listCoefficientsParamToListCoefficient(it) }?.let {
            if (NetworkHelper.isNetworkConnected(this@InsurersActivity))
                vm.save(it)
            else
                Toast.makeText(this, getString(R.string.noInternetConnection), Toast.LENGTH_LONG).show()
        }

        /*
        val listViewInsurers = findViewById<ListView>(R.id.listViewInsurers)
        val listViewAdapter = InsurersListAdapter(this, vm.listInsurers.value)
        listViewInsurers.adapter = listViewAdapter
        MainScreenUtils.setListViewHeightBasedOnChildren(listViewInsurers)

        vm.listInsurers.observe(this, Observer {
            listViewInsurers.adapter = InsurersListAdapter(applicationContext, vm.listInsurers.value)
            buttonCount.isEnabled = true
        })

        */
    }
    
    private fun inflateInsusersLayout(
        context: Context,
        layoutInsurers: LinearLayout,
        insurersArray: Array<InsurerParam>?
    ) {
        var rowView: View

        if (insurersArray == null)
            for (i in 0..3) {
                rowView = LayoutInflater.from(context)
                    .inflate(R.layout.insurers_blank_list_child_view, layoutInsurers, false)
                layoutInsurers.addView(rowView)
            }
        else
            insurersArray.forEachIndexed { index, insurerParam ->
                rowView = LayoutInflater.from(context)
                    .inflate(R.layout.insurers_list_child_view, layoutInsurers, false)

                val textViewHeading = rowView.findViewById<TextView>(R.id.textViewNameBank)
                textViewHeading.text = insurerParam.name

                val textViewRating = rowView.findViewById<TextView>(R.id.textViewBankRating)
                textViewRating.text = insurerParam.rating.toString()

                val imageViewStar = rowView.findViewById<ImageView>(R.id.imageViewStar)
                imageViewStar.setImageResource(R.drawable.ic_star)

                val textViewCost = rowView.findViewById<TextView>(R.id.textViewInsuranceCost)
                textViewCost.text = "${insurerParam.price.toInt()} ₽"

                val iconSVGurl = insurerParam.bankLogoUrlSVG
                val imageViewIcon = rowView.findViewById<ImageView>(R.id.imageViewIconBank)
                if (iconSVGurl != null) {
                    imageViewIcon.loadSvg(iconSVGurl)
                } else {
                    val width = 36
                    val height = 36

                    val tempBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
                    val tempCanvas = Canvas(tempBitmap)

                    val paint = Paint()
                    paint.color = Color.WHITE
                    tempCanvas.drawPaint(paint)

                    paint.isAntiAlias = true
                    paint.color = Color.parseColor("#" + insurerParam.backgroundColor)
                    paint.style = Paint.Style.FILL
                    tempCanvas.drawCircle(
                        width / 2f,
                        height / 2f,
                        width / 2f,
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

                    imageViewIcon.setImageBitmap(tempBitmap)
                }

                layoutInsurers.addView(rowView)
            }
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

    private fun listCoefficientsParamToListCoefficient(
        listCoefficientsParam: ListCoefficientsParam
    ): List<CoefficientParam> {
        val resultList = List<CoefficientParam>(listCoefficientsParam.getSize()) { index ->
            val element = listCoefficientsParam.getElementById(index)
            CoefficientParam(element.title, element.value)
        }
        return resultList
    }

}