package ru.ytken.sravni.internship.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.ytken.sravni.internship.R
import ru.ytken.sravni.internship.domain.mainactivity.models.ListParametersParam
import ru.ytken.sravni.internship.domain.mainactivity.models.ParameterParam

class MainActivity : AppCompatActivity(), ParameterBottomSheet.ChangeDialog {

    private val vm by viewModel<MainViewModel>()

    private var sentListParametersParam : ListParametersParam? = null

    private val TAG_INIT_FRAGMENT = "TAG_INIT_FRAGMENT"
    private val LOGTAG = "LOGTAGMainActivity"

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonCount = findViewById<Button>(R.id.buttonCount)
        buttonCount.isEnabled = true
        buttonCount.setBackgroundResource(R.drawable.main_button_selector)

        val progressBarLoadCoefficients = findViewById<ProgressBar>(R.id.progressBarLoadCoefficients)
        progressBarLoadCoefficients.visibility = View.INVISIBLE

        buttonCount.setOnClickListener {
            val insurersIntent = Intent(this, InsurersActivity::class.java)
            insurersIntent.putExtra(getString(R.string.TAG_send_coeffs), vm.listCoefficient.value)
            startActivity(insurersIntent)
        }

        vm.currentFragmentNumber.observe(this, Observer {
            val fragment = ParameterBottomSheet()
            fragment.show(supportFragmentManager, TAG_INIT_FRAGMENT)
        })

        val expandableListView = findViewById<ExpandableListView>(R.id.listViewCoefficients)
        var expandableListAdapter: ExpandableListAdapter = ExpandableListAdapter(applicationContext, vm.listCoefficient.value!!)
        expandableListView.setAdapter(expandableListAdapter)
        expandableListView.setOnGroupCollapseListener {
            MainScreenUtils.setExpandableListViewHeightBasedOnChildren(expandableListView)
        }
        expandableListView.setOnGroupExpandListener {
            MainScreenUtils.setExpandableListViewHeightBasedOnChildren(expandableListView)
        }
        vm.listCoefficient.observe(this) {
            progressBarLoadCoefficients.visibility = View.INVISIBLE
            buttonCount.setText(R.string.buttonCalculateCTP)

            expandableListView.collapseGroup(0)
            expandableListView.setAdapter(
                ExpandableListAdapter(
                    applicationContext,
                    it
                )
            )
            Log.d(getString(R.string.TAG_API), "Updating ListCoefficient")

            if (sentListParametersParam?.toArray()
                    ?.none { parameterParam -> parameterParam.value.isEmpty() } == true
            )
                buttonCount.isEnabled = true
        }

        val layoutParameters = findViewById<LinearLayout>(R.id.listViewParameters)

        vm.listParameters.observe(this) {
            layoutParameters.removeAllViews()
            inflateParameterLayout(this, layoutParameters, it.toArray())

            sentListParametersParam = it
        }
    }

    private fun inflateParameterLayout(context: Context, layoutParameters: LinearLayout, parameterArray: Array<ParameterParam>) {
        val inflater = LayoutInflater.from(context)
        parameterArray.forEachIndexed { index, parameterShow ->
            var rowView: View

            if(parameterShow.value.isEmpty()) {
                rowView = inflater.inflate(R.layout.param_empty_list_text_view, layoutParameters, false)
                val textViewRowView = rowView.findViewById<TextView>(R.id.textViewListView)
                textViewRowView.text = parameterShow.hint
            }
            else {
                rowView = inflater.inflate(R.layout.param_info_list_text_view, layoutParameters, false)

                val textViewListViewDescription = rowView.findViewById<TextView>(R.id.textViewListViewDescription)
                textViewListViewDescription.text = parameterShow.hint

                val textViewZnach = rowView.findViewById<TextView>(R.id.textViewZnach)
                textViewZnach.text = "${parameterShow.value} ${parameterShow.dimension}"
            }

            rowView.setOnClickListener {
                vm.setCurrentFragmentNumber(index)
            }

            layoutParameters.addView(rowView)
        }
    }

    override fun setNextDialog(numberView: Int) {
        val listParameters = vm.listParameters.value
        if (listParameters!!.getElementById(numberView + 1).title ==
            listParameters.userYoungest.title)
            if (listParameters.userNumber.value.isEmpty()){
                vm.setCurrentFragmentNumber(numberView + 2)
                return
            }
        vm.setCurrentFragmentNumber(numberView + 1)
    }

    override fun setPreviousDialog(numberView: Int) {
        val listParameters = vm.listParameters.value
        if (listParameters!!.getElementById(numberView - 1).title ==
            listParameters.userYoungest.title)
            if (listParameters.userNumber.value.isEmpty()) {
                vm.setCurrentFragmentNumber(numberView - 2)
                return
            }
        vm.setCurrentFragmentNumber(numberView - 1)
    }

    override fun onFragmentDismissed() {
        if (NetworkHelper.isNetworkConnected(this@MainActivity)) {
            Log.d(LOGTAG, "Call Api")
            vm.save()
            val buttonCount = findViewById<Button>(R.id.buttonCount)
            buttonCount.isEnabled = false
            buttonCount.text = ""

            val progressBarLoadCoefficients = findViewById<ProgressBar>(R.id.progressBarLoadCoefficients)
            progressBarLoadCoefficients.visibility = View.VISIBLE
        } else
            Toast.makeText(this, getString(R.string.noInternetConnection), Toast.LENGTH_LONG).show()
    }

}