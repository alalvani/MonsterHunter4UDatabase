package com.daviancorp.android.data.classes;

/*
 * Class for Armor
 *
 * Note: Subclass of Item
 */
public class Armor extends Item{
	
	private String slot;			// Equipment type
	private int defense;			// Base defense
	private int max_defense;		// Max defense
	private int fire_res;			// Fire resistance
	private int thunder_res;		// Thunder resistance
	private int dragon_res;			// Dragon resistance
	private int water_res;			// Water resistance
	private int ice_res;			// Ice resistance
	private String gender;			// Which gender can equip
	private String hunter_type;		// Which hunter type can equip: Blademaster/Gunner
	private int num_slots;			// Number of slots

	/* Default Constructor */
	public Armor() {
		super();					// Initialize variables from Item
		
		this.slot = "";
		this.defense = -1;
		this.max_defense = -1;
		this.fire_res = -1;
		this.thunder_res = -1;
		this.water_res = -1;
		this.gender = "";
		this.hunter_type = "";
		this.num_slots = -1;
	}

	/* Getters and Setters */
	public String getSlot() {
		return slot;
	}

	public void setSlot(String slot) {
		this.slot = slot;
	}

	public int getDefense() {
		return defense;
	}

	public void setDefense(int defense) {
		this.defense = defense;
	}

	public int getMaxDefense() {
		return max_defense;
	}

	public void setMaxDefense(int max_defense) {
		this.max_defense = max_defense;
	}

	public int getFireRes() {
		return fire_res;
	}

	public void setFireRes(int fire_res) {
		this.fire_res = fire_res;
	}

	public int getThunderRes() {
		return thunder_res;
	}

	public void setThunderRes(int thunder_res) {
		this.thunder_res = thunder_res;
	}

	public int getDragonRes() {
		return dragon_res;
	}

	public void setDragonRes(int dragon_res) {
		this.dragon_res = dragon_res;
	}

	public int getWaterRes() {
		return water_res;
	}

	public void setWaterRes(int water_res) {
		this.water_res = water_res;
	}

	public int getIceRes() {
		return ice_res;
	}

	public void setIceRes(int ice_res) {
		this.ice_res = ice_res;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getHunterType() {
		return hunter_type;
	}

	public void setHunterType(String hunter_type) {
		this.hunter_type = hunter_type;
	}

	public int getNumSlots() {
		return num_slots;
	}

	public void setNumSlots(int num_slots) {
		this.num_slots = num_slots;
	}

    public long getImageId() {
        return Math.max(1+((this.getId()-1914)/5), 530);
    }

	@Override
	public String toString(){
		return this.getName();
	}
	
}
