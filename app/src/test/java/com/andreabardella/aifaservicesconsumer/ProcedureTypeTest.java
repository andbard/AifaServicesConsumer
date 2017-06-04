package com.andreabardella.aifaservicesconsumer;

import com.andreabardella.aifaservicesconsumer.model.ProcedureType;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ProcedureTypeTest {

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @Test
    public void getDrugStatus_success() {
        for (ProcedureType pt : ProcedureType.values()) {
            ProcedureType type = ProcedureType.getType(pt.toString().toUpperCase());
            assertNotNull(type);
            type = ProcedureType.getType(pt.toString().toLowerCase());
            assertNotNull(type);
        }
    }

    @Test
    public void getDrugStatus_unrecognizedBecauseOfUnrecognizedParam() {
        ProcedureType pt = ProcedureType.getType("xyz");
        assertNull(pt);
    }

    @Test
    public void getDrugStatus_unrecognizedBecauseOfNullParam() {
        ProcedureType pt = ProcedureType.getType(null);
        //noinspection ConstantConditions
        assertNull(pt);
    }

}
