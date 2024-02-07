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

    @PostMapping(path = "user/{userId}/upload")
    public String addUserImage(@PathVariable int userId,  @RequestParam("file") MultipartFile file) throws SQLException, IOException {
        byte[] bytes = file.getBytes();
        Blob blob = new javax.sql.rowset.serial.SerialBlob(bytes);

        Image image = new Image();
        image.setUserId(userId);
        image.setImage(blob);
        imageRepository.save(image);
        return success;
    }
    @PostMapping(path = "team/{teamId}/upload")
    public String addTeamImage(@PathVariable int teamId,  @RequestParam("file") MultipartFile file) throws SQLException, IOException {
        byte[] bytes = file.getBytes();
        Blob blob = new javax.sql.rowset.serial.SerialBlob(bytes);

        Image image = new Image();
        image.setTeamId(teamId);
        image.setImage(blob);
        imageRepository.save(image);
        return success;
    }

    @GetMapping("/getimage/user/{userId}")
    public ResponseEntity<byte[]> getUserImageById(@PathVariable int userId) {
        Optional<Image> optionalImage = imageRepository.findByUserId(userId);

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
    @GetMapping("/getimage/team/{teamId}")
    public ResponseEntity<byte[]> getTeamImageById(@PathVariable int teamId) {
        Optional<Image> optionalImage = imageRepository.findByTeamId(teamId);

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
