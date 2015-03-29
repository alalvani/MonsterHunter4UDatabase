package com.daviancorp.android.data.classes;

import android.os.Parcel;
import android.os.Parcelable;

/*
 * Class for Skill
 */
public class Skill2 implements Parcelable {

	private long id;				// id
	private SkillTree skillTree;	// SkillTree; unused at the moment
	private int required_points;	// Required points to unlock skill
	private String name;			// Skill name
	private String jpn_name;		// Japanese skill name; unused at the moment
	private String description;		// Skill description

	/* Default Constructor */
	public Skill2() {
		this.id = -1;
		this.required_points = -1;
		this.name = "";
		this.jpn_name = "";
		this.description = "";
        this.skillTree = new SkillTree();
	}

    public Skill2(Skill s) {
        this.id = s.getId();
        this.required_points = s.getRequiredPoints();
        this.name = s.getName();
        this.jpn_name = s.getJpnName();
        this.description = s.getDescription();
        this.skillTree = new SkillTree();
    }

    public Skill2(Skill s, SkillTree st) {
        this(s);
        this.skillTree = st;
    }

    public Skill2(Parcel in){
        this.id = in.readLong();
        this.required_points = in.readInt();
        this.name = in.readString();
        this.jpn_name = in.readString();
        this.description = in.readString();
        this.skillTree = (SkillTree) in.readValue(SkillTree.class.getClassLoader());
    }

    /* Getters and Setters */
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getRequiredPoints() {
		return required_points;
	}

	public void setRequiredPoints(int required_points) {
		this.required_points = required_points;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getJpnName() {
		return jpn_name;
	}

	public void setJpnName(String jpn_name) {
		this.jpn_name = jpn_name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

    public void setSkillTree(SkillTree t){
        this.skillTree = t;
    }

    public SkillTree getSkillTree(){
        return skillTree;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeLong(this.id);
        dest.writeInt(this.required_points);
        dest.writeString(this.name);
        dest.writeString(this.jpn_name);
        dest.writeString(this.description);
        dest.writeValue(this.skillTree);
    }

    public static final Parcelable.Creator<Skill2> CREATOR= new Parcelable.Creator<Skill2>() {

        @Override
        public Skill2 createFromParcel(Parcel source) {
            return new Skill2(source);  //using parcelable constructor
        }

        @Override
        public Skill2[] newArray(int size) {
            return new Skill2[size];
        }
    };
}
