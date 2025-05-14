package com.suzanahsmartins.maesatipicas;

import androidx.fragment.app.Fragment;

import com.suzanahsmartins.maesatipicas.paginas.Agenda;
import com.suzanahsmartins.maesatipicas.paginas.Agenda_Novo;
import com.suzanahsmartins.maesatipicas.paginas.Principal;

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
        this.pagina = pagina;
        switch(pagina){
            case Principal:
                carregarPagina(new Principal());
                break;
            case Agenda:
                carregarPagina(new Agenda());
                break;
            case Agenda_Novo:
                carregarPagina(new Agenda_Novo());
                break;
            case Forum:
                carregarPagina(new Agenda_Novo());
                break;
            case Diario:
                carregarPagina(new Agenda_Novo());
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
        Agenda,
        Agenda_Novo,
        Diario,
        Forum
    }
}
