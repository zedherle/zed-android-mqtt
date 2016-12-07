package bewo.mqttsample;

import android.content.Context;
import android.util.Log;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 * Created on 07-12-2016.
 */

class Subscribe implements MqttCallback {
    private static final String TAG = "Subscribe";
    private ListenSubscribe listenSubscribe;

    Subscribe(Context context) {
        listenSubscribe = (ListenSubscribe) context;
    }

    void mSubscribe() {
        try {
            MqttConnectOptions conOpt = new MqttConnectOptions();
            conOpt.setCleanSession(true);
            conOpt.setUserName(Constants.user_name);
            conOpt.setPassword(Constants.password);
            MemoryPersistence persistence = new MemoryPersistence();
            MqttClient client = new MqttClient(Constants.broker, Constants.clientId_sub, persistence);
            client.connect(conOpt);
            client.setCallback(this);
            client.subscribe(Constants.topic, Constants.qos);
            //MqttMessage message = new MqttMessage();
            //message.setPayload("A single message from my computer fff".getBytes());
            //client.publish(Constants.topic, message);
        } catch (MqttException e) {
            Log.e(TAG, "MQTT Exception : ");
            e.printStackTrace();
        }
    }

    @Override
    public void connectionLost(Throwable cause) {
        Log.e(TAG, "Connection los "+cause);
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        Log.i(TAG, "messageArrived");
        listenSubscribe.messageArrived(new String(message.getPayload()));
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        Log.i(TAG, "deliveryComplete");
    }
}
