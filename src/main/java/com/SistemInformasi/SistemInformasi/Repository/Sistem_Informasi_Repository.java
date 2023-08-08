package com.SistemInformasi.SistemInformasi.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.SistemInformasi.SistemInformasi.Entity.SistemInformasi;

public interface Sistem_Informasi_Repository extends JpaRepository<SistemInformasi, Integer> {
    
    // @Query(value = "SELECT * FROM sistem_informasi WHERE namafile = ?1",
    //     countQuery = "SELECT count(*) FROM sistem_informasi WHERE namafile = ?1",
    //     nativeQuery = true)
    // SistemInformasi getfilename(String namafile);

}
