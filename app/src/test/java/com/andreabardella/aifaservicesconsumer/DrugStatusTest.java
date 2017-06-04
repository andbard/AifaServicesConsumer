package com.andreabardella.aifaservicesconsumer;

import com.andreabardella.aifaservicesconsumer.model.DrugStatus;
import com.andreabardella.aifaservicesconsumer.model.UnrecognizedDrugStatusException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertNotNull;

public class DrugStatusTest {

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @Test
    public void getDrugStatus_success() throws UnrecognizedDrugStatusException {
        assertNotNull(DrugStatus.getStatus("a"));
        assertNotNull(DrugStatus.getStatus("r"));
        assertNotNull(DrugStatus.getStatus("s"));
    }

    @Test
    public void getDrugStatus_throwExceptionBecauseOfUnrecognizedParam() throws UnrecognizedDrugStatusException {
        expectedException.expect(UnrecognizedDrugStatusException.class);
        expectedException.expectMessage("Cannot convert");
        DrugStatus.getStatus("xyz");
    }

    @Test(expected = UnrecognizedDrugStatusException.class)
    public void getDrugStatus_throwExceptionBecauseOfNullParam() throws UnrecognizedDrugStatusException {
        DrugStatus.getStatus(null);
    }

}
