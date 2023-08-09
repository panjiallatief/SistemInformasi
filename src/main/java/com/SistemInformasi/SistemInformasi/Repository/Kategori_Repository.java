package com.SistemInformasi.SistemInformasi.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.SistemInformasi.SistemInformasi.Entity.Kategori;

public interface Kategori_Repository extends JpaRepository<Kategori, Integer> {
    
    @Query(value = "SELECT * FROM kategori WHERE nama_kategori = ?1",
        countQuery = "SELECT count(*) FROM kategori WHERE nama_kategori = ?1",
        nativeQuery = true)
    Optional <Kategori> findByNamekategori(String nama);
    
    @Query(value = "SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM kategori c WHERE c.nama_kategori = ?1",
        countQuery = "SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM kategori c WHERE c.nama_kategori = ?1",
        nativeQuery = true)
    boolean categoryboolean(String category);
}
