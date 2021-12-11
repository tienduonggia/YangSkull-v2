package com.yangskull.admin.utils;

import com.sun.xml.bind.api.impl.NameConverter;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileUploadUtil {

    //lưu ảnh vào thư mục
    public static void saveFile(String uploadDir, String fileName, MultipartFile multipartFile) throws IOException {
        //E:user-photo/
        Path uploadPath = Paths.get(uploadDir);

        //KIỂM TRA đường dẫn đó tồn tại chưa
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);

        }

        try (InputStream inputStream = multipartFile.getInputStream()) {
            //Nối tên path lại > E:user-photo/ + filename >  E:user-photo/duonggiatien.jpg
            Path filePath = uploadPath.resolve(fileName);

            //file ảnh lúc đầu nó sẽ ở cái mục trên server temp(reset là mất) , mình sẽ copy đó qua bên thư mục real
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IOException("Could not save file: " + fileName, e);
        }
    }

    public static void cleanDir(String dir)
    {
        //forEach từng file trong thư mục đó và xóa
        Path dirPath = Paths.get(dir);
        try{
            Files.list(dirPath).forEach(file -> {
                if(!Files.isDirectory(file)){
                    try{
                        Files.delete(file);
                    }catch (IOException e){
                        System.out.println("Could not delete fail "+file);
                    }
                }
            });
        }catch (IOException e){
            System.out.println("Could not list directory "+ dirPath);
        }
    }
}
