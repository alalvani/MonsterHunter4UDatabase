package com.daviancorp.android.data.classes;

import android.content.Context;
import android.util.Log;
import com.daviancorp.android.data.database.DataManager;

import java.util.*;

/**
 * Represents a session of the user's interaction with the Armor Set Builder.
 * <p/>
 * For using armor arrays:
 * <li>0: Head
 * <li>1: Body
 * <li>2: Arms
 * <li>3: Waist
 * <li>4: Legs
 */
public class ArmorSetBuilderSession {

    public static final int HEAD = 0;
    public static final int BODY = 1;
    public static final int ARMS = 2;
    public static final int WAIST = 3;
    public static final int LEGS = 4;

    private static Armor noArmor = new Armor();

    private static Decoration noDecoration = new Decoration();
    public static Decoration dummy = new Decoration();

    private Armor[] armors;
    private Decoration[][] decorations;

    private List<SkillTreePointsSet> skillTreePointsSets;
    private List<ComponentPointsSet> componentPointsSets;

    private List<OnArmorSetChangedListener> changedListeners;

    /**
     * Default constructor.
     */
    public ArmorSetBuilderSession() {

        armors = new Armor[5];
        for (int i = 0; i < armors.length; i++) {
            armors[i] = noArmor;
        }

        decorations = new Decoration[5][3];
        for (int i = 0; i < decorations.length; i++) {
            for (int j = 0; j < decorations[i].length; j++) {
                decorations[i][j] = noDecoration;
            }
        }

        skillTreePointsSets = new ArrayList<>();
        componentPointsSets = new ArrayList<>();

        changedListeners = new ArrayList<>();
    }

    /**
     * Attempts to add a decoration to the specified armor piece.
     * @param pieceIndex The index of a piece in the set to fetch, according to {@link com.daviancorp.android.data.classes.ArmorSetBuilderSession}.
     * @param decoration The decoration to add.
     * @return True if the piece was successfuly added, otherwise false.
     */
    public boolean addDecoration(int pieceIndex, Decoration decoration) {
        if (getAvailableSlots(pieceIndex) >= decoration.getNumSlots()) { // TODO
            int i = 0;
            while (decorations[pieceIndex][i] != noDecoration) {
                i++;
            }

            decorations[pieceIndex][i] = decoration;
            if (decoration.getNumSlots() == 2) {
                decorations[pieceIndex][i + 1] = dummy;
            }

            if (decoration.getNumSlots() == 3) {
                decorations[pieceIndex][i + 1] = dummy;
                decorations[pieceIndex][i + 2] = dummy;
            }

            notifyArmorSetChangedListeners();
            return true;
        } else {
            return false;
        }
    }

    public void removeDecoration(int pieceIndex, int decorationIndex) {

        if (decorations[pieceIndex][decorationIndex] != dummy) {
            decorations[pieceIndex][decorationIndex] = noDecoration;

            for (int j = decorationIndex + 1; j < decorations[pieceIndex].length; j++) {
                if (decorations[pieceIndex][j] == dummy) {
                    decorations[pieceIndex][j] = noDecoration;
                } else {
                    break;
                }

            }

        }

        int i = 0;
        Decoration[] newDecorations = new Decoration[3]; // We move all of the decorations to a new array so that they are all at the beginning

        for (Decoration d : decorations[pieceIndex]) {
            if (d != noDecoration) {
                newDecorations[i++] = d;
            }
        }

        while (i < newDecorations.length) {
            newDecorations[i++] = noDecoration;
        }

        decorations[pieceIndex] = newDecorations;

        notifyArmorSetChangedListeners();
    }

    public void removeAllDecorations(int pieceIndex) {
        for (int i = 0; i < decorations[pieceIndex].length; i++) {
            decorations[pieceIndex][i] = noDecoration;
        }

        notifyArmorSetChangedListeners();
    }

    public int getAvailableSlots(int pieceIndex) {
        int decorationCount = 0;
        for (Decoration d : decorations[pieceIndex]) {
            if (d != noDecoration) {
                decorationCount++;
            }
        }

        return armors[pieceIndex].getNumSlots() - decorationCount;
    }

    public boolean hasDecorations(int pieceIndex) {
        int decorationCount = 0;
        for (Decoration d : decorations[pieceIndex]) {
            if (d != noDecoration) {
                decorationCount++;
            }
        }

        return decorationCount > 0;
    }

