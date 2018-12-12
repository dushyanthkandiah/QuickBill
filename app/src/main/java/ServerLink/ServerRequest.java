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
import Fragments.FragmentRequestedItems;
import Fragments.FragmentViewRequests;
import Models.ClassProducts;
import Models.ClassRequest;
import Models.ClassRequestList;
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

    public void getBillData(final FragmentViewRequests fragmentViewRequests) {
        String requestName = "getBillData";
        StringRequest stringRequest;
        final Activity activity = fragmentViewRequests.getActivity();
        String url = SessionData.serverUrl + "billing.php?func=" + requestName;


        stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        fragmentViewRequests.homeActivity.hideProgress();
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

                                for (int i = 1; i < jsonArray.length(); i++) {

                                    explrObject = jsonArray.getJSONObject(i);

                                    fragmentViewRequests.list.add(new ClassRequest(
                                            Integer.parseInt(explrObject.get("req_id").toString()),
                                            Integer.parseInt(explrObject.get("user_id").toString()),
                                            Integer.parseInt(explrObject.get("status").toString()),
                                            explrObject.get("req_date").toString(),
                                            explrObject.get("remarks").toString(),
                                            Double.parseDouble(explrObject.get("total").toString())

                                    ));
                                }

                                fragmentViewRequests.PopulateList();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                fragmentViewRequests.homeActivity.hideProgress();
                ShowDialog.showToast(activity, "Connection not Available");

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", SessionData.userId);
                params.put("from_date", fragmentViewRequests.fromDate);
                params.put("to_date", fragmentViewRequests.toDate);
                params.put("status", fragmentViewRequests.status);
                params.put("limit", String.valueOf(fragmentViewRequests.page));

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

        public void getBilledItemData(final FragmentRequestedItems fragmentRequestedItems) {
        String requestName = "getBilledItemData";
        StringRequest stringRequest;
        final Activity activity = fragmentRequestedItems.getActivity();
        String url = SessionData.serverUrl + "billing.php?func=" + requestName;


        stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        fragmentRequestedItems.fragmentViewRequests.homeActivity.hideProgress();
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

                                for (int i = 1; i < jsonArray.length(); i++) {

                                    explrObject = jsonArray.getJSONObject(i);

                                    fragmentRequestedItems.list.add(new ClassRequestList(
                                            Integer.parseInt(explrObject.get("req_id").toString()),
                                            Integer.parseInt(explrObject.get("prd_id").toString()),
                                            explrObject.get("name").toString(),
                                            Double.parseDouble(explrObject.get("qty").toString()),
                                            Double.parseDouble(explrObject.get("sub_total").toString())

                                    ));
                                }

                                fragmentRequestedItems.PopulateList();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                fragmentRequestedItems.fragmentViewRequests.homeActivity.hideProgress();
                ShowDialog.showToast(activity, "Connection not Available");

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("req_id", String.valueOf(fragmentRequestedItems.fragmentViewRequests.requestId));
                params.put("limit", String.valueOf(fragmentRequestedItems.page));

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
