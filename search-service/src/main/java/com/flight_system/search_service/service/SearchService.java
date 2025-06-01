package com.flight_system.search_service.service;

import com.flight_system.search_service.dto.SearchResultDTO;

import java.time.LocalDate;
import java.util.List;

public interface SearchService {
    List<SearchResultDTO> search(String origin, String destination, LocalDate date);
}
