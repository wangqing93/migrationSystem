package com.data.migration.service;

import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public interface LogRecordService {
    void insertLog(List<Map<String, Object>> values);
}
