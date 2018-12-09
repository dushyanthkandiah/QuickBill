package Dialogs;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


import com.example.dushyanth.quickbill.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Adapters.ProductViewAdapter;
import Fragments.FragmentRequest;
import Models.ClassProducts;
import OtherClasses.SessionData;
import OtherClasses.ShowDialog;
import ServerLink.ServerProduct;
import pl.droidsonroids.gif.GifImageView;


@SuppressLint({"ValidFragment", "NewApi"})
public class DialogSelectProduct extends BaseDialogFragment<DialogSelectProduct.OnDialogFragmentClickListener> implements SwipeRefreshLayout.OnRefreshListener {

    public FragmentRequest fragmentRequest;
    private View iView;
    private EditText txtSearch;
    public GifImageView progressBar;
    private RecyclerView rcyView;
    private ProductViewAdapter productViewAdapter;
    public ArrayList<ClassProducts> list;
    private LinearLayoutManager manager;
    private Spinner spinnerSelectType;
    private SwipeRefreshLayout swp2Rfsh;
    public String inputStr = "", type = "";
    public int page = 0;
    private Boolean isScrolling = false;
    private int currentItems, totalItems, scrollOutItems, check = 0;
    ServerProduct serverProduct = new ServerProduct();

    public static DialogSelectProduct newInstance(FragmentRequest fragmentRequest) {
        DialogSelectProduct dialogSelectProduct = new DialogSelectProduct(fragmentRequest);
        return dialogSelectProduct;
    }

    public DialogSelectProduct(FragmentRequest fragmentRequest) {
        this.fragmentRequest = fragmentRequest;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        iView = inflater.inflate(R.layout.dialog_select_product, container, false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().getAttributes().windowAnimations = R.style.ForDialogAnim;
        getDialog().setCanceledOnTouchOutside(true);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setRetainInstance(true);

        progressBar = iView.findViewById(R.id.progressBar);
        spinnerSelectType = iView.findViewById(R.id.spinnerSelectType);
        rcyView = iView.findViewById(R.id.rcyView);
        swp2Rfsh = iView.findViewById(R.id.swp2Rfsh);
        txtSearch = iView.findViewById(R.id.txtSearch);
        loadTypetArray();

        list = new ArrayList<>();

        manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        productViewAdapter = new ProductViewAdapter(list, this);
        rcyView.setAdapter(productViewAdapter);
        rcyView.setLayoutManager(manager);

        rcyView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                currentItems = manager.getChildCount();
                totalItems = manager.getItemCount();
                scrollOutItems = manager.findFirstVisibleItemPosition();

                if (isScrolling && (currentItems + scrollOutItems == totalItems)) {
                    isScrolling = false;

                    fetchData();

                }

            }
        });

        spinnerSelectType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                if (++check > 0) {
                    String selectedItemText = (String) parent.getItemAtPosition(position);

                    if (selectedItemText.equals("Detergent"))
                        type = "det";
                    else if (selectedItemText.equals("Stationary"))
                        type = "sta";
                    else
                        type = "";

                    ((TextView) spinnerSelectType.getSelectedView()).setTextColor(getResources().getColor(R.color.colorPrimary));

                    SearchData();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                inputStr = txtSearch.getText().toString().trim().replace("'", "''");
                SearchData();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        swp2Rfsh.setOnRefreshListener(this);
        swp2Rfsh.setColorSchemeResources(R.color.colorAccent, android.R.color.holo_green_dark, android.R.color.holo_orange_dark, android.R.color.holo_blue_dark);
        onRefresh();


        return iView;
    }


    @Override
    public void onRefresh() {
        isScrolling = false;
        page = 0;
        list.clear();
        fetchData();
    }

    public void SearchData() {
        page = 0;
        list.clear();
        fetchData();

    }

    public void fetchData() {
        progressBar.setVisibility(View.VISIBLE);
        productViewAdapter.notifyDataSetChanged();
        serverProduct.getProductData(this);

    }

    public void PopulateList() {
        productViewAdapter.notifyDataSetChanged();
        swp2Rfsh.setRefreshing(false);
        page += 1;
    }

    private void loadTypetArray() {
        String[] sortArray = new String[]{"All", "Detergent", "Stationary"};

        final List<String> sortArrayList = new ArrayList<>(Arrays.asList(sortArray));

        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, sortArrayList);

        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerSelectType.setAdapter(spinnerArrayAdapter);
    }


    public interface OnDialogFragmentClickListener {

    }
}
