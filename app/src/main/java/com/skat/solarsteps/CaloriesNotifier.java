package com.skat.solarsteps;

public class CaloriesNotifier extends com.skat.solarsteps.Pedometer implements com.skat.solarsteps.StepListener, com.skat.solarsteps.SpeakingTimer.Listener {

    public interface Listener {
        public void valueChanged(float value);
        public void passValue();
    }
    private Listener mListener;
    
    private static double METRIC_RUNNING_FACTOR = 1.02784823;
    private static double IMPERIAL_RUNNING_FACTOR = 0.75031498;

    private static double METRIC_WALKING_FACTOR = 0.708;
    private static double IMPERIAL_WALKING_FACTOR = 0.517;

    private double mCalories = 0;

    com.skat.solarsteps.PedometerSettings mSettings;
    com.skat.solarsteps.Pedometer mPedometer;
    com.skat.solarsteps.Utils mUtils;
    
    boolean mIsMetric;
    boolean mIsRunning;
    float mStepLength;
    float mBodyWeight;
    float mSleep;

    public CaloriesNotifier(Listener listener, com.skat.solarsteps.PedometerSettings settings, com.skat.solarsteps.Utils utils) {
        mListener = listener;
        mUtils = utils;
        mSettings = settings;
        reloadSettings();
    }
    public void setCalories(float calories) {
        mCalories = calories;
        notifyListener();
    }
    public void reloadSettings() {
        mIsMetric = mSettings.isMetric();
        mIsRunning = mSettings.isWalking();
        mStepLength = mSettings.getStepLength();
        mBodyWeight = mSettings.getBodyWeight();
        mSleep = mSettings.getSleep();
        notifyListener();
    }
    public void resetValues() {
        mCalories = 0;
    }

    public void isMetric(boolean isMetric) {
        mIsMetric = isMetric;
    }
    public void setStepLength(float stepLength) {
        mStepLength = stepLength;
    }
    public void setSleep(float sleep) {mSleep = sleep; }
    
    public void onStep() {
        
        if (mIsMetric) {
            mCalories +=
                (mSleep * 65 + 1.73 * mBodyWeight * (mIsRunning ? METRIC_RUNNING_FACTOR : METRIC_WALKING_FACTOR))
                * mStepLength
                / 100000.0;
        }
        else {
            mCalories += 
                (mSleep * 65 + 1.73 * mBodyWeight * (mIsRunning ? IMPERIAL_RUNNING_FACTOR : IMPERIAL_WALKING_FACTOR))
                * mStepLength
                / 63360.0;
        }
        
        notifyListener();
    }
    
    private void notifyListener() {
        mListener.valueChanged((float)mCalories);
    }
    
    public void passValue() {
        
    }
    
    public void speak() {
        if (mSettings.shouldTellCalories()) {
            if (mCalories > 0) {
                mUtils.say("" + (int)mCalories + " calories burned");
            }
        }
        
    }
    

}

