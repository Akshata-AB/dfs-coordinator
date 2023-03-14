package com.scu.ds.dfs.dfscoordinator.controller;

import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Log
@RestController("/documents")
public class DocumentController {
    private final Path root = Paths.get("/Users/akshatakadam/IdeaProjects/dfs-coordinator/uploads");

    @PostMapping("/upload")
    public ResponseEntity uploadDocument(@RequestParam("file") MultipartFile file) throws IOException {

        try {

            Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()));

            log.info("Controller");

        } catch (Exception e) {
            if (e instanceof FileAlreadyExistsException) {
                return ResponseEntity.badRequest().body("A file of that name already exists.");
                // throw new RuntimeException("A file of that name already exists.");
            }
            throw new RuntimeException(e.getMessage());
        }
        return ResponseEntity.ok("Welcome to chunking pandey");
    }

    //@GetMapping("/download")
    // @GetMapping("/files/{filename:.+}")
    @ResponseBody
    @GetMapping("/download/{file:.+}")
    public ResponseEntity downloadDocument(@PathVariable String file) {

        //if(Files.isReadable(this.root.resolve(file.name()))){
        try {
            Resource fileResource = new UrlResource(this.root.resolve(file).toUri());

            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" + fileResource.getFilename() + "\"").body(fileResource);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
