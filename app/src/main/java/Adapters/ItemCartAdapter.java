package Adapters;

import android.annotation.SuppressLint;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dushyanth.quickbill.R;

import java.util.ArrayList;

import Fragments.FragmentRequest;
import Models.ClassItemCart;


@RequiresApi(api = Build.VERSION_CODES.N)
@SuppressLint("NewApi")
public class ItemCartAdapter extends RecyclerView.Adapter<ItemCartAdapter.VHolder> {
    private ArrayList<ClassItemCart> data;
    private FragmentRequest fragmentRequest;

    public ItemCartAdapter(ArrayList<ClassItemCart> data, FragmentRequest fragmentRequest) {
        this.data = data;
        this.fragmentRequest = fragmentRequest;
    }

    @NonNull
    @Override
    public VHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(fragmentRequest.getActivity());
        View view = inflater.inflate(R.layout.rcyvw_item_cart, parent, false);

        return new VHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VHolder holder, int position) {
        holder.lblItemId.setText((position + 1) + "");
        holder.lblItemName.setText(data.get(position).getItemName() + "");
        holder.lblItemQty.setText(data.get(position).getQuantity() + "");
        holder.lblItemSubTotal.setText(data.get(position).getSubTotal() + "");
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public class VHolder extends RecyclerView.ViewHolder {
        TextView lblItemId, lblItemName, lblItemQty, lblItemSubTotal;

        public VHolder(View itemView) {
            super(itemView);
            lblItemId = itemView.findViewById(R.id.lblItemId);
            lblItemName = itemView.findViewById(R.id.lblItemName);
            lblItemQty = itemView.findViewById(R.id.lblItemQty);
            lblItemSubTotal = itemView.findViewById(R.id.lblItemSubTotal);
        }
    }

}
