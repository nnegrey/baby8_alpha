package edu.calpoly.nnegrey.baby8alpha;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by noahnegrey on 5/15/16.
 */
public class Pattern implements Parcelable {

    private ArrayList<Command> commands;
    private String patternName;
    private long id;
    private long displayOrder;

    public Pattern() {

    }

    public Pattern(String pName, ArrayList<Command> c, long id) {
        patternName = pName;
        commands = c;
        this.id = id;
    }

    public Pattern(String pName, ArrayList<Command> c) {
        patternName = pName;
        commands = c;
    }

    public String getPatternName() {
        return patternName;
    }

    public void setPatternName(String patternName) {
        this.patternName = patternName;
    }

    public ArrayList<Command> getCommands() {
        return commands;
    }

    public void setCommands(ArrayList<Command> commands) {
        this.commands = commands;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(long order) {
        this.displayOrder = order;
    }

    @Override
    public String toString() {
        String result = "PatternName: " + patternName + " Commands: ";
        for (Command c : commands) {
            result += c.toString() + ", ";
        }
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        boolean isPattern = false;

        if (obj instanceof Pattern) {
            Pattern p = (Pattern) obj;

            if (p.getPatternName() == this.getPatternName() &&
                   compareCommands(p)) {
                isPattern = true;
            }
        }

        return isPattern;
    }

    private boolean compareCommands(Pattern p) {
        boolean areEqual = true;

        if (p.getCommands().size() != this.getCommands().size()) {
            return false;
        }

        for (int i = 0; i < this.getCommands().size(); i++) {
            if (!p.getCommands().get(i).equals(this.getCommands().get(i))) {
                areEqual = false;
            }
        }

        return areEqual;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(patternName);
        out.writeList(commands);
    }

    public static final Creator<Pattern> CREATOR
            = new Creator<Pattern>() {
        public Pattern createFromParcel(Parcel in) {
            return new Pattern(in.readString(), in.readArrayList((ClassLoader) Command.CREATOR));
        }

        public Pattern[] newArray(int size) {
            return new Pattern[size];
        }
    };
}
