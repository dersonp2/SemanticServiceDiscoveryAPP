package com.example.anderson.subcsparql;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.anderson.subcsparql.Object.Query;
import com.example.anderson.subcsparql.Obs.Listener;
import com.example.anderson.subcsparql.Obs.ObservableImpl;

import org.eclipse.paho.android.service.MqttAndroidClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.btDesc)
    Button btDesc;
    Vibrator vibrator;
    @BindView(R.id.editText)
    EditText subText;
    ObservableImpl consulta;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        btDesc.setVisibility(View.INVISIBLE);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        //Instancia contexto
        consulta = new ObservableImpl(this);
    }


    @OnClick(R.id.btsub)
    public void setconsulta() {
        Query query = new Query();
        query.setPublisherID("Anderson");
        query.setQuery("Enviando a primeira consulta :)");
        query.setQoS(0);
        query.setRetained(false);
        query.setContinuos(true);
        subText.setText("Consulta: " + query.getQuery());

        consulta = new ObservableImpl(this);
        consulta.addListener(query, new Listener() {
            @Override
            public void update(String obj) {

            }
        });
    }


}
