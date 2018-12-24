package Fragments;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.dushyanth.quickbill.R;
import com.example.dushyanth.quickbill.StartUpActivity;

import java.io.IOException;
import java.io.InputStream;

import Models.ClassUsers;
import OtherClasses.ShowDialog;
import OtherClasses.Utils;
import OtherClasses.Validation;
import ServerLink.ServerCustomer;

@SuppressLint({"ValidFragment", "ResourceType"})
public class    FragmentSignUp extends Fragment {

    public StartUpActivity startUpActivity;
    private Button btnSignIn, btnSignUp;
    private EditText txtName, txtEmail, txtPhone, txtNic, txtAddress, txtPassword, txtConfirmPassword;
    private View iView;
    private Validation vd;
    public ProgressDialog pDialog;
    public ClassUsers classUsers;
    ServerCustomer serverCustomer = new ServerCustomer();

    public FragmentSignUp(StartUpActivity startUpActivity) {
        this.startUpActivity = startUpActivity;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        iView = inflater.inflate(R.layout.fragment_sign_up, container, false);

        btnSignIn = iView.findViewById(R.id.btnSignIn);
        btnSignUp = iView.findViewById(R.id.btnSignUp);
        txtName = iView.findViewById(R.id.txtName);
        txtEmail = iView.findViewById(R.id.txtEmail);
        txtPhone = iView.findViewById(R.id.txtPhone);
        txtNic = iView.findViewById(R.id.txtNic);
        txtAddress = iView.findViewById(R.id.txtAddress);
        txtPassword = iView.findViewById(R.id.txtPassword);
        txtConfirmPassword = iView.findViewById(R.id.txtConfirmPassword);

        vd = new Validation(getActivity());
        classUsers = new ClassUsers();

        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
        pDialog.setMessage("Signing Up...");

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startUpActivity.changeFragment("signin");
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkValidation();
            }
        });

        return iView;

    }

    private void checkValidation() {
        String[] fieldName = {"Name", "Phone Number", "NIC", "Address", "Password", "Confirm Password"};
        EditText[] field = {txtName, txtPhone, txtNic, txtAddress, txtPassword, txtConfirmPassword};

        if (vd.CheckEmptyText(fieldName, field)) {
            if (vd.PhoneCheck(txtPhone.getText().toString().trim())) {
                if (vd.PasswordCheck(txtPassword)) {
                    if (vd.PasswordCheck(txtPassword, txtConfirmPassword)) {
                        if (txtNic.getText().toString().trim().length() == 10) {
                            if (vd.NicCheck(txtNic.getText().toString().trim().toLowerCase()))
                                getData();
                        } else if (txtNic.getText().toString().trim().length() == 12) {
                            if (vd.NicCheck12(txtNic.getText().toString().trim().toLowerCase()))
                                getData();
                        } else {
                            ShowDialog.showToast(getActivity(), "Incorrect NIC Number");
                        }
                    }
                }
            }
        }
    }


    private void getData() {
        String gender = vd.getGender(txtNic.getText().toString().trim());
        classUsers.setName(txtName.getText().toString().trim().replace("'", "''"));
        classUsers.setEmail(txtEmail.getText().toString().trim().replace("'", "''"));
        classUsers.setPhone(Long.parseLong(txtPhone.getText().toString().trim().replace("'", "''")));
        classUsers.setNic(txtNic.getText().toString().trim());
        classUsers.setGender(gender);
        classUsers.setDefaultPicture(gender);
        classUsers.setDob(vd.getBirthDate(txtNic.getText().toString().trim()));
        classUsers.setAddress(txtAddress.getText().toString().trim().replace("'", "''"));
        classUsers.setPassword(txtPassword.getText().toString().replace("'", "''"));

        InputStream inputstream;
        if (gender.equals("Male"))
            inputstream = startUpActivity.getResources().openRawResource(R.drawable.profile_male);
        else
            inputstream = startUpActivity.getResources().openRawResource(R.drawable.profile_female);

        byte[] imgByte = new byte[0];

        try {
            imgByte = Utils.getBytes(inputstream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        classUsers.setPicture(imgByte);

//        System.out.println(classUsers.getName() + "\n" + classUsers.getEmail() + "\n" + classUsers.getPhone() + "\n" + classUsers.getNic() + "\n" + classUsers.getGender() + "\n" + classUsers.getDob() + "\n" + classUsers.getAddress() + "\n" + classUsers.getPassword() + "\n" + classUsers.getPicture());

        serverCustomer.createUser(this);

    }

    public void GotoLogin(){
        txtNic.setText("");
        txtName.setText("");
        txtEmail.setText("");
        txtPhone.setText("");
        txtAddress.setText("");
        txtPassword.setText("");
        txtConfirmPassword.setText("");
        startUpActivity.changeFragment("signin");

    }

}













