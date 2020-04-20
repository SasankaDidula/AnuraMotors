package com.anura.anuramotors.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anura.anuramotors.R;
import com.anura.anuramotors.model.BookingInformation;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MyHistoryAdapter extends RecyclerView.Adapter<MyHistoryAdapter.MyViewHolder> {

    Context context;
    List <BookingInformation> bookingInformationList;

    public MyHistoryAdapter(Context context, List<BookingInformation> bookingInformationList) {
        this.context = context;
        this.bookingInformationList = bookingInformationList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_history, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.txt_booking_mechanic_text.setText(bookingInformationList.get(position).getMechanicName());
        holder.txt_booking_time_text.setText(bookingInformationList.get(position).getTime());
        holder.txt_center_address.setText(bookingInformationList.get(position).getCenterAddress());
        holder.txt_center_name.setText(bookingInformationList.get(position).getCenterName());
    }

    @Override
    public int getItemCount() {
        return bookingInformationList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        Unbinder unbinder;

        @BindView(R.id.txt_center_name)
        TextView txt_center_name;

        @BindView(R.id.txt_center_address)
        TextView txt_center_address;

        @BindView(R.id.txt_booking_time_text)
        TextView txt_booking_time_text;

        @BindView(R.id.txt_booking_mechanic_text)
        TextView txt_booking_mechanic_text;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this,itemView);
        }
    }
}
