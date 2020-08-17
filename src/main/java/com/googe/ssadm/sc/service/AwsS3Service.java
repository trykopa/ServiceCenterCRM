package com.googe.ssadm.sc.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class AwsS3Service {

    private final AmazonS3 amazonS3client;
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AwsS3Service.class);


    @Autowired
    public AwsS3Service(AmazonS3 amazonS3client) {
        this.amazonS3client = amazonS3client;
    }

    public boolean upload(File file, String filename, String bucketName){
        try {
            log.info("amazonS3client: putObject");
            amazonS3client.putObject(new PutObjectRequest(bucketName, filename, file)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public boolean createBucket(String bucketName){
        try {
            log.info("amazonS3client: createBucket");
            amazonS3client.createBucket(bucketName);
            return true;
        } catch (Exception e){
            return false;
        }
    }

    public List<String> getAllFileNameInBucket(String bucketName){
        List<String> bucketLst = new ArrayList<>();
        log.info("amazonS3client: listObjects");
        ObjectListing objectListing = amazonS3client.listObjects(bucketName);
        List<S3ObjectSummary> objectSummaries = objectListing.getObjectSummaries();
        for(S3ObjectSummary objectSummary : objectSummaries){
            bucketLst.add(objectSummary.getKey());
        }
        return bucketLst;
    }

    public List<Bucket> getAllBucketNames(){
        log.info("amazonS3client: listBuckets");
        return amazonS3client.listBuckets();
    }


    public ByteArrayOutputStream downloadFile(String fileName, String bucketName){
        try {
            log.info("amazonS3client: getObject");
            S3Object s3object = amazonS3client.getObject(new GetObjectRequest(bucketName, fileName));

            InputStream is = s3object.getObjectContent();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int len;
            byte[] buffer = new byte[4096];
            while ((len = is.read(buffer, 0, buffer.length)) != -1) {
                baos.write(buffer, 0, len);
            }
            return baos;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    public void deleteFromBucket(String bucketName, String fileName){
        try {
            log.info("amazonS3client: deleteObject" + fileName);
            amazonS3client.deleteObject(bucketName, fileName);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean checkBucketExist(String bucketName){
        return amazonS3client.doesBucketExistV2(bucketName);
    }
}
