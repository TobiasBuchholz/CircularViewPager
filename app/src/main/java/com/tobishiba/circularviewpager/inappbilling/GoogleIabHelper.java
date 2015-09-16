package com.tobishiba.circularviewpager.inappbilling;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.*;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;
import com.android.vending.billing.IInAppBillingService;
import com.tobishiba.circularviewpager.R;

/**
 * Helper class for interaction with google wallet service.
 * User: tobiasbuchholz
 * Date: 28.05.13 | Time: 10:56
 */
public class GoogleIabHelper {
    private static final String LOG_TAG                                     = GoogleIabHelper.class.getSimpleName();

    private static final String ITEM_TYPE_INAPP                             = "inapp";
    private static final String ITEM_TYPE_SUBS                              = "subs";

    // Billing response codes
    public static final int         BILLING_RESPONSE_RESULT_OK                  = 0;
    public static final int         BILLING_RESPONSE_RESULT_USER_CANCELED       = 1;
    public static final int         BILLING_RESPONSE_RESULT_BILLING_UNAVAILABLE = 3;
    public static final int         BILLING_RESPONSE_RESULT_ITEM_UNAVAILABLE    = 4;
    public static final int         BILLING_RESPONSE_RESULT_DEVELOPER_ERROR     = 5;
    public static final int         BILLING_RESPONSE_RESULT_ERROR               = 6;
    public static final int         BILLING_RESPONSE_RESULT_ITEM_ALREADY_OWNED  = 7;
    public static final int         BILLING_RESPONSE_RESULT_ITEM_NOT_OWNED      = 8;

    public static final int         REQUEST_CODE_PURCHASE                       = 10101;
    public static final String REQUEST_IAB_ITEM_TYPE                       = "request_iab_item_type";

    // IAB Helper error codes
    public static final int         IABHELPER_BAD_RESPONSE                      = -1002;
    // Keys for the responses from InAppBillingService
    public static final String RESPONSE_CODE                               = "RESPONSE_CODE";
    public static final String RESPONSE_BUY_INTENT                         = "BUY_INTENT";
    public static final String RESPONSE_INAPP_PURCHASE_DATA                = "INAPP_PURCHASE_DATA";
    public static final String RESPONSE_INAPP_SIGNATURE                    = "INAPP_DATA_SIGNATURE";
    public static final String RESPONSE_INAPP_ITEM_LIST                    = "INAPP_PURCHASE_ITEM_LIST";
    public static final String RESPONSE_INAPP_PURCHASE_DATA_LIST           = "INAPP_PURCHASE_DATA_LIST";
    public static final String RESPONSE_INAPP_SIGNATURE_LIST               = "INAPP_DATA_SIGNATURE_LIST";
    public static final String ITEM_ID_LIST                                = "ITEM_ID_LIST";
    public static final String DETAILS_LIST                                = "DETAILS_LIST";

    public static final String INAPP_CONTINUATION_TOKEN                    = "INAPP_CONTINUATION_TOKEN";
    // IAB Helper error codes
    private static final int        IABHELPER_ERROR_BASE                        = -1000;

    private Context mContext;
    private IInAppBillingService mIABService;
    private ServiceConnection mServiceConnection;

    private boolean                 mSupportsIABv3;
    private boolean                 mSupportsSubscription;
    private boolean                 mIsServiceBind                              = false;

    private int                     mRequestCode;

    public GoogleIabHelper(final Context context) {
        mContext = context;
    }

