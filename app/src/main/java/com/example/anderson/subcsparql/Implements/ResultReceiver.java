package com.example.anderson.subcsparql.Implements;

import android.content.Context;

import com.example.anderson.subcsparql.Communication.Communication;
import com.example.anderson.subcsparql.Interfaces.Listener;
import com.example.anderson.subcsparql.Interfaces.Observable;
import com.example.anderson.subcsparql.Model.Query;
import eu.larkc.csparql.common.RDFTuple;


import java.util.ArrayList;


/**
 * Created by Anderson on 07/06/2018.
 *
 * @author Anderson
 * @version 0.1
 */

public class ResultReceiver implements Observable {
    private Communication communication = new Communication();
    private Listener listener;

    Context context;

    public ResultReceiver(Context context){
        this.context = context;
    }

    @Override
    public void addListener(Query query, Listener listener) {
        this.listener = listener;
        communication.query(context, query, this);

    }

    @Override
    public void removeListener(Listener listener) {
        this.listener = null;
        //logger.info("Recebeu o Listener");
    }

    @Override
    public void notifyListener(java.util.Observable o, ArrayList<RDFTuple> rdfTuples) {
        if (listener != null)
            listener.update(o,rdfTuples);
    }

    public void disconnect(Listener listener) {
        Communication communication = new Communication();
        communication.disconnect();
        removeListener(listener);
    }
}
