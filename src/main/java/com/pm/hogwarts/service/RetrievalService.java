package com.pm.hogwarts.service;

import com.pm.hogwarts.dto.CharacterRequestDto;
import com.pm.hogwarts.dto.CharacterResponseDto;
import com.pm.hogwarts.dto.PagedResponse;
import com.pm.hogwarts.exceptionHandler.BadRequestE;
import com.pm.hogwarts.exceptionHandler.ExternalApiE;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class RetrievalService {
    private final RestClient restClient;
    private final CharacterRequestDto[] characters;
    private static final int PAGE_SIZE = 20;

    public RetrievalService(RestClient.Builder builder){
        this.restClient = builder.baseUrl("https://hp-api.onrender.com").build();

        try {
            this.characters = restClient.get().uri("/api/characters").retrieve().body(CharacterRequestDto[].class);
        } catch (Exception e){
            throw new ExternalApiE("Could not fetch API", e);
        }
    }


    public PagedResponse<CharacterRequestDto> getCharactersPagination(int page, String query, String house){
        if (page < 1) {
            throw new BadRequestE("page must be >= 1");
        }

        List<CharacterRequestDto> filtered;

        if(house == null || house.isBlank()) {
            filtered = getAllCharacters();
        }else{
            filtered = filterByHouse(house);
        }

        if(query != null && !query.isBlank()){
            filtered = filterByName(filtered, query);
        }

        int total = filtered.size();
        int from = Math.min((page - 1) * PAGE_SIZE, total);
        if (from >= total && total != 0) {
            throw new BadRequestE("page is out of range");
        }
        int to = Math.min(from + PAGE_SIZE, total);

        List<CharacterRequestDto> items = filtered.subList(from, to);

        return new PagedResponse<>(page, PAGE_SIZE, total, items);
    }

    private List<CharacterRequestDto> filterByHouse(String house){
        house = house.trim().toLowerCase();

        try {
            CharacterRequestDto[] array = restClient.get()
                    .uri("/api/characters/house/{house}", house)
                    .retrieve()
                    .body(CharacterRequestDto[].class);

            return array == null ? List.of() : Arrays.asList(array);

        } catch (Exception e) {
            throw new ExternalApiE("Could not fetch API (house=" + house + ")", e);
        }
    }

    private List<CharacterRequestDto> filterByName(List<CharacterRequestDto> base, String query){
        query = query.trim().toLowerCase();
        List<CharacterRequestDto> filtered = new ArrayList<>();

        for (CharacterRequestDto c : base) {
            String name = c.getName();
            if (name != null && name.toLowerCase().contains(query)) {
                filtered.add(c);
            }
        }
        return filtered;
    }

    private List<CharacterRequestDto> getAllCharacters(){
        return characters == null ? List.of() : Arrays.asList(characters);
    }
}
