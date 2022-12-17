package com.xiaozhao.xiaozhaostart.qiniu;

import com.xiaozhao.xiaozhaoserver.service.QiNiuYunService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;

/**
 * @description:
 * @author: Ding
 * @version: 1.0
 * @createTime: 2022-12-13 19:46:19
 * @modify:
 */

@Slf4j
@SpringBootTest
@SpringJUnitWebConfig
public class QiNiuServiceTest {

    @Autowired
    private QiNiuYunService qiNiuYunService;

    @Test
    public void testSaveMultipartBase64ImageList() throws IOException {
        LinkedList<String> list = new LinkedList<>();
        byte[] bys = null;
        try (InputStream is = Files.newInputStream(Paths.get("C:\\Users\\15074\\Desktop\\1.png"))) {
            bys = new byte[is.available()];
            int read = is.read(bys);
        }
        BASE64Encoder encoder = new BASE64Encoder();
        String encode = encoder.encode(bys);
        list.add(encode);
//        List<String> stringList = qiNiuYunService.saveMultipartBase64ImageList(list, QiNiuProperties.DEFAULT_QINIU_DIRECTORY);
//        System.out.println(stringList);
    }

}
