package bewo.mqttsample;

import android.util.Log;

import java.lang.reflect.Method;

/**
 * Created on 07-12-2016.
 */

public class Constants {
    private static final String TAG = "Constants";

    /**
     * Redirect MQTT to RabbitMQ in Test server.
     * */
//    public static String topic        = "camp_log";
//    public static int qos             = 1;
//    public static String broker       = "tcp://103.228.83.72:1883";
//    public static String clientId     = "mqtt-pub-"+getDeviceSerial()+"-";
//    public static String user_name    = "test";
//    public static char[] password     = {'t','e','s','t'};

    /**
     * Redirect MQTT to RabbitMQ in Production Server.
     * */
    public static String topic        = "test_topic";
    public static int qos             = 1;
    public static String broker       = "tcp://103.228.83.68:1883";
    public static String clientId_pub     = "mqtt-pub-"+getDeviceSerial()+"-";
    public static String user_name    = "rabbit_admin";
    public static char[] password     = {'r','a','b','b','i','t','_','a','d','m','i','n'};
    public static String payload     = "Message from same device";
    public static String clientId_sub     = "mqtt-sub-"+getDeviceSerial()+"-";

    public static String getDeviceSerial() {
        //String serial = Build.SERIAL;
        String serial = null;
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class);
            serial = (String) get.invoke(c, "ro.serialno");
        } catch (Exception ignored) {
            Log.e(TAG, "Error   "+ignored);
        }
        return serial;
    }

}
