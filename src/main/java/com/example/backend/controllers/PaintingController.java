package com.example.backend.controllers;


import com.example.backend.models.Painting;
import com.example.backend.repositories.PaintingRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@CrossOrigin
@RestController
@RequestMapping("/api/v1")
public class PaintingController {

    @Autowired
    PaintingRepository paintingRepository;

    @PostMapping("/paints")
    public ResponseEntity<Object> createMuseum(@RequestBody Painting paint)
            throws Exception {
        try {
            Painting nc = paintingRepository.save(paint);
            System.out.println(nc.name);
            return new ResponseEntity<Object>(nc, HttpStatus.OK);
        } catch (Exception ex) {
            String error;
            if (ex.getMessage().contains("museums.name_UNIQUE"))
                error = "useralreadyexists";
            else
                error = "undefinederror";
            Map<String, String>
                    map = new HashMap<>();
            map.put("error", error);
            return new ResponseEntity<Object>(map, HttpStatus.OK);
        }
    }

    @GetMapping("/paints")
    public Page<Painting> getAllPaints(@RequestParam("page") int page, @RequestParam("limit") int limit) {
        return paintingRepository.findAll(PageRequest.of(page, limit, Sort.by(Sort.Direction.ASC, "name")));
    }

    @PostMapping("/deletepaints")
    public ResponseEntity<HttpStatus> deletePaints(@RequestBody List<Painting> paints) {
        List<Long> listOfIds = new ArrayList<>();
        for (Painting artist: paints){
            listOfIds.add(artist.id);
        }
        paintingRepository.deleteAllById(listOfIds);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/paints/{id}")
    public ResponseEntity<Painting> updatePaints(@PathVariable(value = "id") Long museumId,
                                                  @RequestBody Painting museum) {
        Painting mus = null;
        Optional<Painting> cc = paintingRepository.findById(museumId);
        if (cc.isPresent()) {
            mus = cc.get();
            mus.name = museum.name;
            paintingRepository.save(mus);
            return ResponseEntity.ok(mus);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "artist not found");
        }
    }

}