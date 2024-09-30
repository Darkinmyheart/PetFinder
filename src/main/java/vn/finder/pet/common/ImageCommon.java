package vn.finder.pet.common;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.net.URL;

public class ImageCommon {
    public static List<String> saveImage(List<MultipartFile> file, String url) throws IOException {
        List<String> listUrl = new ArrayList<>();
        if (file != null && !file.isEmpty()) {
            Path directoryPath = Paths.get(url);
            if (Files.notExists(directoryPath)) {
                Files.createDirectories(directoryPath);
            }
            for (MultipartFile f : file) {
                // Generate a random name using current timestamp and random number
                String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                int randomNum = new Random().nextInt(1000);

                String originalFilename = f.getOriginalFilename();
                String fileExtension = "";
                if (originalFilename != null && originalFilename.contains(".")) {
                    fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
                }

                String fileName = timeStamp + "_" + randomNum + (fileExtension.isEmpty() ? "" : "." + fileExtension);

                Path path = directoryPath.resolve(fileName);

                listUrl.add(fileName);
                System.out.println(path);
                if (Files.notExists(path)) {
                    Files.copy(f.getInputStream(), path);
                    System.out.println("File saved successfully with random name: " + path);
                    System.out.println("Absolute path: " + path.toAbsolutePath());
                } else {
                    System.out.println("File already exists.");
                }
            }
        }
        return listUrl;
    }

    public static void deleteImage(String name, String url) throws IOException {
        if (!isHttpUrl(name)) {
            // Tạo đường dẫn đầy đủ tới file cần xóa
            Path pathToFile = Paths.get(url).resolve(name);

            // Kiểm tra xem file có tồn tại không
            if (Files.exists(pathToFile)) {
                // Xóa file
                Files.delete(pathToFile);
                System.out.println("File deleted successfully: " + pathToFile);
            } else {
                System.out.println("File not found: " + pathToFile);
            }
        }
    }

    private static boolean isHttpUrl(String pathString) {
        try {
            new URL(pathString);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
