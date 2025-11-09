package com.inovation.GeradorReceitas.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.inovation.GeradorReceitas.exception.GerarReceitaException;
import com.inovation.GeradorReceitas.exception.IngredientesInvalidosException;
import dev.langchain4j.model.openai.OpenAiChatModel;

@Service
public class ReceitaService {

    @Autowired
    private OpenAiChatModel chatModel;

    
    private void validarPlausibilidade(List<String> ingredientes) {

        String ingredientesStr = String.join(", ", ingredientes);

      
        String validationPrompt = "Analise os itens: [" + ingredientesStr + "]. Sua resposta deve ser apenas uma única palavra. Se CADA item for um ingrediente comestível e reconhecível, retorne 'SIM'. Se houver qualquer item que não seja um ingrediente de comida óbvio (como letras aleatórias), retorne 'NAO'.";

        
        try {
            String validationResult = chatModel.chat(validationPrompt).trim().toUpperCase();

            System.out.println("Resposta da IA para validação: [" + validationResult + "]");

            if (!"SIM".equals(validationResult)) {
               
                throw new IngredientesInvalidosException(
                        "Os itens fornecidos não são reconhecidos como ingredientes comestíveis.");
            }
        } catch (Exception e) {
            
            e.printStackTrace();
            throw new GerarReceitaException("A validação do ingrediente falhou devido a um erro de comunicação.");
        }
    }

    public String gerarReceita(List<String> ingredientes) {

       
        if (ingredientes.isEmpty() || ingredientes == null) {
            throw new IngredientesInvalidosException("a lista de ingredientes nao pode estar vazia");
        }

        for (String ingredient : ingredientes) {
            if (ingredient == null || ingredient.trim().isEmpty() || !ingredient.matches("[A-Za-zÀ-ú ]+")) {
                throw new IngredientesInvalidosException(
                        "Ingrediente invalido detectado " + ingredient + " apenas nomes sao permitidos");
            }
        }

        
        validarPlausibilidade(ingredientes);

        String prompt = "Crie uma receita curta e prática usando os seguintes ingredientes: "
                + String.join(", ", ingredientes) + //
                "Responda no formato:\r\n" + //
                "Título:\r\n" + //
                "Ingredientes:\r\n" + //
                "Modo de preparo (3-5 passos):\r\n" + //
                "Não adicione ingredientes extras além dos listados.";

       
        try {
            String receita = chatModel.chat(prompt);
            return receita;
        }
       
        catch (Exception e) {
            e.printStackTrace();
            throw new GerarReceitaException(
                    "A comunicação com o gerador de receitas falhou. Por favor, tente novamente mais tarde.");
        }
    }
}
