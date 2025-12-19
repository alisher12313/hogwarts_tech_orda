package com.pm.hogwarts.controller;

import com.pm.hogwarts.dto.CharacterRequestDto;
import com.pm.hogwarts.dto.PagedResponse;
import com.pm.hogwarts.service.RetrievalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api")
@RequiredArgsConstructor
public class CharactersController {
    private final RetrievalService service;

    @GetMapping("/characters")
    public ResponseEntity<?> characters(@RequestParam(defaultValue="1") int page,
                                        @RequestParam(required=false) String search,
                                        @RequestParam(required=false) String house)
    {
        return ResponseEntity.ok(service.getCharactersPagination(page, search, house));
    }

}
