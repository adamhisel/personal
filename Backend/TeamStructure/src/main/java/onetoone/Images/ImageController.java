package onetoone.Images;

import onetoone.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@RestController
public class ImageController {

    @Autowired
    ImageRepository imageRepository;
    String success = "Successfully added an image!";

    @PostMapping(path = "/{userId}/upload")
    public String addImage(@PathVariable int userId,  @RequestParam("file") MultipartFile file) throws SQLException, IOException {
        byte[] bytes = file.getBytes();
        Blob blob = new javax.sql.rowset.serial.SerialBlob(bytes);

        Image image = new Image();
        image.setId(userId);
        image.setImage(blob);
        imageRepository.save(image);
        return success;
    }

    @GetMapping("/getimage/{imageId}")
    public ResponseEntity<byte[]> getImageById(@PathVariable int imageId) {
        Optional<Image> optionalImage = imageRepository.findById(imageId);

        if (optionalImage.isPresent()) {
            Image image = optionalImage.get();

            try {
                Blob blob = image.getImage();
                byte[] bytes = blob.getBytes(1, (int) blob.length());

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.IMAGE_PNG); // Set the content type to image/jpeg

                return ResponseEntity.ok().headers(headers).body(bytes);
            } catch (SQLException e) {
                return ResponseEntity.status(500).body("Error retrieving the image".getBytes());
            }
        } else {
            return ResponseEntity.status(404).body("Image not found".getBytes());
        }
    }


}
