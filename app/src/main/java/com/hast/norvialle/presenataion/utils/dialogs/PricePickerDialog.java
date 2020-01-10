package com.hast.norvialle.presenataion.utils.dialogs;

import android.content.Context;

import android.widget.NumberPicker;
import android.widget.TextView;



/**
 * Created by MisterY on 28.12.2015.
 */
public class PricePickerDialog extends PickerDialog {

    private static final int NUMBER_OF_INTEGER_PICKERS = 4;
    private static final int NUMBER_OF_DECIMAL_PICKERS = 0;


    public PricePickerDialog(Context context, String dialogTitle, float startValue) {
        super(context, dialogTitle, startValue, NUMBER_OF_INTEGER_PICKERS, NUMBER_OF_DECIMAL_PICKERS); //3 integers and no decimal picker
    }

    @Override
    protected void setUpStyle(NumberPicker[] integerNumberPickers, NumberPicker[] decimalNumberPickers, TextView result) {
        integerNumberPickers[0].setMinValue(0);
        integerNumberPickers[0].setMaxValue(9);

        integerNumberPickers[1].setMinValue(0);
        integerNumberPickers[1].setMaxValue(9);

        integerNumberPickers[2].setMinValue(0);
        integerNumberPickers[2].setMaxValue(9);

        integerNumberPickers[3].setMinValue(0);
        integerNumberPickers[3].setMaxValue(9);
    }

    /**
     * @param collectedValue normally value obtained by calling method collect
     * @return prepared and formatted value as text
     */
    @Override
    protected String getInnerResultString(float collectedValue) {
        if (collectedValue != 0 && ("" + collectedValue).contains(".")) {
            return ("" + collectedValue).replace(".0", "");
        } else {
            return "";
        }
    }

    @Override
    protected void onNumberPickersValuesChange(NumberPicker[] integerNumberPickers, NumberPicker[] decimalNumberPickers) {

        super.onNumberPickersValuesChange(integerNumberPickers, decimalNumberPickers);// refreshes result, after changes
    }


}