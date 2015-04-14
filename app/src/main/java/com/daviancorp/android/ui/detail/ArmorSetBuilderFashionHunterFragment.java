package com.daviancorp.android.ui.detail;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.daviancorp.android.data.classes.Armor;
import com.daviancorp.android.data.classes.ArmorSetBuilderSession;
import com.daviancorp.android.mh4udatabase.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ArmorSetBuilderFashionHunterFragment extends Fragment implements ArmorSetBuilderActivity.OnArmorSetActivityUpdateListener {

    private ArmorSetBuilderSession session;
    private ImageView img;
    private AssetManager assetManager;
    private InputStream open = null;


    public static ArmorSetBuilderFashionHunterFragment newInstance(ArmorSetBuilderSession session) {
        Bundle args = new Bundle();
        ArmorSetBuilderFashionHunterFragment f = new ArmorSetBuilderFashionHunterFragment();
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
        View v = inflater.inflate(R.layout.fragment_armor_set_builder_fashion_hunter, container, false);
        assetManager = getActivity().getAssets();
        img = (ImageView) v.findViewById(R.id.test_image);
        loadLayers();
        return v;
    }

    private void loadLayers(){
        Drawable [] layers = new Drawable[6];
        int index = 0;
        //Order here is kinda important. The bigger the index, the higher the layer
        Armor[] a = new Armor[5];
        a[0] = session.getArmor(session.ARMS);
        a[1] = session.getArmor(session.BODY);
        a[2] = session.getArmor(session.HEAD);
        a[3] = session.getArmor(session.LEGS);
        a[4] = session.getArmor(session.WAIST);

        try {
            layers[0] = loadBitmap("female_armor/female_armor_main.png");
        }
        catch (IOException e) {
            /* If we cannot load background, kill app.. */
            e.printStackTrace();
            return;
        }

        /* fill up the earlier layers with the background for blank armors */
        for (int i = 0; i < a.length; i++) {
            if (a[i].getId() < 0) layers[++index] = layers[0];
        }
        /* Actually add images now to the end of the array */
        for (int i = 0; i < a.length; i++) {
            if (a[i].getId() > 0) {
                try {
                    switch(a[i].getSlot()){
                        case "Head":
                            layers[++index] = loadBitmap("female_armor/Head/female_armor_head_" + a[i].getImageId() + ".png");
                            break;
                        case "Body":
                            layers[++index] = loadBitmap("female_armor/Body/female_armor_body_" + a[i].getImageId() + ".png");
                            break;
                        case "Arms":
                            layers[++index] = loadBitmap("female_armor/Arms/female_armor_arms_" + a[i].getImageId() + ".png");
                            break;
                        case "Waist":
                            layers[++index] = loadBitmap("female_armor/Waist/female_armor_waist_" + a[i].getImageId() + ".png");
                            break;
                        case "Legs":
                            layers[++index] = loadBitmap("female_armor/Legs/female_armor_legs_" + a[i].getImageId() + ".png");
                            break;
                    }
                }
                catch (IOException e) {
                    //TODO: check to see if we need a ++ here..
                    layers[++index] = layers[0];
                }
            }
        }
        LayerDrawable layerDrawable = new LayerDrawable(layers);
        img.setImageDrawable(layerDrawable);
    }

    // Read a Bitmap from Assets
    private Drawable loadBitmap(String assetUrl) throws IOException{
        Drawable r = null;
        open = assetManager.open(assetUrl);
        Bitmap bitmap = BitmapFactory.decodeStream(open);
        r = new BitmapDrawable(getResources(), bitmap);
        if(open != null) open.close();
        return r;
    }

    @Override
    public void onArmorSetActivityUpdated(ArmorSetBuilderSession s) {
        loadLayers();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((ArmorSetBuilderActivity) getActivity()).addArmorSetChangedListener(this);
    }
}
