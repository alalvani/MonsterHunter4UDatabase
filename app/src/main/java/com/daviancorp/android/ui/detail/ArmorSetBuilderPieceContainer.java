package com.daviancorp.android.ui.detail;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.*;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import com.daviancorp.android.data.classes.ArmorSetBuilderSession;
import com.daviancorp.android.mh4udatabase.R;
import com.daviancorp.android.ui.dialog.ArmorSetBuilderDecorationsDialogFragment;
import com.daviancorp.android.ui.list.ArmorListActivity;
import com.daviancorp.android.ui.list.DecorationListActivity;

import java.io.IOException;
import java.io.InputStream;

public class ArmorSetBuilderPieceContainer extends LinearLayout {

    public static final int MENU_ADD_PIECE = 0;
    public static final int MENU_REMOVE_PIECE = 1;
    public static final int MENU_ADD_DECORATION = 2;
    public static final int MENU_REMOVE_DECORATION = 3;
    public static final int MENU_PIECE_INFO = 4;

    private ImageView icon;
    private TextView text;

    private ImageView[] decorationIcons;

    private ImageView popupMenuButton;

    private ArmorSetBuilderSession session;
    private int pieceIndex;
    private Fragment parentFragment;

    /**
     * Default constructor.
     * <p/>
     * It is required to call {@code initialize} after instantiating this class.
     */
    public ArmorSetBuilderPieceContainer(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_armor_set_builder_piece_container, this);

        icon = (ImageView) findViewById(R.id.armor_builder_item_icon);
        text = (TextView) findViewById(R.id.armor_builder_item_name);

        decorationIcons = new ImageView[3];
        decorationIcons[0] = (ImageView) findViewById(R.id.decoration_1);
        decorationIcons[1] = (ImageView) findViewById(R.id.decoration_2);
        decorationIcons[2] = (ImageView) findViewById(R.id.decoration_3);

