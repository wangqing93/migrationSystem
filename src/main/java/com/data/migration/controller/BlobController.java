package com.data.migration.controller;

import com.data.migration.dao.destination.DestinationMapper;
import com.data.migration.dao.source.SourceMapper;
import com.data.migration.service.DataBaseService;
import com.data.migration.service.LogRecordService;
import com.data.migration.service.MigrationService;
import com.data.migration.service.impl.TaskCronChange;
import com.data.migration.utils.FormatUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.sql.rowset.serial.SerialBlob;
import java.io.UnsupportedEncodingException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


@RestController
@EnableAutoConfiguration
public class BlobController {
    private static org.slf4j.Logger logger = LoggerFactory.getLogger(BlobController.class);

    @Resource(name = "migrationService")
    MigrationService migrationService;

    @Resource(name = "LogRecordService")
    LogRecordService logRecordService;

    @Resource(name = "dataBaseService")
    DataBaseService dataBaseService;

    @Autowired
    DestinationMapper destinationMapper;

    @Autowired
    SourceMapper sourceMapper;

    @RequestMapping("/allMigration")
    public String allMigration() {
            HashSet<String> tableNames = new HashSet<>();
            tableNames.add("CJCGZL_YWCGCJCGQX");
//            HashSet<String> tableNames = migrationService.gatTableNames();
            migrationService.allMigration(tableNames);
        return "{\"IS_SUCCEEDED\": 1, \"tableName\": \"CJCGZL_CCCSPJCG\",  \"message\": \"OK\"}";
    }
    /*
    * 1. 获取所有的含Blob的表格
    * 2. 将其表名及是否迁移写到数据库中
    * 3. 将其Blob字段名称及其对应的文件名写入数据表TABLE_BLOB_COLUMNS中
    *
    *
    * */
    @RequestMapping("/insertTableBlobColumns")
    public String insertTableBlobColumns() {
        try {
            HashSet<String> tableNames = migrationService.gatTableNames();
            for(String tableName : tableNames){
                dataBaseService.insertMigratingTable(tableName, 1);
                Map<String, String> allColumns = dataBaseService.getTableColumn(tableName);
                Set<String> keys = allColumns.keySet();
                for(String key : keys){
                    if(allColumns.get(key).equals("BLOB")){
                        dataBaseService.insertTableBlobColumns(tableName, key + "MC", key);
                    }
                }
            }
        } catch (DataIntegrityViolationException e){
            return "{\"IS_SUCCEEDED\": 0,  \"message\": "+ e.getCause().getMessage() +"}";
        }
        return "{\"IS_SUCCEEDED\": 1,  \"message\": \"OK\"}";
    }
    @RequestMapping("/deleteOldTables")
    public String deleteOldTables(){
        HashSet<String> tableNames = migrationService.gatTableNames();
        dataBaseService.deleteTable(tableNames);
        return "{\"IS_SUCCEEDED\": 1,  \"message\": \"OK\"}";
    }


    @RequestMapping("/hello")
    public void hello(String task) {
        System.out.println(task);
        if (task.equals("")) {
            task = "0/10 * * * * *";
        }
        TaskCronChange.cron = task;
//        logger.info("web:hello 调用");
    }
    @RequestMapping("/test")
    public String test(){
        HashSet<String> tableset = new HashSet<>();
        tableset.add("FOXBPM_MAIL");
        String res = migrationService.updateMigration(tableset ,"2020-01-04 15:00:00");
        return res;
    }

    @RequestMapping("/getPicEntity")
    public String getPicEntity() throws SQLException, UnsupportedEncodingException {
        Map<String, Blob> test = destinationMapper.getBlobTest();
        Blob blob = new SerialBlob(test.get("CJTTJ"));
        String productNo = new String(FormatUtils.blobToBytes(blob),"gbk");
        return productNo;
    }

    @RequestMapping("/updateMigration")
    public String updateMigration(){
//        try {
            HashSet<String> tableNames = new HashSet<>();
            tableNames.add("CJCGZL_CCCSPJCG");
//            HashSet<String> tableNames = migrationService.gatTableNames();
            String lastUpdateTime = "2019-11-11 12:00:00";
            migrationService.updateMigration(tableNames, lastUpdateTime);
        return "{\"IS_SUCCEEDED\": 1, \"tableName\": \"CJCGZL_CCCSPJCG\",  \"message\": \"OK\"}";
    }
}
