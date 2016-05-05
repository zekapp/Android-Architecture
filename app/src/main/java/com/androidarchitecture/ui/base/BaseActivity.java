package com.androidarchitecture.ui.base;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.androidarchitecture.App;
import com.androidarchitecture.di.component.ActivityComponent;
import com.androidarchitecture.di.component.DaggerActivityComponent;
import com.androidarchitecture.di.module.ActivityModule;
import com.androidarchitecture.utils.PermissionUtils;

import java.security.Permission;
import java.util.ArrayList;

import timber.log.Timber;


public class BaseActivity extends AppCompatActivity {

    private ActivityComponent mActivityComponent;

    private int KEY_PERMISSION = 0;
    private String permissionsAsk[];
    private PermissionResult permissionResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public ActivityComponent getActivityComponent() {
        if (mActivityComponent == null) {
            mActivityComponent = DaggerActivityComponent.builder()
                    .appComponent(App.get(this).getComponent())
                    .build();
        }
        return mActivityComponent;
    }

    /*************
     * Permission for Marshmallow and Upper OS -  6.0
     *
     * To get permission for camera usage
     *
     *      public void doSomeCameraWork(){
     *          askForPermission(PermissionUtils.Manifest_CAMERA, new PermissionResult() {
     *                  @Override
     *                  public void permissionGranted() {
     *                      getCameraManger();
     *                  }
     *
     *                  @Override
     *                  public void permissionDenied() {
     *
     *                  }
     *
     *              });
     *          }
     *
     ****************/


    /**
     * For single permission.
     * */
    public void askForPermission(String permission, PermissionResult permissionResult) {
        KEY_PERMISSION = 200;
        permissionsAsk = new String[]{permission};
        this.permissionResult = permissionResult;
        internalRequestPermission(permissionsAsk);
    }

    /**
     * For multiple permissions
     * */
    public void askForPermissions(String permissions[], PermissionResult permissionResult) {
        KEY_PERMISSION = 200;
        permissionsAsk = permissions;
        this.permissionResult = permissionResult;
        internalRequestPermission(permissionsAsk);
    }

    private void internalRequestPermission(String[] permissionAsk) {
        String arrayPermissionNotGranted[];
        ArrayList<String> permissionsNotGranted = new ArrayList<>();

        for (int i = 0; i < permissionAsk.length; i++) {
            if (!isPermissionGranted(BaseActivity.this, permissionAsk[i])) {
                permissionsNotGranted.add(permissionAsk[i]);
            }
        }


        if (permissionsNotGranted.isEmpty()) {

            if (permissionResult != null)
                permissionResult.permissionGranted();

        } else {

            arrayPermissionNotGranted = new String[permissionsNotGranted.size()];
            arrayPermissionNotGranted = permissionsNotGranted.toArray(arrayPermissionNotGranted);
            ActivityCompat.requestPermissions(BaseActivity.this, arrayPermissionNotGranted, KEY_PERMISSION);
        }

    }

    /**
     * Is single permission granted or not?
     * */
    public boolean isPermissionGranted(Context context, String permission) {
        return (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) ||
                (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED);
    }

    /**
     * Called by framework.
     * */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == KEY_PERMISSION) {
            boolean granted = true;

            for (int grantResult : grantResults) {
                if (!(grantResults.length > 0 && grantResult == PackageManager.PERMISSION_GRANTED))
                    granted = false;
            }
            if (permissionResult != null) {
                if (granted) {
                    permissionResult.permissionGranted();
                } else {
                    permissionResult.permissionDenied();
                }
            }
        } else {
            Timber.e("ManagePermission permissionResult callback was null");
        }
    }

}
