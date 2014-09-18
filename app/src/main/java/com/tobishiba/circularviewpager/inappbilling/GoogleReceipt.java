package com.tobishiba.circularviewpager.inappbilling;

import com.google.gson.annotations.SerializedName;

/**
 * User: tobiasbuchholz
 * Date: 10.12.13 | Time: 11:41
 */
public class GoogleReceipt {
    @SerializedName("orderId")
    public String mOrderId;

    @SerializedName("packageName")
    public String mPackageName;

    @SerializedName("productId")
    public String mProductId;

    @SerializedName("purchaseTime")
    public String mPurchaseTime;

    @SerializedName("purchaseState")
    public String mPurchaseState;

    @SerializedName("purchaseToken")
    public String mPurchaseToken;
}
