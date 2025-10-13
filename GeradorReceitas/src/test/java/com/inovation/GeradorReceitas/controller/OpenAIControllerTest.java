package com.inovation.GeradorReceitas.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import com.inovation.GeradorReceitas.exception.IngredientesInvalidosException;
import com.inovation.GeradorReceitas.service.ReceitaService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status; 
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content; 

// classe para testar minha camada controller
@WebMvcTest(OpenAIController.class)
public class OpenAIControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReceitaService service;

    // metodo que verifica se o retorno é igual a 200, passando os dados certos
    @Test
    void deveRetornar200EOConteudoDaReceitaCorretamente() throws Exception{
        
        
        String ingredientesJson = "{\"ingredientes\": [\"ovos\", \"açucar\"]}";
        String receitaEsperada = "Receita de bolo simples";

        Mockito.when(service.gerarReceita(Mockito.anyList()))
                .thenReturn(receitaEsperada);

        
        mockMvc.perform(post("/receita/gerar")
                    .contentType(MediaType.APPLICATION_JSON) 
                    .content(ingredientesJson))
                .andExpect(status().isOk())
                .andExpect(content().string(receitaEsperada)); 
    } 

    // metodo que verifica se o retorno é igual a 400 passando a lista de ingredientes vazia
    @Test
    void deveRetornar400ELancarIngredientesInvalidosException() throws Exception{
        String ingredientesJson = "[]";
        
        Mockito.when(service.gerarReceita(Mockito.anyList()))
               .thenThrow(new IngredientesInvalidosException("a lista de ingredientes nao pode estar vazia"));

        mockMvc.perform(post("/receita/gerar")
                   .contentType(MediaType.APPLICATION_JSON)
                   .content(ingredientesJson))
               
               .andExpect(status().isBadRequest());
                
    }
}