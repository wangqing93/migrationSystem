package com.data.migration.utils;

import org.springframework.boot.configurationprocessor.json.JSONObject;

import javax.sql.rowset.serial.SerialBlob;
import java.io.BufferedInputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Blob;
import java.sql.SQLException;
import java.sql.Timestamp;

import static java.sql.Types.BLOB;

public class FormatUtils {
    public static Blob jsonObjectToBlob(JSONObject jsonObject){
        String temp = jsonObject.toString();
        Blob blob = null;
        try {
            blob = new SerialBlob(temp.getBytes("GBK"));
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return blob;
    }

    public static byte[] blobToBytes(Blob blob) {
        BufferedInputStream is = null;
        byte[] bytes = null;
        try {
            is = new BufferedInputStream(blob.getBinaryStream());
            bytes = new byte[(int) blob.length()];
            int len = bytes.length;
            int offset = 0;
            int read = 0;

            while (offset < len
                    && (read = is.read(bytes, offset, len - offset)) >= 0) {
                offset += read;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return bytes;
    }

    public static Object oracleFormat(Object item){
        if(item instanceof String){
            return "\'" + item + "\'";
        } else {
            return item;
        }
    }
}
