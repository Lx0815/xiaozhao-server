package com.xiaozhao.xiaozhaostart.mapper;

import com.xiaozhao.xiaozhaoserver.mapper.ClientMapper;
import com.xiaozhao.xiaozhaoserver.model.Client;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.UUID;


@SpringBootTest
public class ClientMapperTest {

    @Autowired
    private ClientMapper clientMapper;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    public void testInsert() {
        Client client = new Client();
        client.setClientId(UUID.randomUUID().toString())
                .setLocationId(1)
                .setPersonGroupId(1)
                .setLastUploadDateTime(LocalDateTime.now())
                .setCreateDateTime(LocalDateTime.now())
                .setUpdateDateTime(LocalDateTime.now());
        int row = clientMapper.insert(client);
        System.out.println(row);
        System.out.println("clientMapper.selectById(client.getId()) = \n" + clientMapper.selectById(client.getId()));
    }
}