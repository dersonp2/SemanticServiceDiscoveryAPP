package com.example.anderson.subcsparql.Model;

import java.util.ArrayList;
import java.util.Observable;

import eu.larkc.csparql.common.RDFTuple;

public class ResponseQuery {
    private Observable observable;
    ArrayList<RDFTuple> rdfTuples;

    public Observable getObservable() {
        return observable;
    }

    public void setObservable(Observable observable) {
        this.observable = observable;
    }

    public ArrayList<RDFTuple> getRdfTuples() {
        return rdfTuples;
    }

    public void setRdfTuples(ArrayList<RDFTuple> rdfTuples) {
        this.rdfTuples = rdfTuples;
    }

}
