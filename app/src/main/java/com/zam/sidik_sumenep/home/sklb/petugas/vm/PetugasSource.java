package com.zam.sidik_sumenep.home.sklb.petugas.vm;

import com.zam.sidik_sumenep.home.sklb.petugas.vm.pemilik.PemilikTernak;

/**
 * Created by AbangAzmi on 23/03/2018.
 */

public class PetugasSource {

    private static PetugasSource INSTANCE;

    private Petugas petugas;
    private PemilikTernak pemilikTernak;


    private PetugasSource() {
    }

    public synchronized static PetugasSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PetugasSource();
        }
        return INSTANCE;
    }

    private boolean isPetugasUpdated = false;
    private boolean isPemilikUpdated = false;

    public boolean isPetugasUpdated() {
        return isPetugasUpdated;
    }

    public void setPetugasUpdated(boolean petugasUpdated) {
        isPetugasUpdated = petugasUpdated;
    }

    public boolean isPemilikUpdated() {
        return isPemilikUpdated;
    }

    public void setPemilikUpdated(boolean pemilikUpdated) {
        isPemilikUpdated = pemilikUpdated;
    }

    public Petugas getPetugas() {
        return petugas;
    }

    public void setPetugas(Petugas petugas) {
        this.petugas = petugas;
    }

    public PemilikTernak getPemilikTernak() {
        return pemilikTernak;
    }

    public void setPemilikTernak(PemilikTernak pemilikTernak) {
        this.pemilikTernak = pemilikTernak;
    }
}