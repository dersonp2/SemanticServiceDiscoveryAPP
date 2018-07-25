package com.example.anderson.subcsparql.Obs;

import android.content.Context;

import com.example.anderson.subcsparql.Communication.Communication;
import com.example.anderson.subcsparql.Object.Query;

/**
 * Created by Anderson on 07/06/2018.
 * @author Anderson
 * @version 0.1
 */

public class ObservableImpl implements Observable {
    private Listener listener;
    Communication communication = new Communication();

    Context context;

    public ObservableImpl(Context context){
        this.context = context;
    }

    @Override
    public void addListener(Query query, Listener listener) {
        communication.query(context, query, this);
        this.listener = listener;
        System.out.println("ObservableImpl - Mandou a consulta para o Communication");
    }

    @Override
    public void removeListener(Listener listener) {
        this.listener=null;
    }

    @Override
    public void notifyListener(String obj) {
        listener.update(obj);
    }
}
