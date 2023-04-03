package com.example.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.backend.models.Museum;

@Repository
public interface MuseumRepository  extends JpaRepository<Museum, Long>
{

}
