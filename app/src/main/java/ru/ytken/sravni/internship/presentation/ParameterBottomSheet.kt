package ru.ytken.sravni.internship.presentation

import android.app.Dialog
import android.content.DialogInterface
import android.content.res.TypedArray
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import okhttp3.internal.wait
import ru.ytken.sravni.internship.R
import ru.ytken.sravni.internship.domain.models.ParameterParam

class ParameterBottomSheet(val vm: MainViewModel, val numberView: Int): BottomSheetDialogFragment() {

    private lateinit var behavior: BottomSheetBehavior<View>
    private lateinit var editTextCoeff: EditText

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog

        val view = View.inflate(requireContext(), R.layout.bottom_sheet_edit, null)
        dialog.setContentView(view)

        behavior = BottomSheetBehavior.from(view.parent as View)
        behavior.skipCollapsed = true
        behavior.state = BottomSheetBehavior.STATE_EXPANDED

        return dialog
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
        val parameterShow = vm.listParameters.value?.getElementById(numberView) ?:
                            ParameterParam("title", "value", "hint", "type", "dimension")

        val textViewHeading = view.findViewById<TextView>(R.id.textViewBottomHeading)
        textViewHeading.text = parameterShow.hint

        editTextCoeff = view.findViewById<EditText>(R.id.editTextCoefficient)
        if (parameterShow.type.equals("number"))
            editTextCoeff.inputType = InputType.TYPE_CLASS_NUMBER
        else
            editTextCoeff.inputType = InputType.TYPE_CLASS_TEXT

        editTextCoeff.setOnEditorActionListener { _, i, _ ->
            if (i == EditorInfo.IME_ACTION_DONE) {
                saveParameter(editTextCoeff.text.toString())
            }
            false
        }
        editTextCoeff.imeOptions = EditorInfo.IME_ACTION_DONE
        val keepedValue = parameterShow.value
        if (keepedValue != null && keepedValue.isNotEmpty())
            editTextCoeff.setText(keepedValue)
        editTextCoeff.requestFocus()

        val buttonClearEditText = view.findViewById<ImageView>(R.id.imageViewClearEditText)
        buttonClearEditText.setOnClickListener { editTextCoeff.setText("") }

        val nextButton = view.findViewById<Button>(R.id.buttonNextCoeff)

        if (numberView == (vm.listParameters.value?.getSize() ?: 0) - 1) {
            nextButton.text = getString(R.string.buttonNextFinish)
            nextButton.setOnClickListener {
                saveParameter(editTextCoeff.text.toString())
                vm.fragmentDismissed()
                dismiss()
            }
        }
        else {
            nextButton.setOnClickListener {
                saveParameter(editTextCoeff.text.toString())
                vm.setCurrentFragmentNumber(numberView + 1)
                dismiss()
            }
        }

        val backButton = view.findViewById<ImageButton>(R.id.imageButtonBottomBack)

        if (numberView > 0) {
            backButton.visibility = View.VISIBLE
            backButton.setOnClickListener {
                saveParameter(editTextCoeff.text.toString())
                vm.setCurrentFragmentNumber(numberView - 1)
                dismiss()
            }
        }
        else
            backButton.visibility = View.INVISIBLE
    }

    override fun onCancel(dialog: DialogInterface) {
        saveParameter(editTextCoeff.text.toString())
        vm.fragmentDismissed()
        super.onCancel(dialog)
    }

    private fun saveParameter(value: String) {
        var listParameters = vm.listParameters.value
        if (listParameters != null) {
            listParameters.setValueForElement(numberView, value)
            vm.saveParameter(listParameters)
        }
    }

}