package io.vinnie.servitetracker.android;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class DrawerAdapter extends BaseAdapter implements ListView.OnItemClickListener {

    private Context context;
    private String[] titles;
    private ArrayList<DrawerItemListener> listeners = new ArrayList<DrawerItemListener>();

    public DrawerAdapter(Context context, String[] titles) {
        this.context = context;
        this.titles = titles;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public Object getItem(int position) {
        return titles[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView != null && convertView instanceof TextView) {
            ((TextView) convertView).setText(titles[position]);
            return convertView;
        }
        TextView view = (TextView) View.inflate(context, R.layout.drawer_item, null);
        view.setText(titles[position]);
        return view;
    }

    public void addListener(DrawerItemListener l) {
        listeners.add(l);
    }

    public void removeListener(DrawerItemListener l) {
        listeners.remove(l);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        for (DrawerItemListener l : listeners)
            l.onDrawerItemSelected(position);
    }

    public interface DrawerItemListener {
        public void onDrawerItemSelected(int position);
    }

}
