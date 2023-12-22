package com.zam.sidik_sumenep.login;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.JsonObject;

import com.zam.sidik_sumenep.BaseActivity;
import com.zam.sidik_sumenep.R;
import com.zam.sidik_sumenep.util.Config;
import com.zam.sidik_sumenep.util.Util;
import com.zam.sidik_sumenep.util.VolleySingleton;
import com.zam.sidik_sumenep.util.VolleyStringRequest;

/**
 * Created by supriyadi on 5/7/17.
 */

public class ForgotPasswordActivity extends BaseActivity {

    private static final String VOLLEY_TAG = ForgotPasswordActivity.class.getName();
    private EditText editTextNomorHp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        findViewById(R.id.layout_username).setVisibility(View.GONE);
        //editTextUserId = (EditText) findViewById(R.id.activity_reset_sandi_EditTextUser);
        editTextNomorHp = (EditText) findViewById(R.id.activity_reset_sandi_EditTexNomorHp);
        findViewById(R.id.activity_reset_sandi_Button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });
    }

    private void validate() {
        /*
        String userId = editTextUserId.getText().toString().trim();
        if (userId.isEmpty()) {
            editTextUserId.requestFocus();
            editTextUserId.setError("Harus diisi");
            return;
        }
         */

        String hp = editTextNomorHp.getText().toString().trim();
        if (hp.isEmpty()) {
            editTextNomorHp.requestFocus();
            editTextNomorHp.setError("Harus diisi");
            return;
        }

        if (!Util.isInternetAvailible(this)) {
            Util.noInternetDialog(this);
            return;
        }

        String url = Config.URL_PROFILE + "?aksi=4&hp=" + hp;
        debug(getClass(), "reset sandi url=" + url);
        final Dialog dialog = ProgressDialog.show(this, null, "Harap tunggu...", true, false);
        VolleyStringRequest request = new VolleyStringRequest(url, new VolleyStringRequest.Callback() {
            @Override
            public void onResponse(JsonObject jsonObject) {
                debug(getClass(), "reset sandi response =" + jsonObject);
                dialog.dismiss();
                Util.showDialog(ForgotPasswordActivity.this, jsonObject.get("message").getAsString());

            }
        });
        request.setTag(VOLLEY_TAG);
        VolleySingleton.getInstance(this).getRequestQueue().add(request);
    }


    @Override
    protected void onDestroy() {
        VolleySingleton.getInstance(this).getRequestQueue().cancelAll(VOLLEY_TAG);
        super.onDestroy();
    }
}
