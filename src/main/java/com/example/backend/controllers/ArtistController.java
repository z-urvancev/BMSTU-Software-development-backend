package com.example.backend.controllers;

import com.example.backend.models.Artist;
import com.example.backend.repositories.ArtistRepository;
import com.example.backend.repositories.CountryRepository;
import com.example.backend.tools.DataValidationException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class ArtistController {

    @Autowired
    ArtistRepository artistRepository;

    public ArtistController(ArtistRepository artistRepository, CountryRepository countryRepository) {
        this.artistRepository = artistRepository;
    }

    @GetMapping("/artists")
    public List<Artist> getAllArtists() {
        return (List<Artist>) artistRepository.findAll();
    }

    @GetMapping("/artists/{id}")
    public ResponseEntity<Object> getArtist(@PathVariable(value="id") Long artistId) throws DataValidationException {
        Artist artist = artistRepository.findById(artistId)
                .orElseThrow(() -> new DataValidationException("Артист с таким индексом не найдена"));
        return ResponseEntity.ok(artist);
    }

    @PostMapping("/artists")
    public ResponseEntity<Object> createArtist(@Valid @RequestBody Artist artist) throws DataValidationException {
        try {
            Artist nc = artistRepository.save(artist);
            return new ResponseEntity<Object>(nc, HttpStatus.OK);
        }
        catch(Exception ex) {
            if (ex.getMessage().contains("artist.name_UNIQUE"))
                throw new DataValidationException("Этот артист уже есть в базе");
            else
                throw new DataValidationException("Неизвестная ошибка");
        }
    }

    @PutMapping("/artists/{id}")
    public ResponseEntity<Object> updateArtist(@PathVariable(value = "id") Long artistId, @RequestBody Artist artistDetails) throws DataValidationException {
        try {
            Artist artist = artistRepository.findById(artistId)
                    .orElseThrow(() -> new DataValidationException("Страна с таким индексом не найдена"));
            artist.name = artistDetails.name;
            artist.age = artistDetails.age;
            artistRepository.save(artist);
            return ResponseEntity.ok(artist);
        }
        catch (Exception ex) {
            if (ex.getMessage().contains("artist.name_UNIQUE"))
                throw new DataValidationException("Этот артист уже есть в базе");
            else
                throw new DataValidationException("Неизвестная ошибка");
        }
    }

    @DeleteMapping("/artists/{id}")
    public ResponseEntity<String> deleteArtist(@PathVariable(value = "id") Long artistId) {
        Optional<Artist> artistToDelete  = artistRepository.findById(artistId);
        if (artistToDelete.isEmpty()) {
            return new ResponseEntity<>("Deletion failed", HttpStatus.OK);
        }
        else {
            artistRepository.delete(artistToDelete.get());
            return new ResponseEntity<>("Deletion successful", HttpStatus.OK);
        }
    }

    @PostMapping("/deleteartists")
    public ResponseEntity<Object> deleteArtists(@Valid @RequestBody List<Artist> artists) {
        artistRepository.deleteAll(artists);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
