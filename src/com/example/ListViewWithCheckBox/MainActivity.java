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
        updateView();
    }
    // Initialize the list to strings and index for demo.
    private void initializeList(int size) {
        for (int i = 0; i < size; i++) {
            itemList.add(new SimpleList("entry " + i, false));
        }
        Log.w(TAG, "onCreate:list populated");
    }
    // create a new adapter to force update for deleted/added listview items.
    private void updateView() {
        ArrayAdapter<SimpleList> adapter 
            = new ViewListAdapter(this, R.layout.list_element, itemList);
        ((ListView)findViewById(R.id.my_list)).setAdapter(adapter);
    }
    // used to cache list view elements between access.
    static class ViewHolder {TextView tv; CheckBox cb; int index;}
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
            SimpleList item = itemList.get(position);
            ViewHolder vh;
            // Initialize the items if the view is empty.
            if (itemView == null) {
                itemView = inflater.inflate(R.layout.list_element, parent, false);
                vh = new ViewHolder();
                TextView tv = vh.tv
                        = (TextView) itemView.findViewById(R.id.itemName);
                CheckBox cb = vh.cb 
                        = (CheckBox) itemView.findViewById(R.id.itemSelected);
                tv.setText(item.name);
                cb.setChecked(item.selected);
                vh.index = position;
                itemView.setTag(vh); // stash our view holder.
            }
            vh = (ViewHolder)itemView.getTag();
            vh.cb.setChecked(item.selected);
            Log.w(TAG, "getView Name: \"" + item.name
                + "\", Selected: " + item.selected);
            return itemView;
        }
    }
    // list-view on click method.
    @Override
    public void onClick(View v) {
        // view container was tagged with ViewHolder and both the
        // TextView and CheckBox items call this call back so that
        // clicking the TextView also toggles the CheckBox.
        ViewHolder vh = (ViewHolder)((View)v.getParent()).getTag();
        SimpleList item = itemList.get(vh.index);
        boolean selected = vh.cb.isChecked();
        // if the clicked view was a text view, toggle the checkbox.
        if (v instanceof TextView) 
            vh.cb.setChecked(selected == false);
        item.selected = vh.cb.isChecked();
        Log.i(TAG, "onClick: checkbox " + vh.index + ":" + item.selected);
    }
    // checkbox listener for select all checkbox.
    public void selectAllClick(View v) {
        // Update the list setting their selected state to that
        // of the select all checkbox.
        boolean selected = ((CheckBox) v).isChecked();
        for (SimpleList h : itemList) {
            h.selected = selected;
        }
        updateView();
    }
    // button listener for "add item" button
    public void addNewItemClick(View v) {
        // Generate a random int between 1 and the last item in the list
        // and insert our new item at that location.
        int insert_at = (int) (Math.random() * itemList.size());
        itemList.add(insert_at, 
             new SimpleList("new item at " + insert_at, false));
        Log.w(TAG, "AddNewItem at " + insert_at);
        updateView();
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
        updateView();
    }
}
