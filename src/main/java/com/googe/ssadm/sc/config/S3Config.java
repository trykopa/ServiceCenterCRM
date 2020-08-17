package com.googe.ssadm.sc.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {
    @Value("${aws.accesskey}")
    private String accesskey;

    @Value("${aws.secretkey}")
    private String secretkey;

    @Value("${aws.region}")
    private String region;

    @Bean
    public AmazonS3 awsS3Client() {
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(accesskey, secretkey);
        return AmazonS3ClientBuilder.standard().withRegion(Regions.fromName(region))
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds)).build();
    }
}
