package com.videohost.videohost_server.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

@RestController()
public class VideoController {

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String fileUpload(@RequestParam("file") MultipartFile multipartFile, Long userId) throws IOException {
        File dir = new File("videos/" + userId + "/");
        if (!dir.exists()) {
            boolean mkdirs = dir.mkdirs();
        }
        File convertFile = new File(dir.getAbsolutePath() + "/" + multipartFile.getOriginalFilename());
        convertFile.createNewFile();
        FileOutputStream fout = new FileOutputStream(convertFile);
        fout.write(multipartFile.getBytes());
        fout.close();
        return convertFile.getAbsolutePath();
    }

    @GetMapping("/videos")
    public byte[] streamVideoFile(@RequestParam String path) throws IOException {
        File file = new File(path);
        if (file.exists()) {
            File absoluteFile = file.getAbsoluteFile();
            byte[] fileContent = Files.readAllBytes(absoluteFile.toPath());
            return fileContent;
        } else return null;
    }
}
