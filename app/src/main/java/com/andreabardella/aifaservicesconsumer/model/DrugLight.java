package com.andreabardella.aifaservicesconsumer.model;

import android.os.Parcel;
import android.support.annotation.NonNull;

public class DrugLight extends ItemLight {

    private String company;

    public DrugLight() {
        super();
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    @Override
    public boolean equals(Object other) {
        DrugLight o = other instanceof DrugLight ? ((DrugLight) other) : null;
        return o != null &&
                (this.code != null ? this.code.equals(o.getCode()) : o.getCode() == null) &&
                (this.name != null ? this.name.equalsIgnoreCase(o.getName()) : o.getName() == null) &&
                (this.company != null ? this.company.equalsIgnoreCase(o.getCompany()) : o.getCompany() == null);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += this.code != null ? this.code.hashCode() : 0;
        hash += this.name != null ? (this.name.toLowerCase()).hashCode() : 0;
        hash += this.company != null ? (this.company.toLowerCase()).hashCode() : 0;
        return hash;
    }

    @Override
    public int compareTo(@NonNull ItemLight o) {
        if (this.code.compareTo(o.code) == 0) {
            if ((this.name.toLowerCase()).compareTo(o.name.toLowerCase()) == 0) {
                return (this.company.toLowerCase()).compareTo((((DrugLight) o).company).toLowerCase());
            }
            return (this.name.toLowerCase()).compareTo(o.name.toLowerCase());
        }
        return this.code.compareTo(o.code);
    }

    public DrugLight(Parcel source) {
        super(source);
    }

    @Override
    public void readFromParcel(Parcel source) {
        super.readFromParcel(source);
        this.setCompany(source.readString());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.getCompany());
    }

    public static final Creator<DrugLight> CREATOR = new Creator<DrugLight>() {
        @Override
        public DrugLight createFromParcel(Parcel source) {
            return new DrugLight(source);
        }

        @Override
        public DrugLight[] newArray(int size) {
            return new DrugLight[0];
        }
    };
}
