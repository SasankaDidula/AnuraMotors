package com.anura.anuramotors;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;

import com.anura.anuramotors.adapter.MyViewPagerAdapter;
import com.anura.anuramotors.common.NonSwipeViewPager;
import com.anura.anuramotors.common.common;
import com.anura.anuramotors.model.Mechanic;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.shuhart.stepview.StepView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;

public class BookingActivity extends AppCompatActivity {

    LocalBroadcastManager localBroadcastManager;
    AlertDialog dialog;
    CollectionReference barberRef;

    @BindView(R.id.step_view)
    StepView stepView;

    @BindView(R.id.view_pager)
    NonSwipeViewPager viewPager;

    @BindView(R.id.btn_previous_step)
    Button btn_previous_step;

    @BindView(R.id.btn_next_step)
    Button btn_next_step;

    @OnClick(R.id.btn_previous_step)
    void previousStep(){
        if(common.step == 3 || common.step > 0)
        {
            common.step--;
            viewPager.setCurrentItem(common.step);
            if(common.step < 3)
            {
                btn_next_step.setEnabled(true);
                setColorButton();
            }

        }
    }

    @OnClick(R.id.btn_next_step)
    void nextClick(){
        if(common.step < 3 || common.step ==0)
        {
            common.step++;
            if(common.step == 1)
            {
                if(common.currentCenter != null)
                    loadBarberBySalon(common.currentCenter.getCenterId());
            }
            else if(common.step == 2)
            {
                if(common.currentMechanic != null)
                    loadTimeSlotofBarber(common.currentMechanic.getMechanicId());
            }
            else if(common.step == 3)
            {
                if(common.currentTimeSlot != -1)
                    confirmBooking();
            }
            viewPager.setCurrentItem(common.step);
        }
    }

    private void confirmBooking() {
        Intent intent = new Intent(common.KEY_CONFIRM_BOOKING);
        localBroadcastManager.sendBroadcast(intent);
    }

    private void loadTimeSlotofBarber(String barberId) {
        Intent intent = new Intent(common.KEY_DISPLAY_TIME_SLOT);
        localBroadcastManager.sendBroadcast(intent);
    }

    private void loadBarberBySalon(String salonId) {
        dialog.show();

        //  /AllSalon/Florida/Branch/7IZXXdt0vG56zTHLmQPL/Barbers
        if(!TextUtils.isEmpty(common.city))
        {
            barberRef = FirebaseFirestore.getInstance()
                    .collection("AllCenter")
                    .document(common.city)
                    .collection("Branch")
                    .document(salonId)
                    .collection("Mechanics");

            barberRef.get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            ArrayList<Mechanic> mechanics = new ArrayList<>();
                            for(QueryDocumentSnapshot barberSnapShot:task.getResult())
                            {
                                Mechanic mechanic = barberSnapShot.toObject(Mechanic.class);
                                mechanic.setPassword("");
                                mechanic.setMechanicId(barberSnapShot.getId());

                                mechanics.add(mechanic);
                            }

                            Intent intent = new Intent(common.KEY_MECHANIC_LOAD_DONE);
                            intent.putParcelableArrayListExtra(common.KEY_MECHANIC_LOAD_DONE, mechanics);
                            localBroadcastManager.sendBroadcast(intent);

                            dialog.dismiss();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.dismiss();
                }
            });
        }
    }


    private BroadcastReceiver buttonNextReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            int step = intent.getIntExtra(common.KEY_STEP, 0);
            if(step == 1)
                common.currentCenter = intent.getParcelableExtra(common.KEY_CENTER_STORE);
            else if(step == 2)
                common.currentMechanic = intent.getParcelableExtra(common.KEY_MECHANIC_SELECTED);
            else if(step == 3)
                common.currentTimeSlot = intent.getIntExtra(common.KEY_TIME_SLOT, -1);

            btn_next_step.setEnabled(true);
            setColorButton();
        }
    };

    @Override
    protected void onDestroy() {
        localBroadcastManager.unregisterReceiver(buttonNextReceiver);
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        ButterKnife.bind(BookingActivity.this);

        dialog = new SpotsDialog.Builder().setContext(this).build();

        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(buttonNextReceiver, new IntentFilter(common.KEY_ENABLE_BUTTON_NEXT));


        setupStepView();
        setColorButton();

        viewPager.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager()));
        viewPager.setOffscreenPageLimit(4);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                stepView.go(position, true);
                if(position == 0)
                    btn_previous_step.setEnabled(false);
                else
                    btn_previous_step.setEnabled(true);

                btn_next_step.setEnabled(false);
                setColorButton();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setColorButton() {
        if(btn_next_step.isEnabled())
            btn_next_step.setBackgroundResource(R.color.colorButton);
        else
            btn_next_step.setBackgroundResource(android.R.color.darker_gray);

        if(btn_previous_step.isEnabled())
            btn_previous_step.setBackgroundResource(R.color.colorButton);
        else
            btn_previous_step.setBackgroundResource(android.R.color.darker_gray);
    }

    private void setupStepView() {
        List<String> stepList = new ArrayList<>();
        stepList.add("Center");
        stepList.add("Mechanic");
        stepList.add("Time");
        stepList.add("Confirm");
        stepView.setSteps(stepList);
    }
}
