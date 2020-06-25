package com.data.migration.service;

import org.springframework.stereotype.Component;

import java.net.ConnectException;

@Component
public interface TaskExecService {

    void execAllTasks() throws ConnectException;
}
