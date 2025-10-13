package com.inovation.GeradorReceitas.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.langchain4j.model.openai.OpenAiChatModel;

// classe de teste para verificar minha conexao com a API
@ExtendWith(MockitoExtension.class)
public class ReceitaServiceTest {

    @Mock
    private OpenAiChatModel mockAI;

    @InjectMocks
    private ReceitaService service;
    
    // metodo para testar se minha conexao esta retornando o que deve retorna
    @Test
    void deveGerarReceitaCorretamente(){

        List<String> ingredientes = Arrays.asList("ovos", "açucar");
        String receitaEsperada = "Receita de bolo simples";

        // String prompt = String.join(", ", ingredientes);

        Mockito.when(mockAI.chat(anyString()))
               .thenReturn("Receita de bolo simples");

        String receita = service.gerarReceita(ingredientes);

        assertEquals(receitaEsperada, receita);
    }
}
