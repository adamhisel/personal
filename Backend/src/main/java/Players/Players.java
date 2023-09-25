

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import onetoone.Users.User;

/**
 * @author mason harsh
 */

@Entity
Public class Player{

    @Name
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private String name;

    private int number;

    private String position;


    @OneToOne
    @JsonIgnore

    private Player player;

    public Player(String name, int number, String position){
        this.name = name;
        this.number = number;
        this.position = position;
    }

    public Player(){}

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public int getNumber(){
        return name;
    }

    public void getNumber(int number){
        this.number = number;
    }

    public String getPosition(){
        return position;
    }

    public void setPosition(String position){
        this.position = position;
    }



}