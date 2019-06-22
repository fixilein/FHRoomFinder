package at.fhooe.mc.android.fhroomfinder;

import android.os.Parcel;
import android.os.Parcelable;

public class Room implements Parcelable {
    private int building, floor, number;
    private String name;
    private int x, y;

    public Room(int _building, int _floor, int _number, String _name, int _x, int _y) {
        building = _building;
        floor = _floor;
        number = _number;
        name = _name;
        x = _x;
        y = _y;
    }

    protected Room(Parcel _in) {
        building = _in.readInt();
        floor = _in.readInt();
        number = _in.readInt();
        name = _in.readString();
        x = _in.readInt();
        y = _in.readInt();
    }

    public static final Creator<Room> CREATOR = new Creator<Room>() {
        @Override
        public Room createFromParcel(Parcel in) {
            return new Room(in);
        }

        @Override
        public Room[] newArray(int size) {
            return new Room[size];
        }
    };

    public void setBuilding(int building) {
        this.building = building;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Room() {

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

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getToken() {
        return "FH" + getBuilding() + "." + getFloor() + String.format("%02d", getNumber());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel _dest, int _flags) {
        _dest.writeInt(building);
        _dest.writeInt(floor);
        _dest.writeInt(number);
        _dest.writeString(name);
        _dest.writeInt(x);
        _dest.writeInt(y);
    }
}
