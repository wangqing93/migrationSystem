package com.data.migration.service.impl;

import com.data.migration.dao.destination.DestinationMapper;
import com.data.migration.dao.source.SourceMapper;
import com.data.migration.entity.TABLE_BLOB_COLUMNS;
import com.data.migration.service.DataBaseService;
import oracle.jdbc.OracleDatabaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLNonTransientException;
import java.sql.SQLSyntaxErrorException;
import java.util.*;

@Service("dataBaseService")
public class DataBaseServiceImpl implements DataBaseService {
    @Autowired
    SourceMapper sourceMapper;

    @Autowired
    DestinationMapper destinationMapper;

    @Override
    public List<Map<String, String>> primaryKey(String tableName){
        return destinationMapper.getPrimaryKey(tableName);
    }

    @Override
    public HashSet<Map<String, Object>> getPrimaryKeyList(String tableName, String primaryKey){
        return sourceMapper.getPrimaryKeyList(tableName, primaryKey);
    }

    @Override
    public List<Map<String, String>> getTableAndColumns(){
        return sourceMapper.getTableAndBlobColumns();
    }

    @Override
    public List<Map<String, String>> getTables(){
        return destinationMapper.getTables();
    }

    @Override
    public HashSet<TABLE_BLOB_COLUMNS> getBlobColumn(String tableName){
        return destinationMapper.getBlobColumn(tableName);
    }

    @Override
    public HashSet<Map<String, Object>> getNewItem(String tableName,
                                            String startTime) throws Exception {
        return sourceMapper.getNewItem(tableName, startTime);
    }

    @Override
    public HashSet<Map<String, Object>> getUpdatedItem(String tableName,
                                                String startTime) throws Exception {
        return sourceMapper.getUpdatedItem(tableName, startTime);
    }

    @Override
    public Map<String, String> getTableColumn(String tableName) throws DataIntegrityViolationException{
        Map<String, String> notBlobItem =  new HashMap<>();
        HashSet<Map<String, String>> records = destinationMapper.getColumnType(tableName);
        for(Map<String, String> record : records) {
            notBlobItem.put(record.get("COLUMN_NAME"), record.get("DATA_TYPE"));
        }
        return notBlobItem;
    }

    @Override
    public HashSet<Map<String, Object>> getAllItem(String tableName) throws DataIntegrityViolationException {
        return sourceMapper.getAllItem(tableName);
    }

    @Override
    public void updateItem(String tableName,
                    String updateItems,
                    String primaryCondition) throws DataIntegrityViolationException {
        destinationMapper.updateItem(tableName, updateItems, primaryCondition);
    }

    @Override
    public void insertTableBlobColumns(String tableName, String fileNameColumn, String blobColumn) throws DataIntegrityViolationException{
        destinationMapper.insertTableBlobColumns(tableName, fileNameColumn, blobColumn);
    }
    @Override
    public void insertMigratingTable(String tableName, int isMigrating){
        destinationMapper.insertMigratingTable(tableName, isMigrating);
    }
    @Override
    public void deleteTable(Set<String> tableNames){
        for(String tableName : tableNames){
            destinationMapper.deleteTable(tableName);
        }
    }
    @Override
    public Date getStartTime(String tableName){
        return destinationMapper.getStartTime(tableName);
    }
}
