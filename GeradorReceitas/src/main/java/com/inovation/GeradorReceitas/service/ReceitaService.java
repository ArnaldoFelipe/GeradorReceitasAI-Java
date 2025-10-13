package com.inovation.GeradorReceitas.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inovation.GeradorReceitas.exception.GerarReceitaException;
import com.inovation.GeradorReceitas.exception.IngredientesInvalidosException;

import dev.langchain4j.model.openai.OpenAiChatModel;

// cerebro de toda a aplicacao, literalmente a cozinha, aqui é aonde faço a interaçao com a minha api de AI 
@Service
public class ReceitaService {

    @Autowired
    private OpenAiChatModel chatModel;


    // metodo que recebe a minha lista de ingredientes preparo o prompt envio para a api de AI e recebo a sua resposta guardando na variavel receita e retornando para o usuario
    public String gerarReceita(List<String> ingredientes){
        
        // validar se a lista esta vazia
        if (ingredientes.isEmpty() || ingredientes == null) {
            throw new IngredientesInvalidosException("a lista de ingredientes nao pode estar vazia");
        }

        // validar se existe algum ingrediente invalido na lista
        for(String ingredient: ingredientes){
            if(ingredient == null || ingredient.trim().isEmpty() || !ingredient.matches("[A-Za-zÀ-ú ]+")){
                throw new IngredientesInvalidosException("Ingrediente invalido detectado " + ingredient + " apenas nomes sao permitidos");
            }
        }

        String prompt = "Crie uma receita curta e prática usando os seguintes ingredientes: " + String.join(", ", ingredientes) + //
                        "Responda no formato:\r\n" + //
                        "Título:\r\n" + //
                        "Ingredientes:\r\n" + //
                        "Modo de preparo (3-5 passos):\r\n" + //
                        "Não adicione ingredientes extras além dos listados.";


        // tenta fazer a requisao a api
        try{
            String receita = chatModel.chat(prompt);
            return receita;
        }
        // caso a api falhe de alguma forma
        catch(Exception e){
            e.printStackTrace();
            throw new GerarReceitaException("A comunicação com o gerador de receitas falhou. Por favor, tente novamente mais tarde.");
        }
    }
}
