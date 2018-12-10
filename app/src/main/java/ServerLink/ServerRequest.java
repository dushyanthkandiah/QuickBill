package ServerLink;

import android.app.Activity;
import android.view.View;

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

import Dialogs.DialogSelectProduct;
import Fragments.FragmentRequest;
import Models.ClassProducts;
import OtherClasses.OtherShortcuts;
import OtherClasses.SessionData;
import OtherClasses.ShowDialog;

/**
 * Created by Dushyanth on 2018-12-09.
 */

public class ServerRequest {

    public ServerRequest() {
    }

    public void createBill(final FragmentRequest fragmentRequest) {
        String requestName = "createBill";
        StringRequest stringRequest;
        final Activity activity = fragmentRequest.getActivity();
        String url = SessionData.serverUrl + "billing.php?func=" + requestName;

        fragmentRequest.homeActivity.showProgress();

        stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        fragmentRequest.homeActivity.hideProgress();
                        try {
                            System.out.println(response + " *********");
                                JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("response");
                            JSONObject explrObject = jsonArray.getJSONObject(0);

                            if (explrObject.get("queryResult").toString().equals("nodata")) {
                                ShowDialog.showToast(activity, "Error while Sending your Request");
                            } else if (explrObject.get("queryResult").toString().equals("failed")) {
                                ShowDialog.showToast(activity, "Connection not Available!");
                            } else if (explrObject.get("queryResult").toString().equals("success")) {
                                ShowDialog.showToast(activity, "Your Request was successfully submitted");
                                fragmentRequest.CancelBill();
                                OtherShortcuts.hideKeyboard(activity);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                fragmentRequest.homeActivity.hideProgress();
                ShowDialog.showToast(activity, "Connection not Available");

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                JSONObject JSONestimate = new JSONObject();
                JSONArray myarray = new JSONArray();
                for (int i = 0; i < fragmentRequest.list.size(); i++) {

                    try {
                        JSONestimate.put("data:" + String.valueOf(i + 1), fragmentRequest.list.get(i).getJSONObject());
                        myarray.put(fragmentRequest.list.get(i).getJSONObject());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                params.put("cart_items", myarray.toString());
                params.put("user_id", SessionData.userId);
                params.put("total", String.valueOf(fragmentRequest.total));
                params.put("remarks", fragmentRequest.remarks);
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
