package com.andreabardella.aifaservicesconsumer.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Packaging implements Parcelable {

    private String description;
    private String aic;
    private String status;

    public Packaging() {}

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAic() {
        return aic;
    }

    public void setAic(String aic) {
        this.aic = aic;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Packaging(Parcel source) {
        this();
        readFromParcel(source);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(description);
        dest.writeString(aic);
        dest.writeString(status);
    }

    public void readFromParcel(Parcel source) {
        this.setDescription(source.readString());
        this.setAic(source.readString());
        this.setStatus(source.readString());
    }

    public static final Creator<Packaging> CREATOR = new Creator<Packaging>() {
        @Override
        public Packaging createFromParcel(Parcel source) {
            return new Packaging(source);
        }

        @Override
        public Packaging[] newArray(int size) {
            return new Packaging[size];
        }
    };
}