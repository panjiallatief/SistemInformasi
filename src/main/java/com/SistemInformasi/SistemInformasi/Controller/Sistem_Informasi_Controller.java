package com.SistemInformasi.SistemInformasi.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.SistemInformasi.SistemInformasi.Entity.Media;
import com.SistemInformasi.SistemInformasi.Entity.SistemInformasi;
import com.SistemInformasi.SistemInformasi.Repository.Media_Repository;
import com.SistemInformasi.SistemInformasi.Repository.Sistem_Informasi_Repository;
import com.fasterxml.jackson.core.JsonProcessingException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.data.domain.Sort;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.HashMap;
import java.util.List;
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
    public String index(Model model) {
        List<SistemInformasi> dataa = sistem_informasi_repository.findAll(Sort.by(Sort.Direction.ASC, "updatedAt"));
        model.addAttribute("data", dataa);
        return "index";
    }

    @GetMapping(value = "/findkategori")
    public String findkategori(Model model, @RequestParam String kategori) {
        List<SistemInformasi> dataa = sistem_informasi_repository.showkategori(kategori);
        model.addAttribute("data", dataa);
        return "index";
    }

    @GetMapping(value = "/streamImage")
    public StreamingResponseBody handleRequest(@RequestParam String filename, HttpServletResponse response) {
        response.setContentType("image/jpeg");

        return new StreamingResponseBody() {
            public void writeTo(OutputStream out) throws IOException {
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
    public ResponseEntity<Map> post(@RequestParam String nama, @RequestParam String deskripsi,
            @RequestParam String harga, @RequestParam String kategori,
            @RequestPart(value = "files", required = false) MultipartFile files) throws JsonProcessingException {
        Map data = new HashMap<>();

        String namafile = "";
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
        String ddMMyyyy = sdf.format(new Date());
        String arrSplit[] = files.getOriginalFilename().split("\\.");
        String originalExtension = arrSplit[arrSplit.length - 1];

        SistemInformasi simasi = new SistemInformasi();

        simasi.setUpdatedAt(date);

        if (nama.equals("")) {
            simasi.setNama("-");
        } else {
            simasi.setNama(nama);
        }

        if (deskripsi.equals("")) {
            simasi.setDeskripsi("-");
        } else {
            simasi.setDeskripsi(deskripsi);
        }

        if (kategori.equals("")) {
            simasi.setKategori("-");
        } else {
            simasi.setKategori(kategori);
        }

        if (harga.equals("")) {
            simasi.setHarga("-");
        } else {
            simasi.setHarga(harga);
        }

        sistem_informasi_repository.save(simasi);

        SistemInformasi dataa = sistem_informasi_repository.findById(simasi.getId()).get();
        namafile = ddMMyyyy + "_" + nama + "_" + kategori + "_" + simasi.getId().toString() + "." + originalExtension;
        String space = namafile.replace(" ", "");
        String namafilee = space;

        dataa.setFilename(namafilee);
        sistem_informasi_repository.save(dataa);

        try {
            files.transferTo(new File(env.getProperty("URL.FILE_IN_IMAGE") + "/" + namafilee));
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

    @GetMapping(value = "/find")
    public ResponseEntity<Map> find(@RequestParam(required = true) Integer id) {
        Map data = new HashMap<>();

        if (!sistem_informasi_repository.existsById(id)) {
            data.put("message", "Data Tidak Ditemukan");
            return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
        }
        SistemInformasi dataa = sistem_informasi_repository.findById(id).get();
        data.put("data", dataa);
        return new ResponseEntity<>(data, HttpStatus.OK);

    }

    @PutMapping(value = "/editmateri")
    public ResponseEntity<Map> editmateri(@RequestParam(required = false) String nama,
            @RequestParam(required = false) String harga,
            @RequestParam(required = false) String deskripsi,
            @RequestParam(required = false) String kategori, @RequestParam(required = true) Integer id) {
        Map data = new HashMap<>();
        Date date = new Date();
        if (!sistem_informasi_repository.existsById(id)) {
            data.put("message", "Data Tidak Ditemukan");
            return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
        }
        SistemInformasi simasi = new SistemInformasi();

        simasi.setNama(nama);
        simasi.setUpdatedAt(date);
        simasi.setDeskripsi(deskripsi);
        simasi.setKategori(kategori);
        simasi.setHarga(harga);
        sistem_informasi_repository.save(simasi);

        data.put("icon", "success");
        data.put("message", "data berhasil di Edit");
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<Map> editDraft(@RequestParam(required = true) Integer id) {
        Map data = new HashMap<>();
        if (!sistem_informasi_repository.existsById(id)) {
            data.put("message", "Data Tidak Ditemukan");
            return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
        }
        sistem_informasi_repository.deleteById(id);

        data.put("icon", "success");
        data.put("message", "Data Berhasil di hapus");
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

}