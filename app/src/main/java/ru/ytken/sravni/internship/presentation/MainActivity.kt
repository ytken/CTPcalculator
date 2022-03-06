package ru.ytken.sravni.internship.presentation

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.ytken.sravni.internship.R
import ru.ytken.sravni.internship.databinding.ActivityMainBinding
import ru.ytken.sravni.internship.databinding.FragmentInsurerBinding
import ru.ytken.sravni.internship.domain.insurersactivity.models.InsurerParam
import ru.ytken.sravni.internship.domain.mainactivity.models.ListCoefficientsParam
import ru.ytken.sravni.internship.domain.mainactivity.models.ParameterParamMain
import java.io.Serializable

class MainActivity : AppCompatActivity(),
    ParameterBottomSheet.ChangeActivity,
    ParameterFragment.ChangeDialog {

    private val vm by viewModel<MainViewModel>()

    private var mListCoefficientsParam : ListCoefficientsParam? = null

    private lateinit var binding: ActivityMainBinding
    private var bottomSheetFragment: ParameterBottomSheet? = null
    private var currentParameterFragment: Int? = null

    private val TAG_FRAGMENT = "TAG_BOTTOM_SHEET_FRAGMENT"
    private val TAG_INSURER_FRAGMENT = "TAG_INSURER_FRAGMENT"
    private val LOGTAG = "LOGTAGMainActivity"
    private val TAG_RESULT = 1

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState != null) {
            bottomSheetFragment =
                supportFragmentManager.
                findFragmentByTag(TAG_FRAGMENT)
                        as ParameterBottomSheet?
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonCount.isEnabled = false
        binding.buttonCount.setBackgroundResource(R.drawable.main_button_selector)

        binding.progressBarLoadCoefficients.visibility = View.INVISIBLE

        binding.buttonCount.setOnClickListener {
            val intent = Intent(this,  InsurersActivity::class.java)
            intent.putExtra(resources.getString(R.string.TAG_send_coeffs), mListCoefficientsParam?.list)
            startActivityForResult(intent, TAG_RESULT)
        }

        vm.listCoefficient.observe(this) {
            binding.progressBarLoadCoefficients.visibility = View.INVISIBLE
            binding.buttonCount.setText(R.string.buttonCalculateCTP)

            binding.coefficientListView.setAdapter(ExpandableListAdapter(applicationContext, it.list))
            Log.d(getString(R.string.TAG_API), "Updating ListCoefficient")

            if (vm.listParameters.value?.list
                    ?.none { parameterParam -> parameterParam.value.isEmpty() } == true
            )
                binding.buttonCount.isEnabled = true

            mListCoefficientsParam = it
        }

        vm.listParameters.observe(this) {
            binding.listViewParameters.removeAllViews()
            inflateParameterLayout(this, binding.listViewParameters, it.list)
        }

        vm.currentFragmentNumber.observe(this) {
            currentParameterFragment = it
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            val resultInsurer = data.getSerializableExtra(
                resources.getString(R.string.TAG_insurer_result))
            val insurerFragment = InsurerBottomSheet()

            val bundle = Bundle()
            bundle.putSerializable(getString(R.string.TAG_insurer_result), resultInsurer)

            insurerFragment.arguments = bundle
            insurerFragment.show(supportFragmentManager, TAG_INSURER_FRAGMENT)
        }
        super.onActivityResult(requestCode, resultCode, data)
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
                    if (vm.listParameters.value != null)
                        vm.listParameters.value!!.list[index] else null
                )
                bundle.putBoolean(getString(R.string.TAG_last_view),
                    index + 1 == vm.listParameters.value?.list?.size ?: 0
                )
                bundle.putInt(getString(R.string.TAG_index_pass),
                    passDialogIndex())

                bottomSheetFragment = ParameterBottomSheet()
                bottomSheetFragment!!.arguments = bundle

                bottomSheetFragment!!.show(supportFragmentManager, TAG_FRAGMENT)
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
            vm.listParameters.value?.list?.get(index)
        )
        args.putBoolean(getString(R.string.TAG_last_view),
            index + 1 == vm.listParameters.value?.list?.size ?: 0)
        args.putInt(getString(R.string.TAG_index_pass), passDialogIndex())

        return args
    }

    override fun saveParameterMainActivity(numberView: Int, value: String) {
        val valueToSave =
            if ((vm.listParameters.value!!.list[numberView].title ==
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

    class InsurerBottomSheet : BottomSheetDialogFragment() {

        private var binding: FragmentInsurerBinding? = null
        private var insurerParam: InsurerParam? = null
        //private var insurerIcon: Bitmap? = null

        private var mContext: Context? = null

        override fun onAttach(context: Context) {
            super.onAttach(context)
            mContext = context
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            arguments?.let {
                insurerParam =
                    it.getSerializable(getString(R.string.TAG_insurer_result))
                            as InsurerParam
            }
        }

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            binding = FragmentInsurerBinding.inflate(LayoutInflater.from(context))
            val view = binding!!.root

            val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
            dialog.setContentView(view)
            dialog.setOnShowListener {
                val bottomSheetDialog = it as BottomSheetDialog
                val bottomSheet =
                    bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout?
                val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(bottomSheet!!)
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
            return dialog
        }

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            return inflater.inflate(R.layout.fragment_insurer, container, false)
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            val cardViewInsurer = view.findViewById<LinearLayout>(R.id.cardViewInsurer)
            val buttonInsurerDone = view.findViewById<Button>(R.id.buttonInsurerDone)

            val rowView = LayoutInflater.from(context)
                .inflate(R.layout.insurers_list_child_view, cardViewInsurer, false)

            val textViewHeading = rowView.findViewById<TextView>(R.id.textViewNameBank)
            textViewHeading.text = insurerParam?.name

            val textViewRating = rowView.findViewById<TextView>(R.id.textViewBankRating)
            textViewRating.text = insurerParam?.rating.toString()

            val imageViewStar = rowView.findViewById<ImageView>(R.id.imageViewStar)
            imageViewStar.setImageResource(R.drawable.ic_star)

            val textViewCost = rowView.findViewById<TextView>(R.id.textViewInsuranceCost)
            textViewCost.text = "${insurerParam?.price?.let { Utils.convertStringToPrice(it) }} ₽"

            var bmp: Bitmap?
            val fis = mContext?.openFileInput(getString(R.string.fileIconBankName))
            bmp = BitmapFactory.decodeStream(fis)
            fis?.close()

            if (bmp != null) {
                val imageViewIcon = rowView.findViewById<ImageView>(R.id.imageViewIconBank)
                imageViewIcon.setImageBitmap(bmp)
            }

            cardViewInsurer.addView(rowView)

            buttonInsurerDone.setOnClickListener { dismiss() }
        }

        override fun show(manager: FragmentManager, tag: String?) {
            try {
                val ft = manager.beginTransaction()
                ft.add(this, tag)
                ft.commitAllowingStateLoss()
            } catch (ignored: IllegalStateException) { }
        }
    }
}