package com.hast.norvialle.gui.utils

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.os.Build
import android.telephony.PhoneNumberUtils
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
    var onFocusChanged: ((hasFocus : Boolean) -> Unit)={}
    var view = LayoutInflater.from(context).inflate(R.layout.input_view, this, false)
    var expectedType = EXPECTED_INPUT_TYPE.TEXT

    init {

        addView(view)
        handleAttributes(context, attrs)
        prepareEditTextStyle()
        view.editText.setOnFocusChangeListener { v, hasFocus ->
           v.post{ setColors(validationCheck())
               onFocusChanged(hasFocus)}

        }
    }

    fun prepareEditTextStyle(){
        view.editText.setTextColor(getColor(R.color.gold))
        view.editText.setHintTextColor(getColor(R.color.gold))
        view.editText.backgroundTintList = ColorStateList.valueOf(getColor(R.color.gold))
        view.editText.background.setColorFilter(getColor(R.color.gold), PorterDuff.Mode.SRC_IN)


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
            view.editText.maxLines = 1
            view.editText.ellipsize = TextUtils.TruncateAt.END
        } finally {
            styledAttributes.recycle()
        }

    }

    fun handleExpectedType(expextedType: EXPECTED_INPUT_TYPE) {
        view.editText.addTextChangedListener(getInnerWatcher(expextedType) {
            setColors(it)
            onStateChanged()
        })
        when (expextedType) {
            EXPECTED_INPUT_TYPE.TEXT -> {
                view.editText.inputType = InputType.TYPE_CLASS_TEXT
            }
            EXPECTED_INPUT_TYPE.EMAIL -> {
                view.editText.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            }
            EXPECTED_INPUT_TYPE.PASSWORD -> {
                view.editText.setTransformationMethod(PasswordTransformationMethod.getInstance())
                view.editLayout.isPasswordVisibilityToggleEnabled = true
                view.editLayout.setPasswordVisibilityToggleTintList(AppCompatResources.getColorStateList(context, R.color.gold))

            }
            EXPECTED_INPUT_TYPE.NAME -> {
                view.editText.inputType = InputType.TYPE_CLASS_TEXT
            }
            EXPECTED_INPUT_TYPE.PHONE -> {
                view.editText.inputType = InputType.TYPE_CLASS_PHONE
            }
            EXPECTED_INPUT_TYPE.PRICE -> {
                view.editText.inputType = InputType.TYPE_NULL
                view.editText.isFocusable = false
            }
            EXPECTED_INPUT_TYPE.INSTA_LINK -> {
                view.editText.inputType = InputType.TYPE_CLASS_TEXT
            }
        }
    }



    fun setColors(isValid: Boolean) {

        if (!isValid) {

            val errorStateList = ColorStateList.valueOf(getColor(R.color.red))
            view.editText.backgroundTintList = errorStateList
            view.editLayout.backgroundTintList = errorStateList
            view.editLayout.setHintTextAppearance(R.style.errorHintAppearance)
            view.editLayout.defaultHintTextColor = errorStateList
            view.editText.background.setColorFilter(getColor(R.color.red), PorterDuff.Mode.SRC_IN)
            view.editText.setHintTextColor(getColor(R.color.red))

        } else {
            val okStateList = ColorStateList.valueOf(getColor(R.color.gold))
            view.editLayout.backgroundTintList = okStateList
            view.editLayout.setHintTextAppearance(R.style.normalHintAppearance)
            view.editLayout.defaultHintTextColor = okStateList
            view.editText.backgroundTintList = okStateList
            view.editText.background.setColorFilter(getColor(R.color.gold), PorterDuff.Mode.SRC_IN)
            view.editText.setHintTextColor(getColor(R.color.gold))


        }
    }
private fun textChangeValidation(expextedType: EXPECTED_INPUT_TYPE, text: CharSequence) : Boolean{
    return when(expextedType){
        EXPECTED_INPUT_TYPE.EMAIL->{
            android.util.Patterns.EMAIL_ADDRESS.matcher(
                text
            ).matches()
        }  EXPECTED_INPUT_TYPE.PASSWORD->{
           text.length >= 8
        }
        EXPECTED_INPUT_TYPE.NAME->{
            text.length >= 2
        }
        EXPECTED_INPUT_TYPE.PHONE->{
            PhoneNumberUtils.isGlobalPhoneNumber(text.toString())
        }
        EXPECTED_INPUT_TYPE.PRICE->{
           !TextUtils.isEmpty(text)
        }
        EXPECTED_INPUT_TYPE.INSTA_LINK->{
            text.matches(Regex("(https://?)?(www)?instagram\\.com/[\\S+].{1,30}[?igshid=][a-z,A-Z,0-9].{13,14}"))

        }
        else -> true
    }

}



    override fun validationCheck(): Boolean {

        val isMatching = textChangeValidation(expectedType, getText())
        setColors(isMatching)
        return isMatching
    }

    fun getText(): String {
        return editText.text.toString()
    }

    fun setText(text: String) {
        view.editText.setText(text)
    }

    fun addTextChangedListener(textListener: TextWatcher) {
        view.editText.addTextChangedListener(textListener)
    }
    fun getInnerWatcher(expextedType: EXPECTED_INPUT_TYPE,
                        listener: ((isValid: Boolean) -> Unit)
    ): TextWatcher {
        return object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {

            }

            override fun beforeTextChanged(text: CharSequence, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(text: CharSequence, p1: Int, p2: Int, p3: Int) {
                listener.invoke(textChangeValidation(expextedType, text))
            }
        }
    }

    fun getIntValue(): Int {
          try {
              return getText().toInt()
            } catch (nfe: NumberFormatException) {
              return 0
            }
    }

    enum class EXPECTED_INPUT_TYPE {
        TEXT,
        EMAIL,
        PASSWORD,
        PRICE,
        NAME,
        INSTA_LINK,
        PHONE

    }

    override fun setOnClickListener(listener: OnClickListener?) {
        view.editText.setOnClickListener(listener)
    }
}