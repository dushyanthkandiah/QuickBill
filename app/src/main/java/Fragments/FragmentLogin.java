package Fragments;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.dushyanth.quickbill.HomeActivity;
import com.example.dushyanth.quickbill.R;
import com.example.dushyanth.quickbill.StartUpActivity;

import OtherClasses.SessionData;
import OtherClasses.ShowDialog;
import OtherClasses.Validation;
import ServerLink.ServerCustomer;

@SuppressLint("ValidFragment")
public class FragmentLogin extends Fragment {
    StartUpActivity startUpActivity;
    private Button btnSignUp;
    private EditText txtEmailPhone, txtPassword;
    private Button btnLogin;
    private View iView;
    private Validation vd;
    public String usernameType = "phone", username = "", password="";
    ServerCustomer serverCustomer;
    public ProgressDialog pDialog;

    public FragmentLogin(StartUpActivity startUpActivity) {
        this.startUpActivity = startUpActivity;
        serverCustomer = new ServerCustomer();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        iView = inflater.inflate(R.layout.fragment_login, container, false);

        btnSignUp = iView.findViewById(R.id.btnSignUp);
        txtEmailPhone = iView.findViewById(R.id.txtEmailPhone);
        txtPassword = iView.findViewById(R.id.txtPassword);
        btnLogin = iView.findViewById(R.id.btnLogin);
        vd = new Validation(getActivity());

        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
        pDialog.setMessage("Logging in...");

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startUpActivity.changeFragment("signup");
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkValidation();
            }
        });

        return iView;
    }

    private void checkValidation() {

        String[] fieldName = {"Email/Phone", "Password"};
        EditText[] field = {txtEmailPhone, txtPassword};

        if (vd.CheckEmptyText(fieldName, field)) {
            boolean flag = checkPhone();

            if (flag)
                usernameType = "phone";
            else
                usernameType = "email";

            checkDb();
        }

    }

    private boolean checkPhone() {
        String input = txtEmailPhone.getText().toString();
        boolean flag = false;

        if (vd.PhoneCheckWithoutToast(input)) {
            if (Character.isDigit(input.charAt(input.length() - 1)) && !input.contains("@"))
                flag = true;
        }

        return flag;
    }

    private void checkDb(){
        password = txtPassword.getText().toString().replace("'", "''");
        username = txtEmailPhone.getText().toString().trim().replace("'", "''");

        if (usernameType.equals("phone")){
            username = username.substring(username.length() - 9, username.length());
        }

        serverCustomer.checkLogin(this);

    }

    public void GotoHome(){
        ShowDialog.showToast(getActivity(),"Hello " + SessionData.userName);
        SharedPreferences myPrefs = getActivity().getSharedPreferences(SessionData.PREFS_LOGIN, 0);
        SharedPreferences.Editor editor = myPrefs.edit();
        editor.putString("userId", SessionData.userId);
        editor.putString("userName", SessionData.userName);
        editor.putString("regDate", SessionData.userregisteredDate);
        editor.commit();

        startActivity(new Intent(startUpActivity, HomeActivity.class));
        startUpActivity.overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
        startUpActivity.finish();

    }


}
