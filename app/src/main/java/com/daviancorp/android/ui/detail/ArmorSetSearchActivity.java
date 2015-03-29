package com.daviancorp.android.ui.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;

import com.daviancorp.android.data.classes.ArmorSetBuilderSession;
import com.daviancorp.android.mh4udatabase.R;
import com.daviancorp.android.ui.adapter.ArmorSetSearchPagerAdapter;
import com.daviancorp.android.ui.general.GenericTabActivity;
import com.daviancorp.android.ui.list.adapter.MenuSection;

public class ArmorSetSearchActivity extends GenericTabActivity {
    public static final String EXTRA_FROM_SET_BUILDER = "com.daviancorp.android.ui.detail.from_set_builder";
    public static final String EXTRA_REMAINING_SOCKETS = "com.daviancorp.android.ui.detail.remaining_sockets";
    public static final String EXTRA_PIECE_INDEX = "com.daviancorp.android.ui.detail.piece_index";
    public static final String EXTRA_DECORATION_INDEX = "com.daviancorp.android.ui.detail.decoration_index";

    public static final int BUILDER_REQUEST_CODE = 537;
    public static final int REMOVE_DECORATION_REQUEST_CODE = 538;

    private ArmorSetBuilderSession session;


    private ViewPager viewPager;
    private ArmorSetSearchPagerAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.activity_armor_set_search);

        session = new ArmorSetBuilderSession();

        // Initialization
        viewPager = (ViewPager) findViewById(R.id.pager);
        adapter = new ArmorSetSearchPagerAdapter(getSupportFragmentManager(), session);
        viewPager.setAdapter(adapter);

        mSlidingTabLayout.setViewPager(viewPager);

    }

    @Override
    protected MenuSection getSelectedSection() {
        return MenuSection.ARMOR_SET_SEARCH;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) { // If the user canceled the request, we don't want to do anything.
            if (requestCode == BUILDER_REQUEST_CODE) {

            }
            else if (requestCode == REMOVE_DECORATION_REQUEST_CODE) {
            }
        }

    }

    @Override
    public void onPause() {
        super.onPause();
    }



    public ArmorSetBuilderSession getArmorSetSearchSession() {
        return session;
    }

    /** To be called when a fragment contained within this activity has {@code onActivityResult} manually called on it. */
    public void fragmentResultReceived(int requestCode, int resultCode, Intent data) {
        onActivityResult(requestCode, resultCode, data);
    }

}
