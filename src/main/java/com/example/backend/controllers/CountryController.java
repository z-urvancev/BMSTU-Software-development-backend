package com.example.backend.controllers;

import com.example.backend.models.Artist;
import com.example.backend.models.Country;
import com.example.backend.repositories.CountryRepository;
import com.example.backend.tools.DataValidationException;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1")
public class CountryController {

    private final CountryRepository countryRepository;

    public CountryController(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @GetMapping("/countries")
    public Page <Country> getAllCountries(@RequestParam("page") int page, @RequestParam("limit") int limit) {
        return countryRepository.findAll(PageRequest.of(page, limit, Sort.by(Sort.Direction.ASC, "name")));
    }

    @GetMapping("/countries/{id}")
    public ResponseEntity<Object> getCountry(@PathVariable(value="id") Long countryId) throws DataValidationException {
        Country country = countryRepository.findById(countryId)
                .orElseThrow(() -> new DataValidationException("Страна с таким индексом не найдена"));
        return ResponseEntity.ok(country);
    }


    @GetMapping("/countries/{id}/artists")
    public ResponseEntity<List<Artist>> getCountryArtists(@PathVariable(value = "id") Long country_id) {
        Optional<Country> cc = countryRepository.findById(country_id);
        if (cc.isEmpty()) { return ResponseEntity.ok(new ArrayList<>()); }
        return ResponseEntity.ok(cc.get().artists);
    }


    @PostMapping("/countries")
    public ResponseEntity<Object> createCountry(@Valid @RequestBody Country country) throws DataValidationException {
        try {
            Country nc = countryRepository.save(country);
            return new ResponseEntity<>(nc, HttpStatus.OK);
        }
        catch(Exception ex) {
            if (ex.getMessage().contains("countries.name_UNIQUE"))
                throw new DataValidationException("Эта страна уже есть в базе");
            else
                throw new DataValidationException("Неизвестная ошибка");
        }
    }


    @PutMapping("/countries/{id}")
    public ResponseEntity<Object> updateCountry(@PathVariable(value = "id") Long countryId, @RequestBody Country countryDetails) throws DataValidationException {
        try {
            Country country = countryRepository.findById(countryId)
                    .orElseThrow(() -> new DataValidationException("Страна с таким индексом не найдена"));
            country.name = countryDetails.name;
            countryRepository.save(country);
            return ResponseEntity.ok(country);
        }
        catch (Exception ex) {
            if (ex.getMessage().contains("countries.name_UNIQUE"))
                throw new DataValidationException("Эта страна уже есть в базе");
            else
                throw new DataValidationException("Неизвестная ошибка");
        }
    }

    @DeleteMapping("/countries/{id}")
    public ResponseEntity<String> deleteCountry(@PathVariable(value = "id") Long countryId) {
        Optional<Country> countryToDelete  = countryRepository.findById(countryId);
        if (countryToDelete.isEmpty()) {
            return new ResponseEntity<>("Deletion failed", HttpStatus.OK);
        }
        else {
            countryRepository.delete(countryToDelete.get());
            return new ResponseEntity<>("Deletion successful", HttpStatus.OK);
        }
    }

    @PostMapping("/deletecountries")
    public ResponseEntity<Object> deleteCountries(@Valid @RequestBody List<Country> countries) {
        countryRepository.deleteAll(countries);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
