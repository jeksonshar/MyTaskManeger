package com.jeksonshar.mytaskmaneger.repository;

import android.content.Context;

import com.jeksonshar.mytaskmaneger.repository.room.RoomRepository;

public class RepositoryProvider {

    private static Repository instance;

    public static Repository getInstance(Context context){
        if (instance == null) {
//            instance = new InMemoryRepository();
            instance = new RoomRepository(context);
        }
        return instance;
    }

    private RepositoryProvider() {
    }
}
