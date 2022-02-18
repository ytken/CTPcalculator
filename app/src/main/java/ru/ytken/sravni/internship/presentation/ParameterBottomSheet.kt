package ru.ytken.sravni.internship.presentation

import android.app.Dialog
import android.content.res.TypedArray
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.ytken.sravni.internship.R

class ParameterBottomSheet(val vm: MainViewModel): BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_edit, container, false)
    }

    private lateinit var behavior: BottomSheetBehavior<View>

    override fun onStart() {
        super.onStart()
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog

        val view = View.inflate(requireContext(), R.layout.bottom_sheet_edit, null)
        dialog.setContentView(view)

        behavior = BottomSheetBehavior.from(view.parent as View)
        behavior.skipCollapsed = true

        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val numberView = arguments?.getInt(resources.getString(R.string.TAG_number_view))
        val editTextHint: TypedArray = resources.obtainTypedArray(R.array.listview_hints)


        val textViewHeading = view.findViewById<TextView>(R.id.textViewBottomHeading)
        textViewHeading.text = numberView?.let { editTextHint.getString(it) }

        val editTextCoeff = view.findViewById<EditText>(R.id.editTextCoefficient)
        val expInputTypeNumber = resources.getStringArray(R.array.listview_input_type)
        if (numberView != null)
        if (expInputTypeNumber[numberView].equals("number"))
            editTextCoeff.inputType = InputType.TYPE_CLASS_NUMBER
        else
            editTextCoeff.inputType = InputType.TYPE_CLASS_TEXT
        editTextCoeff.setOnEditorActionListener { _, i, _ ->
            if (i == EditorInfo.IME_ACTION_DONE) {
                saveAndNext(numberView ?: 0, editTextCoeff.text.toString())
            }
            false
        }
        editTextCoeff.imeOptions = EditorInfo.IME_ACTION_DONE
        val keepedValue = numberView?.let { vm.listParameters.value?.get(it) }
        if (keepedValue != null && keepedValue.isNotEmpty())
            editTextCoeff.setText(keepedValue)
        editTextCoeff.requestFocus()

        val buttonClearEditText = view.findViewById<ImageView>(R.id.imageViewClearEditText)
        buttonClearEditText.setOnClickListener { editTextCoeff.setText("") }

        val nextButton = view.findViewById<Button>(R.id.buttonNextCoeff)
        nextButton.setOnClickListener { saveAndNext(numberView ?: 0, editTextCoeff.text.toString()) }
    }

    private fun saveAndNext(numberLine: Int, value: String) {
        Log.d(resources.getString(R.string.TAG_LIVE_DATA), "Saving in bottom sheet")
        var arrayParameters = vm.listParameters.value
        if(arrayParameters.isNullOrEmpty())
            arrayParameters = Array(resources.getStringArray(R.array.listview_hints).size) {""}
        arrayParameters[numberLine] = value
        Log.d(resources.getString(R.string.TAG_LIVE_DATA), "Set to element $numberLine value ${arrayParameters[numberLine]}")
        vm.save(arrayParameters)
        dismiss()
    }

    /*override fun getTheme(): Int {
        return R.style.Theme_App
    */

}