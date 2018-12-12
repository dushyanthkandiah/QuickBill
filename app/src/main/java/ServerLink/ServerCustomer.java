package ServerLink;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.util.Base64;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.dushyanth.quickbill.HomeActivity;
import com.example.dushyanth.quickbill.MySingleton;
import com.example.dushyanth.quickbill.R;
import com.example.dushyanth.quickbill.StartUpActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import Fragments.FragmentLogin;
import Fragments.FragmentProfile;
import Fragments.FragmentSignUp;
import Models.ClassUsers;
import OtherClasses.SessionData;
import OtherClasses.ShowDialog;
import OtherClasses.Utils;


public class ServerCustomer {

    public ServerCustomer() {

    }

    public void checkLogin(final FragmentLogin fragmentLogin) {
        String requestName = "customerSignIn";
        StringRequest stringRequest;
        String url = SessionData.serverUrl + "user_sign_in.php?func=validateUser";

        if (!fragmentLogin.pDialog.isShowing()) fragmentLogin.pDialog.show();

        stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (fragmentLogin.pDialog.isShowing()) fragmentLogin.pDialog.dismiss();
                        try {
                            System.out.println(response + " *********");
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("response");
                            JSONObject explrObject = jsonArray.getJSONObject(0);

                            if (explrObject.get("queryResult").toString().equals("nodata")) {
                                if (fragmentLogin.usernameType.equals("phone"))
                                    ShowDialog.showToast(fragmentLogin.getActivity(), "Please check your Phone/Password correctly");
                                else
                                    ShowDialog.showToast(fragmentLogin.getActivity(), "Please check your Email/Password correctly");

                            } else if (explrObject.get("queryResult").toString().equals("failed")) {
                                ShowDialog.showToast(fragmentLogin.getActivity(), "Connection not Available!");
                            } else if (explrObject.get("queryResult").toString().equals("success")) {

                                explrObject = jsonArray.getJSONObject(1);
                                SessionData.userId = explrObject.get("user_id").toString();
                                SessionData.userName = explrObject.get("name").toString();
                                SessionData.userregisteredDate = explrObject.get("date_joined").toString();

                                fragmentLogin.GotoHome();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (fragmentLogin.pDialog.isShowing()) fragmentLogin.pDialog.dismiss();
                ShowDialog.showToast(fragmentLogin.getActivity(), "Connection not Available");

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", fragmentLogin.username);
                params.put("password", fragmentLogin.password);
                params.put("usernameType", fragmentLogin.usernameType);
                params.put("userType", "cus");
                return params;
            }
        };

        stringRequest.setTag(requestName);

        try {
            MySingleton.getmInstance(fragmentLogin.getActivity()).cancelRequest(requestName);
        } catch (Exception e) {
        }

        MySingleton.getmInstance(fragmentLogin.getActivity()).addToRequestQueue(stringRequest);

    }

