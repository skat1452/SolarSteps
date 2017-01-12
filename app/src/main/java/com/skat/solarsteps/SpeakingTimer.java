package com.skat.solarsteps;

import java.util.ArrayList;

public class SpeakingTimer implements com.skat.solarsteps.StepListener {

    PedometerSettings mSettings;
    com.skat.solarsteps.Utils mUtils;
    boolean mShouldSpeak;
    float mInterval;
    long mLastSpeakTime;
    
    public SpeakingTimer(PedometerSettings settings, com.skat.solarsteps.Utils utils) {
        mLastSpeakTime = System.currentTimeMillis();
        mSettings = settings;
        mUtils = utils;
        reloadSettings();
    }
    public void reloadSettings() {
        mShouldSpeak = mSettings.shouldSpeak();
        mInterval = mSettings.getSpeakingInterval();
    }
    
    public void onStep() {
        long now = System.currentTimeMillis();
        long delta = now - mLastSpeakTime;
        
        if (delta / 60000.0 >= mInterval) {
            mLastSpeakTime = now;
            notifyListeners();
        }
    }
    
    public void passValue() {
    }

    public interface Listener {
        public void speak();
    }
    private ArrayList<Listener> mListeners = new ArrayList<Listener>();

    public void addListener(Listener l) {
        mListeners.add(l);
    }
    public void notifyListeners() {
        mUtils.ding();
        for (Listener listener : mListeners) {
            listener.speak();
        }
    }

    public boolean isSpeaking() {
        return mUtils.isSpeakingNow();
    }
}

