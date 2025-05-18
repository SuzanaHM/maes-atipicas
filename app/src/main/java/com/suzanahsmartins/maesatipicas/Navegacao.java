package com.suzanahsmartins.maesatipicas;

import android.view.View;

import androidx.fragment.app.Fragment;

import com.suzanahsmartins.maesatipicas.diario.Diario;
import com.suzanahsmartins.maesatipicas.diario.Diario_Novo;
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
                carregarPagina(new Diario());
                break;
            case Diario_Novo:
                carregarPagina(new Diario_Novo());
                break;
        }
    }

    public void retornar(){
        switch(pagina){
            case Principal:
            case Agenda:
            case Diario:
            case Forum:
                navegarTo(Pagina.Principal);
                break;
            case Agenda_Novo:
                navegarTo(Pagina.Agenda);
                break;
            case Diario_Novo:
                navegarTo(Pagina.Diario);
                break;
        }
    }

    private void carregarPagina(Fragment fragment) {
        if(fragment instanceof Principal){
            activity.findViewById(R.id.botaoVoltar).setVisibility(View.GONE);
        }else{
            activity.findViewById(R.id.botaoVoltar).setVisibility(View.VISIBLE);
        }
        activity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragment)  // Substitua R.id.fragmentContainer com o ID do seu container
                .commit();
    }

    public enum Pagina{
        Principal,
        Agenda,
        Agenda_Novo,
        Diario,
        Diario_Novo,
        Forum
    }
}
