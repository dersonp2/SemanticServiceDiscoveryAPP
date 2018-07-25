package com.example.anderson.subcsparql.Object;

import java.util.UUID;

/**
 * Created by Anderson on 11/06/2018.
 */

public class Query {
    private String Query;
    private int QoS;
    private Boolean Retained;
    private Boolean Continuos;
    String publisherID;
    final String returnCode = UUID.randomUUID().toString();

    public String getReturnCode() {
        return returnCode;
    }

    public String getPublisherID() {
        return publisherID;
    }

    public void setPublisherID(String publisherID) {
        this.publisherID = publisherID;
    }

    public Boolean getContinuos() {
        return Continuos;
    }

    public void setContinuos(Boolean continuos) {
        Continuos = continuos;
    }

    public String getQuery() {

        return Query;
    }

    public void setQuery(String query) {
        Query = query;
    }

    public int getQoS() {
        return QoS;
    }

    public void setQoS(int qoS) {
        QoS = qoS;
    }

    public Boolean getRetained() {
        return Retained;
    }

    public void setRetained(Boolean retained) {
        Retained = retained;
    }
}
