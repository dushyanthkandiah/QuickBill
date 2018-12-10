package Fragments;


import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.dushyanth.quickbill.HomeActivity;
import com.example.dushyanth.quickbill.R;

import java.util.ArrayList;

import Adapters.ItemCartAdapter;
import Dialogs.DialogAskRemarks;
import Dialogs.DialogSelectProduct;
import Models.ClassItemCart;
import Models.ClassProducts;
import OtherClasses.ShowDialog;
import ServerLink.ServerRequest;

@SuppressLint({"ValidFragment", "NewApi"})
public class FragmentRequest extends Fragment {

    public HomeActivity homeActivity;
    private View iView;
    private RecyclerView recyclerViewCartItems;
    private ItemCartAdapter itemCartAdapter;
    public ArrayList<ClassItemCart> list;
    private LinearLayoutManager manager;
    private TextView lblItemSubTotal, lblTotal;
    private EditText txtQuantity, txtItemName;
    private Button btnAddToList, btnPlaceRequest, btnCancel;
    public String itemName = "", remarks = "";
    public Double price = 0.0, total = 0.0;
    private Double quantity = 0.0;
    private int itemId = 0;
    public ServerRequest serverRequest;

    public FragmentRequest(HomeActivity homeActivity) {
        this.homeActivity = homeActivity;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        iView = inflater.inflate(R.layout.fragment_request, container, false);

        recyclerViewCartItems = iView.findViewById(R.id.recyclerViewCartItems);
        txtItemName = iView.findViewById(R.id.txtItemName);
        btnAddToList = iView.findViewById(R.id.btnAddToList);
        txtQuantity = iView.findViewById(R.id.txtQuantity);
        lblItemSubTotal = iView.findViewById(R.id.lblItemSubTotal);
        lblTotal = iView.findViewById(R.id.lblTotal);
        btnPlaceRequest = iView.findViewById(R.id.btnPlaceRequest);
        btnCancel = iView.findViewById(R.id.btnCancel);

        list = new ArrayList<>();

        manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        itemCartAdapter = new ItemCartAdapter(list, this);
        recyclerViewCartItems.setAdapter(itemCartAdapter);
        recyclerViewCartItems.setLayoutManager(manager);
        serverRequest = new ServerRequest();

        txtItemName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogSelectProduct dialogSelectProduct = DialogSelectProduct.newInstance(FragmentRequest.this);
                dialogSelectProduct.show(getFragmentManager(), "dialog");
            }
        });

        btnAddToList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateFields();
            }
        });


        btnPlaceRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!list.isEmpty()) {

                    DialogAskRemarks dialogAskRemarks = DialogAskRemarks.newInstance(FragmentRequest.this);
                    dialogAskRemarks.show(getFragmentManager(), "dialog");

                } else
                    ShowDialog.showToast(getActivity(), "Please add some items to the list");
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity()).setTitle("Cancelling the Request")
                        .setMessage("Are you sure you want to cancel this Request?")
                        .setPositiveButton("YES",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        CancelBill();
                                        dialog.dismiss();

                                    }
                                })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();
                            }
                        }).create().show();

            }
        });

        txtQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (txtQuantity.getText().toString().equals("0")) {
                    txtQuantity.setText("1");
                }

                try {
                    quantity = Double.valueOf(txtQuantity.getText().toString());
                } catch (NumberFormatException e) {
                    quantity = 0.0;
                }

                lblItemSubTotal.setText((quantity * price) + "");

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return iView;
    }

    private void ValidateFields() {

        if (itemId == 0) {
            ShowDialog.showToastLong(getActivity(), "Please select a Product to add to the cart");
        } else if (txtQuantity.getText().toString().equals("")) {
            ShowDialog.showToast(getActivity(), "Please Enter some quantity");
        } else {

            addToCart();

        }

    }

    private void addToCart() {
        boolean flagItemCheck = true;

        /******* checking if product already exists ******/

        for (int i = 0; i < list.size(); i++) {

            if (list.get(i).getPrdId() == itemId) {
                flagItemCheck = false;
                Double tmpQty = quantity + list.get(i).getQuantity();
                Double tmpSbTtl = tmpQty * price;

                list.set(i, new ClassItemCart(
                        list.get(i).getPrdId(),
                        tmpQty,
                        tmpSbTtl,
                        list.get(i).getItemName()
                ));

            }
        }

        /**************/

        if (flagItemCheck) {
            ClassItemCart classItemCart = new ClassItemCart(itemId, quantity, price * quantity, itemName);
            list.add(classItemCart);

        }
        itemCartAdapter.notifyDataSetChanged();
        clearCart();

        /******* getting the final total ****************/

        total = 0.0;
        for (int i = 0; i < list.size(); i++) {
            total += list.get(i).getSubTotal();
        }

        lblTotal.setText("Total = Rs. " + total);

        /**************/


    }

    public void CancelBill() {
        clearCart();
        list.clear();
        itemCartAdapter.notifyDataSetChanged();
        lblTotal.setText("Total = Rs. 0.00");
        this.total = 0.0;
    }

    private void clearCart() {
        this.itemId = 0;
        this.itemName = "";
        this.price = 0.0;
        txtQuantity.setText("");
        txtQuantity.setHint("Quantity");
        txtItemName.setText("");
        txtItemName.setHint("Select a Product");
        lblItemSubTotal.setText("0.00");
    }

    public void setProductData(ClassProducts classProducts) {
        this.itemId = classProducts.getId();
        this.itemName = classProducts.getName();
        this.price = classProducts.getPrice();
        txtItemName.setText(itemName + "");
        txtQuantity.requestFocus();
    }
}
