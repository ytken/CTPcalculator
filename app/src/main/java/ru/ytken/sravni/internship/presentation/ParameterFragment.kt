package ru.ytken.sravni.internship.presentation

import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.text.InputType
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import ru.ytken.sravni.internship.R
import ru.ytken.sravni.internship.databinding.FragmentParameterBinding
import ru.ytken.sravni.internship.domain.mainactivity.models.ParameterParamMain
import java.lang.RuntimeException

class ParameterFragment : Fragment() {

    private var binding: FragmentParameterBinding? = null

    private var numberView: Int = 0
    private lateinit var parameterShow: ParameterParamMain
    private var isLastParameter: Boolean = false

    var mChangeDialog: ChangeDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (context is ChangeDialog)
            mChangeDialog = context as ChangeDialog
        else
            throw RuntimeException("$context must implement ChangeDialog")

        if(savedInstanceState != null) {
            numberView = savedInstanceState.getInt(getString(R.string.TAG_number_view))
            parameterShow = savedInstanceState.getSerializable(getString(R.string.TAG_parameter_show))
                    as ParameterParamMain
            isLastParameter = savedInstanceState.getBoolean(getString(R.string.TAG_last_view))
        }
        else
            arguments?.let {
                numberView = it.getInt(getString(R.string.TAG_number_view))
                parameterShow = it.getSerializable(getString(R.string.TAG_parameter_show))
                        as ParameterParamMain
                isLastParameter = it.getBoolean(getString(R.string.TAG_last_view))
            }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(getString(R.string.TAG_number_view), numberView)
        outState.putSerializable(getString(R.string.TAG_parameter_show), parameterShow)
        outState.putBoolean(getString(R.string.TAG_last_view), isLastParameter)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_parameter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentParameterBinding.bind(view)

        val headingFont = Typeface.createFromAsset(context?.assets, "font/SF-Pro-Display-Bold.otf")
        binding!!.textViewBottomHeading.text = parameterShow.hint
        binding!!.textViewBottomHeading.typeface = headingFont

        val editTextCoeff = binding!!.editTextCoefficient
        if (parameterShow.type == "number")
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
        if (keepedValue != null && keepedValue.isNotEmpty() && keepedValue != getString(R.string.userNumberDefault))
            editTextCoeff.setText(keepedValue)
        editTextCoeff.requestFocus()

        val buttonClearEditText = view.findViewById<ImageView>(R.id.imageViewClearEditText)
        buttonClearEditText.setOnClickListener { editTextCoeff.setText("") }

        val nextButton = view.findViewById<Button>(R.id.buttonNextCoeff)

        if (isLastParameter == true) {
            nextButton.text = getString(R.string.buttonNextFinish)
            nextButton.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null)
            nextButton.setOnClickListener {
                saveParameter(editTextCoeff.text.toString())
                mChangeDialog?.dismissBottomSheet()
            }
        }
        else {
            nextButton.setOnClickListener {
                saveParameter(editTextCoeff.text.toString())
                mChangeDialog?.setNextDialog(numberView)
            }
        }

        val backButton = view.findViewById<ImageButton>(R.id.imageButtonBottomBack)

        if (numberView > 0) {
            backButton.visibility = View.VISIBLE
            backButton.setOnClickListener {
                saveParameter(editTextCoeff.text.toString())
                mChangeDialog?.setPreviousDialog(numberView)
            }
        }
        else
            backButton.visibility = View.INVISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mChangeDialog?.saveParameterMainActivity(
            numberView,
            binding!!.editTextCoefficient.text.toString()
        )
        binding = null
    }

    private fun saveParameter(value: String) {
        val valueToSave =
            if ((value.isEmpty()) && (parameterShow.title == getString(R.string.userNumberName)))
                getString(R.string.userNumberDefault)
            else
                value
        mChangeDialog!!.saveParameterMainActivity(numberView, valueToSave)
    }

    interface ChangeDialog {
        fun setNextDialog(numberView: Int)
        fun setPreviousDialog(numberView: Int)
        fun saveParameterMainActivity(numberView: Int, value: String)
        fun dismissBottomSheet()
    }

}