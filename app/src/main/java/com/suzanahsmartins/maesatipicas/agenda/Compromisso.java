package com.suzanahsmartins.maesatipicas.agenda;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity
public class Compromisso {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String dia;
    public String descricao;
    public String mes;
    public String hora;
    public String titulo;
    public boolean alerta = false;
}
