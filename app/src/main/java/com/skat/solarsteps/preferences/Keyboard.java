package com.skat.solarsteps.preferences;

import android.content.Context;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

abstract public class Keyboard extends EditTextPreference {

    protected int mTitleResource;

    public Keyboard(Context context) {
        super(context);
        initPreferenceDetails();
    }
    public Keyboard(Context context, AttributeSet attr) {
        super(context, attr);
        initPreferenceDetails();
    }
    public Keyboard(Context context, AttributeSet attr, int defStyle) {
        super(context, attr, defStyle);
        initPreferenceDetails();
    }

    abstract protected void initPreferenceDetails();

    protected void showDialog(Bundle state) {
        setDialogTitle(
                getContext().getString(mTitleResource)
        );

        super.showDialog(state);
    }
    protected void onAddEditTextToDialogView (View dialogView, EditText editText) {
        editText.setRawInputType(
                InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        super.onAddEditTextToDialogView(dialogView, editText);
    }
    public void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            try {
                String.valueOf(((CharSequence)(getEditText().getText())).toString());
            }
            catch (NumberFormatException e) {
                this.showDialog(null);
                return;
            }
        }
        super.onDialogClosed(positiveResult);
    }
}

