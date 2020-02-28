package com.vickykdv.fingerbottomdialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.hardware.fingerprint.FingerprintManager;
import android.os.CancellationSignal;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.vickykdv.fingerbottomdialog.Utils.cashItCipherHelper;
import com.vickykdv.fingerbottomdialog.Utils.cashItFingerprintTkn;
import com.vickykdv.fingerbottomdialog.interFace.cashItGlobalInterface;


public class cashItFingerprint extends RelativeLayout {
    private final static String TAG = "FingerprintView";

    private View fingerprintImageView;
    private View circleView;

    private FingerprintManager fingerprintManager;
    private CancellationSignal cancellationSignal;
    private cashItGlobalInterface.CashitFingerCallback fingerprintCallback;
    private cashItGlobalInterface.CashitFingerSecureCallback fingerprintSecureCallback;
    private cashItGlobalInterface.CashitFingerCountCallback counterCallback;
    private FingerprintManager.CryptoObject cryptoObject;
    private cashItCipherHelper cipherHelper;
    private Handler handler;

    private int fingerprintScanning, fingerprintSuccess, fingerprintError;
    private int circleScanning, circleSuccess, circleError;

    private int limit, tryCounter;
    private int delayAfterError;
    private int size;

    public final static int DEFAULT_DELAY_AFTER_ERROR = 1200;
    public final static int DEFAULT_CIRCLE_SIZE = 50;
    public final static int DEFAULT_FINGERPRINT_SIZE = 30;
    public final static float SCALE = (float) DEFAULT_FINGERPRINT_SIZE/DEFAULT_CIRCLE_SIZE;

    public cashItFingerprint(Context context) {
        super(context);
    }

    public cashItFingerprint(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttributes(context, attrs, 0, 0);
        initView(context);
    }

    public cashItFingerprint(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttributes(context, attrs, defStyleAttr, 0);
        initView(context);
    }

    @SuppressLint("NewApi")
    public cashItFingerprint(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAttributes(context, attrs, defStyleAttr, defStyleRes);
        initView(context);
    }

    @SuppressLint("ResourceType")
    private void initAttributes(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes){
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.cashItFingerprint, defStyleAttr, defStyleRes);
        try {
            fingerprintScanning = a.getResourceId(R.styleable.cashItFingerprint_fingerprintScanningColor, R.color.fingerprint_scanning);
            fingerprintSuccess = a.getResourceId(R.styleable.cashItFingerprint_fingerprintSuccessColor, R.color.fingerprint_success);
            fingerprintError = a.getResourceId(R.styleable.cashItFingerprint_fingerprintErrorColor, R.color.fingerprint_error);

            circleScanning = a.getResourceId(R.styleable.cashItFingerprint_circleScanningColor, R.color.circle_scanning);
            circleSuccess = a.getResourceId(R.styleable.cashItFingerprint_circleSuccessColor, R.color.circle_success);
            circleError = a.getResourceId(R.styleable.cashItFingerprint_circleErrorColor, R.color.circle_error);
        } finally {
            a.recycle();
        }

