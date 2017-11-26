package com.example.adrian.agenda;

import java.util.HashMap;

/**
 * Created by Adrian on 23/11/2017.
 */

class SingletonMap extends HashMap<String,Object>{
    private static class SingletonHolder{
        private static final SingletonMap ourInstance = new SingletonMap();
    }

    public static SingletonMap getInstance(){
        return SingletonHolder.ourInstance;
    }

    private SingletonMap(){}
}
