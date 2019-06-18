package at.fhooe.mc.android.fhroomfinder;

public class Room {
    private int building, floor, number;
    private String name;

    public Room(int building, int floor, int number, String name) {
        this.building = building;
        this.floor = floor;
        this.number = number;
        this.name = name;
    }

    public int getBuilding() {
        return building;
    }

    public int getFloor() {
        return floor;
    }

    public int getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public String getFullNumber() { // TODO rename
        return "FH" + getBuilding() + "." + getFloor() + getNumber();
    }
}