        int[] systemAttrs = {android.R.attr.layout_width, android.R.attr.layout_height};
        TypedArray ta = context.obtainStyledAttributes(attrs, systemAttrs);
        try {
            int width = ta.getLayoutDimension(0, ViewGroup.LayoutParams.WRAP_CONTENT);
            int height = ta.getLayoutDimension(1, ViewGroup.LayoutParams.WRAP_CONTENT);
            if(width==-2 || height==-2) {
                size = dipToPixels(DEFAULT_CIRCLE_SIZE);
            } else{
                size = width;
            }
        } finally {
            ta.recycle();
        }
    }

    @SuppressLint("NewApi")
    private void initView(Context context){
        this.fingerprintManager = (FingerprintManager) context.getSystemService(Context.FINGERPRINT_SERVICE);
        this.cipherHelper = null;
        this.handler = new Handler();
        this.fingerprintCallback = null;
        this.fingerprintSecureCallback = null;
        this.counterCallback = null;
        this.cryptoObject = null;
        this.tryCounter = 0;
        this.delayAfterError = DEFAULT_DELAY_AFTER_ERROR;

        int fingerprintSize = (int) (size*SCALE);
        int circleSize = size;

        fingerprintImageView = new AppCompatImageView(context);
        fingerprintImageView.setLayoutParams(new LayoutParams(fingerprintSize, fingerprintSize));
        fingerprintImageView.setBackgroundResource(R.drawable.fingerprint);
        ((LayoutParams)fingerprintImageView.getLayoutParams()).addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

        circleView = new View(context);
        circleView.setLayoutParams(new LayoutParams(circleSize, circleSize));
        circleView.setBackgroundResource(R.drawable.circle);
        ((LayoutParams)circleView.getLayoutParams()).addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

        setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        addView(circleView);
        addView(fingerprintImageView);

        setStatus(R.drawable.fingerprint, fingerprintScanning, circleScanning);
    }

    private int dipToPixels(int dipValue) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }

    @SuppressLint("NewApi")
    private void setStatus(int drawableId, int drawableColorId, int circleColorId){
        Context context = getContext();
        fingerprintImageView.setBackgroundResource(drawableId);
        fingerprintImageView.setBackgroundTintList(ColorStateList.valueOf(context.getColor(drawableColorId)));
        circleView.setBackgroundTintList(ColorStateList.valueOf(context.getColor(circleColorId)));
    }

    private Runnable returnToScanning = new Runnable() {
        @Override
        public void run() {
            setStatus(R.drawable.fingerprint, fingerprintScanning, circleScanning);
        }
    };

    private Runnable checkForLimit = new Runnable() {
        @Override
        public void run() {
            if(counterCallback!=null && ++tryCounter==limit){
                counterCallback.onTryLimitReached(cashItFingerprint.this);
            }
        }
    };

    /**
     * Set fingerprint callback.
     * @param fingerprintCallback callback
     * @return the Fingerprint object itself
     */
    public cashItFingerprint callback(cashItGlobalInterface.CashitFingerCallback fingerprintCallback){
        this.fingerprintCallback = fingerprintCallback;
        return this;
    }

    /**
     * Set fingerprint secure callback.
     * @param fingerprintSecureCallback secure callback
     * @param KEY_NAME key that will be used for the cipher
     * @return the Fingerprint object itself
     */
    public cashItFingerprint callback(cashItGlobalInterface.CashitFingerSecureCallback fingerprintSecureCallback, String KEY_NAME){
        this.fingerprintSecureCallback = fingerprintSecureCallback;
        this.cipherHelper = new cashItCipherHelper(KEY_NAME);
        return this;
    }

    /**
     * Set a CryptoObject which is going to be unlocked by the fingerprint.
     * @param cryptoObject CryptoObject to be unlocked
     * @return the Fingerprint object itself
     */
    public cashItFingerprint cryptoObject(FingerprintManager.CryptoObject cryptoObject){
        this.cryptoObject = cryptoObject;
        return this;
    }

    /**
     * Set the fingerprint icon color in scanning state.
     * @param fingerprintScanning color id
     * @return the Fingerprint object itself
     */
    @SuppressLint("NewApi")
    public cashItFingerprint cashItfingerprintScanColor(int fingerprintScanning) {
        this.fingerprintScanning = fingerprintScanning;
        this.fingerprintImageView.setBackgroundTintList(ColorStateList.valueOf(getContext().getColor(fingerprintScanning)));
        return this;
    }

    /**
     * Set the fingerprint icon color in success state.
     * @param fingerprintSuccess color id
     * @return the Fingerprint object itself
     */
    @SuppressLint("NewApi")
    public cashItFingerprint cashItfingerprintSuccessColor(int fingerprintSuccess) {
        this.fingerprintSuccess = fingerprintSuccess;
        this.fingerprintImageView.setBackgroundTintList(ColorStateList.valueOf(getContext().getColor(fingerprintSuccess)));
        return this;
    }

    /**
     * Set the fingerprint icon color in error state.
     * @param fingerprintError color id
     * @return the Fingerprint object itself
     */
    @SuppressLint("NewApi")
    public cashItFingerprint cashItfingerprintErrorColor(int fingerprintError) {
        this.fingerprintError = fingerprintError;
        this.fingerprintImageView.setBackgroundTintList(ColorStateList.valueOf(getContext().getColor(fingerprintError)));
        return this;
    }

    /**
     * Set the fingerprint circular background color in scanning state.
     * @param circleScanning color id
     * @return the Fingerprint object itself
     */
    @SuppressLint("NewApi")
    public cashItFingerprint circleScanningColor(int circleScanning) {
        this.circleScanning = circleScanning;
        this.circleView.setBackgroundTintList(ColorStateList.valueOf(getContext().getColor(circleScanning)));
        return this;
    }

    /**
     * Set the fingerprint circular background color in success state.
     * @param circleSuccess color id
     * @return the Fingerprint object itself
     */
    @SuppressLint("NewApi")
    public cashItFingerprint circleSuccessColor(int circleSuccess) {
        this.circleSuccess = circleSuccess;
        this.circleView.setBackgroundTintList(ColorStateList.valueOf(getContext().getColor(circleSuccess)));
        return this;
    }

    /**
     * Set the fingerprint circular background color in error state.
     * @param circleError color id
     * @return the Fingerprint object itself
     */
    @SuppressLint("NewApi")
    public cashItFingerprint circleErrorColor(int circleError) {
        this.circleError = circleError;
        this.circleView.setBackgroundTintList(ColorStateList.valueOf(getContext().getColor(circleError)));
        return this;
    }

    /**
     * Set a failing authentication limit. Android will block automatically the fingerprint scanner when 5 attempts will have failed.
     * @param limit the number of fails accepted
     * @param counterCallback a callback triggered when limit is reached
     * @return the Fingerprint object itself
     */
    public cashItFingerprint tryLimit(int limit, cashItGlobalInterface.CashitFingerCountCallback counterCallback){
        this.limit = limit;
        this.counterCallback = counterCallback;
        return this;
    }

    /**
     * Set delay before scanning again after a failed authentication.
     * @param delayAfterError the delay in milliseconds
     * @return the Fingerprint object itself
     */
    public cashItFingerprint delayAfterError(int delayAfterError){
        this.delayAfterError = delayAfterError;
        return this;
    }

    /**
     * Check if fingerprint authentication is supported by the device and if a fingerprint is enrolled in the device.
     * @param context an activity context
     * @return a boolean value
     */
    @SuppressLint("NewApi")
    public static boolean isAvailable(Context context){
        FingerprintManager fingerprintManager = (FingerprintManager) context.getSystemService(Context.FINGERPRINT_SERVICE);
        if(fingerprintManager!=null){
            return (fingerprintManager.isHardwareDetected() && fingerprintManager.hasEnrolledFingerprints());
        }
        return false;
    }

    /**
     * start fingerprint scan
     */
    @SuppressLint("NewApi")
    public void authenticate(){
        if(fingerprintSecureCallback!=null){
            if(cryptoObject!=null){
                throw new RuntimeException("If you specify a CryptoObject you have to use CashitFingerCallback");
            }
            cryptoObject = cipherHelper.getEncryptionCryptoObject();
            if(cryptoObject==null) {
                fingerprintSecureCallback.onNewFingerprintEnrolled(new cashItFingerprintTkn(cipherHelper));
            }
        }
        else if(fingerprintCallback==null){
            throw new RuntimeException("You must specify a callback.");
        }

        cancellationSignal = new CancellationSignal();
        if(fingerprintManager.isHardwareDetected() && fingerprintManager.hasEnrolledFingerprints()) {
            fingerprintManager.authenticate(cryptoObject, cancellationSignal, 0, new FingerprintManager.AuthenticationCallback() {
                @Override
                public void onAuthenticationError(int errorCode, CharSequence errString) {
                    super.onAuthenticationError(errorCode, errString);
                    setStatus(R.drawable.fingerprint_error, fingerprintError, circleError);
                    handler.postDelayed(returnToScanning, delayAfterError);
                    if(fingerprintSecureCallback!=null){
                        fingerprintSecureCallback.onAuthenticationError(errorCode, errString.toString());
                    } else{
                        fingerprintCallback.onAuthenticationError(errorCode, errString.toString());
                    }
                }

                @Override
                public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
                    super.onAuthenticationHelp(helpCode, helpString);
                    setStatus(R.drawable.fingerprint_error, fingerprintError, circleError);
                    handler.postDelayed(returnToScanning, delayAfterError);
                    if(fingerprintSecureCallback!=null){
                        fingerprintSecureCallback.onAuthenticationError(helpCode, helpString.toString());
                    } else{
                        fingerprintCallback.onAuthenticationError(helpCode, helpString.toString());
                    }
                }

                @Override
                public void onAuthenticationSucceeded(final FingerprintManager.AuthenticationResult result) {
                    super.onAuthenticationSucceeded(result);
                    handler.removeCallbacks(returnToScanning);
                    setStatus(R.drawable.fingerprint_success, fingerprintSuccess, circleSuccess);
                    if(fingerprintSecureCallback!=null){
                        fingerprintSecureCallback.onAuthenticationSucceeded();
                    }
                    else{
                        fingerprintCallback.onAuthenticationSucceeded();
                    }
                    tryCounter = 0;
                }

                @Override
                public void onAuthenticationFailed() {
                    super.onAuthenticationFailed();
                    setStatus(R.drawable.fingerprint_error, fingerprintError, circleError);
                    handler.postDelayed(returnToScanning, delayAfterError);
                    handler.postDelayed(checkForLimit, delayAfterError);
                    if(fingerprintSecureCallback!=null){
                        fingerprintSecureCallback.onAuthenticationFailed();
                    }
                    else{
                        fingerprintCallback.onAuthenticationFailed();
                    }
                }
            }, null);
        }
        else{
            Log.e(TAG, "fingerPrint tidak terdeteksi, gunakan pengecekan dengan fungsi isAvailabel pada oncreate");
        }
    }

    /**
     * cancel fingerprint scan
     */
    public void cancel(){
        cancellationSignal.cancel();
        returnToScanning.run();
    }
}