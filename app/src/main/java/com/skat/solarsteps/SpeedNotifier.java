package com.skat.solarsteps;

public class SpeedNotifier implements PaceNotifier.Listener, SpeakingTimer.Listener {

    public interface Listener {
        public void valueChanged(float value);
        public void passValue();
    }
    private Listener mListener;
    
    int mCounter = 0;
    float mSpeed = 0;
    
    boolean mIsMetric;
    float mStepLength;

    PedometerSettings mSettings;
    com.skat.solarsteps.Utils mUtils;

    float mDesiredSpeed;

    boolean mShouldTellFasterslower;
    boolean mShouldTellSpeed;

    private long mSpokenAt = 0;
    
    public SpeedNotifier(Listener listener, PedometerSettings settings, com.skat.solarsteps.Utils utils) {
        mListener = listener;
        mUtils = utils;
        mSettings = settings;
        mDesiredSpeed = mSettings.getDesiredSpeed();
        reloadSettings();
    }
    public void setSpeed(float speed) {
        mSpeed = speed;
        notifyListener();
    }
    public void reloadSettings() {
        mIsMetric = mSettings.isMetric();
        mStepLength = mSettings.getStepLength();
        mShouldTellSpeed = mSettings.shouldTellSpeed();
        mShouldTellFasterslower = 
            mSettings.shouldTellFasterslower()
            && mSettings.getMaintainOption() == PedometerSettings.M_SPEED;
        notifyListener();
    }
    public void setDesiredSpeed(float desiredSpeed) {
        mDesiredSpeed = desiredSpeed;
    }
    
    private void notifyListener() {
        mListener.valueChanged(mSpeed);
    }
    
    public void paceChanged(int value) {
        if (mIsMetric) {
            mSpeed =
                value * mStepLength
                / 100000f * 60f;
        }
        else {
            mSpeed =
                value * mStepLength
                / 63360f * 60f;
        }
        tellFasterSlower();
        notifyListener();
    }

    private void tellFasterSlower() {
        if (mShouldTellFasterslower && mUtils.isSpeakingEnabled()) {
            long now = System.currentTimeMillis();
            if (now - mSpokenAt > 3000 && !mUtils.isSpeakingNow()) {
                float little = 0.10f;
                float normal = 0.30f;
                float much = 0.50f;
                
                boolean spoken = true;
                if (mSpeed < mDesiredSpeed * (1 - much)) {
                    mUtils.say("much faster!");
                }
                else
                if (mSpeed > mDesiredSpeed * (1 + much)) {
                    mUtils.say("much slower!");
                }
                else
                if (mSpeed < mDesiredSpeed * (1 - normal)) {
                    mUtils.say("faster!");
                }
                else
                if (mSpeed > mDesiredSpeed * (1 + normal)) {
                    mUtils.say("slower!");
                }
                else
                if (mSpeed < mDesiredSpeed * (1 - little)) {
                    mUtils.say("a little faster!");
                }
                else
                if (mSpeed > mDesiredSpeed * (1 + little)) {
                    mUtils.say("a little slower!");
                }
                else {
                    spoken = false;
                }
                if (spoken) {
                    mSpokenAt = now;
                }
            }
        }
    }
    
    public void passValue() {
    }

    public void speak() {
        if (mSettings.shouldTellSpeed()) {
            if (mSpeed >= .01f) {
                mUtils.say(("" + (mSpeed + 0.000001f)).substring(0, 4) + (mIsMetric ? " kilometers per hour" : " miles per hour"));
            }
        }
        
    }

}

