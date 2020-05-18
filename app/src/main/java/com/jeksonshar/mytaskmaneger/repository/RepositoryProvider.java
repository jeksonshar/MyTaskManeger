package com.jeksonshar.mytaskmaneger.repository;

import android.content.Context;

public class RepositoryProvider {

    private static Repository instance;

    public static Repository getInstance(Context context){
        if (instance == null) {
            instance = new InMemoryRepository();
        }
        return instance;
    }

    private RepositoryProvider() {
    }
}
