package com.vickykdv.fingerbottomdialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.res.ResourcesCompat;

import com.vickykdv.fingerbottomdialog.Utils.cashItFingerprintTkn;
import com.vickykdv.fingerbottomdialog.interFace.cashItGlobalInterface;

public class cashItFingerBottomDlg extends cashItBottomDialog<cashItFingerBottomDlg> {
    private cashItFingerprint fingerprint;
    private TextView dialogTitle, dialogMessage, dialogStatus;
    private AppCompatButton cancelButton;

    private cashItGlobalInterface.CashitFingerDlgCallback fingerprintDialogCallback;
    private cashItGlobalInterface.CashitFingerDlgSecureCallback fingerprintDialogSecureCallback;

    private int statusScanningColor, statusSuccessColor, statusErrorColor;
    private Handler handler;

    private int delayAfterError, delayAfterSuccess;

    private final static String TAG = "cashItFingerBottomDlg";

    private cashItFingerBottomDlg(Context context){
        super(context);
        init();
    }

    private void init(){
        this.handler = new Handler();
        this.delayAfterError = cashItFingerprint.DEFAULT_DELAY_AFTER_ERROR;
        this.delayAfterSuccess = cashItFingerprint.DEFAULT_DELAY_AFTER_ERROR;

        this.statusScanningColor = R.color.status_scanning;
        this.statusSuccessColor = R.color.status_success;
        this.statusErrorColor = R.color.status_error;

        this.dialogView = layoutInflater.inflate(R.layout.fingerprint_dialog, null);
        this.fingerprint = dialogView.findViewById(R.id.fingerprint_dialog_fp);
        this.dialogTitle = dialogView.findViewById(R.id.fingerprint_dialog_title);
        this.dialogMessage = dialogView.findViewById(R.id.fingerprint_dialog_message);
        this.dialogStatus = dialogView.findViewById(R.id.fingerprint_dialog_status);
        this.cancelButton = dialogView.findViewById(R.id.fingerprint_dialog_cancel);
    }



    /**
     * Check if a fingerprint scanner is available and if at least one finger is enrolled in the phone.
     * @param context A context
     * @return True is authentication is available, False otherwise
     */
    @SuppressLint("NewApi")
    public static boolean isAvailable(Context context){
        FingerprintManager manager = (FingerprintManager) context.getSystemService(Context.FINGERPRINT_SERVICE);
        return (manager!=null && manager.isHardwareDetected() && manager.hasEnrolledFingerprints());
    }

    /**
     * Create a cashItFingerBottomDlg instance.
     * @param context Activity Context
     * @return cashItFingerBottomDlg instance
     */
    public static cashItFingerBottomDlg initialize(Context context){
        return new cashItFingerBottomDlg(context);
    }

    /**
     * Set an authentication callback.
     * @param fingerprintDialogCallback The callback
     * @return cashItFingerBottomDlg object
     */
    public cashItFingerBottomDlg callback(cashItGlobalInterface.CashitFingerDlgCallback fingerprintDialogCallback){
        this.fingerprintDialogCallback = fingerprintDialogCallback;
        this.fingerprint.callback(fingerprintCallback);
        return this;
    }

    /**
     * Set a callback for secured authentication.
     * @param fingerprintDialogSecureCallback The callback
     * @param KEY_NAME An arbitrary string used to create a cipher pair in the Android KeyStore
     * @return cashItFingerBottomDlg object
     */
    public cashItFingerBottomDlg callback(cashItGlobalInterface.CashitFingerDlgSecureCallback fingerprintDialogSecureCallback, String KEY_NAME){
        this.fingerprintDialogSecureCallback = fingerprintDialogSecureCallback;
        this.fingerprint.callback(fingerprintSecureCallback, KEY_NAME);
        return this;
    }

    /**
     * Perform a secured authentication using that particular CryptoObject.
     * @param cryptoObject CryptoObject to use
     * @return cashItFingerBottomDlg object
     */
    public cashItFingerBottomDlg cryptoObject(FingerprintManager.CryptoObject cryptoObject){
        this.fingerprint.cryptoObject(cryptoObject);
        return this;
    }

