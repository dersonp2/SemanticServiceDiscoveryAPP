package com.example.anderson.subcsparql.Obs;

import com.example.anderson.subcsparql.Object.Query;

/**
 * Created by Anderson on 07/06/2018.
 */

public interface Observable {
    public void addListener(Query query, Listener listener);
    public void removeListener(Listener listener);
    public void notifyListener(String obj);
}
