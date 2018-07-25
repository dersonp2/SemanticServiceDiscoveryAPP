package com.example.anderson.subcsparql.Communication;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.anderson.subcsparql.Object.Query;
import com.example.anderson.subcsparql.Obs.ObservableImpl;
import com.google.gson.Gson;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Created by Anderson on 07/06/2018.
 */

public class Communication extends AppCompatActivity {

    final private String TOPIC_PREFIX = "cddl-mhub/queryCsparql";
    String clientId = MqttClient.generateClientId();
    String uri = "tcp://lsdi.ufma.br:1883";
    Boolean conn = false;
    String topic;
    String msg;

    MqttAndroidClient client;
    ObservableImpl observableImpl;
    Context context;
    private MqttMessage mqttMessage;
    private Query query;
    final private Gson gson = new Gson();

    public void query(Context context, Query query, ObservableImpl observableImpl) {
        this.observableImpl = observableImpl;
        this.context = context;
        this.query = new Query();
        this.query = query;
        System.out.println("Communication - Recebeu a consulta");
        try {
            connect();
        } catch (MqttException e) {
            e.printStackTrace();
            System.out.println("Erro ao conectar");
        }
    }

    public void connect() throws MqttException {

        try {
            client = new MqttAndroidClient(this.context.getApplicationContext(), uri, clientId);
            IMqttToken token = client.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    System.out.println("Conectou");
                    Toast.makeText(context, "Conectou", Toast.LENGTH_SHORT).show();
                    publish();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    System.out.println("Falhou");
                    Toast.makeText(context, "Falhou", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void responseQuery(String returnCode) {
            System.out.println("Communication - ResponseQuery");
            topic = "responseQuery/" + returnCode;
            try {
                client.subscribe(topic, query.getQoS());
                System.out.println("Communication - Subscreveu em " + topic);
                client.setCallback(new MqttCallback() {
                    @Override
                    public void connectionLost(Throwable cause) {

                    }

                    @Override
                    public void messageArrived(String topic, MqttMessage message) throws Exception {
                        msg = String.valueOf(message.getPayload());
                        System.out.println("Communication - Recebeu uma mensagem");
                        observableImpl.notifyListener(msg);
                    }

                    @Override
                    public void deliveryComplete(IMqttDeliveryToken token) {

                    }
                });
            } catch (MqttException e) {
                e.printStackTrace();
            }
    }

    public void publish() {
        try {
            String m = gson.toJson(query);
            MqttMessage message = new MqttMessage();
            message.setQos(query.getQoS());
            message.setRetained(query.getRetained());
            message.setPayload(m.getBytes());
            client.publish("QueryCSparql", message);
            System.out.println("Publicou a consulta: "+query.getQuery());
            responseQuery(query.getReturnCode());
        } catch (MqttException e) {
            System.out.println("Erro");
            e.printStackTrace();
        }

    }


    public void desconectar() {
        try {
            IMqttToken token = client.disconnect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
