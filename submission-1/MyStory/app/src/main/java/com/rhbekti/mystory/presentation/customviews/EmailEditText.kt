package com.rhbekti.mystory.presentation.customviews

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.rhbekti.mystory.R

class EmailEditText : TextInputLayout {

    private lateinit var editText: TextInputEditText

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        editText = TextInputEditText(this.context)
        editText.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        addView(editText)

        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                error =
                    if (s.toString().isEmpty()) context?.getString(R.string.error_field_required)
                    else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(s.toString()).matches())
                        context?.getString(R.string.error_invalid_email) else null
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
    }
}
