package com.example.anderson.subcsparql;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener{

    Button btSub, btDesc;
    TextView tResultado;
    MqttAndroidClient client;
    String clientId = MqttClient.generateClientId();
    // TextView subText;
    String topic = "ResponseQuery";
    Vibrator vibrator;
    Ringtone ringtone;
    EditText subText ;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        btSub = (Button) findViewById(R.id.btsub);
        btSub.setOnClickListener(this);
        btDesc = (Button) findViewById(R.id.btDesc);
        btDesc.setOnClickListener(this);
        subText = (EditText) findViewById(R.id.editText);
        btDesc.setVisibility(View.INVISIBLE);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btsub:
                conectar();
                break;
            case R.id.btDesc:
                desconectar();
                break;
        }
    }
    public void setSubscription() {
        try {
            client.subscribe(topic, 0);
            Toast.makeText(Main2Activity.this, "Subscreveu", Toast.LENGTH_SHORT).show();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
    public void conectar() {
        client = new MqttAndroidClient(this.getApplicationContext(), "tcp://lsdi.ufma.br:1883",
                clientId);
        try {
            IMqttToken token = client.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Toast.makeText(Main2Activity.this, "Conectou", Toast.LENGTH_SHORT).show();
                    setSubscription();
                    btDesc.setVisibility(View.VISIBLE);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Toast.makeText(Main2Activity.this, "Falhou", Toast.LENGTH_SHORT).show();

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                subText.setText(subText.getText()+"\n"+new String(message.getPayload()));
                vibrator.vibrate(500);
                ringtone.play();
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }



    public void desconectar() {
        try {
            IMqttToken token = client.disconnect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Toast.makeText(Main2Activity.this, "Desconectado", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Toast.makeText(Main2Activity.this, "Falhou", Toast.LENGTH_SHORT).show();

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
