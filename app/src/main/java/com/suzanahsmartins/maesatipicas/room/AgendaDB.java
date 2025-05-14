package com.suzanahsmartins.maesatipicas.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.suzanahsmartins.maesatipicas.agenda.Compromisso;

import java.util.List;

@Dao
public interface AgendaDB {
    @Insert
    void inserir(Compromisso compromisso);

    @Query("SELECT * FROM Compromisso WHERE mes = :mes")
    List<Compromisso> listarPorMes(String mes);

    @Delete
    void deletar(Compromisso compromisso);
}
