package com.andreabardella.aifaservicesconsumer.model;

public enum ProcedureType {

    NATIONAL("N"),
    MUTUAL_RECOGNITION("M"),
    DECENTRALIZED("D"),
    E("E"),
    O("O"),
    P("P"),
    R("R");

    private final String type;

    ProcedureType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }

    public static ProcedureType getType(String type) {
        if (type != null) {
            for (ProcedureType pt : ProcedureType.values()) {
                if (pt.toString().equalsIgnoreCase(type)) {
                    return pt;
                }
            }
        }
        return null;
    }

}
