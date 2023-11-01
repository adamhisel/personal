package appBackend.Shots;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import appBackend.Shots.ShotRepository;

@Service
public class ShotsService {
    @Autowired
    private ShotRepository shotsRepository;

    public Shots saveShot(Shots shot) {
        return shotsRepository.save(shot);
    }
}