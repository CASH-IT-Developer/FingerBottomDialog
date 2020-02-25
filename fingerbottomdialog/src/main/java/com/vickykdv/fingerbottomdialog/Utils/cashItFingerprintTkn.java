package com.vickykdv.fingerbottomdialog.Utils;

/**
 * Created by Omar on 10/07/2017.
 */

public class cashItFingerprintTkn {
    private cashItCipherHelper cipherHelper;

    public cashItFingerprintTkn(cashItCipherHelper cipherHelper) {
        this.cipherHelper = cipherHelper;
    }

    public void validate(){
        if(cipherHelper !=null) {
            cipherHelper.generateNewKey();
        }
    }
}
