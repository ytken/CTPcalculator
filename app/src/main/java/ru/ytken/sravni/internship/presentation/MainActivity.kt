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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ru.ytken.sravni.internship.R

class MainActivity : AppCompatActivity(){

    private lateinit var vm: MainViewModel

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val expListHeadings: TypedArray = resources.obtainTypedArray(R.array.expandable_list_headings)

        vm = ViewModelProvider(this, MainViewModelFactory(this))
            .get(MainViewModel::class.java)

        val buttonCount = findViewById<Button>(R.id.buttonCount)
        buttonCount.isEnabled = false
        buttonCount.setOnClickListener {
            Toast.makeText(this, "Следующий экран: список страховых", Toast.LENGTH_SHORT).show()
        }

        val expandableListView = findViewById<ExpandableListView>(R.id.listViewCoefficients)
        var expandableListAdapter: ExpandableListAdapter = ExpandableListAdapter(applicationContext, vm)
        expandableListView.setAdapter(expandableListAdapter)
        vm.listCoefficient.observe(this, Observer {
            expandableListAdapter.notifyDataSetChanged()
        })
        expandableListView.setOnGroupCollapseListener {
            val layoutParams = expandableListView.layoutParams
            layoutParams.height = getValueInPixel(68)
            expandableListView.layoutParams = layoutParams
        }
        expandableListView.setOnGroupExpandListener {
            val layoutParams = expandableListView.layoutParams
            layoutParams.height = getValueInPixel(513)
            expandableListView.layoutParams = layoutParams
        }

        val listViewParameters = findViewById<ListView>(R.id.listViewParameters)
        var listViewAdapter = ParameterListAdapter(this, vm)
        listViewParameters.adapter = listViewAdapter
        listViewParameters.layoutParams.height = getValueInPixel(454)
        vm.listParameters.observe(this, Observer {
            listViewAdapter.notifyDataSetChanged()
            Log.d(resources.getString(R.string.TAG_LIVE_DATA), "Notify data set changed")
            buttonCount.isEnabled = it.none { x -> x.isEmpty()}
        })
        listViewParameters.setOnItemClickListener { adapterView, view, i, l ->
            val bundle = Bundle()
            bundle.putInt(resources.getString(R.string.TAG_number_view), i)

            val fragment = ParameterBottomSheet(vm)
            fragment.arguments = bundle
            fragment.show(supportFragmentManager, null)
        }

    }

    private fun getValueInPixel(dpValue: Int): Int {
        val scale = applicationContext.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    class ExpandableListAdapter(private val mContext: Context, vm: MainViewModel)
        : BaseExpandableListAdapter() {

        private val arrayCoeffs = vm.listCoefficient.value

        private val expListHeadings: TypedArray = mContext.resources.obtainTypedArray(R.array.expandable_list_headings)
        private val expListSubheadings: TypedArray = mContext.resources.obtainTypedArray(R.array.expandable_list_subheadings)
        private val expListDescription: TypedArray = mContext.resources.obtainTypedArray(R.array.expandable_list_description)
        private val expListCoefficients: TypedArray = mContext.resources.obtainTypedArray(R.array.listview_coefficients)

        override fun getGroupCount(): Int { return 1 }

        override fun getChildrenCount(p0: Int): Int { return expListHeadings.length() }

        override fun getGroup(p0: Int): Any {
            return expListHeadings
        }

        override fun getChild(p0: Int, p1: Int): String? {
            return expListHeadings.getString(0)
        }

        override fun getGroupId(p0: Int): Long {
            return 0L
        }

        override fun getChildId(p0: Int, p1: Int): Long {
            return p1.toLong()
        }

        override fun hasStableIds(): Boolean {
            return true
        }

        override fun getGroupView(p0: Int, p1: Boolean, p2: View?, p3: ViewGroup?): View {
            var convertView: View
            if (p2 == null) {
                val inflater: LayoutInflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                convertView = inflater.inflate(R.layout.exp_list_group_view, null)
            }
            else
                convertView = p2
            val imageViewIcon = convertView.findViewById<ImageView>(R.id.imageViewIconCalculator)
            imageViewIcon.setImageDrawable(mContext.resources.getDrawable(R.drawable.ic_avatars,null))

            val imageArrow = convertView.findViewById<ImageView>(R.id.imageArrow)
            if (p1)
                imageArrow.setImageResource(R.drawable.ic_arrow_up)
            else
                imageArrow.setImageResource(R.drawable.ic_arrow_down)

            return convertView
        }

        override fun getChildView(p0: Int, p1: Int, p2: Boolean, p3: View?, p4: ViewGroup?): View {
            var convertView: View

            if (p3 == null) {
                val inflater: LayoutInflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                convertView = inflater.inflate(R.layout.exp_list_child_view, null)


                val textViewHeading = convertView.findViewById<TextView>(R.id.textViewHeading)
                val textViewSubheading = convertView.findViewById<TextView>(R.id.textViewSubheading)
                val textViewDescription = convertView.findViewById<TextView>(R.id.textViewDescription)
                val textViewCoefficient = convertView.findViewById<TextView>(R.id.textViewCoefficient)

                textViewHeading.text = expListHeadings.getString(p1)
                textViewSubheading.text = "(${expListSubheadings.getString(p1)})"
                textViewDescription.text = expListDescription.getString(p1)
                if (arrayCoeffs.isNullOrEmpty())
                    textViewCoefficient.text = expListCoefficients.getString(p1)
                else
                    textViewCoefficient.text = arrayCoeffs[p1]
            }
            else
                convertView = p3

            return convertView
        }

        override fun isChildSelectable(p0: Int, p1: Int): Boolean {
            return false
        }

    }

    class ParameterListAdapter(val mContext: Activity,val vm: MainViewModel) : ArrayAdapter<String>(mContext, R.layout.param_empty_list_text_view) {

        private val editTextHint: TypedArray = mContext.resources.obtainTypedArray(R.array.listview_hints)

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val arrayParameters = vm.listParameters.value
            val expAddTextParameters = mContext.resources.getStringArray(R.array.listview_add_text_parameters)
            var rowView: View
            if (!arrayParameters.isNullOrEmpty() && arrayParameters[position].isNotEmpty()) {
                rowView = LayoutInflater.from(mContext).inflate(R.layout.param_info_list_text_view, parent, false)
                val textViewListViewDescription = rowView.findViewById<TextView>(R.id.textViewListViewDescription)
                val textViewZnach = rowView.findViewById<TextView>(R.id.textViewZnach)
                textViewListViewDescription.text = editTextHint.getString(position)
                textViewZnach.text = "${arrayParameters[position]} ${expAddTextParameters[position]}"
            }
            else {
                rowView = LayoutInflater.from(mContext).inflate(R.layout.param_empty_list_text_view, parent, false)
                val textViewRowView = rowView.findViewById<TextView>(R.id.textViewListView)
                textViewRowView.text = editTextHint.getString(position)
            }

            return rowView
        }

        override fun getCount(): Int {
            return editTextHint.length()
        }

    }

}