package thuong.todolist.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import thuong.todolist.exception.LoadFileException;
import thuong.todolist.exception.UploadFileException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileServiceImp implements FileService {

    @Value("${upload.path}")
    private String pathFile;

    @Override
    public void uploadFile(MultipartFile file) {
        Path root = Paths.get(pathFile);
        System.out.println("Upload path: " + root.toAbsolutePath());
        try {
            if(!Files.exists(root)){
                Files.createDirectories(root);
            }
            Path paths = root.resolve(file.getOriginalFilename());
            Files.copy(file.getInputStream(),paths, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            throw new UploadFileException("UploadFileException Could Not Save "+ e.getMessage());
        }
    }

    @Override
    public Resource loadFile(String fileName) {
        try {
            Path filePath = Paths.get(pathFile).resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("File not found: " + fileName);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error loading file: " + e.getMessage());
        }
    }

}
