package com.kenji1947.reselectorlibrary.choice_mode;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kenji1947.reselector.Reselector;
import com.kenji1947.reselector.SelectionObserver;
import com.kenji1947.reselectorlibrary.DataLab;
import com.kenji1947.reselectorlibrary.R;

import java.util.HashSet;
import java.util.List;

/**
 * Created by kenji1947 on 16.04.2017.
 */

public class ChoiceModeActivity extends AppCompatActivity {
    private String TAG = ChoiceModeActivity.class.getSimpleName();
    private final ActionModeCallback actionModeCallback = new ActionModeCallback();
    private String SELECTED_ITEMS = "Selected_Items";

    private ChoiceModeAdapter adapter;
    public RecyclerView recyclerView;
    Button printButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.list_activity);
        printButton = (Button) findViewById(R.id.buttonPrint);
        printButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                printHolders(false, "PRINT HOLDERS BUTTON");
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.gallery);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<String> list = DataLab.getData();
        //mAdapter = new SelectableRecyclerViewAdapter(this, dataSet);
        adapter = new ChoiceModeAdapter(list, this);
        if (savedInstanceState != null) {
            adapter.getReselector()
                    .setSelectedItems((HashSet<Integer>) savedInstanceState.getSerializable(SELECTED_ITEMS));
        }
        recyclerView.setAdapter(adapter);

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                          RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                        int location = viewHolder.getAdapterPosition();
                        Log.i(TAG, "onSwiped holder.pos:" + viewHolder.getAdapterPosition()
                                + " holder.layout_pos:" + viewHolder.getLayoutPosition()
                        );
                        //delete from adapter
                        //and notify
                        DataLab.getData().remove(viewHolder.getAdapterPosition());
                        adapter.removeHolder(viewHolder);
                    }
                };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

    public void printHolders(boolean isDelay, final String title) {
        if (isDelay) {
            recyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    printHolders(title);
                }
            }, 5);
        } else {
            printHolders(title);
        }
    }

    private void printHolders(String title) {
        Log.i(TAG, title);
        String info;
        List<String> list = DataLab.getData();
        for (int i = 0; i < list.size(); i++) {
            info = "";
            RecyclerView.ViewHolder holder = recyclerView.findViewHolderForAdapterPosition(i);
            if (holder != null) {
                info = info.concat("array i:" + i
                        + "  array text:" + list.get(i)
                        + "  holder text:" + ((TextView)(holder.itemView.findViewById(R.id.textViewId))).getText()
                        + "  adapter_pos:" + holder.getAdapterPosition()
                        + "  layout_pos:" + holder.getLayoutPosition()
                        + "  old:" + holder.getOldPosition()
                        + "  :" + holder
                );
            } else {
                info = info.concat("array i:" + i
                        + "  array text:" + list.get(i)
                        + "  null");
            }
            Log.i(TAG, info);
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(SELECTED_ITEMS, adapter.getReselector().getSelectedItems());
    }

    public void startActionMode() {
        startActionMode(actionModeCallback);
    }

    private class ActionModeCallback implements ActionMode.Callback, SelectionObserver {
        private ActionMode mActionMode;

        @Override
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode actionMode) {
            Reselector reselector = adapter.getReselector();
            reselector.unregisterSelectionObserver(this);
            mActionMode = null;
            reselector.setActionModeStatusAndNotifyObservers(false);
            reselector.clearSelectionAndNotifyObservers();
        }

        @Override
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            mActionMode = actionMode;
            mActionMode.getMenuInflater().inflate(R.menu.gallery_selection, menu);
            adapter.getReselector().registerSelectionObserver(this);
            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.menu_toast:
                    Toast.makeText(ChoiceModeActivity.this,
                            "Toas msg", Toast.LENGTH_SHORT).show();
                    break;
            }
            return true;
        }

        @Override
        public void onSelectedChanged(int pos, boolean isSelected) {
            if (mActionMode != null) {
                int checkedImagesCount = adapter.getReselector().getSelectedItems().size();
                mActionMode.setTitle(String.valueOf(checkedImagesCount));
            }
        }

        @Override
        public void onSelectableChanged(boolean isSelectable) {
            if (!isSelectable) {
                mActionMode.finish();
            }
        }
    }
}
