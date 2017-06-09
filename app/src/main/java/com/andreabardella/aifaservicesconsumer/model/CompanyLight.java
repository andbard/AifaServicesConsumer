package com.andreabardella.aifaservicesconsumer.model;

import android.os.Parcel;

public class CompanyLight extends ItemLight {

    public CompanyLight() {
        super();
    }

    public CompanyLight(Parcel source) {
        super(source);
    }

    public static final Creator<CompanyLight> CREATOR = new Creator<CompanyLight>() {
        @Override
        public CompanyLight createFromParcel(Parcel source) {
            return new CompanyLight(source);
        }

        @Override
        public CompanyLight[] newArray(int size) {
            return new CompanyLight[0];
        }
    };
}