    public void setUpServiceConnection() {
        mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceDisconnected(final ComponentName name) {
                mIABService = null;
            }

            @Override
            public void onServiceConnected(final ComponentName name, final IBinder service) {
                mIABService = IInAppBillingService.Stub.asInterface(service);
                checkSupports();
            }
        };
        bindInAppBillingService();
    }

    private void checkSupports() {
        final String packageName = mContext.getPackageName();
        try {
            int response = mIABService.isBillingSupported(3, packageName, ITEM_TYPE_INAPP);
            mSupportsIABv3 = response == BILLING_RESPONSE_RESULT_OK;

            if(!mSupportsIABv3) {
                Log.w(LOG_TAG, "Device does not support InAppBilling v3. Response code: " + response);
            }

            response = mIABService.isBillingSupported(3, packageName, ITEM_TYPE_SUBS);
            mSupportsSubscription = response == BILLING_RESPONSE_RESULT_OK;

            if(!mSupportsSubscription) {
                Log.w(LOG_TAG, "Subscriptions NOT AVAILABLE. Response code: " + response);
            }
        } catch (RemoteException e) {
            Log.e(LOG_TAG, ":: checkSupports ::" + e, e);
        }
    }

    private void bindInAppBillingService() {
        final Intent serviceIntent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");
        if (!mContext.getPackageManager().queryIntentServices(serviceIntent, 0).isEmpty()) {
            mContext.bindService(serviceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
            mIsServiceBind = true;
        } else {
            Log.e(LOG_TAG, "Billing service unavailable on device.");
        }
    }

    public void unbindServiceConnection() {
        if (mServiceConnection != null) {
            if(mIsServiceBind) {
                mContext.unbindService(mServiceConnection);
                mIsServiceBind = false;
            }
            mServiceConnection = null;
            mIABService = null;
            mSupportsIABv3 = false;
            mSupportsSubscription = false;
        }
    }

    public boolean isReady() {
        return mIsServiceBind && mSupportsIABv3;
    }

    public boolean isValidPurchaseIntent(final int requestCode, final Intent intent) {
        if(requestCode == mRequestCode) {
            final int responseCode = getResponseCode(intent);
            final String purchaseData = intent.getStringExtra(RESPONSE_INAPP_PURCHASE_DATA);
            final String dataSignature = intent.getStringExtra(RESPONSE_INAPP_SIGNATURE);

            if (responseCode == BILLING_RESPONSE_RESULT_OK) {
                if (purchaseData != null && dataSignature != null) {
                    return true;
                }
                Log.e(LOG_TAG, "Either purchaseData or dataSignature are null");
            }
        } else {
            Log.e(LOG_TAG, ":: isValidPurchaseIntent :: requestCode didn't match: expected = " + requestCode + " <-> but was = " + mRequestCode);
        }
        return false;
    }

    public void purchaseItem(final Activity activity, final String productId) {
        purchaseItem(activity, productId, ITEM_TYPE_INAPP);
    }

    private void purchaseItem(final Activity activity, final String productId, final String itemType) {
        try {
            tryPurchaseItem(activity, productId, itemType);
        } catch (Exception e) {
            Log.e(LOG_TAG, ":: purchaseItem ::" + e, e);
        }
    }

    private void tryPurchaseItem(final Activity activity, final String productId, final String itemType) throws RemoteException, IntentSender.SendIntentException {
        final Bundle buyIntentBundle = mIABService.getBuyIntent(3, mContext.getPackageName(), productId, itemType, null);
        final int responseCode = getResponseCode(buyIntentBundle);

        if(responseCode == BILLING_RESPONSE_RESULT_OK) {
            mRequestCode = REQUEST_CODE_PURCHASE;
            final PendingIntent pendingIntent = buyIntentBundle.getParcelable(RESPONSE_BUY_INTENT);
            activity.startIntentSenderForResult(pendingIntent.getIntentSender(), REQUEST_CODE_PURCHASE, new Intent(), 0, 0, 0);
        } else {
            handlePurchaseError(activity, responseCode);
        }
    }

    private void handlePurchaseError(final Activity activity, final int responseCode) {
        final String description = getResponseDescription(responseCode);
        Toast.makeText(mContext, activity.getString(R.string.toast_error_in_app_billing, description), Toast.LENGTH_SHORT).show();
        Log.e(LOG_TAG, "Problem with purchasing item. Response Code: " + description);
    }

    public void consumeItemAsync(final String purchaseToken) {
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(final Void... params) {
                return consumeItem(purchaseToken);
            }
        }.execute();
    }

    private boolean consumeItem(final String purchaseToken) {
        try {
            final int responseCode = mIABService.consumePurchase(3, mContext.getPackageName(), purchaseToken);
            if(responseCode == BILLING_RESPONSE_RESULT_OK) {
                return true;
            } else {
                Log.w(LOG_TAG, "consumeItem() failed: " + getResponseDescription(responseCode));
            }
        } catch (RemoteException e) {
            Log.e(LOG_TAG, ":: consumeItem ::" + e, e);
        }
        return false;
    }

    public String getPurchaseReceipt(final Intent intent) {
        return intent.getStringExtra(GoogleIabHelper.RESPONSE_INAPP_PURCHASE_DATA);
    }

    /** Workaround to bug where sometimes response codes come as Long instead of Integer (from android example). */
    private int getResponseCode(final Object responseObject) {
        Object responseCodeObject = null;
        if(responseObject instanceof Intent) {
            responseCodeObject = ((Intent) responseObject).getExtras().get(RESPONSE_CODE);
        } else if(responseObject instanceof Bundle) {
            responseCodeObject = ((Bundle) responseObject).get(RESPONSE_CODE);
        }

        if(responseCodeObject == null) {
            Log.i(LOG_TAG, "Bundle with null response code, assuming OK (known issue)");
            return BILLING_RESPONSE_RESULT_OK;
        } else if(responseCodeObject instanceof Integer) {
            return (Integer) responseCodeObject;
        } else if(responseCodeObject instanceof Long) {
            return (int)((Long) responseCodeObject).longValue();
        } else {
            Log.e(LOG_TAG, "Unexpected type for bundle response code: " + responseCodeObject.getClass().getName());
        }
        return BILLING_RESPONSE_RESULT_ERROR;
    }

    /** Returns a human-readable description for the given response code (from android example). */
    private String getResponseDescription(int code) {
        final String[] iabMessages = ("0: OK/" +
                                      "1: User Canceled/" +
                                      "2: Unknown/" +
                                      "3: Billing Unavailable/" +
                                      "4: Item unavailable/" +
                                      "5: Developer Error/" +
                                      "6: Error/" +
                                      "7: Item Already Owned/" +
                                      "8: Item not owned")
                                      .split("/");

        final String[] iabHelperMessages = ("0: OK/-1001:Remote exception during initialization/" +
                                            "-1002: Bad response received/" +
                                            "-1003: Purchase signature verification failed/" +
                                            "-1004: Send intent failed/" +
                                            "-1005: User cancelled/" +
                                            "-1006: Unknown purchase response/" +
                                            "-1007: Missing token/" +
                                            "-1008: Unknown error/" +
                                            "-1009: Subscriptions not available/" +
                                            "-1010: Invalid consumption attempt")
                                            .split("/");

        if (code <= IABHELPER_ERROR_BASE) {
            final int index = IABHELPER_ERROR_BASE - code;
            if (index >= 0 && index < iabHelperMessages.length) {
                return iabHelperMessages[index];
            } else {
                return code + ": Unknown IAB Helper Error";
            }
        } else if (code < 0 || code >= iabMessages.length) {
            return code + ": Unknown";
        } else {
            return iabMessages[code];
        }
    }
}
