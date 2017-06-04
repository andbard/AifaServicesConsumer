package com.andreabardella.aifaservicesconsumer.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import timber.log.Timber;

/**
 * See <a href="https://developer.android.com/guide/topics/security/permissions.html">Android permissions</a>
 * <table>
 *     <tr>
 *         <td><b>Permission Group</b></td>
 *         <td><b>Permissions</b></td>
 *     </tr>
 *     <tr>
 *         <td>CALENDAR</td>
 *         <td>READ_CALENDAR<br>WRITE_CALENDAR</td>
 *     </tr>
 *     <tr>
 *         <td>CAMERA</td>
 *         <td>CAMERA</td>
 *     </tr>
 *     <tr>
 *         <td>CONTACTS</td>
 *         <td>READ_CONTACTS<br>WRITE_CONTACTS<br>GET_ACCOUNTS</td>
 *     </tr>
 *     <tr>
 *         <td>LOCATION</td>
 *         <td>ACCESS_FINE_LOCATION<br>ACCESS_COARSE_LOCATION</td>
 *     </tr>
 *     <tr>
 *         <td>MICROPHONE</td>
 *         <td>RECORD_AUDIO</td>
 *     </tr>
 *     <tr>
 *         <td>PHONE</td>
 *         <td>READ_PHONE_STATE<br>CALL_PHONE<br>READ_CALL_LOG<br>WRITE_CALL_LOG<br>ADD_VOICEMAIL<br>USE_SIP<br>PROCESS_OUTGOING_CALLS</td>
 *     </tr>
 *     <tr>
 *         <td>SENSORS</td>
 *         <td>BODY_SENSORS</td>
 *     </tr>
 *     <tr>
 *         <td>SMS</td>
 *         <td>SEND_SMS<br>RECEIVE_SMS<br>READ_SMS<br>RECEIVE_WAP_PUSH<br>RECEIVE_MMS</td>
 *     </tr>
 *     <tr>
 *         <td>STORAGE</td>
 *         <td>READ_EXTERNAL_STORAGE<br>WRITE_EXTERNAL_STORAGE</td>
 *     </tr>
 * </table>
 */
public class PermissionUtils implements ActivityCompat.OnRequestPermissionsResultCallback {

    private static final int PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE = 101;

//    private static final int PERMISSION_REQUEST_CAMERA = 102;

    private static final int PERMISSION_REQUEST_ALL = 201;

    /**
     * Check if a set of permissions has been granted
     *
     * @param context the application context
     * @param permissions the array representing the set of permissions
     * @return true if all the requested permission are already granted, false otherwise
     */
    public static boolean hasPermissions(Context context, String... permissions) {
        boolean result = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                int permissionCode = ActivityCompat.checkSelfPermission(context, permission);
                if (permissionCode != PackageManager.PERMISSION_GRANTED) {
                    result = false;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * Request the Android OS to grant permissions
     *
     * @param activity the current activity
     * @param permissions the requested permissions
     */
    public static void requestPermissions(Activity activity, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && permissions != null) {
            if (permissions != null) {
                if (permissions.length > 1) {
                    ActivityCompat.requestPermissions(activity, permissions, PERMISSION_REQUEST_ALL);
                } else {
                    if (permissions[0] != null) {
                        int permissionRequestCode = -1;
                        switch (permissions[0]) {
                            case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                                permissionRequestCode = PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE;
                                break;

//                            case Manifest.permission_group.CAMERA:
//                                permissionRequestCode = PERMISSION_REQUEST_CAMERA;
//                                break;

                            default:
                                Timber.w("Permission %s not recognized", permissions[0]);
                                break;
                        }
                        if (permissionRequestCode != -1) {
                            ActivityCompat.requestPermissions(activity, permissions, permissionRequestCode);
                        } else {
                            Toast.makeText(activity, "Permission " + permissions[0] + " not recognized", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        // If request is cancelled, the result arrays are empty.
        switch (requestCode) {
            case PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Timber.i("Manifest.permission.WRITE_EXTERNAL_STORAGE granted");
                } else {
                    // permission denied! Disable the functionality that depends on this permission.
                    Timber.w("Manifest.permission.WRITE_EXTERNAL_STORAGE denied");
                }
                break;

//            case PERMISSION_REQUEST_CAMERA:
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    Timber.i("permission granted");
//                } else {
//                    // permission denied! Disable the functionality that depends on this permission.
//                    Timber.w("Manifest.permission.CAMERA denied");
//                }
//                break;

            case PERMISSION_REQUEST_ALL:
                for (int i=0; i<permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        Timber.i("permission %s granted", permissions[i]);
                    } else {
                        Timber.w("permission %s denied", permissions[i]);
                    }
                }
                break;

            default:
                Timber.w("onRequestPermissionsResult(), requestCode %d not recognized", requestCode);
                break;
        }
    }

}
