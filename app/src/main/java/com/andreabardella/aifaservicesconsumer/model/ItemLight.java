package com.andreabardella.aifaservicesconsumer.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

public abstract class ItemLight implements Comparable<ItemLight>, Parcelable {

    protected String code;
    protected String name;

    public ItemLight() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += this.code != null ? this.code.hashCode() : 0;
        hash += this.name != null ? (this.name.toLowerCase()).hashCode() : 0;
        return hash;
    }

    @Override
    public boolean equals(Object other) {
        ItemLight o = this.getClass().equals(other.getClass()) ? ((ItemLight) other) : null;
        return o != null &&
                (this.code != null ? this.code.equals(o.getCode()) : o.getCode() == null) &&
                (this.name != null ? this.name.equalsIgnoreCase(o.getName()) : o.getName() == null);
    }

    @Override
    public int compareTo(@NonNull ItemLight o) {
        if (this.code.compareTo(o.code) == 0) {
            return (this.name.toLowerCase()).compareTo(o.name.toLowerCase());
        }
        return this.code.compareTo(o.code);
    }

    public ItemLight(Parcel source) {
        this();
        readFromParcel(source);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void readFromParcel(Parcel source) {
        this.setCode(source.readString());
        this.setName(source.readString());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.getCode());
        dest.writeString(this.getName());
    }
}
