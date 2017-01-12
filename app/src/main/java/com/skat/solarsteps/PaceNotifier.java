package com.skat.solarsteps;

import java.util.ArrayList;

public class PaceNotifier implements com.skat.solarsteps.StepListener, com.skat.solarsteps.SpeakingTimer.Listener {

    public interface Listener {
        public void paceChanged(int value);
        public void passValue();
    }
    private ArrayList<Listener> mListeners = new ArrayList<Listener>();
    
    int mCounter = 0;
    
    private long mLastStepTime = 0;
    private long[] mLastStepDeltas = {-1, -1, -1, -1};
    private int mLastStepDeltasIndex = 0;
    private long mPace = 0;

    com.skat.solarsteps.PedometerSettings mSettings;
    com.skat.solarsteps.Utils mUtils;

    int mDesiredPace;

    boolean mShouldTellFasterslower;

    private long mSpokenAt = 0;

    public PaceNotifier(com.skat.solarsteps.PedometerSettings settings, com.skat.solarsteps.Utils utils) {
        mUtils = utils;
        mSettings = settings;
        mDesiredPace = mSettings.getDesiredPace();
        reloadSettings();
    }
    public void setPace(int pace) {
        mPace = pace;
        int avg = (int)(60*1000.0 / mPace);
        for (int i = 0; i < mLastStepDeltas.length; i++) {
            mLastStepDeltas[i] = avg;
        }
        notifyListener();
    }
    public void reloadSettings() {
        mShouldTellFasterslower = 
            mSettings.shouldTellFasterslower()
            && mSettings.getMaintainOption() == com.skat.solarsteps.PedometerSettings.M_PACE;
        notifyListener();
    }
    
    public void addListener(Listener l) {
        mListeners.add(l);
    }

    public void setDesiredPace(int desiredPace) {
        mDesiredPace = desiredPace;
    }

    public void onStep() {
        long thisStepTime = System.currentTimeMillis();
        mCounter ++;

        if (mLastStepTime > 0) {
            long delta = thisStepTime - mLastStepTime;
            
            mLastStepDeltas[mLastStepDeltasIndex] = delta;
            mLastStepDeltasIndex = (mLastStepDeltasIndex + 1) % mLastStepDeltas.length;
            
            long sum = 0;
            boolean isMeaningfull = true;
            for (int i = 0; i < mLastStepDeltas.length; i++) {
                if (mLastStepDeltas[i] < 0) {
                    isMeaningfull = false;
                    break;
                }
                sum += mLastStepDeltas[i];
            }
            if (isMeaningfull && sum > 0) {
                long avg = sum / mLastStepDeltas.length;
                mPace = 60*1000 / avg;

                if (mShouldTellFasterslower && !mUtils.isSpeakingEnabled()) {
                    if (thisStepTime - mSpokenAt > 3000 && !mUtils.isSpeakingNow()) {
                        float little = 0.10f;
                        float normal = 0.30f;
                        float much = 0.50f;
                        
                        boolean spoken = true;
                        if (mPace < mDesiredPace * (1 - much)) {
                            mUtils.say("much faster!");
                        }
                        else
                        if (mPace > mDesiredPace * (1 + much)) {
                            mUtils.say("much slower!");
                        }
                        else
                        if (mPace < mDesiredPace * (1 - normal)) {
                            mUtils.say("faster!");
                        }
                        else
                        if (mPace > mDesiredPace * (1 + normal)) {
                            mUtils.say("slower!");
                        }
                        else
                        if (mPace < mDesiredPace * (1 - little)) {
                            mUtils.say("a little faster!");
                        }
                        else
                        if (mPace > mDesiredPace * (1 + little)) {
                            mUtils.say("a little slower!");
                        }
                        else {
                            spoken = false;
                        }
                        if (spoken) {
                            mSpokenAt = thisStepTime;
                        }
                    }
                }
            }
            else {
                mPace = -1;
            }
        }
        mLastStepTime = thisStepTime;
        notifyListener();
    }
    
    private void notifyListener() {
        for (Listener listener : mListeners) {
            listener.paceChanged((int)mPace);
        }
    }
    
    public void passValue() {
    }
    
    public void speak() {
        if (mSettings.shouldTellPace()) {
            if (mPace > 0) {
                mUtils.say(mPace + " steps per minute");
            }
        }
    }
    

}