    /**
     * Set color of the fingerprint scanning status.
     * @param fingerprintScanningColor resource color
     * @return cashItFingerBottomDlg object
     */
    public cashItFingerBottomDlg fingerprintScanningColor(int fingerprintScanningColor){
        this.fingerprint.cashItfingerprintScanColor(fingerprintScanningColor);
        return this;
    }

    /**
     * Set color of the fingerprint success status.
     * @param fingerprintSuccessColor resource color
     * @return cashItFingerBottomDlg object
     */
    public cashItFingerBottomDlg fingerprintSuccessColor(int fingerprintSuccessColor){
        this.fingerprint.cashItfingerprintSuccessColor(fingerprintSuccessColor);
        return this;
    }

    /**
     * Set color of the fingerprint error status.
     * @param fingerprintErrorColor resource color
     * @return cashItFingerBottomDlg object
     */
    public cashItFingerBottomDlg fingerprintErrorColor(int fingerprintErrorColor){
        this.fingerprint.cashItfingerprintErrorColor(fingerprintErrorColor);
        return this;
    }

    /**
     * Set color of the circle scanning status.
     * @param circleScanningColor resource color
     * @return cashItFingerBottomDlg object
     */
    public cashItFingerBottomDlg circleScanningColor(int circleScanningColor){
        this.fingerprint.circleScanningColor(circleScanningColor);
        return this;
    }

    /**
     * Set color of the circle success status.
     * @param circleSuccessColor resource color
     * @return cashItFingerBottomDlg object
     */
    public cashItFingerBottomDlg circleSuccessColor(int circleSuccessColor){
        this.fingerprint.circleSuccessColor(circleSuccessColor);
        return this;
    }

    /**
     * Set color of the circle error status.
     * @param circleErrorColor resource color
     * @return cashItFingerBottomDlg object
     */
    public cashItFingerBottomDlg circleErrorColor(int circleErrorColor){
        this.fingerprint.circleErrorColor(circleErrorColor);
        return this;
    }

    /**
     * Set color of the text scanning status.
     * @param statusScanningColor resource color
     * @return cashItFingerBottomDlg object
     */
    public cashItFingerBottomDlg statusScanningColor(int statusScanningColor){
        this.statusScanningColor = statusScanningColor;
        return this;
    }

    /**
     * Set color of the text success status.
     * @param statusSuccessColor resource color
     * @return cashItFingerBottomDlg object
     */
    public cashItFingerBottomDlg statusSuccessColor(int statusSuccessColor){
        this.statusSuccessColor = statusSuccessColor;
        return this;
    }

    /**
     * Set color of the text error status.
     * @param statusErrorColor resource color
     * @return cashItFingerBottomDlg object
     */
    public cashItFingerBottomDlg statusErrorColor(int statusErrorColor){
        this.statusErrorColor = statusErrorColor;
        return this;
    }

    /**
     * Set delay before triggering callback after a failed attempt to authenticate.
     * @param delayAfterError delay in milliseconds
     * @return cashItFingerBottomDlg object
     */
    public cashItFingerBottomDlg delayAfterError(int delayAfterError){
        this.delayAfterError = delayAfterError;
        this.fingerprint.delayAfterError(delayAfterError);
        return this;
    }

    /**
     * Set delay before triggering callback after a successful authentication.
     * @param delayAfterSuccess delay in milliseconds
     * @return cashItFingerBottomDlg object
     */
    public cashItFingerBottomDlg delayAfterSuccess(int delayAfterSuccess){
        this.delayAfterSuccess = delayAfterSuccess;
        return this;
    }

    /**
     * Set a fail limit. Android blocks automatically when 5 attempts failed.
     * @param limit number of tries
     * @param counterCallback callback to be triggered when limit is reached
     * @return cashItFingerBottomDlg object
     */
    public cashItFingerBottomDlg tryLimit(int limit, final cashItGlobalInterface.CashitFingerFailCountDlgCallback counterCallback){
        this.fingerprint.tryLimit(limit, new cashItGlobalInterface.CashitFingerCountCallback() {
            @Override
            public void onTryLimitReached(cashItFingerprint fingerprint) {
                counterCallback.onTryLimitReached(cashItFingerBottomDlg.this);
            }
        });
        return this;
    }

