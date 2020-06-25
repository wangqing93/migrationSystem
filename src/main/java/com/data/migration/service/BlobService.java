package com.data.migration.service;

import org.apache.ibatis.annotations.Param;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;

import java.net.ConnectException;
import java.security.NoSuchAlgorithmException;
import java.sql.Blob;
import java.sql.SQLSyntaxErrorException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
@Component
public interface BlobService {
    Map<String, String> getBlobColumn(String tableName);
    Map<String, String> getNotBlobColumn(String tableName);

    Map<String, Object> getBlobValue(String fileNameColumn,
                                   String blobColumn,
                                   String tableName,
                                   String condition,
                                   String primaryCondition) throws SQLSyntaxErrorException;
    HashSet<Map<String, Object>> getNotBlobValue(String tableName, String columns);

    void insertBlobItem(String tableName,
                         String primaryCondition,
                         String fileNameColumn,
                         String fileName,
                         String blobFileColumn,
                         String blobFile);
    void insertNotBlobItem(List<String> values);
    JSONObject uploadBlob(String fileName, Blob data) throws NoSuchAlgorithmException, ResourceAccessException, ConnectException;
}
