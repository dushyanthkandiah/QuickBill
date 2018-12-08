package Fragments;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.dushyanth.quickbill.R;
import com.example.dushyanth.quickbill.StartUpActivity;

@SuppressLint("ValidFragment")
public class FragmentSignUp extends Fragment {

    StartUpActivity startUpActivity;
    private Button btnSignIn, btnSignUp;
    private EditText txtName, txtEmail, txtPhone, txtNic, txtAddress, txtPassword, txtConfirmPassword;
    private View iView;

    public FragmentSignUp(StartUpActivity startUpActivity) {
        this.startUpActivity = startUpActivity;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        iView = inflater.inflate(R.layout.fragment_sign_up, container, false);

        btnSignIn = iView.findViewById(R.id.btnSignIn);
        btnSignUp = iView.findViewById(R.id.btnSignUp);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startUpActivity.changeFragment("signin");
            }
        });

        return iView;

    }

}
