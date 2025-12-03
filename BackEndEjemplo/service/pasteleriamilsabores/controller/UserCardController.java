package com.service.pasteleriamilsabores.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.service.pasteleriamilsabores.dto.CardDto;
import com.service.pasteleriamilsabores.service.CardService;

@RestController
@RequestMapping("/api/users/{run}/cards")
public class UserCardController {

    private final CardService cardService;

    public UserCardController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CardDto>> list(@PathVariable("run") String run) {
        return ResponseEntity.ok(cardService.listByUserRun(run));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CardDto> add(@PathVariable("run") String run, @RequestBody CardDto dto) {
        CardDto created = cardService.addCard(run, dto);
        return ResponseEntity.status(201).body(created);
    }
}
