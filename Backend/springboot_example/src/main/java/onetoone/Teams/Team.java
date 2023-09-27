package onetoone.Teams;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import onetoone.Players.Player;

@Entity
public class Team {

    @Id // Corrected annotation for primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Use a Long for the primary key instead of String

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "player_id") // Corrected the column name to match Player's primary key
    private Player player;

    private String name;

    public Team(String name) {
        this.name = name;
    }

    public Team() {
    }

    // =============================== Getters and Setters for each field ================================== //


    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public Player getPlayer(){
        return player;
    }

    public void setPlayer(Player player){
        this.player = player;
    }
    
}
