package com.hast.norvialle.gui.utils

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.os.Build
import android.text.Editable
import android.text.InputType
import android.text.TextUtils
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.ViewCompat
import com.hast.norvialle.R
import com.hast.norvialle.utils.data_validation.Validatable
import kotlinx.android.synthetic.main.input_view.view.*

/**
 * Created by Konstantyn Zakharchenko on 22.12.2019.
 */
class InputView(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs, 0),
    Validatable {

    override var onStateChanged: (() -> Unit)={}
    var view = LayoutInflater.from(context).inflate(R.layout.input_view, this, false)
    var expectedType = EXPECTED_INPUT_TYPE.TEXT

    init {

        addView(view)
        handleAttributes(context, attrs)
        prepareEditTextStyle()

    }

    fun prepareEditTextStyle(){
        view.editText.setTextColor(getColor(R.color.gold))
        view.editText.setHintTextColor(getColor(R.color.gold))
        view.editText.backgroundTintList = ColorStateList.valueOf(getColor(R.color.gold))


    }
    fun getColor(colorRes: Int): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context.resources.getColor(colorRes, null)
        } else {
            context.resources.getColor(colorRes)
        }
    }

    fun handleAttributes(context: Context, attrs: AttributeSet) {
        val styledAttributes = context.getTheme().obtainStyledAttributes(
            attrs,
            R.styleable.InputView,
            0, 0
        )

        try {

            expectedType = EXPECTED_INPUT_TYPE.values()[styledAttributes.getInteger(R.styleable.InputView_expectedInputType, 0)]
            handleExpectedType(expectedType)
            val hint = styledAttributes.getString(R.styleable.InputView_hint)
            view.editLayout.hint = hint
        } finally {
            styledAttributes.recycle();
        }

    }

    fun handleExpectedType(expextedType: EXPECTED_INPUT_TYPE) {
        when (expextedType) {
            EXPECTED_INPUT_TYPE.EMAIL -> {
                view.editText.addTextChangedListener(getEmailWatcher {
                    setColors(it)
                    onStateChanged()
                })
                view.editText.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS

            }
            EXPECTED_INPUT_TYPE.PASSWORD -> {
                view.editText.setTransformationMethod(PasswordTransformationMethod.getInstance())
                view.editLayout.isPasswordVisibilityToggleEnabled = true
                view.editLayout.setPasswordVisibilityToggleTintList(AppCompatResources.getColorStateList(context, R.color.gold))


                view.editText.addTextChangedListener(getPasswordWatcher {
                    setColors(it)
                    onStateChanged()
                })


            }
        }
    }



    fun setColors(isValid: Boolean) {

        if (!isValid) {

            val errorStateList = ColorStateList.valueOf(getColor(R.color.red))
            view.editText.backgroundTintList = errorStateList
            view.editLayout.backgroundTintList = errorStateList
            view.editText.setHintTextColor(getColor(R.color.red))


        } else {
            val okStateList = ColorStateList.valueOf(getColor(R.color.gold))
            view.editText.backgroundTintList = okStateList
            view.editLayout.backgroundTintList = okStateList
            view.editText.setHintTextColor(getColor(R.color.gold))

        }
    }


    fun getEmailWatcher(
        listener: ((isValidEmail: Boolean) -> Unit)
    ): TextWatcher {
        return object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {

            }

            override fun beforeTextChanged(text: CharSequence, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(text: CharSequence, p1: Int, p2: Int, p3: Int) {
                listener.invoke(
                    TextUtils.isEmpty(text) || android.util.Patterns.EMAIL_ADDRESS.matcher(
                        text
                    ).matches()

                )
            }
        }
    }
    fun getPasswordWatcher(
        listener: ((isValidEmail: Boolean) -> Unit)
    ): TextWatcher {
        return object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
            }

            override fun beforeTextChanged(text: CharSequence, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(text: CharSequence, p1: Int, p2: Int, p3: Int) {
                listener.invoke(
                    TextUtils.isEmpty(text) || text.length >= 8
                )
            }
        }
    }


    enum class EXPECTED_INPUT_TYPE {
        TEXT,
        EMAIL,
        PASSWORD,
        PRRICE,
        NAME,
        INSTA_LINK,
        PHONE

    }

    override fun validationCheck(): Boolean {
        val text = editText.text
        if (text != null) {
            if (expectedType == EXPECTED_INPUT_TYPE.EMAIL) {
                val matches = android.util.Patterns.EMAIL_ADDRESS.matcher(text).matches()
                setColors(matches)
                return matches
            } else if (expectedType == EXPECTED_INPUT_TYPE.PASSWORD) {
                val matches = text.length >= 8
                setColors(matches)
                return matches
            }
        }
        return true


    }

    fun getText(): String {
        return editText.text.toString()
    }

}