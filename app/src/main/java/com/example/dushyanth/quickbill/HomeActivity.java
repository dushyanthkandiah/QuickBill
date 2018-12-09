package com.example.dushyanth.quickbill;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import Fragments.FragmentProfile;
import Fragments.FragmentRequest;
import Fragments.FragmentViewRequests;
import OtherClasses.SessionData;
import OtherClasses.ShowDialog;
import ServerLink.ServerCustomer;
import pl.droidsonroids.gif.GifImageView;

public class HomeActivity extends AppCompatActivity {

    private ImageView imgLogOut, imgProfile, imgRequest;
    private FragmentProfile fragmentProfile;
    private FragmentRequest fragmentRequest;
    private FragmentViewRequests fragmentViewRequests;
    private TextView lblFragmentTitle;
    private GifImageView progressBar;
    private Toolbar toolbar;
    public ProgressDialog pDialog;
    public ServerCustomer serverCustomer = new ServerCustomer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        imgLogOut = findViewById(R.id.imgLogOut);
        imgRequest = findViewById(R.id.imgRequest);
        imgProfile = findViewById(R.id.imgProfile);
        progressBar = findViewById(R.id.progressBar);
        lblFragmentTitle = findViewById(R.id.lblFragmentTitle);


        fragmentRequest = new FragmentRequest(this);
        fragmentViewRequests = new FragmentViewRequests(this);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Checking Validity...");

        serverCustomer.checkUserValidity(this);

        changeFragment("request");

        imgRequest.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(v.getContext(), "View All Requests", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        imgProfile.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(v.getContext(), "View Profile", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        imgLogOut.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(v.getContext(), "Log Out", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

    }

    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    public void imgProfile(View d) {
        if (!SessionData.currentFragment.equals("profile"))
            changeFragment("profile");
    }

    public void imgRequest(View a) {
        if (!SessionData.currentFragment.equals("view_request"))
            changeFragment("view_request");
    }

    public void changeFragment(String frag) {
        if (frag.equals("profile")) {
            fragmentProfile = new FragmentProfile(this);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            lblFragmentTitle.setText("My Profile");
            SessionData.currentFragment = "profile";
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragmentProfile, "fragment");
            ft.commit();
        } else if (frag.equals("request")) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            lblFragmentTitle.setText("Quik Del");
            SessionData.currentFragment = "request";
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragmentRequest, "fragment");
            ft.commit();
        } else {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            lblFragmentTitle.setText("My Requests");
            SessionData.currentFragment = "view_request";
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragmentViewRequests, "fragment");
            ft.commit();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void imgLogOut(View s) {
        new AlertDialog.Builder(HomeActivity.this).setTitle("Log Out Confirmation")
                .setMessage("Are you sure you want to Sign Out?")
                .setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                logOut();
                                dialog.dismiss();

                            }
                        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create().show();

    }

    public void logOut(){
        SharedPreferences myPrefs = getSharedPreferences(SessionData.PREFS_LOGIN, 0);
        SharedPreferences.Editor editor = myPrefs.edit();
        editor.putString("userId", "");
        editor.putString("userName", "");
        editor.commit();

        startActivity(new Intent(HomeActivity.this, StartUpActivity.class));
        overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (SessionData.currentFragment.equals("profile") || SessionData.currentFragment.equals("view_request"))
            changeFragment("request");
        else
            super.onBackPressed();
    }
}
