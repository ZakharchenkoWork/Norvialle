package com.hast.norvialle.presenataion.utils.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Build;
import android.text.Html;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.annotation.StyleRes;

import com.hast.norvialle.R;


/**
 * Created by Natali-Pi on 08.12.2017.
 */

public class SimpleDialog extends AlertDialog.Builder {
    public static final int DEFAULT = -1;
    protected static final String TAG_TITLE = "tt";
    protected static final String TAG_TEXT_VIEW = "tv";
    protected static final String TAG_EDIT_TEXT = "et";


    private static SimpleDialog lastDialog;
    private static Typeface typeface = null;
    private static Integer textStyle = null;

    private Typeface customTypeface = null;
    private Integer customTextStyle = null;
    private Integer customTitleStyle = null;

    private DIALOG_TYPE dialogType = DIALOG_TYPE.MESSAGE_ONLY;
    private String message = null;
    private String editTextDefaultData = null;
    private String editTextHint = null;
    private String positiveButtonText = "OK";
    private String negativeButtonText = "Cancel";

    private int inputType = DEFAULT;
    protected LinearLayout layout;
    private OKListener okListener = null;
    boolean isCancaleble = false;
    private AlertDialog lastInstance = null;

    /**
     * Easy to use tool for simple dialogs.
     *
     * @param ctx        any Android Context
     * @param dialogType type of the dialog, to show.
     */
    public SimpleDialog(final Context ctx, DIALOG_TYPE dialogType) {
        super(ctx);

        if (lastDialog != null) {
            return;
        } else {
            lastDialog = this;
        }

        lastInstance = create();

        this.dialogType = dialogType;
    }

    /**
     * Set input type for EditText inside this dialog, or SimpleDialog.DEFAULT.
     * <p>
     * Call of this method is not necessary.
     *
     * @param inputType InputType constant
     * @return this
     */

    public SimpleDialog setInputType(int inputType) { // TODO: Think about InputType anotation
        this.inputType = inputType;
        return this;
    }

    /**
     * Set text for the positive button inside this dialog.
     * <p>
     * Call of this method is not necessary.
     *
     * @param positiveButtonText text for button.
     * @return this
     */
    public SimpleDialog setOkButtonText(String positiveButtonText) {
        this.positiveButtonText = positiveButtonText;
        return this;
    }

    /**
     * Set text for the negative button inside this dialog.
     * <p>
     * Call of this method is not necessary.
     *
     * @param negativeButtonText text for button.
     * @return this
     */
    public SimpleDialog setCancelButtonText(String negativeButtonText) {
        this.negativeButtonText = negativeButtonText;
        return this;
    }

    /**
     * Set typeface for any text inside all of the OneButtonDialogs.
     * Ignored if setCustomTypeface() is called with no null value.
     * <p>
     * Call of this method is not necessary.
     *
     * @param typeface text for button.
     * @return this
     */
    public static void seAllDialogsTypeface(Typeface typeface) {
        SimpleDialog.typeface = typeface;
    }

    /**
     * Set typeface for any text inside this and only this SimpleDialog.
     * Ignoring typeface setted for all other OneButtonDialogs.
     * <p>
     * Call of this method is not necessary.
     *
     * @param typeface text for button.
     * @return this
     */
    public void setCustomTypeface(Typeface typeface) {
        this.customTypeface = typeface;
    }

    /**
     * Set Style for any text inside all of the OneButtonDialogs.
     * Ignored if setCustomTextStyle() is called with no null and no DEFAULT value.
     * <p>
     * Call of this method is not necessary.
     *
     * @param textStyle text for button.
     * @return this
     */
    public static void setAllDialogsTextStyle(@StyleRes int textStyle) {
        SimpleDialog.textStyle = textStyle;
    }

