package com.andreabardella.aifaservicesconsumer.util;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class FileUtils {

    private static final String TAG = "FileUtils";

    /**
     * Get the desired internal storage directory (creating it if it doesn't exist)
     * <br>
     * @param context the application context
     * @param directory the required directory
     * @return the directory or null
     */
    public static File getInternalStorageDirectory(Context context, String directory) {
        File dir = new File(context.getFilesDir(), directory);
        if (!dir.exists()) {
            Log.d(TAG, dir.getAbsolutePath() + " does not exist => create this folder");
            if (!dir.mkdirs()) {
                Log.w(TAG, "Failed to create directory " + directory);
                return null;
            }
        }
        return dir;
    }

    public static boolean isEnvironmentExternalStorageMediaMounted() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * Get the desired external storage directory (creating it if it doesn't exist)
     * <br>
     * @param context the application context
     * @param directory the required directory
     * @return the directory or null
     */
    public static File getExternalStorageDirectory(Context context, String directory) {
        // To be safe check that the SDCard is mounted using Environment.getExternalStorageState() before doing this
        if (isEnvironmentExternalStorageMediaMounted()) {
            File dir = new File(Environment.getExternalStorageDirectory(), directory); // File dir = new File(context.getExternalFilesDir(null), directory);
            if (!dir.exists()) {
                Log.d(TAG, dir.getAbsolutePath() + " does not exist => create this folder");
                if (!dir.mkdirs()) {
                    Log.e(TAG, "Failed to create directory " + directory);
                    /* NotaBene:
                    deleting or re-creating the medical report directory shouldn't be an issue
                    but it happens to be.
                    For example, if the directory and its content are deleted while another process
                    is holding a reference to the same file(s) (e.g. a file manager app)
                    and then we try to re-create the previous folder, the following error happens:
                    "open failed: EROFS (Read-only file system)at libcore.io.IoBridge.open android"
                    This issue may be cause by
                    - two or more process reference the same file
                    - file was deleted, but the reference not be killed
                    However, deleted it, only one reference was killed and maybe one or more process
                    are still referencing this file also
                    ###
                    There is some workaround (no solution) to avoid this based on what we are doing
                    and what we want to achieve: do not delete, at least, the reports folder,
                    rename the directory before deleting it, rename the file before deleting it */
                    return null;
                }
            }
            return dir;
        } else {
            Log.w(TAG, "SDCard might be not mounted! Environment.getExternalStorageState() = " + Environment.getExternalStorageState());
            return null;
        }
    }

    /**
     * Delete a directory (recursively) or a file
     * <br>
     * What happens if a folder has a symbolic link on itself? -> infinite loop?
     * @param fileOrDirectory The directory or file to delete (recursively if directory)
     * @return true if the directory (along with its content) or file is successfully deleted
     */
    public static boolean deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory == null) {
            Log.w(TAG, "This directory does not exist");
            return false;
        }
        if (fileOrDirectory.isDirectory()) {
            File[] children = fileOrDirectory.listFiles();
            if (children != null && children.length > 0) {
                for (File child : children) {
                    if (!deleteRecursive(child)) {
                        return false;
                    }
                }
            }
        }
        Log.d(TAG, "Deleting " + fileOrDirectory.getAbsolutePath());
        return fileOrDirectory.delete();
    }

    /**
     * Delete the content of a directory (not the directory itself)
     * <br>
     * @param directory The directory whose content has to be deleted
     * @return true if the directory's content is successfully deleted
     */
    public static boolean deleteDirectoryContent(File directory) {
        if (directory == null) {
            Log.w(TAG, "This directory does not exist");
            return false;
        }
        if (directory.isDirectory()) {
            for (File child : directory.listFiles()) {
                if (!deleteRecursive(child)) {
                    return false;
                }
            }
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Copy the src file in the dst file
     * <br>
     * @param src The file to be copied
     * @param dst The copy of the file
     * @return TRUE if succeeded in copying the file, FALSE otherwise
     */
    public static boolean copy(File src, File dst) {
        boolean success;
        InputStream is;
        try {
            is = new FileInputStream(src);
            success = copy(is, dst);
        } catch (IOException e) {
            Log.e(TAG, "Failure copying the file", e);
            success = false;
        }
        return success;
    }

    /**
     * Copy the InputStream content in the destination file.
     * The InputStream is closed after having copied the related file.
     * <br>
     * @param is The input stream representing the file to be copied
     * @param dst The copy of the file
     * @return TRUE if succeeded in copying the file, FALSE otherwise
     */
    public static boolean copy(InputStream is, File dst) {
        boolean success = true;
        OutputStream os = null;
        // choose only one of the available implementation
        try {
            /*==================*/
            /* Implementation 1 */
            /*==================*/
            os = new FileOutputStream(dst);
            // transfer bytes from in to os
            byte[] buf = new byte[1024];
            int len;
            while ((len = is.read(buf)) > 0) {
                os.write(buf, 0, len);
            }
            /*==================*/
            /* Implementation 2 */
            /*==================*/
            /*os = new FileOutputStream(dst);
            FileChannel inChannel = ((FileInputStream) is).getChannel();
            FileChannel outChannel = ((FileOutputStream) os).getChannel();
            inChannel.transferTo(0, inChannel.size(), outChannel);*/
        } catch (IOException e) {
            Log.e(TAG, "Exception copying the file", e);
            success = false;
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                Log.e(TAG, "Exception closing the input stream", e);
            }
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                Log.e(TAG, "Exception closing the output stream", e);
            }
        }
        return success;
    }

    /* e.g.: is = getAssets().open("filename.txt") */
    public static void readInputStream(InputStream is) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            // do reading, usually loop until end of file reading
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                // todo: process line
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
