package thuong.todolist.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    void uploadFile(MultipartFile file);
    Resource loadFile(String fileName);
}
