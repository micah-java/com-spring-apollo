package com.apollo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class OssConfig {

    @Bean
    public S3Presigner s3Presigner(){
        //
        AwsBasicCredentials awsBasicCredentials = AwsBasicCredentials.create(
                "AKIASTMS6M2JXHNTVGHL",
                "VFX85k+xluzbhUIwkLPLADLudEw5658XT65947LJ"
        );
        return S3Presigner.builder()
                .credentialsProvider(StaticCredentialsProvider.create(awsBasicCredentials))
                .region(Region.of("ap-east-1"))
                .build();
    }

    @Bean
    public S3Client s3Client(){
        //
        AwsBasicCredentials awsBasicCredentials = AwsBasicCredentials.create(
                "AKIASTMS6M2JXHNTVGHL",
                "VFX85k+xluzbhUIwkLPLADLudEw5658XT65947LJ"
        );
        return S3Client.builder().region(Region.of("ap-east-1"))
                .credentialsProvider(StaticCredentialsProvider.create(awsBasicCredentials))
                .build();
    }









}
