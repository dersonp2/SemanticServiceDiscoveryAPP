package com.example.anderson.subcsparql;

import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.anderson.subcsparql.Formatter.Formatter;
import com.example.anderson.subcsparql.Model.OntologyPrefix;
import com.example.anderson.subcsparql.Model.Query;
import com.example.anderson.subcsparql.Interfaces.Listener;
import com.example.anderson.subcsparql.Implements.ResultReceiver;
import com.example.anderson.subcsparql.StaticModel.StaticModel;

import java.util.ArrayList;
import java.util.Observable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import eu.larkc.csparql.common.RDFTuple;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.btDesc)
    Button btDesc;
    Vibrator vibrator;
    @BindView(R.id.editText)
    EditText subText;
    ResultReceiver consulta;
    private OntologyPrefix prefix;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        btDesc.setVisibility(View.INVISIBLE);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        //Instancia contexto
        consulta = new ResultReceiver(this);
    }


    @OnClick(R.id.btsub)
    public void setconsulta() {
        new StaticModel().putStaticNamedModel(this, "http://streamreasoning.org/roomConnection",
                "examples_files/OntoRDF.owl");

        String query2 = "REGISTER QUERY MhubSemantic AS "
                + "PREFIX iotlite:<" + prefix.getIotlite() + "> "
                + "PREFIX sosa:<" + prefix.getSosa() + "> "
                + "SELECT ?result "
                + "FROM STREAM <" + prefix.getStreamLsdi() + "> [RANGE 10s STEP 5s] "
                + "WHERE { "
                + "?id iotlite:hasQuatityKind iotlite:Temperature . "
                + "?id sosa:hasResult ?result"
                + " }";

        Query q = new Query.Builder().query(query2).continuos(true)
                .publisherID("Anderson@lsdi.ufma.br").build();


        ResultReceiver receiver = new ResultReceiver(this);
        receiver.addListener(q, new Listener() {

            @Override
            public void update(Observable o, ArrayList<RDFTuple> rdfTuples) {
                ArrayList<String> result = new Formatter().toString(rdfTuples);
                updateGUI(result.get(0), 1);
            }
        });
    }

    private void updateGUI(String s, int i) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
            }
        });
    }
}



