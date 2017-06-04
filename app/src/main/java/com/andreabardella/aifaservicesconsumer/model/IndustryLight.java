package com.andreabardella.aifaservicesconsumer.model;

import android.os.Parcel;

public class IndustryLight extends ItemLight {

    public IndustryLight() {
        super();
    }

    public IndustryLight(Parcel source) {
        super(source);
    }

    public static final Creator<IndustryLight> CREATOR = new Creator<IndustryLight>() {
        @Override
        public IndustryLight createFromParcel(Parcel source) {
            return new IndustryLight(source);
        }

        @Override
        public IndustryLight[] newArray(int size) {
            return new IndustryLight[0];
        }
    };
}