    public void checkUserValidity(final HomeActivity homeActivity) {
        String requestName = "checkUserValidity";
        StringRequest stringRequest;
        final Context activity = homeActivity.getApplicationContext();
        String url = SessionData.serverUrl + "user_sign_in.php?func=" + requestName;

        if (!homeActivity.pDialog.isShowing()) homeActivity.pDialog.show();

        stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (homeActivity.pDialog.isShowing()) homeActivity.pDialog.dismiss();
                        try {
                            System.out.println(response + " *********");
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("response");
                            JSONObject explrObject = jsonArray.getJSONObject(0);

                            if (explrObject.get("queryResult").toString().equals("nodata")) {

                                new AlertDialog.Builder(homeActivity).setTitle("Account Blocked")
                                        .setMessage("Your account has been blocked by your vendor")
                                        .setPositiveButton("OK",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        SharedPreferences myPrefs = homeActivity.getSharedPreferences(SessionData.PREFS_LOGIN, 0);
                                                        SharedPreferences.Editor editor = myPrefs.edit();
                                                        editor.putString("userId", "");
                                                        editor.putString("userName", "");
                                                        editor.commit();

                                                        homeActivity.startActivity(new Intent(homeActivity, StartUpActivity.class));
                                                        homeActivity.overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
                                                        homeActivity.finish();

                                                        dialog.dismiss();

                                                    }
                                                }).create().show();

                            } else if (explrObject.get("queryResult").toString().equals("failed")) {
                                ShowDialog.showToast(activity, "Connection not Available!");
                            } else if (explrObject.get("queryResult").toString().equals("success")) {

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (homeActivity.pDialog.isShowing()) homeActivity.pDialog.dismiss();
                ShowDialog.showToast(activity, "Connection not Available");

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", SessionData.userId);
                return params;
            }
        };

        stringRequest.setTag(requestName);

        try {
            MySingleton.getmInstance(homeActivity).cancelRequest(requestName);
        } catch (Exception e) {
        }

        MySingleton.getmInstance(homeActivity).addToRequestQueue(stringRequest);

    }

    public void createUser(final FragmentSignUp fragmentSignUp) {
        String requestName = "createUser";
        final Activity activity = fragmentSignUp.getActivity();
        StringRequest stringRequest;
        String url = SessionData.serverUrl + "user_sign_in.php?func=" + requestName;

        if (!fragmentSignUp.pDialog.isShowing()) fragmentSignUp.pDialog.show();

        stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (fragmentSignUp.pDialog.isShowing()) fragmentSignUp.pDialog.dismiss();
                        try {
                            System.out.println(response + " *********");
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("response");
                            JSONObject explrObject = jsonArray.getJSONObject(0);

                            if (explrObject.get("queryResult").toString().equals("error")) {
                                ShowDialog.showToast(activity, "Error while Creating your account");
                            } else if (explrObject.get("queryResult").toString().equals("failed")) {
                                ShowDialog.showToast(activity, "Connection not Available!");
                            } else if (explrObject.get("queryResult").toString().equals("success")) {
                                ShowDialog.showToast(activity, "Your account created successfully");
                                fragmentSignUp.GotoLogin();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (fragmentSignUp.pDialog.isShowing()) fragmentSignUp.pDialog.dismiss();
                ShowDialog.showToast(activity, "Connection not Available");

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                ClassUsers classUsers = fragmentSignUp.classUsers;

                String encodedImage = Base64.encodeToString(classUsers.getPicture(), Base64.DEFAULT);

                params.put("name", classUsers.getName());
                params.put("email", classUsers.getEmail());
                params.put("phone", String.valueOf(classUsers.getPhone()));
                params.put("nic", classUsers.getNic());
                params.put("gender", classUsers.getGender());
                params.put("default_picture", classUsers.getDefaultPicture());
                params.put("dob", classUsers.getDob());
                params.put("address", classUsers.getAddress());
                params.put("picture", encodedImage);
                params.put("password", classUsers.getPassword());
                params.put("designation", "cus");

//                System.out.println(params);
                return params;
            }
        };

        stringRequest.setTag(requestName);

        try {
            MySingleton.getmInstance(activity).cancelRequest(requestName);
        } catch (Exception e) {
        }

        MySingleton.getmInstance(activity).addToRequestQueue(stringRequest);

    }

    public void updateUser(final FragmentProfile fragmentProfile) {
        String requestName = "updateUser";
        final Activity activity = fragmentProfile.getActivity();
        StringRequest stringRequest;
        String url = SessionData.serverUrl + "user_sign_in.php?func=" + requestName;

        fragmentProfile.homeActivity.showProgress();

        stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        fragmentProfile.homeActivity.hideProgress();
                        try {
                            System.out.println(response + " *********");
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("response");
                            JSONObject explrObject = jsonArray.getJSONObject(0);

                            if (explrObject.get("queryResult").toString().equals("error")) {
                                ShowDialog.showToast(activity, "Error while Updating your Account");
                            } else if (explrObject.get("queryResult").toString().equals("failed")) {
                                ShowDialog.showToast(activity, "Connection not Available!");
                            } else if (explrObject.get("queryResult").toString().equals("success")) {
                                ShowDialog.showToast(activity, "Your Account Updated Successfully");
                                getUserDetails(fragmentProfile);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                fragmentProfile.homeActivity.hideProgress();
                ShowDialog.showToast(activity, "Connection not Available");

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                ClassUsers classUsers = fragmentProfile.classUsers;

                String encodedImage = Base64.encodeToString(classUsers.getPicture(), Base64.DEFAULT);

                params.put("user_id", SessionData.userId);
                params.put("name", classUsers.getName());
                params.put("email", classUsers.getEmail());
                params.put("phone", String.valueOf(classUsers.getPhone()));
                params.put("nic", classUsers.getNic());
                params.put("gender", classUsers.getGender());
                params.put("default_picture", classUsers.getDefaultPicture());
                params.put("dob", classUsers.getDob());
                params.put("address", classUsers.getAddress());
                params.put("picture", encodedImage);
                params.put("password", classUsers.getPassword());
                params.put("designation", "cus");
                params.put("picture_path", classUsers.getPicturePath());

//                System.out.println(params);
                return params;
            }
        };

        stringRequest.setTag(requestName);

        try {
            MySingleton.getmInstance(activity).cancelRequest(requestName);
        } catch (Exception e) {
        }

        MySingleton.getmInstance(activity).addToRequestQueue(stringRequest);

    }

    public void deleteUser(final FragmentProfile fragmentProfile) {
        String requestName = "deleteUser";
        final Activity activity = fragmentProfile.getActivity();
        StringRequest stringRequest;
        String url = SessionData.serverUrl + "user_sign_in.php?func=" + requestName;

        fragmentProfile.homeActivity.showProgress();

        stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        fragmentProfile.homeActivity.hideProgress();
                        try {
                            System.out.println(response + " *********");
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("response");
                            JSONObject explrObject = jsonArray.getJSONObject(0);

                            if (explrObject.get("queryResult").toString().equals("error")) {
                                ShowDialog.showToast(activity, "Error while Deleting your Account");
                            } else if (explrObject.get("queryResult").toString().equals("failed")) {
                                ShowDialog.showToast(activity, "Connection not Available!");
                            } else if (explrObject.get("queryResult").toString().equals("success")) {
                                ShowDialog.showToast(activity, "Your Account Deleted Successfully");
                                fragmentProfile.homeActivity.logOut();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                fragmentProfile.homeActivity.hideProgress();
                ShowDialog.showToast(activity, "Connection not Available");

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", SessionData.userId);

//                System.out.println(params);
                return params;
            }
        };

        stringRequest.setTag(requestName);

        try {
            MySingleton.getmInstance(activity).cancelRequest(requestName);
        } catch (Exception e) {
        }

        MySingleton.getmInstance(activity).addToRequestQueue(stringRequest);

    }

    public void getUserDetails(final FragmentProfile fragmentProfile) {
        String requestName = "getUserDetails";
        StringRequest stringRequest;
        final Activity activity = fragmentProfile.getActivity();
        String url = SessionData.serverUrl + "user_sign_in.php?func=" + requestName;

        fragmentProfile.homeActivity.showProgress();

        stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        fragmentProfile.homeActivity.hideProgress();
                        try {
                            System.out.println(response + " *********");
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("response");
                            JSONObject explrObject = jsonArray.getJSONObject(0);

                            if (explrObject.get("queryResult").toString().equals("nodata")) {
                                ShowDialog.showToast(activity, "No Records Found");
                            } else if (explrObject.get("queryResult").toString().equals("failed")) {
                                ShowDialog.showToast(activity, "Connection not Available!");
                            } else if (explrObject.get("queryResult").toString().equals("success")) {

                                explrObject = jsonArray.getJSONObject(1);

                                byte[] bytes = Base64.decode(explrObject.get("photo_byte").toString(), Base64.DEFAULT);

                                fragmentProfile.classUsers.setName(explrObject.get("name").toString());
                                fragmentProfile.classUsers.setEmail(explrObject.get("email").toString());
                                fragmentProfile.classUsers.setPhone(Long.parseLong(explrObject.get("phone").toString()));
                                fragmentProfile.classUsers.setNic(explrObject.get("nic").toString());
                                fragmentProfile.classUsers.setAddress(explrObject.get("address").toString());
                                fragmentProfile.classUsers.setPassword(explrObject.get("password").toString());
                                fragmentProfile.classUsers.setId(Integer.parseInt(explrObject.get("user_id").toString()));
                                fragmentProfile.classUsers.setDateJoined(explrObject.get("date_joined").toString());
                                fragmentProfile.classUsers.setDob(explrObject.get("dob").toString());
                                fragmentProfile.classUsers.setGender(explrObject.get("gender").toString());
                                fragmentProfile.classUsers.setDefaultPicture(explrObject.get("default_picture").toString());
                                fragmentProfile.classUsers.setPicture(bytes);
                                fragmentProfile.classUsers.setPicturePath(explrObject.get("picture").toString());

                                fragmentProfile.populateFields();

                                SessionData.userName = explrObject.get("name").toString();


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                fragmentProfile.homeActivity.hideProgress();
                ShowDialog.showToast(activity, "Connection not Available");

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", SessionData.userId);
                return params;
            }
        };

        stringRequest.setTag(requestName);

        try {
            MySingleton.getmInstance(activity).cancelRequest(requestName);
        } catch (Exception e) {
        }

        MySingleton.getmInstance(activity).addToRequestQueue(stringRequest);

    }

}
