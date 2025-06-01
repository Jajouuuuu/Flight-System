package com.flight_system.search_service.controller;

import com.flight_system.search_service.dto.SearchResultDTO;
import com.flight_system.search_service.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping
    public List<SearchResultDTO> search(@RequestParam String origin, @RequestParam String destination, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return searchService.search(origin, destination, date);
    }
}
