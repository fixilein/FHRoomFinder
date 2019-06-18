package at.fhooe.mc.android.fhroomfinder;

import android.util.Log;

public class Room {
    private int building, floor, number;
    private String name;

    public Room(int building, int floor, int number, String name) {
        this.building = building;
        this.floor = floor;
        this.number = number;
        this.name = name;
    }

    public static Room fromLine(String _text) {
        Log.i(MainActivity.TAG, _text);
        int startIndex = _text.indexOf("(FH");
        String substring = _text.substring(startIndex);

        String name = _text.replace(_text.substring(startIndex - 1), "");
        int building = Character.getNumericValue(substring.charAt(3));
        int floor = Character.getNumericValue(substring.charAt(5));
        int number = Character.getNumericValue(substring.charAt(6)) * 10 +
                Character.getNumericValue(substring.charAt(7));

        return new Room(building, floor, number, name);
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
        return "FH" + getBuilding() + "." + getFloor() + String.format("%02d", getNumber());
    }


}
