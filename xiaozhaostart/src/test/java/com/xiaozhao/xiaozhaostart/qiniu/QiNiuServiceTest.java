package com.xiaozhao.xiaozhaostart.qiniu;

import com.xiaozhao.xiaozhaoserver.service.QiNiuYunService;
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
import java.util.List;

/**
 * @description:
 * @author: Ding
 * @version: 1.0
 * @createTime: 2022-12-13 19:46:19
 * @modify:
 */

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
        List<String> stringList = qiNiuYunService.saveMultipartBase64ImageList(list, "");
        System.out.println(stringList);
    }

}
