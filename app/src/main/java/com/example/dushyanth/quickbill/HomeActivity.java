package com.example.dushyanth.quickbill;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import Dialogs.DialogAboutDeveloper;
import Fragments.FragmentProfile;
import Fragments.FragmentRequest;
import Fragments.FragmentRequestedItems;
import Fragments.FragmentViewRequests;
import OtherClasses.SessionData;
import ServerLink.ServerCustomer;
import pl.droidsonroids.gif.GifImageView;

public class HomeActivity extends AppCompatActivity {

    private ImageView imgProfile, imgRequest;
    private FragmentProfile fragmentProfile;
    private FragmentRequest fragmentRequest;
    private FragmentViewRequests fragmentViewRequests;
    private FragmentRequestedItems fragmentRequestedItems;
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
        if (!SessionData.currentFragment.equals("view_request") && !SessionData.currentFragment.equals("view_requested_item"))
            changeFragment("view_request");
    }

    public void changeFragment(String frag) {
        if (frag.equals("profile")) {
            fragmentProfile = new FragmentProfile(this);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            lblFragmentTitle.setText("My Profile");
            SessionData.currentFragment = "profile";
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.anim.animation_enter, R.anim.animation_leave);
            ft.replace(R.id.content_frame, fragmentProfile, "fragment");
            ft.commit();
        } else if (frag.equals("request")) {
            fragmentRequest = new FragmentRequest(this);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            lblFragmentTitle.setText("Quik Del");
            SessionData.currentFragment = "request";
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragmentRequest, "fragment");
            ft.commit();
        } else if (frag.equals("view_request")) {
            fragmentViewRequests = new FragmentViewRequests(this);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            lblFragmentTitle.setText("My Requests");
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.anim.animation_enter, R.anim.animation_leave);
            ft.replace(R.id.content_frame, fragmentViewRequests, "fragment");
            ft.commit();
            SessionData.currentFragment = "view_request";
        } else {
            fragmentRequestedItems = new FragmentRequestedItems(fragmentViewRequests);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            lblFragmentTitle.setText("Request Items");
            SessionData.currentFragment = "view_requested_item";
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.anim.animation_enter, R.anim.animation_leave);
            ft.replace(R.id.content_frame, fragmentRequestedItems, "fragment");
            ft.commit();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void logOut() {
        SharedPreferences myPrefs = getSharedPreferences(SessionData.PREFS_LOGIN, 0);
        SharedPreferences.Editor editor = myPrefs.edit();
        editor.putString("userId", "");
        editor.putString("userName", "");
        editor.putString("regDate", "");
        editor.commit();

        startActivity(new Intent(HomeActivity.this, StartUpActivity.class));
        overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
        finish();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.logout) {
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
//        } else if (id == R.id.developer) {
//            DialogAboutDeveloper dialogAboutDeveloper = DialogAboutDeveloper.newInstance();
//            dialogAboutDeveloper.show(getSupportFragmentManager(), "dialog");
//        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        if (SessionData.currentFragment.equals("view_requested_item")) {
            changeFragment("view_request");
        } else {
            if (SessionData.currentFragment.equals("profile") || SessionData.currentFragment.equals("view_request"))
                changeFragment("request");
            else
                super.onBackPressed();
        }
    }
}
