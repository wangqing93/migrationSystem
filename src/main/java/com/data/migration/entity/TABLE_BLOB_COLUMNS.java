package com.data.migration.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.Date;
@Component
public class TABLE_BLOB_COLUMNS {
    private String TABLE_NAME;
    private String FILENAME_COLUMN;
    private String BLOB_COLUMN;
    private Date LAST_UPDATE_TIMESTAMP;

    public String getTABLE_NAME() {
        return TABLE_NAME;
    }

    public void setTABLE_NAME(String TABLE_NAME) {
        this.TABLE_NAME = TABLE_NAME;
    }

    public String getFILENAME_COLUMN() {
        return FILENAME_COLUMN;
    }

    public void setFILENAME_COLUMN(String FILENAME_COLUMN) {
        this.FILENAME_COLUMN = FILENAME_COLUMN;
    }

    public String getBLOB_COLUMN() {
        return BLOB_COLUMN;
    }

    public void setBLOB_COLUMN(String BLOB_COLUMN) {
        this.BLOB_COLUMN = BLOB_COLUMN;
    }

    public TABLE_BLOB_COLUMNS() {
    }

    public Date getLAST_UPDATE_TIMESTAMP() {
        return LAST_UPDATE_TIMESTAMP;
    }

    public void setLAST_UPDATE_TIMESTAMP(Date LAST_UPDATE_TIMESTAMP) {
        this.LAST_UPDATE_TIMESTAMP = LAST_UPDATE_TIMESTAMP;
    }
}
