package com.pm.hogwarts.controller;
import com.pm.hogwarts.service.RetrievalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PageController {
    private final RetrievalService service;

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/houses")
    public String houses(Model model) {
        model.addAttribute("houses", List.of(
                new House("Gryffindor", "#7F0909", "#FFC500", "Lion", "Bravery, courage, determination."),
                new House("Slytherin", "#0D6217", "#AAAAAA", "Serpent", "Ambition, cunning, leadership."),
                new House("Hufflepuff", "#EEE117", "#000000", "Badger", "Loyalty, patience, fair play."),
                new House("Ravenclaw", "#000A90", "#946B2D", "Eagle", "Wisdom, learning, creativity.")
        ));
        return "houses";
    }

    @GetMapping("/characters")
    public String charactersPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String house,
            Model model
    ) {
        var resp = service.getCharactersPagination(page, search, house);

        int totalPages = (int) Math.ceil((double) resp.total() / resp.size());

        model.addAttribute("resp", resp);
        model.addAttribute("search", search == null ? "" : search);
        model.addAttribute("house", house == null ? "" : house);
        model.addAttribute("totalPages", totalPages);

        return "characters";
    }

    public record House(String name, String primary, String secondary, String symbol, String description) {}
}