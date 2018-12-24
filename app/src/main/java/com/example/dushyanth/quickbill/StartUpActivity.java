package com.example.dushyanth.quickbill;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import Fragments.FragmentLogin;
import Fragments.FragmentSignUp;
import OtherClasses.SessionData;
import OtherClasses.ShowDialog;
import io.trialy.library.Trialy;
import io.trialy.library.TrialyCallback;

import static io.trialy.library.Constants.STATUS_TRIAL_JUST_ENDED;
import static io.trialy.library.Constants.STATUS_TRIAL_JUST_STARTED;
import static io.trialy.library.Constants.STATUS_TRIAL_NOT_YET_STARTED;
import static io.trialy.library.Constants.STATUS_TRIAL_OVER;
import static io.trialy.library.Constants.STATUS_TRIAL_RUNNING;

public class StartUpActivity extends AppCompatActivity {

    private FragmentLogin fragmentLogin;
    private FragmentSignUp fragmentSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up);

        //Initialize the library and check the current trial status on every launch
//        Trialy mTrialy = new Trialy(getApplicationContext(), "17OHIFAY466GMEH8RLK");
//        mTrialy.checkTrial("default", mTrialyCallback);
        Continue();

    }
//
//    private TrialyCallback mTrialyCallback = new TrialyCallback() {
//        @Override
//        public void onResult(int status, long timeRemaining, String sku) {
//            switch (status) {
//                case STATUS_TRIAL_JUST_STARTED:
//                    //The trial has just started - enable the premium features for the user
//                    break;
//                case STATUS_TRIAL_RUNNING:
//
//                    break;
//                case STATUS_TRIAL_JUST_ENDED:
//                    //The trial has just ended - block access to the premium features
//                    break;
//                case STATUS_TRIAL_NOT_YET_STARTED:
//                    //The user hasn't requested a trial yet - no need to do anything
//                    break;
//                case STATUS_TRIAL_OVER:
//                    ShowDialog.showToast(getApplicationContext(), "Your trial period is over");
//                    break;
//            }
//            Log.i("TRIALY", "Returned status: " + Trialy.getStatusMessage(status));
//        }
//
//    };

    public void Continue() {
        fragmentLogin = new FragmentLogin(this);
        fragmentSignUp = new FragmentSignUp(this);

        SharedPreferences myPrefs = getSharedPreferences(SessionData.PREFS_LOGIN, 0);

        if (myPrefs.contains("userId")) {

            SessionData.userId = myPrefs.getString("userId", "0");
            SessionData.userName = myPrefs.getString("userName", "No Name");
            SessionData.userregisteredDate = myPrefs.getString("regDate", "28-11-2018");

            if (!SessionData.userId.equals("")) {
                startActivity(new Intent(this, HomeActivity.class));
                overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
                finish();
            } else {
                changeFragment("signin");

            }
        } else
            changeFragment("signin");
    }

    public void changeFragment(String frag) {
        if (frag.equals("signin")) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragmentLogin, "fragment");
            ft.commit();
        } else {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragmentSignUp, "fragment");
            ft.commit();
        }
    }
}
