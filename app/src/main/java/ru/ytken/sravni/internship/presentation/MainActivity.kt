package ru.ytken.sravni.internship.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.Observer
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.ytken.sravni.internship.R

class MainActivity : AppCompatActivity(), ParameterBottomSheet.ChangeDialog {

    private val vm by viewModel<MainViewModel>()
    private val TAG_INIT_FRAGMENT = "TAG_INIT_FRAGMENT"
    private val LOGTAG = "LOGTAGMainActivity"

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonCount = findViewById<Button>(R.id.buttonCount)
        buttonCount.isEnabled = false

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
        vm.listCoefficient.observe(this, Observer {
            expandableListView.collapseGroup(0)
            expandableListView.setAdapter(ExpandableListAdapter(applicationContext, vm.listCoefficient.value!!))
            Log.d(getString(R.string.TAG_API), "Updating ListCoefficient")
        })

        val listViewParameters = findViewById<ListView>(R.id.listViewParameters)
        val listViewAdapter = ParameterListAdapter(this, vm)
        listViewParameters.adapter = listViewAdapter
        vm.listParameters.observe(this, Observer {
            listViewAdapter.notifyDataSetChanged()
        })
        listViewParameters.setOnItemClickListener { _, _, i, _ ->
            vm.setCurrentFragmentNumber(i)
        }
        MainScreenUtils.setListViewHeightBasedOnChildren(listViewParameters)

        vm.responseFromApi.observe(this, Observer {
            progressBarLoadCoefficients.visibility = View.INVISIBLE
            buttonCount.setText(R.string.buttonCalculateCTP)
            if(vm.listParameters.value!!.toArray().none { parameterParam -> parameterParam.value.isEmpty() })
                buttonCount.isEnabled = true
        })
    }

    class ParameterListAdapter(context: Context, vm: MainViewModel) : ArrayAdapter<String>(context, R.layout.param_empty_list_text_view) {

        private val listParameters = vm.listParameters.value
        private val mContext = context

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val rowView: View

            if (listParameters != null) {
                val parameterShow = listParameters.getElementById(position)

                if(parameterShow.value.isEmpty()) {
                    rowView = LayoutInflater.from(mContext).inflate(R.layout.param_empty_list_text_view, parent, false)
                    val textViewRowView = rowView.findViewById<TextView>(R.id.textViewListView)
                    textViewRowView.text = parameterShow.hint
                }
                else {
                    rowView = LayoutInflater.from(mContext).inflate(R.layout.param_info_list_text_view, parent, false)

                    val textViewListViewDescription = rowView.findViewById<TextView>(R.id.textViewListViewDescription)
                    textViewListViewDescription.text = parameterShow.hint

                    val textViewZnach = rowView.findViewById<TextView>(R.id.textViewZnach)
                    textViewZnach.text = "${parameterShow.value} ${parameterShow.dimension}"
                }
                return rowView
            }
            return LayoutInflater.from(mContext).inflate(R.layout.param_empty_list_text_view, parent, false)
        }

        override fun getCount(): Int {
            return listParameters?.getSize() ?: 0
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
        MainScreenUtils.callApi(vm, LOGTAG)

        val buttonCount = findViewById<Button>(R.id.buttonCount)
        buttonCount.isEnabled = false
        buttonCount.text = ""

        val progressBarLoadCoefficients = findViewById<ProgressBar>(R.id.progressBarLoadCoefficients)
        progressBarLoadCoefficients.visibility = View.VISIBLE
    }
}