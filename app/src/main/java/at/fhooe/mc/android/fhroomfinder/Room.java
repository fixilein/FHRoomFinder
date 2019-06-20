package at.fhooe.mc.android.fhroomfinder;

import android.os.Parcel;
import android.os.Parcelable;

public class Room implements Parcelable {
    private int building, floor, number;
    private String name;

    public Room(int building, int floor, int number, String name) {
        this.building = building;
        this.floor = floor;
        this.number = number;
        this.name = name;
    }

    protected Room(Parcel in) {
        building = in.readInt();
        floor = in.readInt();
        number = in.readInt();
        name = in.readString();
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

    public static Room fromString(String _text) {
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

    public String getToken() {
        return "FH" + getBuilding() + "." + getFloor() + String.format("%02d", getNumber());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(building);
        dest.writeInt(floor);
        dest.writeInt(number);
        dest.writeString(name);
    }
}
