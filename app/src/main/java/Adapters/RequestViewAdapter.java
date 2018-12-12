package Adapters;

import android.annotation.SuppressLint;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dushyanth.quickbill.R;

import java.util.ArrayList;

import Fragments.FragmentViewRequests;
import Models.ClassRequest;


@RequiresApi(api = Build.VERSION_CODES.N)
@SuppressLint("NewApi")
public class RequestViewAdapter extends RecyclerView.Adapter<RequestViewAdapter.VHolder> {
    private ArrayList<ClassRequest> data;
    private FragmentViewRequests fragmentViewRequests;

    public RequestViewAdapter(ArrayList<ClassRequest> data, FragmentViewRequests fragmentViewRequests) {
        this.data = data;
        this.fragmentViewRequests = fragmentViewRequests;
    }

    @NonNull
    @Override
    public VHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(fragmentViewRequests.getActivity());
        View view = inflater.inflate(R.layout.rcyvw_view_requests, parent, false);

        return new VHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VHolder holder, final int position) {
        holder.lblItemId.setText(data.get(position).getReqId() + "");
        holder.lblRequestDate.setText(data.get(position).getReqDate() + "");
        holder.lblRequestTotal.setText(data.get(position).getTotal() + "");
        holder.lblRemarks.setText("Remarks : " + data.get(position).getRemarks());


        if (data.get(position).getStatus() == 0) {
            holder.lblStatus.setText("Pending");
        } else {
            holder.lblStatus.setText("Cleared");
        }

        if (position == data.size() - 1)
            holder.divider.setVisibility(View.GONE);
        else
            holder.divider.setVisibility(View.VISIBLE);

        holder.cardClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentViewRequests.callRequestList(data.get(position).getReqId(), data.get(position).getTotal());
            }
        });
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public class VHolder extends RecyclerView.ViewHolder {
        TextView lblItemId, lblRequestDate, lblRequestTotal, lblStatus, lblRemarks;
        View divider;
        CardView cardClick;

        public VHolder(View itemView) {
            super(itemView);
            lblItemId = itemView.findViewById(R.id.lblItemId);
            lblRequestDate = itemView.findViewById(R.id.lblRequestDate);
            lblRequestTotal = itemView.findViewById(R.id.lblRequestTotal);
            lblStatus = itemView.findViewById(R.id.lblStatus);
            lblRemarks = itemView.findViewById(R.id.lblRemarks);
            divider = itemView.findViewById(R.id.divider);
            cardClick = itemView.findViewById(R.id.cardClick);
        }
    }

}