    /**
     * Display a "use password" button on the dialog.
     * @param onUsePassword OnClickListener triggered when button is clicked
     * @return cashItFingerBottomDlg object
     */

    /**
     * Show the dialog.
     */
    public void show(){
        if(title==null || message==null){
            throw new RuntimeException("Title or message cannot be null.");
        }

        showDialog();
    }

    /**
     * Dismiss the dialog.
     */
    public void dismiss(){
        fingerprint.cancel();
        if(dialog.isShowing()){
            dialog.dismiss();
        }
    }

    @SuppressLint("NewApi")
    private void showDialog(){
        dialogTitle.setText(title);
        dialogMessage.setText(message);
        cancelButton.setText(R.string.fingerprint_cancel);
        setStatus(R.string.fingerprint_state_scanning, statusScanningColor);



        dialog.setContentView(dialogView);
        dialog.create();

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fingerprint.cancel();
                if(fingerprintDialogSecureCallback!=null){
                    fingerprintDialogSecureCallback.onAuthenticationCancel();
                }
                else{
                    fingerprintDialogCallback.onAuthenticationCancel();
                }
                dialog.cancel();
            }
        });


        dialog.setCanceledOnTouchOutside(cancelOnTouchOutside);
        dialog.setCancelable(cancelOnPressBack);
        dialog.show();

        authenticate();
    }

    private void authenticate(){
        fingerprint.authenticate();
    }

    private void setStatus(int textId, int textColorId){
        setStatus(context.getResources().getString(textId), textColorId);
    }

    private void setStatus(String text, int textColorId){
        dialogStatus.setTextColor(ResourcesCompat.getColor(context.getResources(), textColorId, context.getTheme()));
        dialogStatus.setText(text);
    }

    private Runnable returnToScanning = new Runnable() {
        @Override
        public void run() {
            setStatus(R.string.fingerprint_state_scanning, statusScanningColor);
        }
    };

    private cashItGlobalInterface.CashitFingerCallback fingerprintCallback = new cashItGlobalInterface.CashitFingerCallback() {
        @Override
        public void onAuthenticationSucceeded() {
            handler.removeCallbacks(returnToScanning);
            setStatus(R.string.fingerprint_state_success, statusSuccessColor);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    dialog.cancel();
                    if(fingerprintDialogCallback!=null){
                        fingerprintDialogCallback.onAuthenticationSucceeded();
                    }
                }
            }, delayAfterSuccess);
        }

        @Override
        public void onAuthenticationFailed() {
            setStatus(R.string.fingerprint_state_failure, statusErrorColor);
            handler.postDelayed(returnToScanning, delayAfterError);
        }

        @Override
        public void onAuthenticationError(int errorCode, String error) {
            setStatus(error, statusErrorColor);
            handler.postDelayed(returnToScanning, delayAfterError);
        }
    };

    private cashItGlobalInterface.CashitFingerSecureCallback fingerprintSecureCallback = new cashItGlobalInterface.CashitFingerSecureCallback() {
        @Override
        public void onAuthenticationSucceeded() {
            handler.removeCallbacks(returnToScanning);
            setStatus(R.string.fingerprint_state_success, statusSuccessColor);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    dialog.cancel();
                    if(fingerprintDialogSecureCallback!=null){
                        fingerprintDialogSecureCallback.onAuthenticationSucceeded();
                    }
                }
            }, delayAfterSuccess);
        }

        @Override
        public void onAuthenticationFailed() {
            setStatus(R.string.fingerprint_state_failure, statusErrorColor);
            handler.postDelayed(returnToScanning, delayAfterError);
        }

        @Override
        public void onNewFingerprintEnrolled(cashItFingerprintTkn token) {
            dialog.cancel();
            if(fingerprintDialogSecureCallback!=null){
                fingerprintDialogSecureCallback.onNewFingerprintEnrolled(token);
            }
        }

        @Override
        public void onAuthenticationError(int errorCode, String error) {
            setStatus(error, statusErrorColor);
            handler.postDelayed(returnToScanning, delayAfterError);
        }
    };
}