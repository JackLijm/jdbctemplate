/**
 * <p>文件名称: FileController.java</p>
 * <p>文件描述: </p>
 * <p>版权所有: 版权所有(C)2016-</p>
 * <p>公    司: 金证财富南京科技有限公司</p>
 * <p>内容摘要: </p>
 * <p>其他说明: </p>
 * <p>创建日期： 2020/3/23 14:41 </p>
 * <p>完成日期：</p>
 * <p>修改记录1:</p>
 * <pre>
 *    修改日期：
 *    版 本 号：
 *    修 改 人：
 *    修改内容：
 * </pre>
 * <p>修改记录2：…</p>
 *
 * @version 1.0
 * @author lijm@szkingdom.com
 */
package com.example.jdbcteplate.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

@RestController
@RequestMapping("file")
public class FileController {

    @RequestMapping(value = "/download", method = {RequestMethod.GET, RequestMethod.POST})
    public void downLoadFile(HttpServletRequest request, HttpServletResponse response, String fileName) throws IOException {
        //获取本地文件
        ClassPathResource filePath = new ClassPathResource("file/fileTemplate.txt");
        FileInputStream inputStream = new FileInputStream(filePath.getFile());
        OutputStream outputStream = response.getOutputStream();
        //指定浏览器以附件的形式下载文件
        response.setHeader("content-disposition","attachment;filename="+ URLEncoder.encode("你好.txt","utf-8"));
        //缓冲区
        byte[] b = new byte[1024];
        int index = 0;
        while ((index = inputStream.read(b)) > -1) {
            //往输出流写数据
            outputStream.write(b, 0, index);
        }
        inputStream.close();
        outputStream.close();
    }
}
