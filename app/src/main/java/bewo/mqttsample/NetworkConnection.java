package bewo.mqttsample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created on 03-06-2016.
 */
public class NetworkConnection extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        sync(context);
    }

    public static void sync(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

        if (activeNetwork != null && activeNetwork.isConnected()) {
            Intent intent = new Intent("com.bewo.genieclient.AdScreen.Events");
            intent.putExtra("Event", "InternetAvailable");
            context.sendBroadcast(intent);
        }
    }
}
