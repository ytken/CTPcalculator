package ru.ytken.sravni.internship.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.ytken.sravni.internship.R
import ru.ytken.sravni.internship.databinding.ActivityMainBinding
import ru.ytken.sravni.internship.domain.mainactivity.models.ListParametersParam
import ru.ytken.sravni.internship.domain.mainactivity.models.ParameterParamMain
import java.io.Serializable

class MainActivity : AppCompatActivity(),
    ParameterBottomSheet.ChangeActivity,
    ParameterFragment.ChangeDialog{

    private val vm by viewModel<MainViewModel>()

    private var sentListParametersParam : ListParametersParam? = null
    private lateinit var binding: ActivityMainBinding
    private var bottomSheetFragment: ParameterBottomSheet? = null
    private var currentParameterFragment: Int? = null

    private val TAGFRAGMENT = "TAG_BOTTOM_SHEET_FRAGMENT"
    private val LOGTAG = "LOGTAGMainActivity"

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonCount.isEnabled = false
        binding.buttonCount.setBackgroundResource(R.drawable.main_button_selector)

        binding.progressBarLoadCoefficients.visibility = View.INVISIBLE

        binding.buttonCount.setOnClickListener {
            val insurersIntent = Intent(this, InsurersActivity::class.java)
            insurersIntent.putExtra(getString(R.string.TAG_send_coeffs), vm.listCoefficient.value!!.list as Serializable)
            Log.d(getString(R.string.TAG_API), vm.listCoefficient.value!!.list.joinToString("\n"))
            startActivity(insurersIntent)
        }

        vm.listCoefficient.observe(this) {
            binding.progressBarLoadCoefficients.visibility = View.INVISIBLE
            binding.buttonCount.setText(R.string.buttonCalculateCTP)

            binding.coefficientListView.setAdapter(ExpandableListAdapter(applicationContext, it.list))
            Log.d(getString(R.string.TAG_API), "Updating ListCoefficient")

            if (sentListParametersParam?.list
                    ?.none { parameterParam -> parameterParam.value.isEmpty() } == true
            )
                binding.buttonCount.isEnabled = true
        }

        vm.listParameters.observe(this) {
            binding.listViewParameters.removeAllViews()
            inflateParameterLayout(this, binding.listViewParameters, it.list)

            sentListParametersParam = it
        }

        vm.currentFragmentNumber.observe(this) {
            currentParameterFragment = it
        }
    }

    private fun inflateParameterLayout(context: Context, layoutParameters: LinearLayout, parameterArray: ArrayList<ParameterParamMain>) {
        val inflater = LayoutInflater.from(context)
        parameterArray.forEachIndexed { index, parameterShow ->
            var rowView: View

            if(parameterShow.value.isEmpty()) {
                rowView = inflater.inflate(R.layout.param_empty_list_text_view, layoutParameters, false)
                val textViewRowView = rowView.findViewById<TextView>(R.id.textViewListView)
                textViewRowView.text = parameterShow.hint
            }
            else {
                if ((parameterShow.title == "userNumber") && (parameterShow.value == "!")) {
                    rowView = inflater.inflate(R.layout.param_info_list_text_view, layoutParameters, false)

                    val textViewListViewDescription = rowView.findViewById<TextView>(R.id.textViewListViewDescription)
                    textViewListViewDescription.text = parameterShow.hint

                    val textViewZnach = rowView.findViewById<TextView>(R.id.textViewZnach)
                    textViewZnach.text = getString(R.string.userNumberNoLimit)
                } else {
                    rowView = inflater.inflate(R.layout.param_info_list_text_view, layoutParameters, false)

                    val textViewListViewDescription = rowView.findViewById<TextView>(R.id.textViewListViewDescription)
                    textViewListViewDescription.text = parameterShow.hint

                    val textViewZnach = rowView.findViewById<TextView>(R.id.textViewZnach)
                    textViewZnach.text = "${parameterShow.value} ${parameterShow.dimension}"
                }

            }

            rowView.setOnClickListener {
                vm.setCurrentFragmentNumber(index)

                val bundle = Bundle()
                bundle.putInt(getString(R.string.TAG_number_view), index)
                bundle.putSerializable(getString(R.string.TAG_parameter_show),
                    if (sentListParametersParam != null)
                        sentListParametersParam!!.list[index] else null
                )
                bundle.putBoolean(getString(R.string.TAG_last_view),
                    index + 1 == sentListParametersParam?.list?.size ?: 0
                )
                bundle.putInt(getString(R.string.TAG_index_pass),
                    passDialogIndex())

                bottomSheetFragment = ParameterBottomSheet()
                bottomSheetFragment!!.arguments = bundle

                bottomSheetFragment!!.show(supportFragmentManager, TAGFRAGMENT)
            }

            layoutParameters.addView(rowView)
        }
    }

    private fun passDialogIndex() : Int {
        val listParameters = vm.listParameters.value?.list!!
        if (listParameters.filter {
                    parameterParam -> parameterParam.title ==
                    getString(R.string.userNumberName) }[0].value ==
            getString(R.string.userNumberDefault))
                        return listParameters.indexOf(listParameters.filter {
                                parameterParam -> parameterParam.title ==
                                getString(R.string.userYoungestName) }[0])
        return -1
    }

    override fun callApi() {
        if (NetworkHelper.isNetworkConnected(this@MainActivity)) {
            Log.d(LOGTAG, "Call Api")
            vm.save()
            binding.buttonCount.isEnabled = false
            binding.buttonCount.text = ""

            binding.progressBarLoadCoefficients.visibility = View.VISIBLE
        } else
            Toast.makeText(this, getString(R.string.noInternetConnection), Toast.LENGTH_LONG).show()
    }

    override fun setNextDialog(numberView: Int) {
        val index =
            if (passDialogIndex() == numberView + 1)
                numberView + 2
            else
                numberView + 1

        val args = setBottomSheetFragmentArgs(index)
        bottomSheetFragment?.addParameterFragment(args)
        bottomSheetFragment?.arguments = args
    }

    override fun setPreviousDialog(numberView: Int) {
        val index =
            if (passDialogIndex() == numberView - 1)
                numberView - 2
            else
                numberView - 1

        val args = setBottomSheetFragmentArgs(index)
        bottomSheetFragment?.addParameterFragment(args)
        bottomSheetFragment?.arguments = args
    }

    private fun setBottomSheetFragmentArgs(index: Int) : Bundle {
        val args = Bundle()
        args.putInt(getString(R.string.TAG_number_view), index)
        args.putSerializable(getString(R.string.TAG_parameter_show),
            sentListParametersParam?.list?.get(index)
        )
        args.putBoolean(getString(R.string.TAG_last_view),
            index + 1 == sentListParametersParam?.list?.size ?: 0)
        args.putInt(getString(R.string.TAG_index_pass), passDialogIndex())

        return args
    }

    override fun saveParameterMainActivity(numberView: Int, value: String) {
        val valueToSave =
            if ((sentListParametersParam!!.list[numberView].title ==
                getString(R.string.userNumberName)) && value.isEmpty())
                getString(R.string.userNumberDefault)
            else
                value
        vm.saveParameter(numberView, valueToSave)
    }

    override fun dismissBottomSheet() {
        bottomSheetFragment?.dismiss()
        callApi()
    }

}