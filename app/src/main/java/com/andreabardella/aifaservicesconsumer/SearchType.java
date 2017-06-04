package com.andreabardella.aifaservicesconsumer;

public enum SearchType {

    AIC(1, "aic"),
    ACTIVE_INGREDIENT(2, "active_ingredient"),
    DRUG(3, "drug"),
    INDUSTRY(4, "industry");

    private String machineName;
    private int id;

    SearchType(int id, String machineName) {
        this.id = id;
        this.machineName = machineName;
    }

    public String getMachineName() {
        return this.machineName;
    }

    public int getId() {
        return this.id;
    }

    /**
     * Return the enum associated to the input parameter
     * @param machineName the input parameter
     * @return a SearchType or null if no enum is associated to the machineName
     */
    public SearchType getByMachineName(String machineName) {
        for (SearchType type : SearchType.values()) {
            if (type.getMachineName().equals(machineName)) {
                return type;
            }
        }
        return null;
    }
}
