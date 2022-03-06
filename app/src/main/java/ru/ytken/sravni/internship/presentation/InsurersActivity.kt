package ru.ytken.sravni.internship.presentation

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.ytken.sravni.internship.R
import ru.ytken.sravni.internship.databinding.ActivityInsurersBinding
import ru.ytken.sravni.internship.domain.insurersactivity.models.CoefficientParam
import ru.ytken.sravni.internship.domain.insurersactivity.models.InsurerParam
import ru.ytken.sravni.internship.domain.mainactivity.models.CoefficientParamMain
import ru.ytken.sravni.internship.presentation.Utils.convertStringToPrice
import java.io.*


class InsurersActivity : AppCompatActivity() {

    private val vm by viewModel<InsurersViewModel>()
    lateinit var binding: ActivityInsurersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInsurersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonInsurersBack.setOnClickListener {
            this.finish()
        }

        binding.buttonCountExactPrice.isEnabled = false

        val listCoeffs = intent.getSerializableExtra(getString(R.string.TAG_send_coeffs)) as ArrayList<CoefficientParamMain>
        listCoeffs.let {
            Utils.inflateCoefficientLayout(this, binding.linearCoefficientsInsurers, it, false)
        }

        vm.listInsurers.observe(this) {
            binding.listViewInsurers.removeAllViews()
            inflateInsusersLayout(this, binding.listViewInsurers, it)
            binding.buttonCountExactPrice.isEnabled = true
        }

        listCoeffs.let { listCoefficientsParamToListCoefficient(it) }?.let {
            if (NetworkHelper.isNetworkConnected(this@InsurersActivity))
                vm.save(it)
            else
                Toast.makeText(this, getString(R.string.noInternetConnection), Toast.LENGTH_LONG).show()
        }

    }
    
    private fun inflateInsusersLayout(
        context: Context,
        layoutInsurers: LinearLayout,
        insurersArray: List<InsurerParam>?
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
                textViewCost.text = "${convertStringToPrice(insurerParam.price)}₽"

                val iconSVGurl = insurerParam.bankLogoUrlSVG
                val imageViewIcon = rowView.findViewById<ImageView>(R.id.imageViewIconBank)
                if (iconSVGurl != null) {
                    imageViewIcon.loadSvg(iconSVGurl)
                } else {
                    imageViewIcon.setImageBitmap(generateIcon(insurerParam))
                }

                layoutInsurers.addView(rowView)


                rowView.setOnClickListener {
                    val rowViewIcon = it.findViewById<ImageView>(R.id.imageViewIconBank)
                    val stream = openFileOutput(getString(R.string.fileIconBankName), Context.MODE_PRIVATE)
                    val bmp = rowViewIcon.drawable.toBitmap(36,36)
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, stream)

                    stream.close()
                    bmp.recycle()

                    val result = Intent()
                    result.putExtra(
                        getString(R.string.TAG_insurer_result),
                        insurerParam as Serializable
                    )
                    setResult(Activity.RESULT_OK, result)
                    finish()
                }
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

    private fun generateIcon(insurerParam: InsurerParam): Bitmap {
        val width = 36
        val height = 36
        val radiusCorners = resources.getDimension(R.dimen.iconRoundedCornersRadius)

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

    private fun listCoefficientsParamToListCoefficient(
        listCoefficientsParam: ArrayList<CoefficientParamMain>
    ): List<CoefficientParam> {
        val resultList = List<CoefficientParam>(listCoefficientsParam.size) { index ->
            val element = listCoefficientsParam[index]
            CoefficientParam(element.title, element.value)
        }
        return resultList
    }

}