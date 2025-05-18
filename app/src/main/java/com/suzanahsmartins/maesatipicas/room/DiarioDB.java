package com.suzanahsmartins.maesatipicas.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.suzanahsmartins.maesatipicas.diario.DiarioPagina;

import java.util.List;

@Dao
public interface DiarioDB {

    @Insert
    void inserir(DiarioPagina dp);

    @Query("DELETE FROM DiarioPagina WHERE data = :dataMillis")
    void deletar(long dataMillis);

    // Pega todos ordenados da data mais recente para a mais antiga
    @Query("SELECT * FROM DiarioPagina ORDER BY data DESC")
    List<DiarioPagina> getAll();

    // Pega o item da data exata (id)
    @Query("SELECT * FROM DiarioPagina WHERE data = :dataMillis")
    DiarioPagina get(long dataMillis);
}
