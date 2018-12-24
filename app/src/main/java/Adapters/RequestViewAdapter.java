package Adapters;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
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
import OtherClasses.SessionData;


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
            holder.cardCancelRequest.setVisibility(View.VISIBLE);
        } else if (data.get(position).getStatus() == 1) {
            holder.lblStatus.setText("Cleared");
            holder.cardCancelRequest.setVisibility(View.GONE);
        } else if (data.get(position).getStatus() == 2) {
            holder.lblStatus.setText("Cancel");
            holder.cardCancelRequest.setVisibility(View.GONE);
        }

        if (position == data.size() - 1)
            holder.divider.setVisibility(View.GONE);
        else
            holder.divider.setVisibility(View.VISIBLE);

        holder.cardClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionData.classRequest = data.get(position);
                fragmentViewRequests.homeActivity.changeFragment("view_requested_item");
            }
        });

        holder.cardCancelRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                fragmentViewRequests.serverRequest.cancelRequest(fragmentViewRequests, data.get(position).getReqId());
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:

                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(fragmentViewRequests.getActivity());
                builder.setMessage("Are you sure you want to cancel this bill?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
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
        CardView cardClick, cardCancelRequest;

        public VHolder(View itemView) {
            super(itemView);
            lblItemId = itemView.findViewById(R.id.lblItemId);
            lblRequestDate = itemView.findViewById(R.id.lblRequestDate);
            lblRequestTotal = itemView.findViewById(R.id.lblRequestTotal);
            lblStatus = itemView.findViewById(R.id.lblStatus);
            lblRemarks = itemView.findViewById(R.id.lblRemarks);
            divider = itemView.findViewById(R.id.divider);
            cardClick = itemView.findViewById(R.id.cardClick);
            cardCancelRequest = itemView.findViewById(R.id.cardCancelRequest);
        }
    }

}
