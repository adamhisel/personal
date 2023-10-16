package onetoone.Teams;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import onetoone.Players.Player;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Vivek Bengre
 *
 */

@Entity
public class Team {

    /*
     * The annotation @ID marks the field below as the primary key for the table created by springboot
     * The @GeneratedValue generates a value if not already present, The strategy in this case is to start from 1 and increment for each table
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String teamName;


    /*
     * @OneToOne creates a relation between the current entity/table(Laptop) with the entity/table defined below it(User)
     * cascade is responsible propagating all changes, even to children of the class Eg: changes made to laptop within a user object will be reflected
     * in the database (more info : https://www.baeldung.com/jpa-cascade-types)
     * @JoinColumn defines the ownership of the foreign key i.e. the user table will have a field called laptop_id
     */
    @OneToMany
    private List<Player> players;

    public Team(String teamName) {
        this.teamName = teamName;
        players = new ArrayList<>();
    }

    public Team() {
        players = new ArrayList<>();
    }

    // =============================== Getters and Setters for each field ================================== //

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getTeamName() { return teamName; }

    public void setTeamName(String teamName) { this.teamName = teamName; }

    public List getPlayers(){ return players; }

    public void setPlayers(List<Player> players){
        this.players = players;
    }

    public void addPlayer(Player player){
        this.players.add(player);
    }

}

