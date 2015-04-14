package com.daviancorp.android.ui.list;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.daviancorp.android.data.classes.ArmorSetBuilderSession;
import com.daviancorp.android.data.classes.Component;
import com.daviancorp.android.mh4udatabase.R;
import com.daviancorp.android.ui.detail.ArmorSetBuilderActivity;

import java.util.List;

public class ArmorSetBuilderComponentFragment extends Fragment implements ArmorSetBuilderActivity.OnArmorSetActivityUpdateListener {

    private ArmorSetBuilderSession session;
    private ArmorSetBuilderComponentsAdapter adapter;

    public static ArmorSetBuilderComponentFragment newInstance(ArmorSetBuilderSession session) {
        Bundle args = new Bundle();
        ArmorSetBuilderComponentFragment f = new ArmorSetBuilderComponentFragment();
        f.setArguments(args);
        f.session = session;
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_armor_set_builder_components, container, false);

        ListView listView = (ListView) v.findViewById(R.id.list);

        adapter = new ArmorSetBuilderComponentsAdapter(getActivity().getApplicationContext(), session.getComponentPointsSets(), session);
        listView.setAdapter(adapter);

        return v;
    }

    @Override
    public void onArmorSetActivityUpdated(ArmorSetBuilderSession s) {
        session.updateComponentPointsSets(getActivity().getApplicationContext());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((ArmorSetBuilderActivity) getActivity()).addArmorSetChangedListener(this);
    }

    private static class ArmorSetBuilderComponentsAdapter extends ArrayAdapter<ArmorSetBuilderSession.ComponentPointsSet> {

        private ArmorSetBuilderSession session;

        public ArmorSetBuilderComponentsAdapter(Context context, List<ArmorSetBuilderSession.ComponentPointsSet> comps, ArmorSetBuilderSession session) {
            super(context, R.layout.fragment_armor_set_builder_components_item, comps);
            this.session = session;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater.from(getContext()));
            View itemView = inflater.inflate(R.layout.fragment_armor_set_builder_components_item, parent, false); // Conditional inflation really isn't necessary simply because of how many skills you'd have to have.

            TextView treeName = (TextView) itemView.findViewById(R.id.skill_tree_name);
            TextView headPoints = (TextView) itemView.findViewById(R.id.helmet);
            TextView bodyPoints = (TextView) itemView.findViewById(R.id.body);
            TextView armsPoints = (TextView) itemView.findViewById(R.id.arms);
            TextView waistPoints = (TextView) itemView.findViewById(R.id.waist);
            TextView legsPoints = (TextView) itemView.findViewById(R.id.legs);
            TextView totalPoints = (TextView) itemView.findViewById(R.id.total);

            treeName.setText(getItem(position).getStatName());

            /* Separator logic */
            if (getItem(position).getStatName().equals(" ")) return itemView;

            if (session.isPieceSelected(ArmorSetBuilderSession.HEAD)) {
                headPoints.setText(String.valueOf(getItem(position).getHeadPoints()));
            }

            if (session.isPieceSelected(ArmorSetBuilderSession.BODY)) {
                bodyPoints.setText(String.valueOf(getItem(position).getBodyPoints()));
            }

            if (session.isPieceSelected(ArmorSetBuilderSession.ARMS)) {
                armsPoints.setText(String.valueOf(getItem(position).getArmsPoints()));
            }

            if (session.isPieceSelected(ArmorSetBuilderSession.WAIST)) {
                waistPoints.setText(String.valueOf(getItem(position).getWaistPoints()));
            }

            if (session.isPieceSelected(ArmorSetBuilderSession.LEGS)) {
                legsPoints.setText(String.valueOf(getItem(position).getLegsPoints()));
            }

            totalPoints.setText(String.valueOf(getItem(position).getTotal()));
            
            //itemView.setOnClickListener(new SkillClickListener(getContext(), getItem(position).getSkillTree().getId()));

            return itemView;
        }
    }



}
