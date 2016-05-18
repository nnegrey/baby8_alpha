package edu.calpoly.nnegrey.baby8alpha;

import android.os.Parcel;
import android.os.Parcelable;


public class Command implements Parcelable {

	private int type;
	private int direction;
	private int velocity;
	private int duration;
	private int head_degree;
	private String effect;
	private long id;

	public Command() {
		direction = 0;
		velocity = 0;
		duration = 0;
		head_degree = 0;
	}

	public Command(int dir, int vel, int dur, int hdeg) {
		type = 0;
		direction = dir;
		velocity = vel;
		duration = dur;
		head_degree = hdeg;
		effect = "";
	}

	public Command(String e) {
		type = 1;
		direction = 0;
		velocity = 0;
		duration = 0;
		head_degree = 0;
		effect = e;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int d) {
		direction = d;
	}

	public int getVelocity() {
		return velocity;
	}

	public void setVelocity(int d) {
		velocity = d;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int d) {
		this.duration = d;
	}

	public int getHeadDegree() {
		return head_degree;
	}

	public void setHeadDegree(int h) {
		this.head_degree = h;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getEffect() {
		return effect;
	}

	public void setEffect(String effect) {
		this.effect = effect;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		if (type == 0) {
			return "Direction: " + direction + " Velocity: " + velocity + " Duration: " + duration + " Head Degree: " + head_degree;
		}
		else {
			return "Effect: " + effect;
		}
	}

	@Override
	public boolean equals(Object obj) {
		boolean isCommand = false;

		if (obj instanceof Command) {
			Command c = (Command) obj;

			if (this.getType() != c.getType()) {
				return false;
			}

			if (type == 0) {
				if (c.getDirection() == this.getDirection() &&
						c.getVelocity() == this.getVelocity() &&
						c.getDuration() == this.getDuration() &&
						c.getHeadDegree() == this.getHeadDegree()) {
					isCommand = true;
				}
			}
			else {
				if (c.getEffect().equals(this.getEffect()));
			}
		}

		return isCommand;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeInt(type);
		if (type == 0) {
			out.writeInt(direction);
			out.writeInt(velocity);
			out.writeInt(duration);
			out.writeInt(head_degree);
		}
		else {
			out.writeString(effect);
		}
	}

	public static final Creator<Command> CREATOR
			= new Creator<Command>() {
		public Command createFromParcel(Parcel in) {
			if (in.readInt() == 0) {
				return new Command(in.readInt(), in.readInt(), in.readInt(), in.readInt());
			}
			else {
				return new Command(in.readString());
			}
		}

		public Command[] newArray(int size) {
			return new Command[size];
		}
	};
}
