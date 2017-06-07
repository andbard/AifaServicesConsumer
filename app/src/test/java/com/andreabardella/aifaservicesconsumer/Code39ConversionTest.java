package com.andreabardella.aifaservicesconsumer;

import com.andreabardella.aifaservicesconsumer.util.Code39Conversion;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class Code39ConversionTest {

    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @Test
    public void base32ToBase10_success() throws Exception {
        String toConvert = "0M590N";
        String expectedConversion = "020096020";
        String result = Code39Conversion.base32ToBase10(toConvert);
        junit.framework.Assert.assertNotNull(result);
        org.junit.Assert.assertEquals(result + " != " + expectedConversion, result, expectedConversion);
    }

    @Test
    public void base32ToBase10_conversionSucceededButNotAic() throws Exception {
        String toConvert = "Z0M590N";
        String result = Code39Conversion.base32ToBase10(toConvert);
        Assert.assertNotEquals(null, result.length(), 9);
    }

    @Test
    public void base32ToBase10_exception() throws Exception {
        String toConvert = "0M590E";
        expectedException.expect(Exception.class);
        expectedException.expectMessage(toConvert + " cannot be converted");
        Code39Conversion.base32ToBase10(toConvert);
    }

    @Test
    public void base10ToBase32_success() throws Exception {
        String toConvert = "020096020";
        String expectedConversion = "0M590N";

        String result = Code39Conversion.base10ToBase32(toConvert);
        junit.framework.Assert.assertNotNull(result);
        org.junit.Assert.assertEquals(result + " != " + expectedConversion, result, expectedConversion);

        toConvert = "A020096020";
        result = Code39Conversion.base10ToBase32(toConvert);
        junit.framework.Assert.assertNotNull(result);
        org.junit.Assert.assertEquals(result + " != " + expectedConversion, result, expectedConversion);
    }

    @Test
    public void base10ToBase32_conversionSucceededForNotAic() throws Exception {
        String toConvert =  "01020096020";
        String result = Code39Conversion.base10ToBase32(toConvert);
        junit.framework.Assert.assertNotNull(result);
    }

    @Test
    public void base10ToBase32_exceptionBecauseMaxIntegerValueExceeded() throws Exception {
        String toConvert =  "09020096020";
        expectedException.expect(NumberFormatException.class);
        // "unable to convert " + input + " into a number"
        expectedException.expectMessage("unable to convert " + toConvert + " into a number");
        Code39Conversion.base10ToBase32(toConvert);
    }

    @Test
    public void base10ToBase32_exception() throws Exception {
        String toConvert =  "AA020096020";
        expectedException.expect(NumberFormatException.class);
        expectedException.expectMessage("unable to convert " + toConvert + " into a number");
        Code39Conversion.base10ToBase32(toConvert);
    }
}
