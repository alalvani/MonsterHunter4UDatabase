package com.daviancorp.android.ui.detail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.daviancorp.android.data.classes.ArmorSetBuilderSession;
import com.daviancorp.android.data.classes.Skill;
import com.daviancorp.android.data.classes.SkillTree;
import com.daviancorp.android.data.database.DataManager;
import com.daviancorp.android.data.database.SkillCursor;
import com.daviancorp.android.mh4udatabase.R;
import com.daviancorp.android.ui.list.SkillPickerActivity;
import com.daviancorp.android.ui.list.SkillTreeListActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * This is where the magic happens baby. Users can define a custom armor set in this fragment.
 */
public class ArmorSetBuilderSearchFragment extends Fragment implements ArmorSetBuilderActivity.OnArmorSetActivityUpdateListener {

    public static final int SKILL_PICKER_REQUEST_CODE = 782;

    private ArmorSetBuilderSession session;
    private ArmorSetBuilderSearchSkillsAdapter listAdapter;
    private Context context;
    private  ArrayList<SkillTree> st = new ArrayList<>();


    Switch switchClass;
    Switch switchGender;
    Switch switchBadSkills;
    Switch switchArenaArmor;
    Switch switchEventArmor;
    Switch switchExcavatedArmor;
    Switch switchExcavatedWeapon;
    Switch switchLowerTierArmor;
    Button search;
    Button addSkill;
    protected ListView skillList;

    public static ArmorSetBuilderSearchFragment newInstance() {
        Bundle args = new Bundle();
        ArmorSetBuilderSearchFragment f = new ArmorSetBuilderSearchFragment();
        f.setArguments(args);
        //f.session = session;
        return f;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_armor_set_builder_search, container, false);

        //ArmorSetBuilderSession s = ((ArmorSetBuilderActivity) getActivity()).getArmorSetBuilderSession();
        context = getActivity().getApplicationContext();

        View header = inflater.inflate(R.layout.fragment_armor_set_builder_search_header, null, false);
        switchGender = (Switch) header.findViewById(R.id.switch_gender);
        switchClass = (Switch) header.findViewById(R.id.switch_class);
        switchBadSkills = (Switch) header.findViewById(R.id.armor_builder_allow_bad_skills);
        switchArenaArmor = (Switch) header.findViewById(R.id.armor_builder_allow_arena_armor);
        switchEventArmor = (Switch) header.findViewById(R.id.armor_builder_allow_event_armor);
        switchExcavatedArmor = (Switch) header.findViewById(R.id.armor_builder_allow_excavated_armor);
        switchExcavatedWeapon = (Switch) header.findViewById(R.id.armor_builder_allow_excavated_weapon);
        switchLowerTierArmor = (Switch) header.findViewById(R.id.armor_builder_allow_lower_tier_armor);

        addSkill = (Button) header.findViewById(R.id.skill_list_add_skill_button);
        addSkill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SkillPickerActivity.class);
                startActivityForResult(intent, SKILL_PICKER_REQUEST_CODE);
            }
        });

        //generate some skills?
//        for (int i = 1; i < 50; i++)
//        {
//            st.add(DataManager.get(context).getSkillTree(i));
//        }
        listAdapter = new ArmorSetBuilderSearchSkillsAdapter(context, session, st);
        skillList = (ListView) view.findViewById(R.id.armor_builder_search_skill_list);
        skillList.addHeaderView(header);
        skillList.setAdapter(listAdapter);
        return view;
    }


    @Override
    public void onArmorSetActivityUpdated(ArmorSetBuilderSession s) {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // We have to check to make sure that the Activity that this is being attached to is connected to the callback interface for this fragment.
        try {
            ArmorSetBuilderActivity a = (ArmorSetBuilderActivity) getActivity();
            a.addArmorSetChangedListener(this);
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + " must be a ArmorSetBuilderActivity.");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SKILL_PICKER_REQUEST_CODE){
            long skillId = data.getLongExtra("skillId", -1);;
            long skillPoints = data.getIntExtra("skillPoints", -1);;
            long skillTreeId = data.getLongExtra("skillTreeId", -1);
            if (skillTreeId > -1) st.add(DataManager.get(context).getSkillTree(skillTreeId));
            listAdapter.notifyDataSetChanged();
        }
        else {
           ((ArmorSetBuilderActivity) getActivity()).fragmentResultReceived(requestCode, resultCode, data);
        }
    }

    //    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == 1){
//            if(resultCode == Activity.RESULT_OK){
//                //here is your result
//                String result=data.getStringExtra("result");
//                Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
//            }
//            if (resultCode == Activity.RESULT_CANCELED) {
//                //Write your code if there's no result
//                Toast.makeText(context, "Nothing Returned!", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }


    private static class ArmorSetBuilderSearchSkillsAdapter extends ArrayAdapter<SkillTree> implements ListAdapter {

        public ArmorSetBuilderSearchSkillsAdapter(Context context, ArmorSetBuilderSession s, List<SkillTree> objects) {
            super(context, R.layout.fragment_armor_set_builder_search_item, objects);
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater.from(getContext()));
            View itemView = inflater.inflate(R.layout.fragment_armor_set_builder_search_item, parent, false); // Conditional inflation really isn't necessary simply because of how many skills you'd have to have.

            ImageButton deleteBtn = (ImageButton)itemView.findViewById(R.id.search_skill_remove_img);
            TextView skillName = (TextView) itemView.findViewById(R.id.search_skill_tree_name);

            skillName.setText(getItem(position).getName());

            deleteBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    //do something
                    remove(getItem(position));
                    notifyDataSetChanged();
                }
            });
            //itemView.setOnClickListener(new SkillClickListener(getContext(), getItem(position).getSkillTree().getId()));

            return itemView;
        }

    }
}