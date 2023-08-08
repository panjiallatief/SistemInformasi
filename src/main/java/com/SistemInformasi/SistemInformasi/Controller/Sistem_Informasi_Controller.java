package com.SistemInformasi.SistemInformasi.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.SistemInformasi.SistemInformasi.Entity.Media;
import com.SistemInformasi.SistemInformasi.Entity.SistemInformasi;
import com.SistemInformasi.SistemInformasi.Repository.Media_Repository;
import com.SistemInformasi.SistemInformasi.Repository.Sistem_Informasi_Repository;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.core.env.Environment;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;


@Controller
public class Sistem_Informasi_Controller {
    
    @Autowired
    private Sistem_Informasi_Repository sistem_informasi_repository;

    @Autowired
    private Environment env;

    @Autowired
    private Media_Repository mediarepository;

    @GetMapping(value = "/")
    public String index() {
        
        return "index";
    }

    @GetMapping(value = "/streamImage")
    public StreamingResponseBody handleRequest (@RequestParam String filename, HttpServletResponse response) {
    response.setContentType("image/jpeg");

        return new StreamingResponseBody() {
            public void writeTo (OutputStream out) throws IOException {
                File Image = new File(env.getProperty("URL.FILE_PRIEVIEW_Image") + "/" + filename);

                try {
                    byte[] fileContent = Files.readAllBytes(Image.toPath());
                    out.write(fileContent);
                } catch (IOException image) {
                    System.out.println(image);
                }
            }
        };
    }

    @RequestMapping(value = "/post", consumes = {
            MediaType.MULTIPART_FORM_DATA_VALUE }, produces = APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Map> post(@RequestParam String nama, @RequestParam String deskripsi, @RequestParam String kategori, @RequestParam(required = false) MultipartFile files){
        Map data = new HashMap<>();

        String namafile = "";
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
        String ddMMyyyy = sdf.format(new Date());

        SistemInformasi simasi = new SistemInformasi();
        namafile = ddMMyyyy + "_" + nama + "_" + kategori + simasi.getId().toString();

        simasi.setNama(nama);
        simasi.setUpdatedAt(date);
        simasi.setDeskripsi(deskripsi);
        simasi.setKategori(kategori);
        simasi.setFilename(namafile);
        sistem_informasi_repository.save(simasi);

        try {
            files.transferTo(new File(env.getProperty("URL.FILE_IN_IMAGE") + "/" + namafile));
            Media media = new Media();
            media.setFilename(namafile);
            mediarepository.save(media);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        data.put("icon", "success");
        data.put("message", "data berhasil di insert");
        return new ResponseEntity<>(data, HttpStatus.OK);

    }
}
