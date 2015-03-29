package com.daviancorp.android.data.classes;

import android.os.Parcel;
import android.os.Parcelable;

/*
 * Class for SkillTree
 */
public class SkillTree implements Parcelable {

	private long id;			// Id
	private String name;		// SkillTree name
	private String jpn_name;	// Japanese name; unused at the moment
	
	/* Default Constructor */
	public SkillTree() {
		this.id = -1;
		this.name = "";
		this.jpn_name = "";
	}

    //parcel part
    public SkillTree(Parcel in){
        this.id              = in.readLong();
        this.name            = in.readString();
        this.jpn_name        = in.readString();
    }

	/* Getters and Setters */
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.name);
        dest.writeString(this.jpn_name);
    }

    public static final Parcelable.Creator<SkillTree> CREATOR= new Parcelable.Creator<SkillTree>() {

        @Override
        public SkillTree createFromParcel(Parcel source) {
            return new SkillTree(source);  //using parcelable constructor
        }

        @Override
        public SkillTree[] newArray(int size) {
            return new SkillTree[size];
        }
    };

}
