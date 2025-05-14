package com.suzanahsmartins.maesatipicas.paginas;

import android.app.Activity;

import androidx.fragment.app.Fragment;

import com.suzanahsmartins.maesatipicas.MainActivity;
import com.suzanahsmartins.maesatipicas.R;

public class Navegacao {

    private MainActivity activity;

    public Navegacao(MainActivity activity){
        this.activity = activity;
    }

    private Pagina pagina = Pagina.Principal;


    public Pagina getPagina() {
        return pagina;
    }

    public void navegarTo(Pagina pagina){
        switch(pagina){
            case Principal:
                carregarPagina(new Principal());
                break;
            case Agenda:
                carregarPagina(new Agenda());
                break;
        }
    }

    public void retornar(){
        switch(pagina){
            case Principal:
                break;
        }
    }

    private void carregarPagina(Fragment fragment) {
        activity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragment)  // Substitua R.id.fragmentContainer com o ID do seu container
                .commit();
    }

    public enum Pagina{
        Principal,
        Agenda
    }
}
