package com.anura.anuramotors.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anura.anuramotors.Interface.IRecyclerItemSelectedListener;
import com.anura.anuramotors.R;
import com.anura.anuramotors.common.common;
import com.anura.anuramotors.model.Center;

import java.util.ArrayList;
import java.util.List;

public class MyCenterAdapter extends RecyclerView.Adapter<MyCenterAdapter.MyViewHolder> {

    Context context;
    List<Center> centerList;
    List<CardView> cardViewList;
    LocalBroadcastManager localBroadcastManager;

    public MyCenterAdapter(Context context, List<Center> centerList) {
        this.context = context;
        this.centerList = centerList;
        cardViewList = new ArrayList<>();
        localBroadcastManager = LocalBroadcastManager.getInstance(context);
    }

    @NonNull
    @Override
    public MyCenterAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.layout_center, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyCenterAdapter.MyViewHolder holder, int position) {
        holder.txt_center_name.setText(centerList.get(position).getName());
        holder.txt_center_address.setText(centerList.get(position).getAddress());

        if(!cardViewList.contains(holder.card_center))
            cardViewList.add(holder.card_center);

        holder.setiRecyclerItemSelectedListener(new IRecyclerItemSelectedListener() {
            @Override
            public void onItemsSelectedListener(View view, int pos) {
                for(CardView cardView:cardViewList)
                    cardView.setCardBackgroundColor(context.getResources().getColor(android.R.color.white));

                holder.card_center.setCardBackgroundColor(context.getResources()
                .getColor(android.R.color.holo_orange_dark));

                Intent intent = new Intent(common.KEY_ENABLE_BUTTON_NEXT);
                intent.putExtra(common.KEY_CENTER_STORE, centerList.get(pos));
                intent.putExtra(common.KEY_STEP,1);
                localBroadcastManager.sendBroadcast(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return centerList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txt_center_name, txt_center_address;
        CardView card_center;

        IRecyclerItemSelectedListener iRecyclerItemSelectedListener;

        public void setiRecyclerItemSelectedListener(IRecyclerItemSelectedListener iRecyclerItemSelectedListener) {
            this.iRecyclerItemSelectedListener = iRecyclerItemSelectedListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            card_center = (CardView)itemView.findViewById(R.id.card_center);
            txt_center_address = (TextView)itemView.findViewById(R.id.txt_center_address);
            txt_center_name = (TextView)itemView.findViewById(R.id.txt_center_name);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            iRecyclerItemSelectedListener.onItemsSelectedListener(v, getAdapterPosition());
        }
    }
}
