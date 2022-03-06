package ru.ytken.sravni.internship.presentation

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.res.Configuration
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.ytken.sravni.internship.R
import ru.ytken.sravni.internship.databinding.BottomSheetEditBinding


class ParameterBottomSheet : BottomSheetDialogFragment() {

    private var binding: BottomSheetEditBinding? = null
    private var passFragmentIndex: Int? = null
    private var mChangeActivity: ChangeActivity? = null

    interface ChangeActivity {
        fun callApi()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is ChangeActivity)
            mChangeActivity = context
        else
            throw RuntimeException("$context must implement ChangeActivity")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            passFragmentIndex = it.getInt(getString(R.string.TAG_index_pass))
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = BottomSheetEditBinding.inflate(LayoutInflater.from(context))
        val view = binding!!.root

        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.setContentView(view)
        dialog.setOnShowListener {
            setupFullHeight(it as BottomSheetDialog)
        }

        return dialog
    }

    private fun setupFullHeight(bottomSheetDialog: BottomSheetDialog) {
        val bottomSheet =
            bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout?
        val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(bottomSheet!!)
        val layoutParams = bottomSheet.layoutParams

        val displayMetrics = DisplayMetrics()
        val activity = context as Activity?
        activity!!.windowManager.defaultDisplay.getMetrics(displayMetrics)

        if (layoutParams != null) {
            if (activity.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
                layoutParams.width = (displayMetrics.widthPixels * 0.5).toInt()
            else
                layoutParams.height = (displayMetrics.heightPixels * 0.5).toInt()
        }
        bottomSheet.layoutParams = layoutParams
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addParameterFragment(arguments)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)

        val bottomSheet = dialog?.findViewById(com.google.android.material.R.id.design_bottom_sheet) as ViewGroup
        val behavior = BottomSheetBehavior.from(bottomSheet)
        behavior.skipCollapsed = true
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    fun addParameterFragment(args: Bundle?) {
        val fragment = ParameterFragment()
        fragment.arguments = args
        Log.d("ParameterBottomSheet", "addParameterFragment")

        childFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerBottomSheet, fragment)
            .commit()
    }

    private fun getSystemHeight(): Int {
        val displayMetrics = DisplayMetrics()
        val activityContext = context as AppCompatActivity
        activityContext.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }

    override fun onCancel(dialog: DialogInterface) {
        mChangeActivity?.callApi()
        super.onCancel(dialog)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onDetach() {
        super.onDetach()
        mChangeActivity = null
    }

}