package com.fitshare.backend.api.service;

import java.io.File;

public interface S3Service {

    String upload(File uploadFile, String dirName);

    void delete(String currentFilePath);
}
