package com.example.anderson.subcsparql.Interfaces;

import com.example.anderson.subcsparql.Model.Query;

import eu.larkc.csparql.common.RDFTuple;

import java.util.ArrayList;

/**
 * Created by Anderson on 07/06/2018.
 */

public interface Observable {
    public void addListener(Query query, Listener listener);
    public void removeListener(Listener listener);
    public void notifyListener(java.util.Observable o, ArrayList<RDFTuple> rdfTuples);
}
