package coms309.people;

public class CourtController {
    private int xCoord;
    private int yCoord;
    private boolean shot;

    public CourtController(int x, int y, boolean makemiss){
        this.xCoord=x;
        this.yCoord = y;
        this.shot = makemiss;
    }

    public int[] getCoords(){
        return new int[]{this.xCoord,this.yCoord};
    }

    public boolean getMakeMiss(){return this.shot;}

    public void setxCoord(int xCoord) {
        this.xCoord = xCoord;
    }

    public void setyCoord(int yCoord) {
        this.yCoord = yCoord;
    }

    public void setShot(boolean shot) {
        this.shot = shot;
    }

}
