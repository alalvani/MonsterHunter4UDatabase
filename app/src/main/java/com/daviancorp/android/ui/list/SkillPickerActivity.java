package com.daviancorp.android.ui.list;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.daviancorp.android.data.classes.ArmorSetBuilderSession;
import com.daviancorp.android.data.classes.Skill;
import com.daviancorp.android.data.classes.Skill2;
import com.daviancorp.android.data.classes.SkillTree;
import com.daviancorp.android.data.database.DataManager;
import com.daviancorp.android.mh4udatabase.R;
import com.daviancorp.android.ui.general.GenericActivity;
import com.daviancorp.android.ui.list.adapter.MenuSection;

import java.util.ArrayList;
import java.util.List;

public class SkillPickerActivity extends Activity {

    ArrayList<Skill2> skills = new ArrayList<>();
    SkillPickerAdapter adapter;
    ListView list;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Pick a skill to add");
        setContentView(R.layout.activity_skill_picker);

        skills.addAll(DataManager.get(this).querySkill2Array());

        adapter = new SkillPickerAdapter(this, null, skills);
        list = (ListView) findViewById(R.id.skill_picker_list);
        list.setAdapter(adapter);
        // Enable drawer button instead of back button
        //super.enableDrawerIndicator();
    }

    public void callback(long skillId, int skillPoints, long skillTreeId)
    {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("skillId", skillId);
        resultIntent.putExtra("skillPoints", skillPoints);
        resultIntent.putExtra("skillTreeId", skillTreeId);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private class SkillPickerAdapter extends ArrayAdapter<Skill2> implements ListAdapter {

        public SkillPickerAdapter(Context context, ArmorSetBuilderSession s, List<Skill2> objects) {
            super(context, R.layout.fragment_skill_detail_listitem, objects);
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater.from(getContext()));
            View itemView = inflater.inflate(R.layout.fragment_skill_detail_listitem, parent, false); // Conditional inflation really isn't necessary simply because of how many skills you'd have to have.

            itemView.setClickable(true);

            TextView name = (TextView) itemView.findViewById(R.id.skill); //skill,  pts, description
            TextView pts  = (TextView) itemView.findViewById(R.id.pts);
            TextView desc = (TextView) itemView.findViewById(R.id.description);

            Skill2 s = getItem(position);
            name.setText(s.getName());
            pts.setText( s.getSkillTree().getName() + ": " + s.getRequiredPoints());
            desc.setText(s.getDescription());

            //itemView.setOnClickListener(new SkillClickListener(getContext(), getItem(position).getSkillTree().getId()));
            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    // returing result back
//                    Intent resultIntent = new Intent();
//                    resultIntent.putExtra("skillId", s.getName());
//                    resultIntent.putExtra("skillPoints", s.getRequiredPoints());
//                    resultIntent.putExtra("skillTreeId", s.getSkillTree().getId());
                    //setResult(RESULT_OK, resultIntent);
                    //finish();
                    //callback(s.getId(), s.getRequiredPoints(), s.getSkillTree().getId());
                    // if you don't want to return any result
                    // setResult(RESULT_CANCELED, resultIntent);
                }
            });
            return itemView;
        }


    }


}

//
//public class SecondActivity extends Activity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//
//        Button returnResult = (Button) findViewById(R.id.return_button);
//        returnResult.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                // returing result back
//                Intent resultIntent = new Intent();
//                resultIntent.putExtra("result", "Getting Smile Back!!");
//                setResult(RESULT_OK, resultIntent);
//                finish();
//
//                // if you don't want to return any result
//                // setResult(RESULT_CANCELED, resultIntent);
//            }
//        });
//
//        Button back = (Button) findViewById(R.id.cancel_button);
//        back.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // if you don't want to return any result
//                Intent resultIntent = new Intent();
//                setResult(RESULT_CANCELED, resultIntent);
//                finish();
//            }
//        });
//    }
//}