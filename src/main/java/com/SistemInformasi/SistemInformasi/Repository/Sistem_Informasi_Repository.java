package com.SistemInformasi.SistemInformasi.Repository;

import javax.print.attribute.standard.Media;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.SistemInformasi.SistemInformasi.Entity.Sistem_Informasi;

public interface Sistem_Informasi_Repository extends JpaRepository<Sistem_Informasi, Integer> {
    
    @Query(value = "SELECT * FROM sistem_informasi WHERE namafile = ?1",
        countQuery = "SELECT count(*) FROM sistem_informasi WHERE namafile = ?1",
        nativeQuery = true)
    Media getfilename(String namafile);

}
