package com.data.migration.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.params.shadow.com.univocity.parsers.conversions.Conversions;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.sql.Timestamp;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BlobControllerTest {

    private MockMvc mvc;

    @Before
    public void setUp() throws Exception {
        mvc = MockMvcBuilders.standaloneSetup(new BlobController()).build();
    }

    @Test
    public void migrationBlobTest() throws Exception {
//        mvc.perform(MockMvcRequestBuilders.get("/migration").accept(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andDo(MockMvcResultHandlers.print())
//                .andReturn();
        String key = "2019-10-20 12:22:21.0";
        System.out.println(key.substring(0, key.length()-2));
//        System.out.println((Class) java.sql.Timestamp.class);
//        System.out.println("------");
    }
}
