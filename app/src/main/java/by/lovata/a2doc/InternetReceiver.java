package by.lovata.a2doc;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.app.AlertDialog;

public class InternetReceiver extends BroadcastReceiver {

    public static boolean status = true;
    private static AlertDialog alertDialog;


    @Override
    public void onReceive(Context context, Intent intent) {
        checkStatus(context);
    }

    private void checkStatus(Context context) {
        if (!hasConnection(context) && status) {
            showDialog(context);
            status = !status;
        } else {
            if (alertDialog != null) {
                alertDialog.cancel();
                status = !status;
            }
        }
    }


    private void showDialog(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(context.getString(R.string.API_connect_title));
        builder.setMessage(context.getString(R.string.API_connect_message));
        builder.setCancelable(false);
        builder.setNegativeButton(context.getString(R.string.API_connect_negative_btn),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((Activity) context).moveTaskToBack(true);
                    }
                });

        alertDialog = builder.create();
        alertDialog.show();
    }

    private boolean hasConnection(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiInfo != null && wifiInfo.isConnected()) {
            return true;
        }
        wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (wifiInfo != null && wifiInfo.isConnected()) {
            return true;
        }
        wifiInfo = cm.getActiveNetworkInfo();
        if (wifiInfo != null && wifiInfo.isConnected()) {
            return true;
        }
        return false;
    }

}