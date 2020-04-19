package com.anura.anuramotors.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anura.anuramotors.Interface.IRecyclerItemSelectedListener;
import com.anura.anuramotors.R;
import com.anura.anuramotors.common.common;
import com.anura.anuramotors.model.Mechanic;

import java.util.ArrayList;
import java.util.List;

public class MyMechanicAdapter extends RecyclerView.Adapter<MyMechanicAdapter.MyViewHolder> {

    Context context;
    List<Mechanic> mechanicList;
    List<CardView> cardViewList;
    LocalBroadcastManager localBroadcastManager;

    public MyMechanicAdapter(Context context, List<Mechanic> mechanicList) {
        this.context = context;
        this.mechanicList = mechanicList;
        cardViewList = new ArrayList<>();
        localBroadcastManager = LocalBroadcastManager.getInstance(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.layout_mechanic, parent, false);
        return new MyMechanicAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txt_barber_name.setText(mechanicList.get(position).getName());
        holder.ratingBar.setRating((float) mechanicList.get(position).getRating());
        if(!cardViewList.contains(holder.card_barber))
            cardViewList.add(holder.card_barber);

        holder.setiRecyclerItemSelectedListener(new IRecyclerItemSelectedListener() {
            @Override
            public void onItemsSelectedListener(View view, int pos) {
                for(CardView cardView: cardViewList)
                {
                    cardView.setCardBackgroundColor(context.getResources()
                    .getColor(android.R.color.white));
                }

                holder.card_barber.setCardBackgroundColor(
                        context.getResources()
                        .getColor(android.R.color.holo_orange_dark)
                );

                Intent intent = new Intent(common.KEY_ENABLE_BUTTON_NEXT);
                intent.putExtra(common.KEY_MECHANIC_SELECTED, mechanicList.get(pos));
                intent.putExtra(common.KEY_STEP, 2);
                localBroadcastManager.sendBroadcast(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mechanicList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_barber_name;
        RatingBar ratingBar;
        CardView card_barber;

        IRecyclerItemSelectedListener iRecyclerItemSelectedListener;

        public void setiRecyclerItemSelectedListener(IRecyclerItemSelectedListener iRecyclerItemSelectedListener) {
            this.iRecyclerItemSelectedListener = iRecyclerItemSelectedListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            card_barber = (CardView)itemView.findViewById(R.id.card_mechanic);
            txt_barber_name = (TextView)itemView.findViewById(R.id.txt_mechanic_name);
            ratingBar = (RatingBar)itemView.findViewById(R.id.rtb_mechanic);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            iRecyclerItemSelectedListener.onItemsSelectedListener(v, getAdapterPosition());
        }
    }
}
