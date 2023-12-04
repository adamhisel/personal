package onetoone.CustomWorkout;

import onetoone.Shots.Shots;

import javax.persistence.*;
import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import onetoone.Points.Points;

@Entity
public class CustomWorkout {
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Id
   private int customWoutId;
   @ElementCollection
   @CollectionTable(name = "points", joinColumns = @JoinColumn(name = "customWoutId"))
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
