package com.vickykdv.fingerbottomdialog.interFace;


import com.vickykdv.fingerbottomdialog.Utils.cashItFingerprintTkn;
import com.vickykdv.fingerbottomdialog.cashItFingerBottomDlg;
import com.vickykdv.fingerbottomdialog.cashItFingerprint;

public class cashItGlobalInterface {
    public interface CashitFingerCallback {
        void onAuthenticationSucceeded();
        void onAuthenticationFailed();
        void onAuthenticationError(int errorCode, String error);
    }

    public interface CashitFingerSecureCallback {
        void onAuthenticationSucceeded();
        void onAuthenticationFailed();
        void onNewFingerprintEnrolled(cashItFingerprintTkn token);
        void onAuthenticationError(int errorCode, String error);
    }

    public interface CashitFingerFailCountDlgCallback {
        void onTryLimitReached(cashItFingerBottomDlg fingerprintDialog);
    }

    public interface CashitFingerDlgSecureCallback {
        void onAuthenticationSucceeded();
        void onAuthenticationCancel();
        void onNewFingerprintEnrolled(cashItFingerprintTkn token);
    }

    public interface CashitFingerDlgCallback {
        void onAuthenticationSucceeded();
        void onAuthenticationCancel();
    }

    public interface CashitFingerCountCallback {
        void onTryLimitReached(cashItFingerprint fingerprint);
    }
}
