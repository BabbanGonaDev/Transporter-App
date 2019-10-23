package com.bgenterprise.transporterapp.Onboarding;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.graphics.Color;
import android.os.Bundle;

import com.bgenterprise.transporterapp.R;
import com.bgenterprise.transporterapp.SessionManager;
import com.github.paolorotolo.appintro.AppIntro;

public class OnBoardingActivity extends AppIntro {
    SessionManager sessionM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionM = new SessionManager(getApplicationContext());
        addSlide(SampleSlide.newInstance(R.layout.onboard_slide_1));
        addSlide(SampleSlide.newInstance(R.layout.onboard_slide_2));
        addSlide(SampleSlide.newInstance(R.layout.onboard_slide_3));
        addSlide(SampleSlide.newInstance(R.layout.onboard_slide_4));
        setSkipText("Skip");
        setDoneText("Finish");
        setNextArrowColor(Color.BLUE);
        setColorDoneText(Color.WHITE);
        setColorSkipButton(Color.WHITE);
        setBarColor(Color.TRANSPARENT);
        setIndicatorColor(Color.BLACK, Color.GRAY);
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        sessionM.SET_ONBOARD_STATUS(true);
        finish();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        sessionM.SET_ONBOARD_STATUS(true);
        finish();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
    }
}
