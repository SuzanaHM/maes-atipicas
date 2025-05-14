package com.suzanahsmartins.maesatipicas.room;

import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Delete;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.PrimaryKey;
import androidx.room.Query;
import androidx.room.RoomDatabase;

import java.util.List;

public class agenda {
    @Entity
    public static class Compromisso {
        @PrimaryKey(autoGenerate = true)
        public int id;

        public String dia;
        public String descricao;
        public String mes;
        public String hora;
    }

    @Dao
    public interface CompromissoDao {
        @Insert
        void inserir(Compromisso compromisso);
        @Query("SELECT * FROM Compromisso WHERE mes = :mes")
        List<Compromisso> listarPorMes(String mes);
        @Delete
        void deletar(Compromisso compromisso);
    }

}
