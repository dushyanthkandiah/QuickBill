package Fragments;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dushyanth.quickbill.HomeActivity;
import com.example.dushyanth.quickbill.R;

@SuppressLint({"ValidFragment", "ResourceType"})
public class FragmentRequest extends Fragment {

    public HomeActivity homeActivity;
    private View iView;

    public FragmentRequest(HomeActivity homeActivity) {
this.homeActivity = homeActivity;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        iView= inflater.inflate(R.layout.fragment_request, container, false);



        return iView;
    }

}
