package com.example.ListViewWithCheckBox;
/*
 * Demo for list view.  Includes code for deleting and adding new members
 * to the list.
 */

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {
    String TAG = "ListviewWithCheckbox";
    private static List<SimpleList> itemList = new ArrayList<SimpleList>();
    private static ArrayAdapter<SimpleList> listViewAdapter; 

    class SimpleList {
        private String name; private boolean selected;
        private SimpleList(String name, boolean selected) {
            this.name = name; this.selected = selected;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        if (itemList.isEmpty()) {
            initializeList(20);
        }
        listViewAdapter = new ViewListAdapter(this, R.layout.list_element, itemList);
        ((ListView)findViewById(R.id.my_list)).setAdapter(listViewAdapter);
    }
    // Initialize the list to strings and index for demo.
    private void initializeList(int size) {
        for (int i = 0; i < size; i++) {
            itemList.add(new SimpleList("entry " + i, false));
        }
        Log.w(TAG, "onCreate:list populated");
    }
    // used to cache list view elements between access.
    static class ViewHolder {
        private TextView tv;
        private CheckBox cb;
        private int index;
        SimpleList item;
    }
    // Our list adapter...
    private class ViewListAdapter extends ArrayAdapter<SimpleList> {
        LayoutInflater inflater;
        public ViewListAdapter(Context context, int id, List<SimpleList> itemList) {
            super(context, id, itemList);
            inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override 
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            ViewHolder vh = null;
            if (itemView != null)
                vh = (ViewHolder)itemView.getTag();
            // Initialize the items if the view is empty.
            if (vh == null || vh.index != position) {
                itemView = inflater.inflate(R.layout.list_element, parent, false);
                SimpleList item = itemList.get(position);
                vh = new ViewHolder();
                vh.item = item;
                TextView tv = vh.tv
                        = (TextView) itemView.findViewById(R.id.itemName);
                CheckBox cb = vh.cb 
                        = (CheckBox) itemView.findViewById(R.id.itemSelected);
                tv.setText(vh.item.name);
                cb.setChecked(item.selected);
                vh.index = position;
                itemView.setTag(vh); // stash our view holder.
            }
            vh = (ViewHolder)itemView.getTag();
            vh.cb.setChecked(vh.item.selected);
            Log.w(TAG, "getView Name: \"" + vh.item.name
                + "\", Selected: " + vh.item.selected);
            return itemView;
        }
    }
    // list-view on click method.
    @Override
    public void onClick(View v) {
        // view container was tagged with ViewHolder and both the
        // TextView and CheckBox items call this call back so that
        // clicking the TextView also toggles the CheckBox.
        ViewHolder vh = (ViewHolder)(v.getTag());
        vh.item.selected = vh.item.selected == false;
        vh.cb.setChecked(vh.item.selected);
        Log.i(TAG, "onClick: checkbox " + vh.index + ":" + vh.item.selected);
    }
    // checkbox listener for select all checkbox.
    public void selectAllClick(View v) {
        // Update the list setting their selected state to that
        // of the select all checkbox.
        boolean selected = ((CheckBox) v).isChecked();
        for (SimpleList h : itemList) {
            h.selected = selected;
        }
        listViewAdapter.notifyDataSetChanged();
    }
    // button listener for "add item" button
    public void addNewItemClick(View v) {
        // Generate a random int between 1 and the last item in the list
        // and insert our new item at that location.
        int insert_at = (int) (Math.random() * itemList.size());
        itemList.add(insert_at, 
             new SimpleList("new item at " + insert_at, false));
        Log.w(TAG, "AddNewItem at " + insert_at);
        listViewAdapter.notifyDataSetChanged();
    }
    // button listener for "delete selected" button
    public void deleteSelectedClick(View v) {
        // Delete selected items in list starting from greatest index.
        for (int i = itemList.size() - 1; i >= 0; i--) {
             if (itemList.get(i).selected) {
                 itemList.remove(i);
                 Log.w(TAG, "deleteSelectedClick: " + i);

             }
        }
        listViewAdapter.notifyDataSetChanged();
    }
}
