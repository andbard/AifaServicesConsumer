package com.andreabardella.aifaservicesconsumer.util;

import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class JsonHelper {

    private final static String TAG = "JsonHelper";

    private static ObjectMapper mapper;

    public static void setMapper(ObjectMapper mapper) {
        JsonHelper.mapper = mapper;
    }

    public static <T> T readJsonFromFile(File file, Class<T> pojoClass) {
        T result = null;
        try {
            result = mapper.readValue(file, pojoClass);
        } catch (IOException e) {
            Log.e(TAG, "Error calling readJsonFromFile()", e);
        }
        return result;
    }

    public static void writeJsonToFile(File file, Object object) {
        try {
            mapper.writeValue(file, object);
        } catch (IOException e) {
            Log.e(TAG, "Error calling writeJsonToFile()", e);
        }
    }

    /*public static <T> ObjectReader getObjectReader(Class<T> pojoClass) {
        return defaultMapper().reader(pojoClass);
    }

    public static <T> ObjectWriter getObjectWriter(Class<T> pojoClass) {
        return defaultMapper().writerWithType(pojoClass);
    }*/

    public static <T> T readJsonString(String json, Class<T> pojoClass) {
        if (mapper == null) {
            Log.e(TAG, "ObjectMapper MUST not be null");
        }
        T result = null;
        try {
            result = mapper.readValue(json, pojoClass);
        } catch (IOException e) {
            Log.e(TAG, "Error calling readJsonString()", e);
        }
        return result;
    }

    public static String writeJsonString(Object object) {
        String result = null;
        /*OutputStream os = new ByteArrayOutputStream();
        try {
            defaultMapper().writeValue(os, object);
            if (os != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                os.write(baos.toByteArray());
                result = baos.toString();
            }
        } catch (IOException e) {
            Log.e(TAG "");
        }*/
        try {
            result = mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            Log.e(TAG, "writeJsonString()", e);
        }
        return result;
    }

    /*public static <T> T readJsonOutputStream(InputStream is, Class<T> pojoClass) {
        try {
            return getObjectReader(pojoClass).readValue(is);
        } catch (IOException e) {
            Log.e(TAG, "");
        }
        return null;
    }

    public static OutputStream writeJsonOutputStream(Object object) {
        OutputStream result = null;
        try {
            getObjectWriter(object.getClass()).writeValue(result, object);
        } catch (IOException e) {
            Log.e(TAG, "");
        }
        return result;
    }*/

    public static <T> HashMap<String, T> readJsonStringRepresentingHashMap(String json, Class<T> pojoClass) {
        if (mapper == null) {
            Log.e(TAG, "ObjectMapper MUST not be null");
        }
        HashMap<String, T> result = null;
        try {
            TypeFactory typeFactory = mapper.getTypeFactory();
            MapType mapType = typeFactory.constructMapType(HashMap.class, String.class, pojoClass);
            result = mapper.readValue(json, mapType);
        } catch (IOException e) {
            Log.e(TAG, "Error calling readJsonString()", e);
        }
        return result;
    }

    public static <T> ArrayList<T> readJsonStringRepresentingArrayList(String json, Class<T> pojoClass) {
        if (mapper == null) {
            Log.e(TAG, "ObjectMapper MUST not be null");
        }
        ArrayList<T> result = null;
        try {
            TypeFactory typeFactory = mapper.getTypeFactory();
            CollectionType collectionType = typeFactory.constructCollectionType(ArrayList.class, pojoClass);
            result = mapper.readValue(json, collectionType);
        } catch (IOException e) {
            Log.e(TAG, "Error calling readJsonString()", e);
        }
        return result;
    }

    public static <T> HashMap<String, T> readJsonStringRepresentingMap(String json, Class<T> pojoClass) {
        if (mapper == null) {
            Log.e(TAG, "ObjectMapper MUST not be null");
        }
        HashMap<String, T> result = null;
        try {
            TypeFactory typeFactory = mapper.getTypeFactory();
            MapType mapType = typeFactory.constructMapType(HashMap.class, String.class, pojoClass);
            result = mapper.readValue(json, mapType);
        } catch (IOException e) {
            Log.e(TAG, "Error calling readJsonString()", e);
        }
        return result;
    }
}
