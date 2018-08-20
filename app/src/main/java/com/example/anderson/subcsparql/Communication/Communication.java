package com.example.anderson.subcsparql.Communication;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.example.anderson.subcsparql.Model.Query;
import com.example.anderson.subcsparql.Implements.ResultReceiver;
import com.example.anderson.subcsparql.Model.ResponseQuery;
import com.google.gson.Gson;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Created by Anderson on 07/06/2018.
 */

public class Communication extends AppCompatActivity {

    final private String TOPIC_PREFIX = "cddl-mhub/queryCsparql";
    private String topic;
    private String msg;

    private MqttAndroidClient client;
    private ResultReceiver resultReceiver;
    private Query query;
    final private String TOPIC_RESPONSE = "responseQuery/";
    final private String TOPIC_QUERY = "QueryCSparql";

    private Gson gson = null;

    public void query(Context context, Query query, ResultReceiver resultReceiver) {
        this.resultReceiver = resultReceiver;
        this.query = new Query.Builder().build();
        this.query = query;
        System.out.println("Communication - Recebeu a consulta");
        try {
            connect(context, query.getPublisherID());
            if (client != null){
                publish();
            }else{
                failedConnect();
            }
        } catch (MqttException e) {
            e.printStackTrace();
            System.out.println("Erro ao conectar");
        }

    }

    public void connect(final Context context, String clientID) throws MqttException {
        client = Connect.getInstance().Connection(context, clientID);
    }

    public void responseQuery() {
        topic = TOPIC_RESPONSE + query.getReturnCode();
        try {
            client.subscribe(topic, 2);
            System.out.println("Communication.Communication  - Subscribed in response\n");
            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    System.out.println("Communication.Communication  - connectionLost\n"+cause.getMessage());
                    responseQuery();
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    gson = new Gson();
                    String mgson = new String(message.getPayload());
                    ResponseQuery rq = gson.fromJson(mgson, ResponseQuery.class);
                    notifyListener(rq);
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
            gson = new Gson();
            String m = gson.toJson(query);
            MqttMessage message = new MqttMessage();
            message.setQos(2);
            message.setRetained(false);
            message.setPayload(m.getBytes());
            client.publish(TOPIC_QUERY, message);
            System.out.println("Communication.Communication  - published the query\n");
            responseQuery();
        } catch (MqttException e) {
            System.out.println("Erro");
            e.printStackTrace();
        }

    }


    public void disconnect() {
        try {
            IMqttToken token = client.unsubscribe(topic);
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

    private void failedConnect() {
        //observable.notifyListener("Failed to connect to broker");
    }
    public void notifyListener(ResponseQuery rq){
        resultReceiver.notifyListener(rq.getObservable(), rq.getRdfTuples());
    }

}
