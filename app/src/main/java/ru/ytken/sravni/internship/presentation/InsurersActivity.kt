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
            Utils.inflateCoefficientLayout(this, binding.linearCoefficientsInsurers, it, vm.listExpanded)
        }

        vm.listInsurers.observe(this) {
            binding.listViewInsurers.removeAllViews()
            inflateInsusersLayout(this, binding.listViewInsurers, it)
            binding.buttonCountExactPrice.isEnabled = true
        }

        listCoefficientsParamToListCoefficient(listCoeffs).let {
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
            insurersArray.forEach { insurerParam ->
                val rowView = Utils.createRowViewInsurer(
                    context, layoutInsurers,
                    insurerParam)
                layoutInsurers.addView(rowView)

                rowView.setOnClickListener {
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



    private fun listCoefficientsParamToListCoefficient(
        listCoefficientsParam: ArrayList<CoefficientParamMain>
    ): List<CoefficientParam> {
        return listCoefficientsParam.map {
            CoefficientParam(it.title, it.value)
        }
    }

}