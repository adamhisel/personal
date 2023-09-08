package coms309.people;


/**
 * Provides the Definition/Structure for the people row
 *
 * @author Vivek Bengre
 */

public class Person {

    private String firstName;

    private String lastName;

    private int number;

    private int makes;

    private int misses;

    public Person(){
        
    }

    public Person(String firstName, String lastName, int number, int makes, int misses){
        this.firstName = firstName;
        this.lastName = lastName;
        this.number = number;
        this.makes = makes;
        this.misses = misses;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getNumber() {
        return this.number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getMakes() {
        return this.makes;
    }

    public void setMakes(int makes) {
        this.makes = makes;
    }

    public int getMisses() { return this.misses; }

    public void setMisses(int misses) { this.misses = misses; }

    @Override
    public String toString() {
        return firstName + " " 
               + lastName + " "
               + number + " "
               + makes + " "
                + misses;
    }
}
