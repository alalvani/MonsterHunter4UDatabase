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
import com.daviancorp.android.mh4udatabase.R;
import com.daviancorp.android.ui.ClickListeners.SkillClickListener;
import com.daviancorp.android.ui.detail.ArmorSetBuilderActivity;

import java.util.List;

public class ArmorSetBuilderStatsListFragment extends Fragment implements ArmorSetBuilderActivity.OnArmorSetActivityUpdateListener {

    private ArmorSetBuilderSession session;
    private ArmorSetBuilderStatsAdapter adapter;

    public static ArmorSetBuilderStatsListFragment newInstance(ArmorSetBuilderSession session) {
        Bundle args = new Bundle();
        ArmorSetBuilderStatsListFragment f = new ArmorSetBuilderStatsListFragment();
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
        View v = inflater.inflate(R.layout.fragment_armor_set_builder_stats, container, false);

        ListView listView = (ListView) v.findViewById(R.id.list);

        adapter = new ArmorSetBuilderStatsAdapter(getActivity().getApplicationContext(), session.getStatPointsSets(), session);
        listView.setAdapter(adapter);

        return v;
    }

    @Override
    public void onArmorSetActivityUpdated(ArmorSetBuilderSession s) {
        session.updateStatPointsSets(getActivity().getApplicationContext());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((ArmorSetBuilderActivity) getActivity()).addArmorSetChangedListener(this);
    }

    private static class ArmorSetBuilderStatsAdapter extends ArrayAdapter<ArmorSetBuilderSession.StatPointsSet> {

        private ArmorSetBuilderSession session;

        public ArmorSetBuilderStatsAdapter(Context context, List<ArmorSetBuilderSession.StatPointsSet> trees, ArmorSetBuilderSession session) {
            super(context, R.layout.fragment_armor_set_builder_stats_item, trees);
            this.session = session;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater.from(getContext()));
            View itemView = inflater.inflate(R.layout.fragment_armor_set_builder_stats_item, parent, false); // Conditional inflation really isn't necessary simply because of how many skills you'd have to have.

            TextView treeName = (TextView) itemView.findViewById(R.id.stat_name);
            TextView headPoints = (TextView) itemView.findViewById(R.id.helmet);
            TextView bodyPoints = (TextView) itemView.findViewById(R.id.body);
            TextView armsPoints = (TextView) itemView.findViewById(R.id.arms);
            TextView waistPoints = (TextView) itemView.findViewById(R.id.waist);
            TextView legsPoints = (TextView) itemView.findViewById(R.id.legs);
            TextView totalPoints = (TextView) itemView.findViewById(R.id.total);

            treeName.setText(getItem(position).getStatName());

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
