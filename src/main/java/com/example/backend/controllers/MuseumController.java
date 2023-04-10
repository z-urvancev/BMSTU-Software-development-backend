package com.example.backend.controllers;


import com.example.backend.models.Country;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.backend.models.Museum;
import com.example.backend.repositories.MuseumRepository;

import java.util.*;

@RestController
@RequestMapping("/api/v1")
public class MuseumController {

    private final MuseumRepository museumRepository;

    public MuseumController(MuseumRepository museumRepository) {
        this.museumRepository = museumRepository;
    }

    @GetMapping("/museums")
    public List<Museum> getAllMuseums() {
        return (List<Museum>) museumRepository.findAll();
    }

    @PostMapping("/museums")
    public ResponseEntity<Object> createMuseum(@RequestBody Museum museum)
                    throws Exception {
            try {
                Museum mm = museumRepository.save(museum);
                return new ResponseEntity<>(mm, HttpStatus.OK);
            }
            catch(Exception ex) {
                String error;
                if (ex.getMessage().contains("museums.name_UNIQUE"))
                    error = "museumalreadyexists";
                else
                    error = "undefinederror";
                Map<String, String>
                        map =  new HashMap<>();
                map.put("error", error);
                return new ResponseEntity<Object> (map, HttpStatus.OK);
            }
    }
}