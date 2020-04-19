package com.anura.anuramotors.Interface;


import com.anura.anuramotors.model.BookingInformation;

public interface IBookingInfoLoadListener {
    void onBookInfoLoadEmpty();
    void onBookInfoLoadSuccess(BookingInformation bookingInformation, String documentId);
    void onBookInfoLoadFailed(String message);
}
