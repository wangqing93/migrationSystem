package com.data.migration.dao.source;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Repository;

import java.sql.Blob;
import java.sql.SQLSyntaxErrorException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface SourceMapper {
    Map<String, Object> getBlobValue(@Param(value="fileNameColumn") String fileNameColumn,
                            @Param(value="blobColumn") String blobColumn,
                            @Param(value="tableName") String tableName,
                            @Param(value="condition") String condition,
                            @Param(value="primaryCondition") String primaryCondition) throws SQLSyntaxErrorException;

    HashSet<Map<String, Object>> getNotBlobValue(@Param(value="tableName") String tableName,
                                                 @Param(value="columns") String columns);

    HashSet<Map<String, Object>> getPrimaryKeyList(@Param(value="tableName") String tableName,
                                                   @Param(value="primaryKey") String primaryKey);
    List<Map<String, String>> getTableAndBlobColumns();
    HashSet<Map<String, Object>> getNewItem(@Param(value="tableName") String tableName,
                                            @Param(value="startTime") String startTime);
    HashSet<Map<String, Object>> getUpdatedItem(@Param(value="tableName") String tableName,
                                                @Param(value="startTime") String startTime);
    HashSet<Map<String, Object>> getAllItem(@Param(value="tableName") String tableName);
}
