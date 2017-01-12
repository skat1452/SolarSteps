package com.skat.solarsteps.preferences;

import android.content.Context;
import android.util.AttributeSet;

import com.skat.solarsteps.R;

public class SleepPreference extends com.skat.solarsteps.preferences.EditMeasurementPreference {

    public SleepPreference(Context context) {
        super(context);
    }
    public SleepPreference(Context context, AttributeSet attr) {
        super(context, attr);
    }
    public SleepPreference(Context context, AttributeSet attr, int defStyle) {
        super(context, attr, defStyle);
    }

    protected void initPreferenceDetails() {
        mTitleResource = R.string.sleep_setting_title;
        mMetricUnitsResource = R.string.hours;
        mImperialUnitsResource = R.string.hours;
    }
}