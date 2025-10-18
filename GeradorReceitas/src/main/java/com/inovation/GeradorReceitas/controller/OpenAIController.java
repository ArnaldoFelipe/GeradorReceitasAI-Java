package com.inovation.GeradorReceitas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.inovation.GeradorReceitas.entities.Ingredientes;
import com.inovation.GeradorReceitas.exception.IngredientesInvalidosException;
import com.inovation.GeradorReceitas.service.ReceitaService;


// camada controller onde eu vou receber minhas requisiçoes
@RestControllerAdvice
@RestController
@RequestMapping("/receita")
@CrossOrigin(origins = "http://localhost:5173")
public class OpenAIController {


    @Autowired
    private ReceitaService service;

    // requisao onde eu recebo minha lista de ingredientes e retorno a receita para o usuario
    @PostMapping("/gerar")
    public ResponseEntity<String> gerarReceita(@RequestBody Ingredientes ingredientes){

        try{
            // chama o service para gerar a receita
            String receita = service.gerarReceita(ingredientes.getIngredientes());
            return ResponseEntity.ok(receita);
        }
        // lista de ingredientes inválida → retorna 400
        catch(IngredientesInvalidosException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        // erro interno ou falha na IA → retorna 500
        catch(RuntimeException e){
            return ResponseEntity.internalServerError().body("Erro ao gerar receita. Tente novamente mais tarde.");
        }
    }
}
