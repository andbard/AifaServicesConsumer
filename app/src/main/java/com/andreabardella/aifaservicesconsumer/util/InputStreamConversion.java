package com.andreabardella.aifaservicesconsumer.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Utility class for InputStream conversion
 */
public class InputStreamConversion {

    /**
     * This method converts an InputStream object into a String
     * @param inputStream The InputStream to convert
     *                    that will be closed at the end of this operation
     * @return The String representing the converted InputStream or null if the inputStream is null
     *
     * @throws IOException If an error occurs while reading the InputStream
     * or, after the reading, trying to close the stream
     */
    public static String inputStreamToString(InputStream inputStream) throws IOException {
        if(inputStream == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        try {
            while((line = reader.readLine()) != null) {
                builder.append(line + "\n");
            }
        } catch (IOException e) {
            throw new IOException("Exception reading the InputStream", e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                throw new IOException("Exception closing the InputStream", e);
            }
        }
        return builder.toString();
    }

    /**
     * This method converts an InputStream object into a byte array
     * @param inputStream The InputStream to convert
     *                   that will be closed at the end of this operation
     * @return The byte[] representing the converted InputStream or null if the inputStream is null
     *
     * @throws IOException If an error occurs while reading the InputStream
     * or, after the reading, trying to close the stream
     */
    public static byte[] inputStreamToByteArray(InputStream inputStream) throws IOException {
        if(inputStream == null) {
            return null;
        }

        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        try {
            while ((len = inputStream.read(buffer)) != -1) {
                byteBuffer.write(buffer, 0, len);
            }
        } catch (IOException e) {
            throw new IOException("Exception reading the InputStream", e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                throw new IOException("Exception closing the InputStream", e);
            }
        }
        return byteBuffer.toByteArray();
    }

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            // [b7,b6,b5,b4,b3,b2,b1,b0] & 11111111 => [b7,b6,b5,b4,b3,b2,b1,b0]
            int v = bytes[j] & 0xFF;
            // [0,0,0,0,b7,b6,b5,b4] >> 4 => [b7,b7,b7,b7,b7,b6,b5,b4]
            hexChars[j * 2] = hexArray[v >>> 4];
            // => [0,0,0,0,b3,b2,b1,b0]
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}
