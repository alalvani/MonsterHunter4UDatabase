package com.daviancorp.android.ui.ClickListeners;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.daviancorp.android.ui.detail.DecorationDetailActivity;
import com.daviancorp.android.ui.detail.SkillTreeDetailActivity;

/**
 * Created by Mark on 2/24/2015.
 */
public class SkillClickListener implements View.OnClickListener {
    private Context c;
    private Long id;
    private boolean fromSetbuilder = false;

    public SkillClickListener(Context context, Long id) {
        super();
        this.id = id;
        this.c = context;
    }

    public SkillClickListener(Context context, Long id, boolean fromSetBuilder){
        this(context, id);
        this.fromSetbuilder = fromSetBuilder;
    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent(c, SkillTreeDetailActivity.class);
        i.putExtra(SkillTreeDetailActivity.EXTRA_SKILLTREE_ID, id);
        if (fromSetbuilder) i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        c.startActivity(i);
    }
}
