package com.zam.sidik_sumenep.profilku.model;

import com.google.gson.annotations.SerializedName;

public class JenisUsaha {

    @SerializedName("jenis")
    private String jenis;

    @SerializedName("id")
    private String id;

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public String getJenis() {
        return jenis;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}