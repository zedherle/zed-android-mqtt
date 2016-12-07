package bewo.mqttsample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MainActivity extends AppCompatActivity implements MqttCallback, View.OnClickListener, ListenSubscribe {

    private static final String TAG = "MainActivity";

    EditText et_text;
    TextView subscribe;
    Subscribe subs ;

    private BroadcastReceiver onServiceEvent = new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent i) {
            Log.d(TAG, "connect internet");
            if(subs==null) {
                subs = new Subscribe(MainActivity.this);
            }
            subs.mSubscribe();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        subs = new Subscribe(MainActivity.this);
        subs.mSubscribe();
        et_text = (EditText) findViewById(R.id.et_publish);
        subscribe = (TextView) findViewById(R.id.txt_subscribe);

        IntentFilter f = new IntentFilter();
        f.addAction("com.bewo.genieclient.AdScreen.Events");
        registerReceiver(onServiceEvent, f);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(onServiceEvent);
    }

        @Override
    public void connectionLost(Throwable cause) {
        Log.e(TAG, "Connection to " + Constants.broker + " lost!" + cause);
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        Log.d(TAG, "Message Arrived Topic:\t" + topic +
                "  Message:\t" + new String(message.getPayload()) +
                "  QoS:\t" + message.getQos());
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        Log.d(TAG, "DeliveryCompleted: " + token);
    }

    @Override
    public void messageArrived(String message) {
        final String mesg = message;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!mesg.isEmpty()) {
                    String n_msg = "";
                    String old_message = subscribe.getText().toString();
                    if (!old_message.isEmpty()) {
                        n_msg = n_msg.concat(old_message);
                        n_msg = n_msg.concat("\n");
                    }
                    n_msg = n_msg.concat(mesg);
                    subscribe.setText(n_msg);
                } else {
                    subscribe.setText("");
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_publish:
                String text = et_text.getText().toString();
                if(!text.isEmpty()) {
                    publish(text);
                    et_text.setText("");
                } else {
                    Toast.makeText(getApplicationContext(), "Empty", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    void publish(final String content) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    // Construct the connection options object that contains connection parameters
                    // such as cleanSession and LWT
                    MqttConnectOptions conOpt = new MqttConnectOptions();
                    conOpt.setCleanSession(true);
                    conOpt.setUserName(Constants.user_name);
                    conOpt.setPassword(Constants.password);
                    MemoryPersistence persistence = new MemoryPersistence();
                    // Construct an MQTT blocking mode client
                    MqttClient client = new MqttClient(Constants.broker, Constants.clientId_pub, persistence);
                    // Connect to the MQTT server
                    client.connect(conOpt);
                    // Set this wrapper as the callback handler
                    client.setCallback(MainActivity.this);
                    // client.subscribe(Constants.topic, Constants.qos);

                    // Create and configure a message
                    MqttMessage message = new MqttMessage(content.getBytes());
                    message.setQos(Constants.qos);

                    client.publish(Constants.topic, message); // Blocking publish
                    client.disconnect();
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }
}
