package com.anura.anuramotors.fragmnets;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.anura.anuramotors.R;
import com.anura.anuramotors.common.common;
import com.anura.anuramotors.model.BookingInformation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dmax.dialog.SpotsDialog;
import io.paperdb.Paper;

public class BookingStep4Fragment extends Fragment {

    SimpleDateFormat simpleDateFormat;
    LocalBroadcastManager localBroadcastManager;
    Unbinder unbinder;
    AlertDialog dialog;

    @BindView(R.id.txt_booking_mechanic_text)
    TextView txt_booking_mechanic_text;

    @BindView(R.id.txt_booking_time_text)
    TextView txt_booking_time_text;

    @BindView(R.id.txt_center_address)
    TextView txt_center_address;

    @BindView(R.id.txt_center_name)
    TextView txt_center_name;

    @BindView(R.id.txt_center_open_hours)
    TextView txt_center_open_hours;

    @BindView(R.id.txt_center_phone)
    TextView txt_center_phone;

    @BindView(R.id.txt_center_website)
    TextView txt_center_website;

    @OnClick(R.id.btn_confirm)
    void confirmBooking(){

        dialog.show();

        String startTime = common.convertTimeSlotToString(common.currentTimeSlot);
        String[] convertTime = startTime.split("-");
        String[] startTimeConvert = convertTime[0].split(":");
        int startHourInt = Integer.parseInt(startTimeConvert[0].trim());
        int startMinInt = Integer.parseInt(startTimeConvert[1].trim());

        Calendar bookingDateWithoutHours = Calendar.getInstance();
        bookingDateWithoutHours.setTimeInMillis(common.bookingDate.getTimeInMillis());
        bookingDateWithoutHours.set(Calendar.HOUR_OF_DAY, startHourInt);
        bookingDateWithoutHours.set(Calendar.MINUTE, startMinInt);

        Timestamp timestamp = new Timestamp(bookingDateWithoutHours.getTime());

        final BookingInformation bookingInformation = new BookingInformation();

        bookingInformation.setCityBook(common.city);
        bookingInformation.setTimestamp(timestamp);
        bookingInformation.setDone(false);
        bookingInformation.setMechanicId(common.currentMechanic.getMechanicId());
        bookingInformation.setMechanicName(common.currentMechanic.getName());
        bookingInformation.setCustomerName(common.currentUser.getName());
        bookingInformation.setCustomerPhone(common.currentUser.getPhoneNumber());
        bookingInformation.setCenterId(common.currentCenter.getCenterId());
        bookingInformation.setCenterAddress(common.currentCenter.getAddress());
        bookingInformation.setMechanicName(common.currentCenter.getName());
        bookingInformation.setTime(new StringBuilder(common.convertTimeSlotToString(common.currentTimeSlot))
                .append(" at ")
                .append(simpleDateFormat.format(bookingDateWithoutHours.getTime())).toString());
        bookingInformation.setSlot(Long.valueOf(common.currentTimeSlot));


        DocumentReference bookingDate = FirebaseFirestore.getInstance()
                .collection("AllCenter")
                .document(common.city)
                .collection("Branch")
                .document(common.currentCenter.getCenterId())
                .collection("Mechanics")
                .document(common.currentMechanic.getMechanicId())
                .collection(common.simpleFormatDate.format(common.bookingDate.getTime()))
        .document(String.valueOf(common.currentTimeSlot));

        bookingDate.set(bookingInformation)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        addToUserBooking(bookingInformation);

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addToUserBooking(BookingInformation bookingInformation) {

        final CollectionReference userBooking = FirebaseFirestore.getInstance()
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
                        if(task.getResult().isEmpty())
                        {
                            userBooking.document()
                                    .set(bookingInformation)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            if(dialog.isShowing())
                                                dialog.dismiss();

                                            addToCalender(common.bookingDate, common.convertTimeSlotToString(common.currentTimeSlot));
                                            resetStaticData();
                                            getActivity().finish();
                                            Toast.makeText(getContext(), "Success!", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    if(dialog.isShowing())
                                        dialog.dismiss();
                                    Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        else
                        {
                            if(dialog.isShowing())
                                dialog.dismiss();
                            resetStaticData();
                            getActivity().finish();
                            Toast.makeText(getContext(), "Success!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void addToCalender(Calendar bookingDate, String startDate) {
        String startTime = common.convertTimeSlotToString(common.currentTimeSlot);
        String[] convertTime = startTime.split("-");
        String[] startTimeConvert = convertTime[0].split(":");
        int startHourInt = Integer.parseInt(startTimeConvert[0].trim());
        int startMinInt = Integer.parseInt(startTimeConvert[1].trim());

        String[] endTimeConvert = convertTime[1].split(":");
        int endHourInt = Integer.parseInt(endTimeConvert[0].trim());
        int endMinInt = Integer.parseInt(endTimeConvert[1].trim());

        Calendar startEvent = Calendar.getInstance();
        startEvent.setTimeInMillis(bookingDate.getTimeInMillis());
        startEvent.set(Calendar.HOUR_OF_DAY, startHourInt);
        startEvent.set(Calendar.MINUTE, startMinInt);

        Calendar endEvent = Calendar.getInstance();
        endEvent.setTimeInMillis(bookingDate.getTimeInMillis());
        endEvent.set(Calendar.HOUR_OF_DAY, endHourInt);
        endEvent.set(Calendar.MINUTE, endMinInt);

        SimpleDateFormat calenderDateFormat = new SimpleDateFormat("dd_MM_yyyy HH:mm");
        String startEventTime = calenderDateFormat.format(startEvent.getTime());
        String endEventTime = calenderDateFormat.format(endEvent.getTime());

        addToDeviceCalender(startEventTime, endEventTime, "Booking", new StringBuilder("Hair cut from ")
        .append(startTime)
        .append(" with ")
        .append(common.currentMechanic.getName())
        .append(" at ")
        .append(common.currentCenter.getName()).toString(),
                new StringBuilder("Address: ").append(common.currentCenter.getAddress()).toString());
    }

    private void addToDeviceCalender(String startEventTime, String endEventTime, String title, String description, String location) {
        SimpleDateFormat calenderDateFormat = new SimpleDateFormat("dd_MM_yyyy HH:mm");

        try{
            Date start = calenderDateFormat.parse(startEventTime);
            Date end = calenderDateFormat.parse(endEventTime);

            ContentValues event = new ContentValues();

            event.put(CalendarContract.Events.CALENDAR_ID, getCalendar(getContext()));
            event.put(CalendarContract.Events.TITLE, title);
            event.put(CalendarContract.Events.DESCRIPTION, description);
            event.put(CalendarContract.Events.EVENT_LOCATION, location);

            event.put(CalendarContract.Events.DTSTART, start.getTime());
            event.put(CalendarContract.Events.DTEND, end.getTime());
            event.put(CalendarContract.Events.ALL_DAY, 0);
            event.put(CalendarContract.Events.HAS_ALARM, 1);

            String timeZone = TimeZone.getDefault().getID();
            event.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone);

            Uri calendars;
            if(Build.VERSION.SDK_INT >=8 )
                calendars = Uri.parse("content://com.android.calendar/events");
            else
                calendars = Uri.parse("content://calendar/events");

            Uri uri_save = getActivity().getContentResolver().insert(calendars, event);
            Paper.init(getActivity());
            Paper.book().write(common.EVENT_URI_CACHE, uri_save.toString());

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private String getCalendar(Context context) {
        String gmailIdCalender = "";
        String projection[]={"_id", CalendarContract.Calendars.CALENDAR_DISPLAY_NAME};
        Uri calendars = Uri.parse("content://com.android.calendar/calendars");

        ContentResolver contentResolver = context.getContentResolver();
        Cursor managedCursor = contentResolver.query(calendars, projection, null, null, null);

        if(managedCursor.moveToFirst())
        {
            String calName;
            int nameCol = managedCursor.getColumnIndex(projection[1]);
            int idCol = managedCursor.getColumnIndex(projection[0]);
            do{
                calName = managedCursor.getString(nameCol);
                if(calName.contains("@gmail.com"))
                {
                    gmailIdCalender = managedCursor.getString(idCol);
                    break;
                }
            }while (managedCursor.moveToNext());
            managedCursor.close();
        }

        return gmailIdCalender;
    }

    private void resetStaticData() {
        common.step = 0;
        common.currentTimeSlot = -1;
        common.currentCenter = null;
        common.currentMechanic = null;
        common.bookingDate.add(Calendar.DATE, 0);

    }

    BroadcastReceiver confirmBookingReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setData();
        }
    };

    private void setData() {
        txt_booking_mechanic_text.setText(common.currentMechanic.getName());
        txt_booking_time_text.setText(new StringBuilder(common.convertTimeSlotToString(common.currentTimeSlot))
        .append(" at ")
        .append(simpleDateFormat.format(common.bookingDate.getTime())));

        txt_center_address.setText(common.currentCenter.getAddress());
        txt_center_website.setText(common.currentCenter.getWebsite());
        txt_center_name.setText(common.currentCenter.getName());
        txt_center_open_hours.setText(common.currentCenter.getOpenHours());

    }

    static BookingStep4Fragment instance;

    public static BookingStep4Fragment getInstance(){
        if(instance == null)
            instance = new BookingStep4Fragment();
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        localBroadcastManager.registerReceiver(confirmBookingReceiver, new IntentFilter(common.KEY_CONFIRM_BOOKING));

        dialog = new SpotsDialog.Builder().setContext(getContext()).setCancelable(false).build();
    }

    @Override
    public void onDestroy() {
        localBroadcastManager.unregisterReceiver(confirmBookingReceiver);
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View itemView =  inflater.inflate(R.layout.fragment_booking_step4, container, false);
        unbinder = ButterKnife.bind(this, itemView);
        return itemView;
    }
}