package com.data.migration.service.impl;
import com.data.migration.dao.destination.DestinationMapper;
import com.data.migration.dao.source.SourceMapper;
import com.data.migration.entity.FastDFSFile;
import com.data.migration.entity.TABLE_BLOB_COLUMNS;
import com.data.migration.service.BlobService;
import com.data.migration.utils.FormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.net.ConnectException;
import java.security.NoSuchAlgorithmException;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.Blob;
import java.sql.SQLSyntaxErrorException;
import java.util.*;

@Service("blobService")
public class BlobServiceImpl implements BlobService {
    private static Logger logger = LoggerFactory.getLogger(BlobServiceImpl.class);

    @Autowired
    SourceMapper sourceMapper;

    @Autowired
    DestinationMapper destinationMapper;

    @Override
    public Map<String, String> getBlobColumn(String tableName){
        Map<String, String> blobColumnSet = new HashMap<>();
        HashSet<TABLE_BLOB_COLUMNS> records = destinationMapper.getBlobColumn(tableName);
        for(TABLE_BLOB_COLUMNS record : records) {
            blobColumnSet.put(record.getBLOB_COLUMN(), record.getFILENAME_COLUMN());
        }
        return blobColumnSet;
    }

    @Override
    public Map<String, String> getNotBlobColumn(String tableName){
        Map<String, String> notBlobItem =  new HashMap<>();
        HashSet<Map<String, String>> records = destinationMapper.getColumnType(tableName);
        for(Map<String, String> record : records) {
            if(!record.get("DATA_TYPE").equals("BLOB")) {
                notBlobItem.put(record.get("COLUMN_NAME"), record.get("DATA_TYPE"));
            }
        }
        return notBlobItem;
    }
    @Override
    public Map<String, Object> getBlobValue(String fileNameColumn,
                                          String blobColumn,
                                          String tableName,
                                          String condition,
                                          String primaryCondition) throws SQLSyntaxErrorException {
        try {
            return sourceMapper.getBlobValue(fileNameColumn, blobColumn, tableName, condition, primaryCondition);
        }catch (SQLSyntaxErrorException e) {
            throw e;
        }
    }
    @Override
    public HashSet<Map<String, Object>> getNotBlobValue(String tableName, String columns){
        return sourceMapper.getNotBlobValue(tableName, columns);
    }
    @Override
    public void insertBlobItem(String tableName,
                               String primaryCondition,
                               String fileNameColumn,
                               String fileName,
                               String blobFileColumn,
                               String blobFile){
        destinationMapper.insertBlobValue(tableName, primaryCondition, fileNameColumn, fileName, blobFileColumn, blobFile);
    }
    @Override
    public void insertNotBlobItem(List<String> values){
        destinationMapper.insertNotBlobValue(values);
    }

    @Override
    public JSONObject uploadBlob(String fileName, Blob data) throws NoSuchAlgorithmException, ConnectException {
        JSONObject res = null;
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
        byte[] file_buff = FormatUtils.blobToBytes(data);
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] digest = md5.digest(file_buff);
        String hashString = new BigInteger(1, digest).toString(16);
        FastDFSFile file = new FastDFSFile(fileName, file_buff, ext, hashString);
        try {
            res = FastDFSClient.upload2(file);  //upload to fastdfs
        } catch (ConnectException e){
            throw e;
        }
        if (res==null) {
            logger.error("upload file failed,please upload again!");
        }

        return res;
    }
}
