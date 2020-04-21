package com.anura.anuramotors.fragmnets;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.anura.anuramotors.BookingActivity;
import com.anura.anuramotors.HistoryActivity;
import com.anura.anuramotors.Interface.IBannerLoadListener;
import com.anura.anuramotors.Interface.IBookingInfoLoadListener;
import com.anura.anuramotors.Interface.IBookingInformationChangeListener;
import com.anura.anuramotors.Interface.ILookBookLoadListener;
import com.anura.anuramotors.MainActivity;
import com.anura.anuramotors.R;
import com.anura.anuramotors.adapter.HomeSlidesAdapter;
import com.anura.anuramotors.adapter.LookBookAdapter;
import com.anura.anuramotors.common.common;
import com.anura.anuramotors.model.Banner;
import com.anura.anuramotors.model.BookingInformation;
import com.anura.anuramotors.service.PicassoImageLoadingService;
import com.anura.anuramotors.userDetails;
import com.anura.anuramotors.userRegistration;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dmax.dialog.SpotsDialog;
import io.paperdb.Paper;
import ss.com.bannerslider.Slider;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements ILookBookLoadListener, IBannerLoadListener, IBookingInfoLoadListener, IBookingInformationChangeListener {

    private Unbinder unbinder;

    AlertDialog dialog;

    @BindView(R.id.layout_user_information)
    LinearLayout layout_user_information;

    @BindView(R.id.txt_user_name)
    TextView txt_user_name;

    @OnClick(R.id.txt_user_name)
    void user_details(){
        startActivity(new Intent(getActivity(), userDetails.class));
    }

    @BindView(R.id.banner_slider)
    Slider banner_slider;

    @BindView(R.id.recycler_look_book)
    RecyclerView recycler_look_book;

    @BindView(R.id.card_booking_info)
    CardView card_booking_info;

    @BindView(R.id.txt_center_address)
    TextView txt_center_address;

    @BindView(R.id.txt_center_mechanic)
    TextView txt_center_mechanic;

    @BindView(R.id.txt_time)
    TextView txt_time;

    @BindView(R.id.txt_time_remain)
    TextView txt_time_remain;

    @OnClick(R.id.btn_delete_booking)
    void deleteBooking()
    {
        deleteBookingFrommechanic(false);
    }

    @OnClick(R.id.btn_change_booking)
    void changeBooking(){
        changeBookingFromUser();
    }

    private void changeBookingFromUser() {
        androidx.appcompat.app.AlertDialog.Builder confirmDialog = new androidx.appcompat.app.AlertDialog.Builder(getActivity())
                .setCancelable(false)
                .setTitle("Hey!")
                .setMessage("Do you really want to change booking informaation?\nBecause we will delete your old booking information\nJust confirm")
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteBookingFrommechanic(true);
                    }
                });
        confirmDialog.show();
    }

    private void deleteBookingFrommechanic(boolean isChange) {

        if(common.currentBooking != null)
        {
            dialog.show();
            DocumentReference mechanicBookingInfo = FirebaseFirestore.getInstance()
                    .collection("AllCenter")
                    .document(common.currentBooking.getCityBook())
                    .collection("Branch")
                    .document(common.currentBooking.getCenterId())
                    .collection("Mechanics")
                    .document(common.currentBooking.getMechanicId())
                    .collection(common.covertTimeStampToStringKey(common.currentBooking.getTimestamp()))
                    .document(common.currentBooking.getSlot().toString());

            mechanicBookingInfo.delete().addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    deleteBookingFromUser(isChange);
                }
            });
        }
        else
        {
            Toast.makeText(getContext(), "Current Booking must not be null", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteBookingFromUser(boolean isChange) {
        if(!TextUtils.isEmpty(common.currentBookingId))
        {
            DocumentReference userBookingInfo = FirebaseFirestore.getInstance()
                    .collection("User")
                    .document(common.currentUser.getPhoneNumber())
                    .collection("Booking")
                    .document(common.currentBookingId);
            
            userBookingInfo.delete().addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Paper.init(getActivity());
                    Uri eventUri = Uri.parse(Paper.book().read(common.EVENT_URI_CACHE).toString());
                    getActivity().getContentResolver().delete(eventUri, null, null);

                    Toast.makeText(getActivity(), "Success delete booking !", Toast.LENGTH_SHORT).show();

                    loadUserBooking();

                    if(isChange)
                        iBookingInformationChangeListener.onBookingInformationChange();
                    dialog.dismiss();
                }
            });
        }
        else
        {
            dialog.dismiss();
            Toast.makeText(getContext(), "Booking information ID must not be empty", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.card_view_booking)
    void booking()
    {
        startActivity(new Intent(getActivity(), BookingActivity.class));
    }

    @OnClick(R.id.card_view_history)
    void openHistoryActivity()
    {
        startActivity(new Intent(getActivity(), HistoryActivity.class));
    }

    CollectionReference bannerRef, lookbookRef;

    IBannerLoadListener iBannerLoadListener;
    ILookBookLoadListener iLookBookLoadListener;
    IBookingInfoLoadListener iBookingInfoLoadListener;
    IBookingInformationChangeListener iBookingInformationChangeListener;

    public HomeFragment() {
        bannerRef = FirebaseFirestore.getInstance().collection("Banner");
        lookbookRef = FirebaseFirestore.getInstance().collection("Lookbook");
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUserBooking();
    }

    private void loadUserBooking() {
        CollectionReference userBooking = FirebaseFirestore.getInstance()
                .collection("User")
                .document(common.currentUser.getPhoneNumber())
                .collection("Booking");

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 0);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);

        Timestamp toDayTimeStamp = new Timestamp(calendar.getTime());
        
        userBooking
                .whereGreaterThanOrEqualTo("timestamp", toDayTimeStamp)
                .whereEqualTo("done", false)
                .limit(1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if(task.isSuccessful())
                        {
                            if(!task.getResult().isEmpty())
                            {
                                for(QueryDocumentSnapshot queryDocumentSnapshot:task.getResult())
                                {
                                    BookingInformation bookingInformation = queryDocumentSnapshot.toObject(BookingInformation.class);
                                    iBookingInfoLoadListener.onBookInfoLoadSuccess(bookingInformation, queryDocumentSnapshot.getId());
                                    break;
                                }
                            }
                            else
                            {
                                iBookingInfoLoadListener.onBookInfoLoadEmpty();
                            }
                        }
                        
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iBookingInfoLoadListener.onBookInfoLoadFailed(e.getMessage());
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dialog = new SpotsDialog.Builder().setContext(getContext()).setCancelable(false).build();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);

        Slider.init(new PicassoImageLoadingService());
        iBannerLoadListener = this;
        iLookBookLoadListener = this;
        iBookingInfoLoadListener = this;
        iBookingInformationChangeListener = this;

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null)
        {
            setUserInformation();
            loadBanner();
            loadLookBook();
            loadUserBooking();
        }

        return view;

    }

    private void loadLookBook() {
        lookbookRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<Banner> lookbooks = new ArrayList<>();
                        if(task.isSuccessful())
                        {
                            for(QueryDocumentSnapshot bannerSnapShot:task.getResult())
                            {
                                Banner banner = bannerSnapShot.toObject(Banner.class);
                                lookbooks.add(banner);
                            }
                            iLookBookLoadListener.onLookBookLoadSuccess(lookbooks);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iLookBookLoadListener.onLookBookLoadFailed(e.getMessage());
            }
        });
    }

    private void loadBanner() {
        bannerRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<Banner> banners = new ArrayList<>();
                        if(task.isSuccessful())
                        {
                            for(QueryDocumentSnapshot bannerSnapShot:task.getResult())
                            {
                                Banner banner = bannerSnapShot.toObject(Banner.class);
                                banners.add(banner);
                            }
                            iBannerLoadListener.onBannerLoadSuccess(banners);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                iBannerLoadListener.onBannerLoadFailed(e.getMessage());
            }
        });
    }

    private void setUserInformation() {
        layout_user_information.setVisibility(View.VISIBLE);
        txt_user_name.setText(common.currentUser.getName());
    }

    @Override
    public void onLookBookLoadSuccess(List<Banner> banners) {
        recycler_look_book.setHasFixedSize(true);
        recycler_look_book.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler_look_book.setAdapter(new LookBookAdapter(getActivity(), banners));
    }

    @Override
    public void onLookBookLoadFailed(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBannerLoadSuccess(List<Banner> banners) {
        banner_slider.setAdapter(new HomeSlidesAdapter(banners));
    }

    @Override
    public void onBannerLoadFailed(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBookInfoLoadEmpty() {
        card_booking_info.setVisibility(View.GONE);
    }

    @Override
    public void onBookInfoLoadSuccess(BookingInformation bookingInformation, String bookingId) {

        common.currentBooking = bookingInformation;
        common.currentBookingId = bookingId;

        txt_center_address.setText(bookingInformation.getCenterAddress());
        txt_center_mechanic.setText(bookingInformation.getMechanicName());
        txt_time.setText(bookingInformation.getTime());
        String dateRemain = DateUtils.getRelativeTimeSpanString(
                Long.valueOf(bookingInformation.getTimestamp().toDate().getTime()),
                Calendar.getInstance().getTimeInMillis(), 0).toString();
        txt_time_remain.setText(dateRemain);

        card_booking_info.setVisibility(View.VISIBLE);


        dialog.dismiss();
    }

    @Override
    public void onBookInfoLoadFailed(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBookingInformationChange() {
        startActivity(new Intent(getActivity(), BookingActivity.class));
    }
}
