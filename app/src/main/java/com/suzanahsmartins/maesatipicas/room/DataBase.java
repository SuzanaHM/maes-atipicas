package com.suzanahsmartins.maesatipicas.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.suzanahsmartins.maesatipicas.agenda.Compromisso;

@Database(entities = {Compromisso.class}, version = 3)
public abstract class DataBase extends RoomDatabase {
    public abstract AgendaDB getAgenda();

    public static DataBase getInstance(Context context, String database){
        return Room.databaseBuilder(context, DataBase.class, database).fallbackToDestructiveMigration(false).build();
    }
}
