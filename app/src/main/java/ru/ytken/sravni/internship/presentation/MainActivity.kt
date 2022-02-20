package ru.ytken.sravni.internship.presentation

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.TypedArray
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ru.ytken.sravni.internship.R
import ru.ytken.sravni.internship.domain.models.CoefficientParam
import ru.ytken.sravni.internship.domain.models.ListCoefficientsParam
import ru.ytken.sravni.internship.domain.models.ParameterParam

class MainActivity : AppCompatActivity(), FragmentManager.OnBackStackChangedListener{

    private lateinit var vm: MainViewModel
    private val TAG_INIT_FRAGMENT = "TAG_INIT_FRAGMENT"

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.addOnBackStackChangedListener(this)

        vm = ViewModelProvider(this, MainViewModelFactory(this))
            .get(MainViewModel::class.java)

        val buttonCount = findViewById<Button>(R.id.buttonCount)
        buttonCount.isEnabled = false
        buttonCount.setOnClickListener {
            Toast.makeText(this, "Следующий экран: список страховых", Toast.LENGTH_SHORT).show()
        }

        vm.currentFragmentNumber.observe(this, Observer {
            if (supportFragmentManager.findFragmentByTag(TAG_INIT_FRAGMENT) != null){
                Log.d(TAG_BACKSTACK, "Replace fragment")
                val fragment = ParameterBottomSheet(vm, it)
                val transaction = fragment.show(supportFragmentManager, TAG_INIT_FRAGMENT)
                /*supportFragmentManager.beginTransaction()
                    .replace(R.id.bottom_sheet, ParameterBottomSheet(vm, it))
                    .addToBackStack(null)
                    .commit()*/
            }
            else {
                Log.d(TAG_BACKSTACK, "Create new fragment")
                val fragment = ParameterBottomSheet(vm, it)
                val transaction = fragment.show(supportFragmentManager, TAG_INIT_FRAGMENT)
            }
        })

        val expandableListView = findViewById<ExpandableListView>(R.id.listViewCoefficients)
        var expandableListAdapter: ExpandableListAdapter = ExpandableListAdapter(applicationContext, vm)
        expandableListView.setAdapter(expandableListAdapter)
        vm.listCoefficient.observe(this, Observer {
            expandableListAdapter.notifyDataSetChanged()
        })
        expandableListView.setOnGroupCollapseListener {
            MainScreenUtils.setListViewHeightBasedOnChildren(expandableListView)
        }
        expandableListView.setOnGroupExpandListener {
            MainScreenUtils.setListViewHeightBasedOnChildren(expandableListView)
        }

        val listViewParameters = findViewById<ListView>(R.id.listViewParameters)
        var listViewAdapter = ParameterListAdapter(this, vm)
        listViewParameters.adapter = listViewAdapter
        listViewParameters.layoutParams.height = getValueInPixel(454)
        vm.listParameters.observe(this, Observer {
            listViewAdapter.notifyDataSetChanged()
        })
        listViewParameters.setOnItemClickListener { _, _, i, _ ->
            vm.setCurrentFragmentNumber(i)
        }

    }

    fun getValueInPixel(dpValue: Int): Int {
        val scale = applicationContext.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    class ExpandableListAdapter(private val mContext: Context, vm: MainViewModel)
        : BaseExpandableListAdapter() {

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

                val KVStextView = myConvertView.findViewById<TextView>(R.id.textViewKVS)
                KVStextView.text = listCoeffs.KVS.headerValue

                val KOtextView = myConvertView.findViewById<TextView>(R.id.textViewKO)
                KOtextView.text = listCoeffs.KO.headerValue
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

    class ParameterListAdapter(val mContext: Activity,val vm: MainViewModel) : ArrayAdapter<String>(mContext, R.layout.param_empty_list_text_view) {

        var listParameters = vm.listParameters.value

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            listParameters = vm.listParameters.value
            //add DiffUtils in future
            var rowView: View

            if (listParameters != null) {
                val parameterShow = listParameters?.getElementById(position)

                if(parameterShow?.value == null || parameterShow.value.isEmpty()) {
                    rowView = LayoutInflater.from(mContext).inflate(R.layout.param_empty_list_text_view, parent, false)
                    val textViewRowView = rowView.findViewById<TextView>(R.id.textViewListView)
                    textViewRowView.text = parameterShow?.hint
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

    private val TAG_BACKSTACK = "TAG_BACKSTACK"

    fun logBackStackEntry(entry: FragmentManager.BackStackEntry) {
        Log.d(TAG_BACKSTACK, "BackstackEntry Name: " + entry.let { "name" })
    }

    override fun onBackStackChanged() {
        Log.d(TAG_BACKSTACK, "Backstack changed")
        Log.d(TAG_BACKSTACK, "Backstack content::")
        for (i in 0 until supportFragmentManager.backStackEntryCount)
            logBackStackEntry(supportFragmentManager.getBackStackEntryAt(i))
    }

}