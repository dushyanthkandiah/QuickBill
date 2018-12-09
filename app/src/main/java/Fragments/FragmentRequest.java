package Fragments;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import Dialogs.DialogSelectProduct;
import Models.ClassItemCart;
import ServerLink.ServerRequest;

@SuppressLint({"ValidFragment", "NewApi"})
public class FragmentRequest extends Fragment {

    public HomeActivity homeActivity;
    private View iView;
    private RecyclerView recyclerViewCartItems;
    private ItemCartAdapter itemCartAdapter;
    private ArrayList<ClassItemCart> list;
    private LinearLayoutManager manager;
    private TextView lblItemSubTotal, lblTotal;
    private EditText txtQuantity, txtItemName;
    private Button btnAddToList, btnPlaceRequest, btnCancel;
    private String itemName = "";
    private Double price = 0.0, stock = 0.0, total = 0.0;
    private Double quantity = 0.0;
    private int itemId = 0;
    ServerRequest serverRequest;

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

        return iView;
    }

}
