package ru.ytken.sravni.internship.presentation

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.ytken.sravni.internship.R
import ru.ytken.sravni.internship.domain.mainactivity.models.ParameterParam
import java.lang.RuntimeException


class ParameterBottomSheet : BottomSheetDialogFragment() {

    private lateinit var behavior: BottomSheetBehavior<View>
    private lateinit var editTextCoeff: EditText

    private val vm by activityViewModels<MainViewModel>()

    var mChangeDialog: ChangeDialog? = null

    interface ChangeDialog {
        fun setNextDialog(numberView: Int)
        fun setPreviousDialog(numberView: Int)
        fun onFragmentDismissed()
    }

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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ChangeDialog)
            mChangeDialog = context as ChangeDialog
        else
            throw RuntimeException("$context must implement ChangeDialog")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val numberView: Int =
            if (vm.currentFragmentNumber.value != null)
                vm.currentFragmentNumber.value!!
            else 0

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
                mChangeDialog?.onFragmentDismissed()
                dismiss()
            }
        }
        else {
            nextButton.setOnClickListener {
                saveParameter(editTextCoeff.text.toString())
                mChangeDialog?.setNextDialog(numberView)
                dismiss()
            }
        }

        val backButton = view.findViewById<ImageButton>(R.id.imageButtonBottomBack)

        if (numberView > 0) {
            backButton.visibility = View.VISIBLE
            backButton.setOnClickListener {
                saveParameter(editTextCoeff.text.toString())
                mChangeDialog?.setPreviousDialog(numberView)
                dismiss()
            }
        }
        else
            backButton.visibility = View.INVISIBLE
    }

    override fun onCancel(dialog: DialogInterface) {
        saveParameter(editTextCoeff.text.toString())
        mChangeDialog?.onFragmentDismissed()
        super.onCancel(dialog)
    }

    private fun saveParameter(value: String) {
        val numberView: Int = if (vm.currentFragmentNumber.value != null)
            vm.currentFragmentNumber.value!!
        else 0
        val listParameters = vm.listParameters.value
        if (listParameters != null) {
            listParameters.setValueForElement(numberView, value)
            vm.saveParameter(listParameters)
        }
    }

    override fun onDetach() {
        super.onDetach()
        mChangeDialog = null
    }

}