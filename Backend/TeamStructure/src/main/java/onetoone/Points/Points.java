package onetoone.Points;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Points {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

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
}