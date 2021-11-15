package com.sdk.actnow.s3;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3Uploader {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String upload(MultipartFile multipartFile, String dirName) throws IOException {
        File uploadFile = convert(multipartFile)
                .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File로 전환이 실패했습니다."));
        System.out.println("컨버트 성공");
        return upload(uploadFile, dirName);
    }

    private String getUuid() {
        return UUID.randomUUID().toString().replaceAll("-","");
    }

    private String upload(File uploadFile, String dirName) {
        String fileName = dirName + "/" + getUuid() + uploadFile.getName();
        System.out.println("uuid성공");
        String uploadImageUrl = putS3(uploadFile, fileName);
        System.out.println("업로드 성공");
        removeNewFile(uploadFile);
        return uploadImageUrl;
    }

    private String putS3(File uploadFile, String fileName) {
        final TransferManager transferManager = new TransferManager(this.amazonS3Client);
        // 업로드 시도
        final Upload upload =  transferManager.upload(new PutObjectRequest(bucket, fileName, uploadFile));

        try {
            upload.waitForCompletion();
        } catch (AmazonClientException amazonClientException) {
            log.error(amazonClientException.getMessage());
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }
        String url = "https://actnow-bucket.s3.ap-northeast-2.amazonaws.com/"+fileName;
        return url;
    }

//    private String deleteS3(String fileName) {
//        final TransferManager transferManager = new TransferManager(this.amazonS3Client);
//        // 업로드 시도
//        final Upload upload =  transferManager.upload(new PutObjectRequest(bucket, fileName, uploadFile));
//
//        try {
//            upload.waitForCompletion();
//        } catch (AmazonClientException amazonClientException) {
//            log.error(amazonClientException.getMessage());
//        } catch (InterruptedException e) {
//            log.error(e.getMessage());
//        }
//        String url = "https://actnow-bucket.s3.ap-northeast-2.amazonaws.com/"+fileName;
//        return url;
//    }

    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("파일이 삭제되었습니다.");
        } else {
            log.info("파일이 삭제되지 못했습니다.");
        }
    }

    private Optional<File> convert(MultipartFile file) throws IOException {
        File convertFile = new File(file.getOriginalFilename()+".jpg");
        if(convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }

        return Optional.empty();
    }
}


