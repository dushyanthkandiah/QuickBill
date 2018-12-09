package Fragments;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dushyanth.quickbill.HomeActivity;
import com.example.dushyanth.quickbill.R;
import com.example.dushyanth.quickbill.StartUpActivity;

import java.io.IOException;
import java.io.InputStream;

import Models.ClassUsers;
import OtherClasses.RotateBitmap;
import OtherClasses.ShowDialog;
import OtherClasses.Utils;
import OtherClasses.Validation;
import ServerLink.ServerCustomer;

import static android.app.Activity.RESULT_OK;

@SuppressLint({"ValidFragment", "ResourceType"})
public class FragmentProfile extends Fragment {

    public HomeActivity homeActivity;
    public EditText txtName, txtEmail, txtPhone, txtNic, txtAddress, txtPassword, txtConfirmPassword;
    public TextView lblUniqueId, lblDateJoined, lblDob, lblGender;
    public ImageView imgProfilePicture;
    private Button btnDelete, btnUpdate;
    private View iView;
    private Validation vd;
    public ClassUsers classUsers;
    ServerCustomer serverCustomer = new ServerCustomer();
    private byte[] imgByte;

    public FragmentProfile(HomeActivity homeActivity) {
        this.homeActivity = homeActivity;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        iView = inflater.inflate(R.layout.fragment_profile, container, false);

        txtName = iView.findViewById(R.id.txtName);
        txtEmail = iView.findViewById(R.id.txtEmail);
        txtPhone = iView.findViewById(R.id.txtPhone);
        txtNic = iView.findViewById(R.id.txtNic);
        txtAddress = iView.findViewById(R.id.txtAddress);
        txtPassword = iView.findViewById(R.id.txtPassword);
        txtConfirmPassword = iView.findViewById(R.id.txtConfirmPassword);
        btnUpdate = iView.findViewById(R.id.btnUpdate);
        btnDelete = iView.findViewById(R.id.btnDelete);
        lblUniqueId = iView.findViewById(R.id.lblUniqueId);
        lblDateJoined = iView.findViewById(R.id.lblDateJoined);
        lblDob = iView.findViewById(R.id.lblDob);
        lblGender = iView.findViewById(R.id.lblGender);
        imgProfilePicture = iView.findViewById(R.id.imgProfilePicture);

        vd = new Validation(getActivity());
        classUsers = new ClassUsers();

        serverCustomer.getUserDetails(this);

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity()).setTitle("Log Out Confirmation")
                        .setMessage("Are you sure you want to Sign Out?")
                        .setPositiveButton("YES",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        serverCustomer.deleteUser(FragmentProfile.this);
                                        dialog.dismiss();

                                    }
                                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
            }
        });

        imgProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGallery();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
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

        classUsers.setDob(vd.getBirthDate(txtNic.getText().toString().trim()));
        classUsers.setAddress(txtAddress.getText().toString().trim().replace("'", "''"));
        classUsers.setPassword(txtPassword.getText().toString().replace("'", "''"));

        if (!classUsers.getDefaultPicture().equals("Custom")){ // if profile is changed from default will not go inside
            classUsers.setDefaultPicture(gender);
            InputStream inputstream;
            if (gender.equals("Male"))
                inputstream = homeActivity.getResources().openRawResource(R.drawable.profile_male);
            else
                inputstream = homeActivity.getResources().openRawResource(R.drawable.profile_female);

            byte[] imgByte = new byte[0];

            try {
                imgByte = Utils.getBytes(inputstream);
            } catch (IOException e) {
                e.printStackTrace();
            }

            classUsers.setPicture(imgByte);
        }

//        System.out.println(classUsers.getName() + "\n" + classUsers.getEmail() + "\n" + classUsers.getPhone() + "\n" + classUsers.getNic() + "\n" + classUsers.getGender() + "\n" + classUsers.getDob() + "\n" + classUsers.getAddress() + "\n" + classUsers.getPassword() + "\n" + classUsers.getPicture());

        serverCustomer.updateUser(this);

    }

    public void populateFields() {
        txtName.setText(classUsers.getName());
        txtEmail.setText(classUsers.getEmail());
        txtPhone.setText(classUsers.getPhone() + "");
        txtNic.setText(classUsers.getNic());
        txtAddress.setText(classUsers.getAddress());
        txtPassword.setText(classUsers.getPassword());
        txtConfirmPassword.setText(classUsers.getPassword());

        lblUniqueId.setText(classUsers.getId() + "");
        lblDateJoined.setText(classUsers.getDateJoined().substring(0, 10));
        lblDob.setText(classUsers.getDob());
        lblGender.setText(classUsers.getGender());
        imgProfilePicture.setImageBitmap(Utils.getImage(classUsers.getPicture()));
        imgByte = classUsers.getPicture();
    }

    private void startGallery() {

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2000);
        } else {
            Intent cameraIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            cameraIntent.setType("image/*");
            if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivityForResult(cameraIntent, 1000);
            }
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            classUsers.setDefaultPicture("Custom");
            if (requestCode == 1000) {
                Uri returnUri = data.getData();
                Bitmap bitmapImage = null;
                try {

                    RotateBitmap rotateBitmap = new RotateBitmap();
                    bitmapImage = rotateBitmap.HandleSamplingAndRotationBitmap(getActivity(), returnUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                int quality = 20;

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    long fileSize = bitmapImage.getAllocationByteCount();
                    System.out.println(fileSize + " file");
                }

                imgByte = Utils.getImageBytes(bitmapImage, quality);
                classUsers.setPicture(imgByte);
                imgProfilePicture.setImageBitmap(Utils.getImage(imgByte));
            }
        }

    }
}
