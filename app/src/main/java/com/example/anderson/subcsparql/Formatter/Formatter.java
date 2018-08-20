package com.example.anderson.subcsparql.Formatter;

import eu.larkc.csparql.common.RDFTuple;

import java.util.ArrayList;

public class Formatter{
String m = null;

    public ArrayList<String> rdfTuplesToString (ArrayList<RDFTuple> rdfT){
        ArrayList<String> stringRdfTuple = new ArrayList();
        ArrayList<RDFTuple> rdfTuples = new ArrayList<>();
        rdfTuples = rdfT;
        int n = rdfTuples.size();
        int i = 0;

        for (i = 0; i < n; i++) {
            RDFTuple t = new RDFTuple();
            t = rdfTuples.get(i);
            stringRdfTuple.add(t.toString());
        }
        return stringRdfTuple;
    }

    public ArrayList<String> toString (ArrayList<RDFTuple> rdfT){

        ArrayList<String> stringRdfTuple = new ArrayList();
        ArrayList<String> result = new ArrayList<>();
        ArrayList<String> resultT = new ArrayList<>();
        ArrayList<RDFTuple> rdfTuples = new ArrayList<>();


        rdfTuples = rdfT;
        int n = rdfTuples.size();
        int i = 0;

        for (i = 0; i < n; i++) {
            RDFTuple t = new RDFTuple();
            t = rdfTuples.get(i);
            stringRdfTuple.add(t.toString());
        }

        i = 0;
        int x = 0;
        String[] separateResultT = new String[n];

        for (i = 0; i < n; i++) {
            String stringRdf = stringRdfTuple.get(i);
            separateResultT = stringRdf.split("\t");

            for (x = 0; x<separateResultT.length; x++){
                resultT.add(separateResultT[x]);
            }

        }

        i=0;
        int m = resultT.size();

        for (i = 0; i < m; i++) {
            String stringT =  resultT.get(i);
            String[] separateResultC =  stringT.split("\\^");
            String stringC =  separateResultC[0];
            String[] separeteResult =  stringC.split("#");
            String strintJ = separeteResult[separeteResult.length-1];
            String[] separeteResultJ = strintJ.split("/");
            result.add(separeteResultJ[separeteResultJ.length-1]);
        }

        return result;
    }
}
