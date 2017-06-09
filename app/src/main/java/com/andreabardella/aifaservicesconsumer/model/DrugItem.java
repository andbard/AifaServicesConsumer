package com.andreabardella.aifaservicesconsumer.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DrugItem implements Parcelable {

    private Set<String> nameSet;
    private Set<CompanyLight> industrySet;
    private String aic;
    private Set<String> activeIngredientSet;
    private String fiUrl;
    private String rcpUrl;
    private int fiSize;
    private int rcpSize;
    private String fiPath;
    private String rcpPath;
    private List<Packaging> packagingList;

    public DrugItem() {}

    public Set<String> getNameSet() {
        return nameSet;
    }

    public void setNameSet(Set<String> nameList) {
        this.nameSet = nameList;
    }

    public Set<CompanyLight> getIndustrySet() {
        return industrySet;
    }

    public void setIndustrySet(Set<CompanyLight> industryList) {
        this.industrySet = industryList;
    }

    public String getAic() {
        return aic;
    }

    public void setAic(String aic) {
        this.aic = aic;
    }

    public Set<String> getActiveIngredientSet() {
        return activeIngredientSet;
    }

    public void setActiveIngredientSet(Set<String> activeIngredientList) {
        this.activeIngredientSet = activeIngredientList;
    }

    public String getFiUrl() {
        return fiUrl;
    }

    public void setFiUrl(String fiUrl) {
        this.fiUrl = fiUrl;
    }

    public String getRcpUrl() {
        return rcpUrl;
    }

    public void setRcpUrl(String rcpUrl) {
        this.rcpUrl = rcpUrl;
    }

    public int getFiSize() {
        return fiSize;
    }

    public void setFiSize(int size) {
        this.fiSize = size;
    }

    public int getRcpSize() {
        return rcpSize;
    }

    public void setRcpSize(int size) {
        this.rcpSize = size;
    }

    public String getFiPath() {
        return fiPath;
    }

    public void setFiPath(String fiPath) {
        this.fiPath = fiPath;
    }

    public String getRcpPath() {
        return rcpPath;
    }

    public void setRcpPath(String rcpPath) {
        this.rcpPath = rcpPath;
    }

    public List<Packaging> getPackagingList() {
        return packagingList;
    }

    public void setPackagingList(List<Packaging> packagingList) {
        this.packagingList = packagingList;
    }

    public DrugItem(Parcel source) {
        this();
        readFromParcel(source);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(nameSet != null ? nameSet.size() : -1);
        if (nameSet != null) {
            for (String str : nameSet) {
                dest.writeString(str);
            }
        }

        dest.writeInt(industrySet != null ? industrySet.size() : -1);
        if (industrySet != null) {
            for (CompanyLight industry : industrySet) {
                dest.writeValue(industry);
            }
        }

        dest.writeString(aic);

        dest.writeInt(activeIngredientSet != null ? activeIngredientSet.size() : -1);
        if (activeIngredientSet != null) {
            for (String str : activeIngredientSet) {
                dest.writeString(str);
            }
        }

        dest.writeString(fiUrl);
        dest.writeInt(fiSize);
        dest.writeString(fiPath);

        dest.writeString(rcpUrl);
        dest.writeInt(rcpSize);
        dest.writeString(rcpPath);

        dest.writeInt(packagingList != null ? packagingList.size() : -1);
        if (packagingList != null) {
            for (Packaging pck : packagingList) {
                dest.writeValue(pck);
            }
        }
    }

    public void readFromParcel(Parcel source) {
        int size = source.readInt();
        if (size > -1) {
            HashSet<String> set = new HashSet<>(size);
            for (int i=0; i<size; i++) {
                set.add(source.readString());
            }
            this.setNameSet(set);
        }

        size = source.readInt();
        if (size > -1) {
            Set<CompanyLight> set = new HashSet<>(size);
            for (int i=0; i<size; i++) {
                set.add((CompanyLight) source.readValue(CompanyLight.class.getClassLoader()));
            }
            this.setIndustrySet(set);
        }

        this.setAic(source.readString());

        size = source.readInt();
        if (size > -1) {
            HashSet<String> set = new HashSet<>(size);
            for (int i=0; i<size; i++) {
                set.add(source.readString());
            }
            this.setActiveIngredientSet(set);
        }

        this.setFiUrl(source.readString());
        this.setFiSize(source.readInt());
        this.setFiPath(source.readString());

        this.setRcpUrl(source.readString());
        this.setRcpSize(source.readInt());
        this.setRcpPath(source.readString());

        size = source.readInt();
        if (size > -1) {
            ArrayList<Packaging> list = new ArrayList<>(size);
            for (int i=0; i<size; i++) {
                list.add((Packaging) source.readValue(Packaging.class.getClassLoader()));
            }
            this.setPackagingList(list);
        }
    }

    public static final Creator<DrugItem> CREATOR = new Creator<DrugItem>() {
        @Override
        public DrugItem createFromParcel(Parcel source) {
            return new DrugItem(source);
        }

        @Override
        public DrugItem[] newArray(int size) {
            return new DrugItem[0];
        }
    };
}
