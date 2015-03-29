package com.daviancorp.android.data.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.daviancorp.android.data.classes.Skill;
import com.daviancorp.android.data.classes.Skill2;
import com.daviancorp.android.data.classes.SkillTree;

/**
 * A convenience class to wrap a cursor that returns rows from the "skill"
 * table. The {@link getSkill()} method will give you a Skill instance
 * representing the current row.
 */
public class Skill2Cursor extends CursorWrapper {

	public Skill2Cursor(Cursor c) {
		super(c);
	}

	/**
	 * Returns a Skill object configured for the current row, or null if the
	 * current row is invalid.
	 */
	public Skill2 getSkill() {
		if (isBeforeFirst() || isAfterLast())
			return null;
		
		Skill2 skill = new Skill2();
		SkillTree st = new SkillTree();

		long id = getLong(getColumnIndex(S.COLUMN_SKILLS_ID));
		long s_tree_id = getLong(getColumnIndex(S.COLUMN_SKILLS_SKILL_TREE_ID));
        int required_points = getInt(getColumnIndex(S.COLUMN_SKILLS_REQUIRED_SKILL_TREE_POINTS));
		String name = getString(getColumnIndex(S.COLUMN_SKILLS_NAME));
		String jpn_name = getString(getColumnIndex(S.COLUMN_SKILLS_JPN_NAME));
		String description = getString(getColumnIndex(S.COLUMN_SKILLS_DESCRIPTION));


        String st_name = getString(getColumnIndex(S.COLUMN_SKILL_TREES_NAME));
        String st_jpn_name = getString(getColumnIndex(S.COLUMN_SKILL_TREES_JPN_NAME));

		skill.setId(id);
        skill.setRequiredPoints(required_points);
		skill.setName(name);
		skill.setJpnName(jpn_name);
		skill.setDescription(description);

        st.setId(s_tree_id);
        st.setName(st_name);
        st.setJpnName(st_jpn_name);

        skill.setSkillTree(st);

		return skill;
	}
}