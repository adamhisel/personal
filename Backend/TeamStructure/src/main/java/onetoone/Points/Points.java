package onetoone.Points;

import com.fasterxml.jackson.annotation.JsonBackReference;
import onetoone.CustomWorkout.CustomWorkout;

import javax.persistence.*;

@Entity
public class Points {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "customWoutId")
    @JsonBackReference(value = "customworkout")
    private CustomWorkout workout;
    private int xCoord;
    private int yCoord;

    public Points(){}
    public Points(int xCoord, int yCoord){
        this.xCoord = xCoord;
        this.yCoord = yCoord;
    }

    public int getxCoord() {
        return xCoord;
    }

    public int getyCoord() {
        return yCoord;
    }

    public void setyCoord(int yCoord) {
        this.yCoord = yCoord;
    }

    public void setxCoord(int xCoord) {
        this.xCoord = xCoord;
    }

    public CustomWorkout getWorkout() {
        return workout;
    }

    public void setWorkout(CustomWorkout workout) {
        this.workout = workout;
    }
}