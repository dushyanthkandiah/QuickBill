package Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dushyanth.quickbill.HomeActivity;
import com.example.dushyanth.quickbill.R;


public class FragmentViewRequests extends Fragment {


    public FragmentViewRequests(HomeActivity homeActivity) {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_requests, container, false);
    }

}
