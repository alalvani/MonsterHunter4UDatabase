package com.daviancorp.android.ui.ClickListeners;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.daviancorp.android.ui.detail.ItemDetailActivity;

/**
 * Created by Mark on 2/24/2015.
 */
public class ItemClickListener implements View.OnClickListener {
    private Context c;
    private Long id;
    private boolean fromSetbuilder = false;

    public ItemClickListener(Context context, Long id) {
        super();
        this.id = id;
        this.c = context;
    }

    public ItemClickListener(Context context, Long id, boolean fromSetBuilder){
        this(context, id);
        this.fromSetbuilder = fromSetBuilder;
    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent(c, ItemDetailActivity.class);
        i.putExtra(ItemDetailActivity.EXTRA_ITEM_ID, id);
        if (fromSetbuilder) i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        c.startActivity(i);
    }
}
