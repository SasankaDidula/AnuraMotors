package com.anura.anuramotors.Interface;

import com.anura.anuramotors.model.TimeSlot;

import java.util.List;

public interface ITimeSlotLoaderListener {
    void onTimeSlotLoadSuccess(List<TimeSlot> timeSlotList);
    void onTimeSlotLoadFailed(String message);
    void onTimeSlotLoadEmpty();
}
