package com.SistemInformasi.SistemInformasi.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.SistemInformasi.SistemInformasi.Entity.Media;

public interface Media_Repository extends JpaRepository<Media, Integer> {

    @Query(value = "SELECT * FROM media WHERE filename = ?1",
        countQuery = "SELECT count(*) FROM media WHERE filename = ?1",
        nativeQuery = true)
    Media getfilename(String filename);

    @Query(value = "SELECT * FROM media WHERE filename = ?1",
        countQuery = "SELECT count(*) FROM media WHERE filename = ?1",
        nativeQuery = true)
    Optional<Media> getfilenameedit(String filename);

}
