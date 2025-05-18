package com.suzanahsmartins.maesatipicas.diario;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Entity
public class DiarioPagina {
    @PrimaryKey
    public long data;
    public String texto;

    public DiarioPagina(long data, String texto) {
        this.data = data;
        this.texto = texto;
    }

    public long getDataMillis() {
        return data;
    }

    public String getData() {
        // Cria um Date com o timestamp
        Date date = new Date(data);

        // Formata para "dd/MM/yyyy às HH:mm"
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy 'às' HH:mm", Locale.getDefault());

        return sdf.format(date);
    }

    public String getTexto() {
        return texto;
    }
}