    /**
     * Set Style for title text inside this and only this SimpleDialog.
     * Ignoring Style setted for all other OneButtonDialogs.
     * <p>
     * Call of this method is not necessary.
     *
     * @param textStyle text for button.
     * @return this
     */
    public SimpleDialog setCustomTitleStyle(@StyleRes int textStyle) {
        this.customTitleStyle = textStyle;
        return this;
    }

    /**
     * Set Style for any text inside this and only this SimpleDialog.
     * Ignoring Style setted for all other OneButtonDialogs.
     * <p>
     * Call of this method is not necessary.
     *
     * @param textStyle text for button.
     * @return this
     */
    public SimpleDialog setCustomTextStyle(@StyleRes int textStyle) {
        this.customTextStyle = textStyle;
        return this;
    }

    public SimpleDialog setEditTextHint(String editTextHint) {
        this.editTextHint = editTextHint;
        return this;
    }

    /**
     * Set Title for this dialog.
     * <p>
     * Call of this method is not necessary.
     *
     * @param title to set, may be null or empty String if you don't need a title bar
     * @return
     */
    private String title = null;

    public SimpleDialog setTitle(String title) {
        if (!isDefault(title)) {
            this.title = title;
        }
        return this;
    }


    /**
     * @param message use getString() to get string from resources.
     * @return this
     */
    public SimpleDialog setMessage(String message) {
        this.message = message;
        return this;
    }


    public SimpleDialog setOkListener(OKListener okListener) {
        this.okListener = okListener;
        return this;
    }

    public SimpleDialog setEditTextDefaultData(String editTextDefaultData) {
        this.editTextDefaultData = editTextDefaultData;
        return this;
    }

    /**
     * @param icon pass Resource id for icon, or SimpleDialog.DEFAULT if no icon needed
     */
    @Override
    public SimpleDialog setIcon(@DrawableRes int icon) {
        if (icon != DEFAULT) {
            setIcon(icon);
        }
        return this;
    }

    /**
     * @return AlertDialog
     * @deprecated you don't need to call show(), it will be called automatically after build();
     */
    @Override
    @Deprecated
    public AlertDialog show() {
        return super.show();
    }

