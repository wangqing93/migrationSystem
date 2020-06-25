package com.data.migration.service;

import com.data.migration.entity.TABLE_BLOB_COLUMNS;
import oracle.jdbc.OracleDatabaseException;
import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLNonTransientException;
import java.sql.SQLSyntaxErrorException;
import java.util.*;

@Component
public interface DataBaseService {
    List<Map<String, String>> primaryKey(String tableName);
    HashSet<Map<String, Object>> getPrimaryKeyList(String tableName, String primaryKey);
    List<Map<String, String>> getTableAndColumns();
    List<Map<String, String>> getTables();
    HashSet<TABLE_BLOB_COLUMNS> getBlobColumn(String tableName);
    HashSet<Map<String, Object>> getNewItem(String tableName,
                                            String startTime) throws Exception;
    HashSet<Map<String, Object>> getUpdatedItem(String tableName,
                                                String startTime) throws Exception;
    Map<String, String> getTableColumn(String tableName) throws DataIntegrityViolationException;
    HashSet<Map<String, Object>> getAllItem(String tableName) throws DataIntegrityViolationException;
    void updateItem(String tableName,
                    String updateItems,
                    String primaryCondition) throws DataIntegrityViolationException;
    void insertTableBlobColumns(String tableName, String fileNameColumn, String blobColumn) throws DataIntegrityViolationException;
    void insertMigratingTable(String tableName, int isMigrating);
    void deleteTable(Set<String> tableNames);
    Date getStartTime(String tableName);
}
