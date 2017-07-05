package com.programming.kantech.bakingmagic.app.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import java.text.DecimalFormat;

/**
 * Created by patrick keogh on 2017-06-23.
 *
 */

public class Utils_General {

    public static String getFormattedMeasurement(Context context, Double qty, String uom){

        String formatted_string = "";

        DecimalFormat format = new DecimalFormat();
        format.setDecimalSeparatorAlwaysShown(false);

        formatted_string += format.format(qty);
        formatted_string += " " + uom.toLowerCase();



        return formatted_string;
    }

    /**
     * Show a toast message.
     */
    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Returns true if the network is available or about to become available.
     *
     * @param c Context used to get the ConnectivityManager
     * @return true if the network is available
     */
    public static boolean isNetworkAvailable(Context c) {
        ConnectivityManager cm =
                (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isAvailable() &&
                activeNetwork.isConnected();


    }


    /**
     * Ensure this class is only used as a utility.
     */
    private Utils_General() {
        throw new AssertionError();
    }


}
