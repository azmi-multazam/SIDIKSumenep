package com.zam.sidik_sumenep.home.newsinfo.mvp;


import java.util.Map;

import com.zam.sidik_sumenep.home.memberhome.mvp.BeritaResponse;
import com.zam.sidik_sumenep.home.newsinfo.mvp.navmenu.NavMenuResponse;

public interface NewsContract {

    interface NewsView {
        void onRequestNavigationMenuStart();

        void onRequestNavigationSuccess(NavMenuResponse response);

        void onRequestNavigationError(String message);

        void onRequestBeritaStart();

        void onRequestBeritaSuccess(BeritaResponse response);

        void onRequestBeritaError(String message);

    }

    interface Presenter {
        void requestNavigation(Map<String, String> map);

        void requestBerita(Map<String, String> map);

        void cancelRequest();
    }
}