    /** @return True if the designated slot is actually in use, false if it is empty. */
    public boolean decorationIsReal(int pieceIndex, int decorationIndex) {
        return decorations[pieceIndex][decorationIndex] != noDecoration && decorations[pieceIndex][decorationIndex] != dummy;
    }

    /** @return True if the designated slot is a "dummy" decoration - that is, the non-first slot in a decoration of size greater than 1 - and false if it is empty or an actual decoration. */
    public boolean decorationIsDummy(int pieceIndex, int decorationIndex) {
        return getDecoration(pieceIndex, decorationIndex) == dummy;
    }

    public boolean isPieceSelected(int pieceIndex) {
        return armors[pieceIndex] != noArmor;
    }

    public void setArmor(int pieceIndex, Armor armor) {
        armors[pieceIndex] = armor;

        notifyArmorSetChangedListeners();
    }

    public Decoration getDecoration(int pieceIndex, int decorationIndex) {
        return decorations[pieceIndex][decorationIndex];
    }

    /**
     * @return A set of the armor set based on the provided piece index.
     * @see com.daviancorp.android.data.classes.ArmorSetBuilderSession
     */
    public Armor getArmor(int pieceIndex) {
        return armors[pieceIndex];
    }

    public void removeArmor(int pieceIndex) {
        armors[pieceIndex] = noArmor;
        removeAllDecorations(pieceIndex);

        notifyArmorSetChangedListeners();
    }

    public List<SkillTreePointsSet> getSkillTreePointsSets() {
        return skillTreePointsSets;
    }

    public List<ComponentPointsSet> getComponentPointsSets(){
        return componentPointsSets;
    }



    /**
     * Adds any skills to the armor set's skill trees that were not there before, and removes those no longer there.
     */
    public void updateSkillTreePointsSets(Context context) {

        /* A map of the current skill trees' ID's in the set and their associated SkillTreePointsSets */
        Map<Long, SkillTreePointsSet> skillTreeToSkillTreePointsSet = new HashMap<>();
        skillTreePointsSets.clear();
        updateStatPointsSets();

        /* empty points to seperate armor stats from skills */
        SkillTreePointsSet separator    = new SkillTreePointsSet(" ");
        skillTreePointsSets.add(separator);
        skillTreePointsSets.add(separator);

//        for (SkillTreePointsSet pointsSet : skillTreePointsSets) {
//            skillTreeToSkillTreePointsSet.put(pointsSet.getSkillTree().getId(), pointsSet);
//        }

        for (int i = 0; i < armors.length; i++) {

            Map<SkillTree, Integer> armorSkillTreePoints = getSkillsFromArmorPiece(i, context); // A map of the current piece of armor's skills, localized so we don't have to keep calling it

            for (SkillTree skillTree : armorSkillTreePoints.keySet()) {

                SkillTreePointsSet s; // The actual points set that we are working with that will be shown to the user

                if (!skillTreeToSkillTreePointsSet.containsKey(skillTree.getId())) { // If the armor set does not yet have this skill tree registered...
                    Log.d("SetBuilder", "Registering skill tree..." + skillTree.getName());

                    s = new SkillTreePointsSet(); // We add it...
                    s.setSkillTree(skillTree);
                    skillTreePointsSets.add(s);

                    skillTreeToSkillTreePointsSet.put(skillTree.getId(), s);
                } else {
                    Log.d("SetBuilder", "Skill tree " + skillTree.getName() + " already registered!");
                    s = skillTreeToSkillTreePointsSet.get(skillTree.getId()); // Otherwise, we just find the skill tree set that's already there
                }

                s.setPoints(i, armorSkillTreePoints.get(skillTree));
            }
        }
    }

    /**
     * A helper method that converts an armor piece present in the current session into a map of the skills it provides and the respective points in each.
     * @param pieceIndex The piece of armor to get the skills from.
     * <li>0: Head
     * <li>1: Body
     * <li>2: Arms
     * <li>3: Waist
     * <li>4: Legs</li>
     * @return A map of all the skills the armor piece provides along with the number of points in each.
     */
    private Map<SkillTree, Integer> getSkillsFromArmorPiece(int pieceIndex, Context context) {
        Map<SkillTree, Integer> skills = new HashMap<>();

        for (ItemToSkillTree itemToSkillTree : DataManager.get(context).queryItemToSkillTreeArrayItem(armors[pieceIndex].getId())) { // We add skills for armor
            skills.put(itemToSkillTree.getSkillTree(), itemToSkillTree.getPoints());
            Log.d("SetBuilder", "Skill tree added to map: " + itemToSkillTree.getSkillTree().getName());
        }

        for (Decoration d : decorations[pieceIndex]) { // Now we work on decorations
            for (ItemToSkillTree itemToSkillTree : DataManager.get(context).queryItemToSkillTreeArrayItem(d.getId())) {
                if (skills.containsKey(itemToSkillTree.getSkillTree())) {
                    int points = skills.get(itemToSkillTree.getSkillTree()) + itemToSkillTree.getPoints();
                    skills.remove(itemToSkillTree.getSkillTree());
                    skills.put(itemToSkillTree.getSkillTree(), points);
                } else {
                    skills.put(itemToSkillTree.getSkillTree(), itemToSkillTree.getPoints());
                }
            }
        }
        return skills;
    }

