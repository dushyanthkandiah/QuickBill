package com.example.dushyanth.quickbill;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import Fragments.FragmentLogin;
import Fragments.FragmentSignUp;
import OtherClasses.SessionData;

public class StartUpActivity extends AppCompatActivity {

    private FragmentLogin fragmentLogin;
    private FragmentSignUp fragmentSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up);

        fragmentLogin = new FragmentLogin(this);
        fragmentSignUp = new FragmentSignUp(this);

        SharedPreferences myPrefs = getSharedPreferences(SessionData.PREFS_LOGIN, 0);

        if (myPrefs.contains("userId")) {
//            SessionData.userId = myPrefs.getString("userId", "0");
//            SessionData.userName = myPrefs.getString("userName", "No Name");
//
//            startActivity(new Intent(this, HomeActivity.class));
//            overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
//            finish();

        } else {

        }
        if (savedInstanceState == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragmentLogin, "fragment");
            ft.commit();
        }

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
