package com.example.anderson.subcsparql.Communication;

import android.content.Context;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttException;

public class Connect {

    public static Connect instance = null;
    private MqttAndroidClient client = null;
    private String uri = "tcp://lsdi.ufma.br:1883";

    public static Connect getInstance() {
        if (instance == null) {
            instance = new Connect();
        }
        return instance;
    }


    public MqttAndroidClient Connection(final Context context, String clientID) {
        if (client == null) {
            try {
                client = new MqttAndroidClient(context.getApplicationContext(), uri, clientID);
                IMqttToken token = client.connect();
                token.setActionCallback(new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        System.out.println("Conectou");
                        Toast.makeText(context, "Conectou", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                        System.out.println("Falhou");
                        Toast.makeText(context, "Falhou", Toast.LENGTH_SHORT).show();
                        client = null;
                    }
                });
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
        return client;
    }
}
