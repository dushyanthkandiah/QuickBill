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

import Dialogs.DialogSelectProduct;
import Models.ClassProducts;


@RequiresApi(api = Build.VERSION_CODES.N)
@SuppressLint("NewApi")
public class ProductViewAdapter extends RecyclerView.Adapter<ProductViewAdapter.VHolder> {
    private ArrayList<ClassProducts> data;
    private DialogSelectProduct dialogSelectProduct;

    public ProductViewAdapter(ArrayList<ClassProducts> data, DialogSelectProduct dialogSelectProduct) {
        this.data = data;
        this.dialogSelectProduct = dialogSelectProduct;
    }

    @NonNull
    @Override
    public VHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(dialogSelectProduct.getActivity());
        View view = inflater.inflate(R.layout.rcyvw_product, parent, false);

        return new VHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull VHolder holder, final int position) {

        holder.lblName.setText("" + data.get(position).getName());
        holder.lblPrice.setText("" + data.get(position).getPrice());
        holder.lblType.setText("" + data.get(position).getType());
        holder.lblDetails.setText("Details : " + data.get(position).getDetails());

        if (position == data.size() - 1)
            holder.divider.setVisibility(View.GONE);
        else
            holder.divider.setVisibility(View.VISIBLE);

        holder.cardClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogSelectProduct.fragmentRequest.setProductData(data.get(position));
                dialogSelectProduct.dismiss();
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class VHolder extends RecyclerView.ViewHolder {
        TextView lblName, lblPrice, lblType, lblDetails;
        CardView cardClick;
        View divider;

        public VHolder(View itemView) {
            super(itemView);
            lblName = itemView.findViewById(R.id.lblName);
            lblPrice = itemView.findViewById(R.id.lblPrice);
            lblType = itemView.findViewById(R.id.lblType);
            lblDetails = itemView.findViewById(R.id.lblDetails);
            cardClick = itemView.findViewById(R.id.cardClick);
            divider = itemView.findViewById(R.id.divider);
        }
    }

}
