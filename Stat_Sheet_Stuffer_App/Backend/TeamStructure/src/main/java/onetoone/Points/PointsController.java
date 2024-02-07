package onetoone.Points;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class PointsController {
    @Autowired
    private PointsRepository pointsRepository;

    @GetMapping(path = "/getpoints")
    public List<Points> getAllPoints() {
        return (List<Points>) pointsRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Points> getPointById(@PathVariable int id) {
        return pointsRepository.findById(id);
    }

    @PostMapping(path = "/addpoint")
    public Points createPoint(@RequestBody Points point) {
        return pointsRepository.save(point);
    }
}
