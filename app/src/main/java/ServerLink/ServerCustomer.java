package ServerLink;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.dushyanth.quickbill.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import Fragments.FragmentLogin;
import OtherClasses.SessionData;
import OtherClasses.ShowDialog;


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

}
