package com.bgenterprise.transporterapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Utility {

    public static boolean isConnectedToNetwork(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        boolean isConnected = false;
        if (connectivityManager != null) {
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            isConnected = (activeNetwork != null) && (activeNetwork.isConnectedOrConnecting());
        }

        return isConnected;
    }

    public static final String[] payment_option = new String[] {"Cash", "BG Prepaid Card", "Bank Account"};

    public static final String[] bank_names = new String[] {"Access Bank",
            "ALAT by WEMA",
            "ASO Savings and Loans",
            "Citibank Nigeria",
            "Ecobank Nigeria",
            "Ekondo Microfinance Bank",
            "Fidelity Bank",
            "First Bank of Nigeria",
            "First City Monument Bank",
            "Guaranty Trust Bank",
            "Heritage Bank",
            "Jaiz Bank",
            "Keystone Bank",
            "Parallex Bank",
            "Polaris Bank",
            "Providus Bank",
            "Stanbic IBTC Bank",
            "Standard Chartered Bank",
            "Sterling Bank",
            "Suntrust Bank",
            "Union Bank of Nigeria",
            "United Bank For Africa",
            "Unity Bank",
            "Wema Bank",
            "Zenith Bank"};


}
