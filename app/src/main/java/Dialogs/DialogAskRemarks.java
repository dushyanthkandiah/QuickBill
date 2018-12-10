package Dialogs;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import ServerLink.ServerProduct;
import pl.droidsonroids.gif.GifImageView;


@SuppressLint({"ValidFragment", "NewApi"})
public class DialogAskRemarks extends BaseDialogFragment<DialogAskRemarks.OnDialogFragmentClickListener> {

    public FragmentRequest fragmentRequest;
    private View iView;
    private EditText txtRemarks;
    private TextView lblNo, lblYes;
    String remarks = "";

    public static DialogAskRemarks newInstance(FragmentRequest fragmentRequest) {
        DialogAskRemarks dialogSelectProduct = new DialogAskRemarks(fragmentRequest);
        return dialogSelectProduct;
    }

    public DialogAskRemarks(FragmentRequest fragmentRequest) {
        this.fragmentRequest = fragmentRequest;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        iView = inflater.inflate(R.layout.dialog_remarks, container, false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().getAttributes().windowAnimations = R.style.ForDialogAnim;
        getDialog().setCanceledOnTouchOutside(true);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setRetainInstance(true);

        txtRemarks = iView.findViewById(R.id.txtRemarks);
        lblYes = iView.findViewById(R.id.lblYes);
        lblNo = iView.findViewById(R.id.lblNo);

        lblYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remarks = txtRemarks.getText().toString().trim().replace("'","''");
                fragmentRequest.remarks = remarks;
                fragmentRequest.serverRequest.createBill(fragmentRequest);
                dismiss();
            }
        });

        lblNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return iView;
    }



    public interface OnDialogFragmentClickListener {

    }
}
