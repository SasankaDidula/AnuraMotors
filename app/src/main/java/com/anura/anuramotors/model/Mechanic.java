package com.anura.anuramotors.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Mechanic implements Parcelable {
    private String name, username, password, mechanicId;
    private Long rating;

    public Mechanic() {
    }

    protected Mechanic(Parcel in) {
        name = in.readString();
        username = in.readString();
        password = in.readString();
        mechanicId = in.readString();
        if (in.readByte() == 0) {
            rating = null;
        } else {
            rating = in.readLong();
        }
    }

    public static final Creator<Mechanic> CREATOR = new Creator<Mechanic>() {
        @Override
        public Mechanic createFromParcel(Parcel in) {
            return new Mechanic(in);
        }

        @Override
        public Mechanic[] newArray(int size) {
            return new Mechanic[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getRating() {
        return rating;
    }

    public void setRating(Long rating) {
        this.rating = rating;
    }

    public String getMechanicId() {
        return mechanicId;
    }

    public void setMechanicId(String mechanicId) {
        this.mechanicId = mechanicId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(username);
        dest.writeString(password);
        dest.writeString(mechanicId);
        if (rating == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(rating);
        }
    }
}
