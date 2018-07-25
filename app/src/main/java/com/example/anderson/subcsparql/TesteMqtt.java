package com.example.anderson.subcsparql;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

/**
 * Created by Anderson on 08/06/2018.
 */

public class TesteMqtt extends AppCompatActivity{

    MqttAndroidClient client;
    String clientId = MqttClient.generateClientId();
    public void conectar() {
        client = new MqttAndroidClient(this, "tcp://lsdi.ufma.br:1883",
                clientId);
        try {
            IMqttToken token = client.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Toast.makeText(TesteMqtt.this, "Conectou", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Toast.makeText(TesteMqtt.this, "ERRO", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }
}
