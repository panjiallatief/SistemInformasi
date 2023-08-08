package com.SistemInformasi.SistemInformasi.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.SistemInformasi.SistemInformasi.Entity.SistemInformasi;

public interface Sistem_Informasi_Repository extends JpaRepository<SistemInformasi, Integer> {
    
    

}