    /**
     * Adds any skills to the armor set's skill trees that were not there before, and removes those no longer there.
     */
    public void updateComponentPointsSets(Context context) {

        /* A map of the current skill trees' ID's in the set and their associated componentPointsSets */
        Map<Component, ComponentPointsSet> componentToComponentPointsSet = new HashMap<>();
        componentPointsSets.clear();

        for (int i = 0; i < armors.length; i++) {
            /* A map of the current piece of armor's components, localized so we don't have to keep calling it */
            Map<Component, Integer> armorComponentPoints = getComponentsFromArmorPiece(i, context);

            for (Component c : armorComponentPoints.keySet()) {
                /* The actual points set that we are working with that will be shown to the user */
                ComponentPointsSet s;

                /* If the armor set does not yet have this skill tree registered... */
                if (!componentToComponentPointsSet.containsKey(c)) {
                    Log.d("SetBuilder", "Registering skill tree..." + c.getComponent().getName());

                    /*  We add it...  */
                    s = new ComponentPointsSet();
                    s.setComponent(c);
                    componentPointsSets.add(s);

                    componentToComponentPointsSet.put(c, s);
                } else {
                    Log.d("SetBuilder", "Skill tree " + c.getComponent().getName() + " already registered!");
                    s = componentToComponentPointsSet.get(c); // Otherwise, we just find the skill tree set that's already there
                }

                s.setPoints(i, armorComponentPoints.get(c));
            }
        }
    }

    /**
     * A helper method that converts an armor piece present in the current session into a map of the components it provides and the respective quantity in each.
     * @param pieceIndex The piece of armor to get the components from.
     * <li>0: Head
     * <li>1: Body
     * <li>2: Arms
     * <li>3: Waist
     * <li>4: Legs</li>
     * @return A map of all the components the armor piece provides along with the quantity in each.
     */
    private Map<Component, Integer> getComponentsFromArmorPiece(int pieceIndex, Context context){
        Map<Component, Integer> comps = new HashMap<>();
        for (Component c : DataManager.get(context).queryComponentArrayCreated(armors[pieceIndex].getId())){
            comps.put(c, c.getQuantity());
        }

        for (Decoration d : decorations[pieceIndex]){
            for (Component c : DataManager.get(context).queryComponentArrayCreated(d.getId())){
                if (comps.containsKey(c)){
                    int points = comps.get(c) + c.getQuantity();
                    comps.remove(c);
                    comps.put(c, points);
                }
                else {
                    comps.put(c, c.getQuantity());
                }
            }
        }
        return comps;
    }

    public void addOnArmorSetChangedListener(OnArmorSetChangedListener l) {
        changedListeners.add(l);
    }

    private void notifyArmorSetChangedListeners() {
        for (OnArmorSetChangedListener l : changedListeners) {
            l.onArmorSetChanged();
        }
    }

