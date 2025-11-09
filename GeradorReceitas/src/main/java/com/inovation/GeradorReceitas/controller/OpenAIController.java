package com.inovation.GeradorReceitas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inovation.GeradorReceitas.entities.Ingredientes;
import com.inovation.GeradorReceitas.exception.GerarReceitaException;
import com.inovation.GeradorReceitas.exception.IngredientesInvalidosException;
import com.inovation.GeradorReceitas.service.ReceitaService;


// camada controller onde eu vou receber minhas requisiçoes

@RestController
@RequestMapping("/receita")
@CrossOrigin(origins = "http://localhost:5173")
public class OpenAIController {


    @Autowired
    private ReceitaService service;

    // requisao onde eu recebo minha lista de ingredientes e retorno a receita para o usuario
    @PostMapping("/gerar")
    public ResponseEntity<String> gerarReceita(@RequestBody Ingredientes ingredientes){

        
        String receita = service.gerarReceita(ingredientes.getIngredientes());
        return ResponseEntity.ok(receita);
       
    }

    @ExceptionHandler(IngredientesInvalidosException.class)
    public ResponseEntity<String> handleIngredientesInvalidos(IngredientesInvalidosException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(GerarReceitaException.class)
    public ResponseEntity<String> handleGerarReceitaException(GerarReceitaException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.SERVICE_UNAVAILABLE);
    }
}
