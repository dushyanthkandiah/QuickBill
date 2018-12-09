package ServerLink;

import android.app.Activity;
import android.util.Base64;
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
import Fragments.FragmentProfile;
import Models.ClassProducts;
import OtherClasses.SessionData;
import OtherClasses.ShowDialog;

/**
 * Created by Dushyanth on 2018-12-09.
 */

public class ServerProduct {

    public ServerProduct() {
    }

    public void getProductData(final DialogSelectProduct dialogSelectProduct) {
        String requestName = "getProductData";
        StringRequest stringRequest;
        final Activity activity = dialogSelectProduct.getActivity();
        String url = SessionData.serverUrl + "products.php?func=" + requestName;


        stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialogSelectProduct.progressBar.setVisibility(View.INVISIBLE);
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

                                    dialogSelectProduct.list.add(new ClassProducts(
                                            Integer.parseInt(explrObject.get("prd_id").toString()),
                                            Integer.parseInt(explrObject.get("pages").toString()),
                                            explrObject.get("name").toString(),
                                            explrObject.get("type").toString(),
                                            Double.parseDouble(explrObject.get("litres").toString()),
                                            Double.parseDouble(explrObject.get("quantity").toString()),
                                            Double.parseDouble(explrObject.get("price").toString())

                                    ));
                                }
                                dialogSelectProduct.PopulateList();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialogSelectProduct.progressBar.setVisibility(View.INVISIBLE);
                ShowDialog.showToast(activity, "Connection not Available");

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("search_input", dialogSelectProduct.inputStr);
                params.put("limit", String.valueOf(dialogSelectProduct.page));
                params.put("type", dialogSelectProduct.type);
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
