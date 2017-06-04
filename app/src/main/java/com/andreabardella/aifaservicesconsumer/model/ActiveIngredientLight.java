package com.andreabardella.aifaservicesconsumer.model;

import android.os.Parcel;

public class ActiveIngredientLight extends ItemLight {

    public ActiveIngredientLight() {
        super();
    }

    public ActiveIngredientLight(Parcel source) {
        super(source);
    }

    public static final Creator<ActiveIngredientLight> CREATOR = new Creator<ActiveIngredientLight>() {
        @Override
        public ActiveIngredientLight createFromParcel(Parcel source) {
            return new ActiveIngredientLight(source);
        }

        @Override
        public ActiveIngredientLight[] newArray(int size) {
            return new ActiveIngredientLight[0];
        }
    };
}
