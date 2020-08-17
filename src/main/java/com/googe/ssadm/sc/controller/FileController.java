package com.googe.ssadm.sc.controller;

import com.amazonaws.services.s3.model.Bucket;
import com.googe.ssadm.sc.service.AwsS3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/files")
public class FileController {

    private final AwsS3Service awsS3Service;

    @Autowired
    public FileController(AwsS3Service awsS3Service) {
        this.awsS3Service = awsS3Service;
    }

    @GetMapping("/files")
    public String redirectHome() {
        return "redirect:/files/filehome";
    }

    @GetMapping("/filehome")
    public String getFileHomePage(Model model) {
        List<Bucket> allBuckets = awsS3Service.getAllBucketNames();
        model.addAttribute("buckets" , allBuckets);
        return "filehome";
    }

    @PostMapping("/filehome")
    public String getFilesInSelectedBucket(@RequestParam("bucketName") String bucketName , Model model) {
        List<String> allFileNameInBucket = awsS3Service.getAllFileNameInBucket(bucketName);
        List<Bucket> allBuckets = awsS3Service.getAllBucketNames();
        model.addAttribute("s3FileNames" , allFileNameInBucket);
        model.addAttribute("buckets" , allBuckets);
        model.addAttribute("bucketName" , bucketName);
        return "filehome";
    }

    @PostMapping("/upload")
    public String uploadToS3(@RequestParam("file") MultipartFile multipartFile ,
                             @RequestParam("bucketName") String bucketName) throws IOException {
        File file = File.createTempFile("tmp" , "tmp");
        multipartFile.transferTo(file);
        awsS3Service.upload(file , multipartFile.getOriginalFilename() , bucketName);
        return "redirect:/files/filehome";
    }

    @PostMapping("/attach")
    public String attachFilesToS3(@RequestParam("file") MultipartFile multipartFile ,
                                  @RequestParam("bucketName") String bucketName ,
                                  @RequestParam("id") Long id) throws IOException {
        File file = File.createTempFile("tmp" , "tmp");
        multipartFile.transferTo(file);
        awsS3Service.createBucket(bucketName);
        awsS3Service.upload(file , multipartFile.getOriginalFilename() , bucketName);
        return "redirect:/orders/edit/" + id;
    }

    @GetMapping("/download/{bucketName}/{filename}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable("filename") String fileName ,
                                               @PathVariable("bucketName") String bucketName) {
        ByteArrayOutputStream byteArrayOutputStream = awsS3Service.downloadFile(fileName , bucketName);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .header(HttpHeaders.CONTENT_DISPOSITION , "attachment; filename=\"" + fileName + "\"")
                .body(byteArrayOutputStream.toByteArray());
    }

    @GetMapping("/delete/{bucketName}/{filename}")
    public String deleteFile(@PathVariable("filename") String fileName ,
                             @PathVariable("bucketName") String bucketName ,
                             HttpServletRequest request) {
        awsS3Service.deleteFromBucket(bucketName , fileName);
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }


}

