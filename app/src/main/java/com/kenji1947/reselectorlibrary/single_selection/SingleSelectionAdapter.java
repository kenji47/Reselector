package com.kenji1947.reselectorlibrary.single_selection;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kenji1947.reselector.AdapterFindHolderByPos;
import com.kenji1947.reselector.HolderClickObserver;
import com.kenji1947.reselector.Reselector;
import com.kenji1947.reselector.ReselectorBuilder;
import com.kenji1947.reselector.ReselectorHolderListener;
import com.kenji1947.reselector.SelectionObserver;
import com.kenji1947.reselectorlibrary.R;
import com.kenji1947.reselectorlibrary.choice_mode.ChoiceModeActivity;

import java.util.List;

/**
 * Created by kenji1947 on 16.04.2017.
 */

public class SingleSelectionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements HolderClickObserver, SelectionObserver, AdapterFindHolderByPos {

    private static final String TAG = SingleSelectionAdapter.class.getSimpleName();
    private final List<String> data;
    private final Reselector reselector;
    private final SingleSelectionActivity activity;
    private ReselectorHolderListener listener;

    public SingleSelectionAdapter(List<String>  data, SingleSelectionActivity activity) {
        this.data = data;
        this.activity = activity;
        reselector = ReselectorBuilder.build(ReselectorBuilder.MODE.SINGLE_SELECTION);
        reselector.registerSelectionObserver(this);
        listener = reselector;
    }

    @Override
    public RecyclerView.ViewHolder findHolderByAdapterPosition(int pos) {
        return activity.recyclerView.findViewHolderForAdapterPosition(pos);
    }
    @Override
    public int getItemCount() {
        return data.size();
    }

    public Reselector getReselector() {
        return reselector;
    }

    public void removeHolder(RecyclerView.ViewHolder viewHolder) {
        //mActivity.printHolders(false, "PRINT BEFORE notifyItemRemoved");
        reselector.removeItem(viewHolder.getAdapterPosition());
        notifyItemRemoved(viewHolder.getAdapterPosition());
        //mActivity.printHolders(false, "PRINT AFTER notifyItemRemoved");
    }
    //--------------------------------------------------------------------------------
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new SingleSelectionAdapter.TextViewHolder(
                inflater.inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        SingleSelectionAdapter.TextViewHolder textViewHolder = (SingleSelectionAdapter.TextViewHolder) holder;
        textViewHolder.bindInfo(data.get(position));

        Log.i(TAG, "onBindViewHolder pos:" + position + " number: " + textViewHolder.number);
        //todo Перенаправлять в реселектор для вызова notify
        holder.itemView.setSelected(reselector.isItemSelected(position));
        //holder.itemView.findViewById(R.id.textViewId).setSelected(reselector.isItemSelected(position));
//        if (isRestoringState) {
//            reselector.onBindHandler(holder);
//        }
    }

    //--------------------------------------------------------------------------------

    @Override
    public void onSelectedChanged(int pos, boolean isSelected) {
        Log.i(TAG, "onSelectedChanged isSelected:" + isSelected);
        findHolderByAdapterPosition(pos).itemView.setSelected(isSelected);
        //findHolderByAdapterPosition(pos).itemView.findViewById(R.id.textViewId).setSelected(isSelected);
    }

    @Override
    public void onSelectableChanged(boolean isSelectable) {
        Log.i(TAG, "onSelectableChanged isSelectable: " + isSelectable);
//        if (isSelectable) {
//            activity.startActionMode();
//        }
    }

    @Override
    public void onHolderClick(int pos) {

    }

    @Override
    public boolean onHolderLongClick(int pos) {
        return false;
    }



    //------------------------------------------------------------------------------
    class TextViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public TextView textView;
        public int number;

        public TextViewHolder(View itemView) {
            super(itemView);
            Log.i(TAG, "Create holder number: " + number + " id: " + getItemId());
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            textView = (TextView) itemView.findViewById(R.id.textViewId);
        }

        public void bindInfo(String text) {
            this.textView.setText(text);
        }

        @Override
        public void onClick(View view) {
            Log.i(TAG, "onClick");
            listener.onHolderClick(this);
        }

        @Override
        public boolean onLongClick(View view) {
            Log.i(TAG, "onLongClick");
            listener.onHolderLongClick(this);
            return true;
        }
    }
    //--------------------------------------------------------------------------------

}
