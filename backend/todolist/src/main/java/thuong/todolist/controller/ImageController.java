package thuong.todolist.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import thuong.todolist.service.FileServiceImp;

@RestController
@RequestMapping("/image")
public class ImageController {
    @Autowired
    private FileServiceImp fileServiceImp;

    @GetMapping("/{filename}")
    public ResponseEntity<?> download(@PathVariable String filename){
        Resource resource = fileServiceImp.loadFile(filename);

        // Tự động xác định Content-Type dựa trên tên file
        String contentType = "application/octet-stream"; // Default
        if (filename.endsWith(".jpg") || filename.endsWith(".jpeg")) {
            contentType = MediaType.IMAGE_JPEG_VALUE;
        } else if (filename.endsWith(".png")) {
            contentType = MediaType.IMAGE_PNG_VALUE;
        } else if (filename.endsWith(".gif")) {
            contentType = MediaType.IMAGE_GIF_VALUE;
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType)) // Xác định loại file
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                .body(resource);
    }
}
