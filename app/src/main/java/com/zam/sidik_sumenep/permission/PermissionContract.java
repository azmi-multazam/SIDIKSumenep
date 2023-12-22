package com.zam.sidik_sumenep.permission;

import com.zam.sidik_sumenep.base.IBasePresenter;

public interface PermissionContract {

    interface View {
        boolean needPermission();

        void onPermissionGranted(int requestCode);

        void onPermissionDenied(int requestCode);
    }

    interface Presenter extends IBasePresenter<View> {
        void requestPermission(String[] permission, int requestCode);

        boolean grantedPermission(String permission);
    }
}