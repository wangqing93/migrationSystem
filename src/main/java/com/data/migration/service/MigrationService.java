package com.data.migration.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashSet;
import java.util.Map;

@Component
public interface MigrationService {
    HashSet<String> gatTableNames() throws DataIntegrityViolationException;
    String updateMigration(HashSet<String> tableNames, String lastUpdateTime);
    String allMigration(HashSet<String> tableNames);
}