        popupMenuButton = (ImageView) findViewById(R.id.popup_menu_button);
		popupMenuButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				createPopupMenu().show();
			}
		});
    }

    /** 
     * Provides necessary external initialization logic.
     * <p/>
     * Should always be called after the container's constructor.
     */
    public void initialize(ArmorSetBuilderSession session, int pieceIndex, Fragment parentFragment) {
        this.session = session;
        this.pieceIndex = pieceIndex;
        this.parentFragment = parentFragment;

        updateContents();
    }

    /** Refreshes the contents of the piece container based on the contents of the {@code ArmorSetBuilderSession}. */
    public void updateContents() {
        updateArmorPiece();
        updateDecorations();
    }

    private void updateArmorPiece() {
        if (session.isPieceSelected(pieceIndex)) {
            text.setText(session.getArmor(pieceIndex).getName());
            icon.setImageBitmap(fetchIcon(session.getArmor(pieceIndex).getRarity()));
        } else {
            onArmorRemoved();
        }
    }

    private void updateDecorations() {
        for (int i = 0; i < 3; i++) {
            if (session.decorationIsReal(pieceIndex, i)) {
                decorationIcons[i].setImageDrawable(getResources().getDrawable(R.drawable.decoration_real));
            } else if (session.decorationIsDummy(pieceIndex, i)) { // The socket index in question is a dummy
                decorationIcons[i].setImageDrawable(getResources().getDrawable(R.drawable.decoration_dummy));
            } else if (session.getArmor(pieceIndex).getNumSlots() > i) { // The socket in question is empty
                decorationIcons[i].setImageDrawable(getResources().getDrawable(R.drawable.decoration_empty));
            } else { // The armor piece has no more sockets
                decorationIcons[i].setImageDrawable(getResources().getDrawable(R.drawable.decoration_none));
            }
        }
    }

    /** Resets the container to its default state. */
    private void onArmorRemoved() {
        text.setText("");
        icon.setImageBitmap(fetchIcon(1));
        updateDecorations();
    }

    /** Helper method that retrieves a rarity-appropriate equipment icon. */
    private Bitmap fetchIcon(int rarity) {
        String slot = "";
        switch (pieceIndex) {
            case ArmorSetBuilderSession.HEAD:
                slot = "head";
                break;
            case ArmorSetBuilderSession.BODY:
                slot = "body";
                break;
            case ArmorSetBuilderSession.ARMS:
                slot = "arms";
                break;
            case ArmorSetBuilderSession.WAIST:
                slot = "waist";
                break;
            case ArmorSetBuilderSession.LEGS:
                slot = "legs";
                break;
        }

        String imageRes = "icons_armor/icons_" + slot + "/" + slot + String.valueOf(rarity) + ".png";
        AssetManager manager = getContext().getAssets();
        InputStream stream;

        try {
            stream = manager.open(imageRes);
            Bitmap bitmap = BitmapFactory.decodeStream(stream);

            stream.close();

            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * The order of the menu items in this menu can be changed by modifying the order in which {@code popup.getMenu().add} is called
     * @return A {@code PopupMenu} that allows the user to modify the armor piece.
     */
    private PopupMenu createPopupMenu() {
        PopupMenu popup = new PopupMenu(new ContextThemeWrapper(getContext(), R.style.PopupMenuStyle), popupMenuButton); // Because we're not in the fragment, we have to use a theme wrapper
        
        boolean pieceSelected = session.isPieceSelected(pieceIndex);
        boolean hasSlotsAvailable = session.getAvailableSlots(pieceIndex) > 0;
        boolean hasDecorations = session.hasDecorations(pieceIndex);
        
        if (!pieceSelected) {
            popup.getMenu().add(Menu.NONE, MENU_ADD_PIECE, Menu.NONE, R.string.armor_set_builder_add_piece);
        }
        else {
            popup.getMenu().add(Menu.NONE, MENU_REMOVE_PIECE, Menu.NONE, R.string.armor_set_builder_remove_piece);
        }

        if (session.getAvailableSlots(pieceIndex) > 0) {
            popup.getMenu().add(Menu.NONE, MENU_ADD_DECORATION, Menu.NONE, R.string.armor_set_builder_add_decoration);
        }

        if (session.hasDecorations(pieceIndex)) {
            popup.getMenu().add(Menu.NONE, MENU_REMOVE_DECORATION, Menu.NONE, R.string.armor_set_builder_remove_decoration);
        }
        
        if (pieceSelected) {
            popup.getMenu().add(Menu.NONE, MENU_PIECE_INFO, Menu.NONE, R.string.armor_set_builder_piece_info);
        }

		popup.setOnMenuItemClickListener(new PiecePopupMenuClickListener());

        return popup;
    }

    /** Listens for when the user clicks on an element in the {@code PopupMenu}. */
    private class PiecePopupMenuClickListener implements PopupMenu.OnMenuItemClickListener {
        
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case MENU_ADD_PIECE:
                    onMenuAddPieceSelected();
                    break;
                case MENU_REMOVE_PIECE:
                    onMenuRemovePieceSelected();
                    break;
				case MENU_ADD_DECORATION:
					onMenuAddDecorationSelected();
					break;
				case MENU_REMOVE_DECORATION:
                    onMenuRemoveDecorationSelected();
					break;
				case MENU_PIECE_INFO: // TODO
                    onMenuGetPieceInfoSelected();
					break;
				default:
					return false;
            }
            return true;
        }

        /** Called when the user chooses to add an armor piece. */
        private void onMenuAddPieceSelected() {
            Intent i = new Intent(getContext(), ArmorListActivity.class);
            i.putExtra(ArmorSetBuilderActivity.EXTRA_FROM_SET_BUILDER, true);
            i.putExtra(ArmorSetBuilderActivity.EXTRA_PIECE_INDEX, pieceIndex);

            ((Activity) getContext()).startActivityForResult(i, ArmorSetBuilderActivity.BUILDER_REQUEST_CODE);
        }

        /** Called when the user chooses to remove an armor piece. */
        private void onMenuRemovePieceSelected() {
            session.removeArmor(pieceIndex);
            updateArmorPiece();
        }

        /** Called when the user chooses to add a decoration. */
        private void onMenuAddDecorationSelected() {
            Intent i = new Intent(getContext(), DecorationListActivity.class);
            i.putExtra(ArmorSetBuilderActivity.EXTRA_FROM_SET_BUILDER, true);
            i.putExtra(ArmorSetBuilderActivity.EXTRA_PIECE_INDEX, pieceIndex);
            i.putExtra(ArmorSetBuilderActivity.EXTRA_REMAINING_SOCKETS, session.getAvailableSlots(pieceIndex));

            ((Activity) getContext()).startActivityForResult(i, ArmorSetBuilderActivity.BUILDER_REQUEST_CODE);
        }

        /** Called when the user chooses to remove a decoration. */
        private void onMenuRemoveDecorationSelected() {
            ArmorSetBuilderDecorationsDialogFragment d = ArmorSetBuilderDecorationsDialogFragment.newInstance(session, pieceIndex);
            d.setTargetFragment(parentFragment, ArmorSetBuilderActivity.REMOVE_DECORATION_REQUEST_CODE);
            d.show(parentFragment.getActivity().getSupportFragmentManager(), "tag");
        }
        
        /** Called when the user chooses to retrieve info about their armor piece. */
        private void onMenuGetPieceInfoSelected() {
            Intent i = new Intent(getContext(), ArmorDetailActivity.class);
            i.putExtra(ArmorDetailActivity.EXTRA_ARMOR_ID, session.getArmor(pieceIndex).getId());
            getContext().startActivity(i);
        }
    }
}
