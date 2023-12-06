package onetoone.CustomWorkout;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import onetoone.Shots.Shots;

import javax.persistence.*;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import onetoone.Points.Points;

@Entity
public class CustomWorkout {
   @GeneratedValue(strategy = GenerationType.AUTO)
   @Id
   private int customWoutId;
   @OneToMany(mappedBy = "workout", cascade = CascadeType.ALL, orphanRemoval = true)
   @JsonIgnoreProperties("workout") // Add this line
   private List<Points> coords;
private String workoutName;
private int userId;



public CustomWorkout (){

}
public CustomWorkout (List<Points> coords, String workoutName, int userId){
   this.coords = coords;
   this.userId = userId;
   this.workoutName = workoutName;
}

   public List<Points> getCoords() {
      return coords;
   }

   public void setCoords(List<Points> coords) {
      this.coords = coords;
   }

   public String getWorkoutName() {
      return workoutName;
   }

   public void setWorkoutName(String workoutName) {
      this.workoutName = workoutName;
   }

   public int getUserId() {
      return userId;
   }

   public void setUserId(int userId) {
      this.userId = userId;
   }
}
