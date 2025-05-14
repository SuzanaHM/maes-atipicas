package com.suzanahsmartins.maesatipicas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class RecadoDiario {

    // Lista estática de recados
    private static final ArrayList<String> recados = new ArrayList<>(Arrays.asList(
            "Você está fazendo o seu melhor, e isso é suficiente.",
            "Respire fundo. Um dia de cada vez.",
            "Seu amor transforma o mundo do seu filho.",
            "Não se compare com outras mães. Sua jornada é única.",
            "Mesmo nos dias difíceis, você é uma mãe incrível.",
            "Permita-se descansar. Cuidar de você também é importante.",
            "A sua força inspira mais pessoas do que você imagina.",
            "Não precisa ser perfeito. Basta ser presente.",
            "Você é exatamente a mãe que seu filho precisa.",
            "Cada pequeno passo é uma grande vitória.",
            "Você é mais forte do que imagina.",
            "Seu carinho é um superpoder.",
            "Pequenos avanços são enormes conquistas.",
            "Confie no seu instinto. Ele é poderoso.",
            "Tudo bem não estar bem o tempo todo.",
            "Você é uma guerreira silenciosa.",
            "Amar também é resistir.",
            "Seus esforços são vistos, mesmo quando não são reconhecidos.",
            "A sua dedicação muda vidas.",
            "Hoje pode ser difícil, mas você não está sozinha.",
            "Você merece se sentir orgulhosa.",
            "Seja gentil consigo mesma.",
            "Não se esqueça: você também importa.",
            "A caminhada pode ser longa, mas você já venceu muito.",
            "A sua presença é o maior presente.",
            "Você tem o dom de transformar o mundo do seu filho.",
            "A sua coragem brilha nos momentos mais escuros.",
            "Cada lágrima que cai também rega sua força.",
            "Você pode estar cansada, mas nunca fracassando.",
            "Seu amor é o elo mais forte dessa jornada.",
            "Você está ensinando resiliência todos os dias.",
            "Não duvide do impacto que você causa.",
            "Você é luz nos dias nublados do seu filho.",
            "Dê valor às suas pequenas vitórias.",
            "Você merece colo também.",
            "Amar como você ama é uma revolução diária.",
            "Você está exatamente onde precisa estar.",
            "Seu filho sente seu amor até nos silêncios.",
            "Você é prova viva de que o amor é ação.",
            "Cada desafio superado é uma medalha invisível.",
            "Ser mãe atípica é uma jornada de força e empatia.",
            "Permita-se errar. Aprender faz parte.",
            "Você não precisa dar conta de tudo sozinha.",
            "Pedir ajuda é um ato de coragem.",
            "Seu olhar acolhe mais que mil palavras.",
            "Sua intuição é uma ferramenta poderosa.",
            "Abrace suas imperfeições com carinho.",
            "Você é uma inspiração silenciosa.",
            "Seu amor é a base de tudo."
    ));

    // Método para retornar um recado aleatório
    public static String getRandom() {
        Random random = new Random();
        return recados.get(random.nextInt(recados.size()));
    }
}