    /**
     * a helper method that adds the stat points of the armor.
     */
    private void updateStatPointsSets() {
        SkillTreePointsSet defense    = new SkillTreePointsSet("Min Defense");
        SkillTreePointsSet maxDefense = new SkillTreePointsSet("Max Defense");
        SkillTreePointsSet fireRes    = new SkillTreePointsSet("Fire Resist");
        SkillTreePointsSet thunderRes = new SkillTreePointsSet("Thunder Resist");
        SkillTreePointsSet dragonRes  = new SkillTreePointsSet("Dragon Resist");
        SkillTreePointsSet waterRes   = new SkillTreePointsSet("Water Resist");
        SkillTreePointsSet iceRes     = new SkillTreePointsSet("Ice Resist");
        SkillTreePointsSet numSlots   = new SkillTreePointsSet("Number of Slots");

        for (int i = 0; i < armors.length; i++)
        {
            if (armors[i].getId() < 0)
            {
                defense.setPoints(i, 0);
                maxDefense.setPoints(i, 0);
                fireRes.setPoints(i, 0);
                thunderRes.setPoints(i, 0);
                dragonRes.setPoints(i, 0);
                waterRes.setPoints(i, 0);
                iceRes.setPoints(i, 0);
                numSlots.setPoints(i, 0);
            }
            else
            {
                defense.setPoints(i, armors[i].getDefense());
                maxDefense.setPoints(i, armors[i].getMaxDefense());
                fireRes.setPoints(i, armors[i].getFireRes());
                thunderRes.setPoints(i, armors[i].getThunderRes());
                dragonRes.setPoints(i, armors[i].getDragonRes());
                waterRes.setPoints(i, armors[i].getWaterRes());
                iceRes.setPoints(i, armors[i].getIceRes());
                numSlots.setPoints(i, armors[i].getNumSlots());
            }
        }
        skillTreePointsSets.clear();
        skillTreePointsSets.add(defense);
        skillTreePointsSets.add(maxDefense);
        skillTreePointsSets.add(fireRes);
        skillTreePointsSets.add(thunderRes);
        skillTreePointsSets.add(dragonRes);
        skillTreePointsSets.add(waterRes);
        skillTreePointsSets.add(iceRes);
        skillTreePointsSets.add(numSlots);
    }



    public static interface OnArmorSetChangedListener {
        public void onArmorSetChanged();
    }



     /**
     * A container class that represents a skill tree as well as a specific number of points provided by each armor piece in a set.
     */
    public static class SkillTreePointsSet {

        private SkillTree skillTree;
        private String statName;
        private int[] points;

        public SkillTreePointsSet(String statName) {
            points = new int[5];
            this.statName = statName;
        }

        public SkillTreePointsSet() {
            points = new int[5];
        }

        public SkillTree getSkillTree() {
            return skillTree;
        }

        public String getStatName() {
            return (skillTree != null) ? skillTree.getName() : statName;
        }

        public int getHeadPoints() {
            return points[HEAD];
        }

        public int getBodyPoints() {
            return points[BODY];
        }

        public int getArmsPoints() {
            return points[ARMS];
        }

        public int getWaistPoints() {
            return points[WAIST];
        }

        public int getLegsPoints() {
            return points[LEGS];
        }

        public int getPoints(int pieceIndex) {
            if (pieceIndex < 5) {
                return points[pieceIndex];
            } else {
                throw new IllegalArgumentException("Please use a number from 0 to 4 when selecting an armor piece index.");
            }
        }

        /**
         * @return The total number of skill points provided to the skill by all pieces in the set.
         */
        public int getTotal() {
            int total = 0;
            for (int piecePoints : points) {
                total += piecePoints;
            }
            return total;
        }

        public void setSkillTree(SkillTree skillTree) {
            this.skillTree = skillTree;
        }

        public void setStatName(String statName) {
            this.statName = statName;
        }

        public void setPoints(int pieceIndex, int piecePoints) {
            points[pieceIndex] = piecePoints;
        }
    }

    /**
     * A container class that represents a skill tree as well as a specific number of points provided by each armor piece in a set.
     */
    public static class ComponentPointsSet {

        private Component c;
        private int[] points;

        public ComponentPointsSet() {
            points = new int[5];
        }

        public Component getComponent() {
            return c;
        }

        public String getStatName() {
            return c.getComponent().getName();
        }

        public int getHeadPoints() {
            return points[HEAD];
        }

        public int getBodyPoints() {
            return points[BODY];
        }

        public int getArmsPoints() {
            return points[ARMS];
        }

        public int getWaistPoints() {
            return points[WAIST];
        }

        public int getLegsPoints() {
            return points[LEGS];
        }

        public int getPoints(int pieceIndex) {
            if (pieceIndex < 5) {
                return points[pieceIndex];
            } else {
                throw new IllegalArgumentException("Please use a number from 0 to 4 when selecting an armor piece index.");
            }
        }

        /**
         * @return The total number of skill points provided to the skill by all pieces in the set.
         */
        public int getTotal() {
            int total = 0;
            for (int piecePoints : points) {
                total += piecePoints;
            }
            return total;
        }

        public void setComponent(Component c) {
            this.c = c;
        }

        public void setPoints(int pieceIndex, int piecePoints) {
            points[pieceIndex] = piecePoints;
        }
    }
}
