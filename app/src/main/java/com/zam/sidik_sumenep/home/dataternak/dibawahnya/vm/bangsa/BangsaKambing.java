package com.zam.sidik_sumenep.home.dataternak.dibawahnya.vm.bangsa;

import com.google.gson.annotations.SerializedName;

public class BangsaKambing {

	@SerializedName("keterangan")
	private String keterangan;

	@SerializedName("id_bangsa")
	private String idBangsa;

	public void setKeterangan(String keterangan){
		this.keterangan = keterangan;
	}

	public String getKeterangan(){
		return keterangan;
	}

	public void setIdBangsa(String idBangsa){
		this.idBangsa = idBangsa;
	}

	public String getIdBangsa(){
		return idBangsa;
	}
}