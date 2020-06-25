package com.data.migration.service.impl;

import com.data.migration.entity.DynamicBean;
import com.data.migration.service.BlobService;
import com.data.migration.service.DataBaseService;
import com.data.migration.service.MigrationService;
import com.data.migration.utils.FormatUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.LoggerFactory;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.thymeleaf.util.StringUtils;
import oracle.jdbc.OracleDatabaseException;
import javax.annotation.Resource;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.ConnectException;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

@Service("migrationService")
public class MigrationServiceImpl implements MigrationService {
    private static org.slf4j.Logger logger = LoggerFactory.getLogger(FastDFSClient.class);
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    @Resource(name = "blobService")
    private BlobService blobService;

    @Resource(name = "dataBaseService")
    private DataBaseService dataBaseService;

    @Override
    public HashSet<String> gatTableNames() throws DataIntegrityViolationException{
        HashSet<String> resultSet = new HashSet<String>();
        List<Map<String, String>> resTemp = dataBaseService.getTables();
        for (Map<String, String> item : resTemp){
            resultSet.add(item.get("TABLE_NAME"));
        }
        return resultSet;
    }

    @Override
    public String updateMigration(HashSet<String> tableNames, String lastUpdateTime) {
        try {
            if (tableNames == null || tableNames.size() == 0){
                return "No table available";
            }
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            for (String tableName : tableNames){
                logger.info(tableName + " starts migrating, " + df.format(new Date()));
                // ------------------------新增----------------------------
                insertValue(dataBaseService.getNewItem(tableName, lastUpdateTime), tableName, false);
                // ------------------------更新----------------------------
                insertValue(dataBaseService.getUpdatedItem(tableName, lastUpdateTime), tableName, true);
                logger.info(tableName + " migration finished " + df.format(new Date()));

            }
        } catch (JsonProcessingException e) {
            return e.getMessage();
        } catch (NoSuchAlgorithmException e) {
            return e.getMessage();
        } catch (Exception e){
            System.out.println("-------------------");
            return e.toString();
//        } catch (SQLException sqlx){
//            System.out.println("***********************************************");
//            if(sqlx.getCause() != null && sqlx.getCause() instanceof OracleDatabaseException) {
//                System.out.println("==============================================");
//                return sqlx.getCause().getMessage();
//            }
//        } catch (IOException e) {
//            return e.getCause().getMessage();
        }
        return "OK";
    }

    @Override
    public String allMigration(HashSet<String> tableNames) {
        try {
            if (tableNames == null || tableNames.size() == 0){
                return "No table available";
            }
            for (String tableName : tableNames){
//                if (tableName.equals("CJCGZL_CJCGTSJT") || tableName.equals("CJBZYGF")){
//                    continue;
//                }
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                logger.info(tableName + " starts migrating, " + df.format(new Date()));
                insertValue(dataBaseService.getAllItem(tableName), tableName, false);
                logger.info(tableName + " migration finished " + df.format(new Date()));
            }
        } catch (JsonProcessingException e) {
            return e.getMessage();
        } catch (NoSuchAlgorithmException e) {
            return e.getMessage();
        } catch (DataIntegrityViolationException e){
            return e.getCause().getMessage();
        } catch (IOException e) {
            return e.getCause().getMessage();
        } catch (NullPointerException e){
            return e.toString();
        }
        return "OK";
    }

    public void insertValue(HashSet<Map<String, Object>> data,
                            String tableName,
                            boolean isUpdate) throws JsonProcessingException, NoSuchAlgorithmException, DataIntegrityViolationException, ConnectException, NullPointerException {
        // 含blob的字段，key是blob列名，value是对应的文件名的字段名
        Map<String, String> blobColumnItemSet = blobService.getBlobColumn(tableName);

        Map<String, String> columnType = dataBaseService.getTableColumn(tableName);
        Set<String> blobColumnSet = blobColumnItemSet.keySet();
        int i = 0;
        List<String> resultList = new LinkedList<>();
        for(Map<String, Object> dataItem : data){
            i++;
            DynamicBean dynamicBean = new DynamicBean(columnType);
            for (String column : dataItem.keySet()){
                if(blobColumnSet.contains(column)){
                    JSONObject item = blobService.uploadBlob((String) dataItem.get(blobColumnItemSet.get(column)), (Blob)dataItem.get(column));
                    dataItem.put(column, item.toString());
                }
                dynamicBean.setValue(column, dataItem.get(column));
            }
            List<String> itemTemp = new ArrayList<>();
            List<String> columnListTemp = new ArrayList<>();
            for (String key : columnType.keySet()){
                columnListTemp.add(key);
                itemTemp.add(formatItem(columnType, dynamicBean, key));
            }
            if(isUpdate){
                List<Map<String, String>> primaryKey = dataBaseService.primaryKey(tableName);
                List<String> primaryList = new ArrayList<>();
                if(primaryKey != null && primaryKey.size() != 0) {
                    for (Map<String, String> primaryItem : primaryKey) {
                        primaryList.add(primaryItem.get("COLUMN_NAME"));
                    }
                }
                List<String> primaryCondition =  new ArrayList<>();
                for(int k = 0; k < primaryList.size(); k++){
                    System.out.println(formatItem(columnType, dynamicBean, primaryList.get(k)));
                    primaryCondition.add(primaryList.get(k) + "=" + formatItem(columnType, dynamicBean, primaryList.get(k)));
                }

                List<String> resTemp = new ArrayList<>();
                for(int j = 0; j < columnListTemp.size(); j++){
                    resTemp.add(columnListTemp.get(j)  + "=" + itemTemp.get(j));
                }
                dataBaseService.updateItem(tableName, String.join(",", resTemp), String.join(",", primaryCondition));
            } else {
                resultList.add("into " + tableName + "(" +  String.join(",", columnListTemp) + ") values (" + String.join(",", itemTemp) + ")");
                System.out.println("insert------------------------");
                if(i % 100 == 0){
                    if(resultList != null && resultList.size() != 0){
                        blobService.insertNotBlobItem(resultList);
                    }
                    resultList = new LinkedList<>();
                }
            }

        }
        if(!isUpdate){
            if(resultList != null && resultList.size() != 0){
                blobService.insertNotBlobItem(resultList);
            }
        }
    }
    public String formatItem (Map<String, String> columnType, DynamicBean dynamicBean, String key){
        if(columnType.get(key).equals("DATE") && dynamicBean.getValue(key)!= null){
            if(key.substring(key.length()-2, key.length()).equals("RQ")){
                return "to_date(\'" + (dynamicBean.getValue(key) + "").substring(0, 10) + "\', \'YYYY-MM-DD\')";
            } else {
                if(key.equals("XGSJ")){
                    return "to_date(\'" + dateFormat.format(new Date()) + "\', \'YYYY-MM-DD HH24:MI:SS\')";
                } else {
                    return "to_date(\'" + (dynamicBean.getValue(key) + "").substring(0, (dynamicBean.getValue(key) + "").length()-2) + "\', \'YYYY-MM-DD HH24:MI:SS\')";
                }
            }
        } else if(columnType.get(key).equals("BLOB") && dynamicBean.getValue(key)!= null){
            return "rawtohex(\'" + dynamicBean.getValue(key).toString() + "\')";
        } else if(columnType.get(key).equals("CLOB") && dynamicBean.getValue(key)!= null){
            return "to_clob(\'" + dynamicBean.getValue(key).toString() + "\')";
        }
        return FormatUtils.oracleFormat(dynamicBean.getValue(key)) + "";
    }
}
