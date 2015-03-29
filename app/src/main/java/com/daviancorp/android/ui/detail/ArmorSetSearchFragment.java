package com.daviancorp.android.ui.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.daviancorp.android.data.classes.ArmorSetBuilderSession;
import com.daviancorp.android.data.classes.Skill2;
import com.daviancorp.android.mh4udatabase.R;
import com.daviancorp.android.ui.list.SkillPickerActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * This is where the magic happens baby. Users can define a custom armor set in this fragment.
 */
public class ArmorSetSearchFragment extends Fragment {

    public static final int SKILL_PICKER_REQUEST_CODE = 782;

    private ArmorSetBuilderSession session;
    private ArmorSetSearchSkillsAdapter listAdapter;
    private Context context;
    private  ArrayList<Skill2> st = new ArrayList<>();


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

    public static ArmorSetSearchFragment newInstance() {
        Bundle args = new Bundle();
        ArmorSetSearchFragment f = new ArmorSetSearchFragment();
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

        View view = inflater.inflate(R.layout.fragment_armor_set_search_detail, container, false);

        //ArmorSetBuilderSession s = ((ArmorSetBuilderActivity) getActivity()).getArmorSetBuilderSession();
        context = getActivity().getApplicationContext();

        View header = inflater.inflate(R.layout.fragment_armor_set_search_detail_header, null, false);
        switchGender = (Switch) header.findViewById(R.id.armor_set_search_switch_gender);
        switchClass = (Switch) header.findViewById(R.id.armor_set_search_switch_class);
        switchBadSkills = (Switch) header.findViewById(R.id.armor_set_search_allow_bad_skills);
        switchArenaArmor = (Switch) header.findViewById(R.id.armor_set_search_allow_arena_armor);
        switchEventArmor = (Switch) header.findViewById(R.id.armor_set_search_allow_event_armor);
        switchExcavatedArmor = (Switch) header.findViewById(R.id.armor_set_search_allow_excavated_armor);
        switchExcavatedWeapon = (Switch) header.findViewById(R.id.armor_set_search_allow_excavated_weapon);
        switchLowerTierArmor = (Switch) header.findViewById(R.id.armor_set_search_allow_lower_tier_armor);

        addSkill = (Button) header.findViewById(R.id.armor_set_search_add_skill_button);
        addSkill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SkillPickerActivity.class);
                startActivityForResult(intent, SKILL_PICKER_REQUEST_CODE);
            }
        });

        search = (Button) header.findViewById(R.id.armor_set_search_button);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(context, SkillPickerActivity.class);
//                startActivityForResult(intent, SKILL_PICKER_REQUEST_CODE);
            }
        });

        listAdapter = new ArmorSetSearchSkillsAdapter(context, session, st);
        skillList = (ListView) view.findViewById(R.id.armor_set_search_skill_list);
        skillList.addHeaderView(header);
        skillList.setAdapter(listAdapter);
        return view;
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SKILL_PICKER_REQUEST_CODE){
            Skill2 s = data.getParcelableExtra("Skill2");
            st.add(s);
            listAdapter.notifyDataSetChanged();
        }
        else {
           ((ArmorSetBuilderActivity) getActivity()).fragmentResultReceived(requestCode, resultCode, data);
        }
    }

    private static class ArmorSetSearchSkillsAdapter extends ArrayAdapter<Skill2> implements ListAdapter {

        public ArmorSetSearchSkillsAdapter(Context context, ArmorSetBuilderSession s, List<Skill2> objects) {
            super(context, R.layout.fragment_armor_set_search_item, objects);
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater.from(getContext()));
            View itemView = inflater.inflate(R.layout.fragment_armor_set_search_item, parent, false); // Conditional inflation really isn't necessary simply because of how many skills you'd have to have.

            ImageButton deleteBtn = (ImageButton)itemView.findViewById(R.id.armor_set_search_skill_remove_img);
            TextView skillName = (TextView) itemView.findViewById(R.id.armor_set_search_skill_tree_name);

            skillName.setText(getItem(position).getName());

            deleteBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    remove(getItem(position));
                    notifyDataSetChanged();
                }
            });
            //itemView.setOnClickListener(new SkillClickListener(getContext(), getItem(position).getSkillTree().getId()));

            return itemView;
        }

    }
}