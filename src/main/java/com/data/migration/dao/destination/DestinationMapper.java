package com.data.migration.dao.destination;

import com.data.migration.entity.TABLE_BLOB_COLUMNS;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.sql.Blob;

import java.util.*;

@Mapper
@Repository
public interface DestinationMapper {
    void insertBlobValue(@Param(value="tableName") String tableName,
                         @Param(value="primaryCondition") String primaryCondition,
                         @Param(value="fileNameColumn") String fileNameColumn,
                         @Param(value="fileName") String fileName,
                         @Param(value="blobFileColumn") String blobFileColumn,
                         @Param(value="blobFile") String blobFile);
    void insertNotBlobValue(@Param(value="values") List<String> values);
    void insertTask(@Param(value="taskId") int taskId,
                    @Param(value="taskName") String taskName,
                    @Param(value="startTaskTime") String startTaskTime,
                    @Param(value="isRepeated") int isRepeated,
                    @Param(value="remarks") String remarks,
                    @Param(value="isAll") int isAll,
                    @Param(value="isDeleted") int isDeleted);

    void insertTableNamesForTaskId(@Param(value="tableNames") Set<String> tableNames,
                                   @Param(value="taskId") int taskId);

    void deleteTask(@Param(value="taskId") int taskId);

    void modifyTask(@Param(value="taskId") int taskId,
                    @Param(value="newStartTime") String newStartTime);

    HashSet<Map<String, Object>> getTasks();
    HashSet<Map<String, Object>> getTablesOneTask(@Param(value="taskId") int taskId);
    Map<String, Blob> getBlobTest();
    HashSet<Map<String, String>> getColumnType(@Param(value="tableName") String tableName);
    HashSet<TABLE_BLOB_COLUMNS> getBlobColumn(@Param(value="tableName") String tableName);
    List<Map<String, String>> getTables();
    List<Map<String, String>> getPrimaryKey(@Param(value="tableName") String tableName);
    void updateItem(@Param(value="tableName") String tableName,
                    @Param(value="updateItems") String updateItems,
                    @Param(value="primaryCondition") String primaryCondition);
    int getTaskId();
    void insertTableBlobColumns(@Param(value="tableName") String tableName,
                                @Param(value="fileNameColumn") String fileNameColumn,
                                @Param(value="blobColumn") String blobColumn);
    void insertMigratingTable(@Param(value="tableName") String tableName,
                              @Param(value="isMigrating") int isMigrating);
    void deleteTable(@Param(value="tableName") String tableName);
    Date getStartTime(@Param(value="tableName") String tableName);
}