    /**
     * @return
     */
    public SimpleDialog build() {
        layout = new LinearLayout(getContext());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layout.setLayoutParams(lp);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding((int) getContext().getResources().getDimension(R.dimen.margin_standard), (int) getContext().getResources().getDimension(R.dimen.margin_standard), (int) getContext().getResources().getDimension(R.dimen.margin_standard), 0);
        if (!isDefault(title)) {
            TextView titleView = new TextView(getContext());
            titleView.setText(title);
            titleView.setTypeface(null, Typeface.BOLD);
            layout.addView(titleView);
            titleView.setTag(TAG_TITLE);
            if (!isDefault(customTitleStyle)) {
                configureStyle(titleView, customTitleStyle);
            }
        }


        final EditText input = new EditText(getContext());
        input.setTag(TAG_EDIT_TEXT);
        if (dialogType == DIALOG_TYPE.MESSAGE_ONLY) {
            if (!isDefault(message)) {
                TextView textView = new TextView(getContext());
                textView.setPadding(0, (int) getContext().getResources().getDimension(R.dimen.margin_standard), 0, 0);
                textView.setLayoutParams(lp);
                textView.setText(Html.fromHtml(message));
                //textView.setGravity(Gravity.CENTER_HORIZONTAL);
                configureStyle(textView);
                layout.addView(textView);
                textView.setTag(TAG_TEXT_VIEW);
                //    setMessage(Html.fromHtml(message)); // TODO: check android versions
            }
        } else {


            input.setLayoutParams(lp);

            if (!isDefault(editTextDefaultData)) {
                if (inputType == InputType.TYPE_CLASS_NUMBER) {
                    if (editTextDefaultData.equals("0") ){
                        input.setText("");
                    } else {
                        input.setText(editTextDefaultData);
                    }
                } else {
                    input.setText(editTextDefaultData);
                }

            }
            if (!isDefault(editTextHint)) {
                input.setHint(editTextHint);
            }
            if (inputType != DEFAULT) {
                input.setInputType(inputType);

            }
            configureStyle(input);
            input.setImeActionLabel("ok", KeyEvent.KEYCODE_ENTER);
            input.setOnEditorActionListener((TextView v, int actionId, KeyEvent event)->{
                if (okListener != null) {

                    okListener.onOKpressed(getData(input));
                }
                lastDialog = null;
                hide();
                return true;
            });
            input.postDelayed(()->{
                InputMethodManager mgr = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.showSoftInput(input, InputMethodManager.SHOW_IMPLICIT);
            },300);

            if (dialogType == DIALOG_TYPE.MESSAGE_AND_INPUT) {


                TextView textView = new TextView(getContext());
                textView.setTag(TAG_TEXT_VIEW);
                textView.setLayoutParams(lp);
                textView.setText(Html.fromHtml(message));
                //textView.setGravity(Gravity.CENTER_HORIZONTAL);
                configureStyle(textView);
                LinearLayout innerLayout = new LinearLayout(getContext());
                innerLayout.setLayoutParams(lp);

                innerLayout.setOrientation(LinearLayout.VERTICAL);
                innerLayout.addView(input);
                innerLayout.addView(textView);

                textView.setPadding(10, (int) getContext().getResources().getDimension(R.dimen.margin_standard), 0, 0);
                layout.addView(innerLayout);

            } else if (dialogType == DIALOG_TYPE.INPUT_ONLY) {
                configureStyle(input);
                layout.addView(input);
            }
        }

        setPositiveButton(positiveButtonText, (DialogInterface dialog, int which) -> {
            if (okListener != null) {
                okListener.onOKpressed(getData(input));
            }
            lastDialog = null;
            dialog.dismiss();

        });
        if (isCancaleble) {
            setNegativeButton(negativeButtonText, (DialogInterface dialog, int which) -> {
                lastDialog = null;
                dialog.dismiss();
            });
        }
        setOnCancelListener((dialog)->{
            lastDialog = null;
        });
        setView(layout);
       lastInstance =  super.show();

        return this;
    }


    @Override
    public SimpleDialog setCancelable(boolean cancelable) {
        isCancaleble = cancelable;
        super.setCancelable(cancelable);
        return this;

    }

    protected String getData(EditText input) {
        if (dialogType != DIALOG_TYPE.MESSAGE_ONLY) {
            return !isDefault(input.getText().toString()) ? input.getText().toString() : "";
        } else {
            return "";
        }
    }
public void hide(){
    if (lastInstance != null) {
        try{
            lastInstance.dismiss();
            lastInstance.cancel();
            lastInstance.hide();
        }catch (Exception e){}
    }
}
    private void configureStyle(TextView view) {
        if (customTypeface != null) {
            view.setTypeface(customTypeface);
        } else if (typeface != null) {
            view.setTypeface(typeface);
        }

        int styleRes = DEFAULT;
        if (!isDefault(customTextStyle)) {
            styleRes = customTextStyle;
        } else if (!isDefault(textStyle)) {
            styleRes = textStyle;
        }

        configureStyle(view, styleRes);
    }

    private void configureStyle(TextView view, int styleRes) {
        if (!isDefault(styleRes)) {
            if (Build.VERSION.SDK_INT < 23) {
                view.setTextAppearance(getContext(), styleRes);
            } else {
                view.setTextAppearance(styleRes);
            }
        }
    }

    private boolean isDefault(String data) {
        return data == null || data.equals("") || data.equals("" + DEFAULT);
    }

    private boolean isDefault(Integer data) {
        return data == null || data == 0 || data == DEFAULT;
    }

    public enum DIALOG_TYPE {
        MESSAGE_ONLY,
        INPUT_ONLY,
        MESSAGE_AND_INPUT
    }

    public interface OKListener {
        void onOKpressed(String userInput);
    }

}