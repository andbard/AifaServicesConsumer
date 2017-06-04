package com.andreabardella.aifaservicesconsumer.model;

public enum DrugStatus {

    AUTHORIZED("A"),
    RETIRED("R"),
    SUSPENDED("S");

    private String status;

    DrugStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return status;
    }

    public static DrugStatus getStatus(String status) throws UnrecognizedDrugStatusException {
        StringBuilder sb = null;
        if (status != null) {
            for (DrugStatus ds : DrugStatus.values()) {
                if (ds.toString().equalsIgnoreCase(status)) {
                    return ds;
                }
                if (sb == null) {
                    sb = new StringBuilder();
                    sb.append(ds);
                } else {
                    sb.append(", ").append(ds);
                }
            }
        } else {
            for (DrugStatus ds : DrugStatus.values()) {
                if (sb == null) {
                    sb = new StringBuilder();
                    sb.append(ds);
                } else {
                    sb.append(", ").append(ds);
                }
            }
        }
        String message = "Cannot convert " +
                (status != null ? "\"" + status + "\"" : "a null string") +
                " to a DrugStatus\n" +
                "The only admitted values are " + sb;
        throw new UnrecognizedDrugStatusException(message);
    }

}
