package com.example.ListViewWithCheckBox;
/*
 * Demo for list view.  Includes code for deleting and adding new members
 * to the list.
 */

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
// import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
    String TAG = "ListviewWithCheckbox";
    private static List<SimpleList> itemList = new ArrayList<SimpleList>();
    class SimpleList {
        private String name; private boolean selected;
        protected SimpleList() {};
        protected SimpleList(String name, boolean selected) {
            this.name = name; this.selected = selected;
        }
        protected boolean Selected() {return selected;}
        protected boolean Selected(boolean selected) {
            return this.selected = selected;
        }
        protected String Name() {return name; }
        protected String Name(String name) {
            return this.name = name;
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
    private void updateView() {
        ArrayAdapter<SimpleList> adapter = new ViewListAdapter();
        ListView list = (ListView) findViewById(R.id.my_list);
        list.setAdapter(adapter);
    }
    // Our list adapter...
    private class ViewListAdapter extends ArrayAdapter<SimpleList> {
        public ViewListAdapter() {
            super(MainActivity.this, R.layout.list_element, itemList);
        }
        @Override 
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            SimpleList item = itemList.get(position);
            // create a view if input is null.
            if (itemView == null) {
                itemView = getLayoutInflater()
                    .inflate(R.layout.list_element, parent, false);
            }
            // Create list item checkbox and text view.
            TextView name = (TextView) itemView.findViewById(R.id.itemName);
            CheckBox selected
                = (CheckBox) itemView.findViewById(R.id.itemSelected);
            // tag the new items with their index into the list so
            // the onClick listener can find them in the list.
            name.setTag(position);
            selected.setTag(position);
            // Set the checkbox and name to that indicated by the itemList.
            selected.setChecked(item.Selected());
            name.setText(item.Name());
            Log.w(TAG, "getView Name: \"" + item.Name()
                + "Selected: " + item.Selected());
            return itemView;
        }
    }
    // list-view on click method.
    @Override
    public void onClick(View v) {
        // each item is taged with its intex into the itemlist when created.
        int viewIndex = (Integer) v.getTag();
        SimpleList item = itemList.get(viewIndex);
        // If this was the checkbox, update the list to reflect its state.
        if (v instanceof CheckBox) {
            item.Selected(((CheckBox)v).isChecked());
        }
        /* 
         *  String mess = "Row: " + viewIndex
         *          + " Selected: " + item.Selected()
         *          + " Name: \"" + item.Name() + "\"";
         *  Log.w(TAG, "onClick:" + mess);
         *  Toast.makeText(MainActivity.this, mess, Toast.LENGTH_LONG).show();
         */
    }
    // checkbox listener for select all checkbox.
    public void selectAllClick(View v) {
        // Update the list setting their selected state to that
        // of the select all checkbox.
        boolean selected = ((CheckBox) v).isChecked();
        for (SimpleList h : itemList) {
            h.Selected(selected);
        }
        updateView();
        // Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
    }
    // button listener for "add item" button
    public void addNewItemClick(View v) {
        // Generate a random int between 1 and the last item in the list.
        int insert_at = (int) (Math.random() * itemList.size());
        // Create an empty item and give it a name.  By default
        // it is not selected.
        SimpleList item = new SimpleList();
        item.Name("new item at " + insert_at);
        itemList.add(insert_at, item);
        updateView();
        // Log.w(TAG, "AddNewItem at " + insert_at);
    }
    // button listener for "delete selected" button
    public void deleteSelectedClick(View v) {
        // Delete selected items in list starting from greatest index.
        for (int i = itemList.size() - 1; i >= 0; i--) {
             if (itemList.get(i).Selected()) {
                 itemList.remove(i);
             }
        }
        updateView();
        // Log.w(TAG, "Delete selected...");
    }
}
