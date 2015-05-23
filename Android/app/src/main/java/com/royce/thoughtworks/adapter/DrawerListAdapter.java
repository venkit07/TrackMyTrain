
package com.royce.thoughtworks.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.royce.thoughtworks.R;

/**
 * Created by RRaju on 2/3/2015.
 */
public class DrawerListAdapter extends BaseAdapter {
    int[] name = {R.string.home, R.string.add_a_reminder, R.string.wake_me_up, R.string.history, R.string.codes, R.string.about_us, R.string.settings};
    Context mContext;
    int[] icons = {R.drawable.ic_home_grey600_48dp, R.drawable.ic_launcher, R.drawable.ic_schedule_grey600_48dp, R.drawable.ic_history_grey600_48dp, R.drawable.ic_code_grey600_48dp, R.drawable.ic_info_grey600_48dp, R.drawable.ic_settings_grey600_48dp};

    public DrawerListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return name.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View list;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            list = new View(mContext);
            list = inflater.inflate(R.layout.row_drawer_layout, null);


        } else {
            list = (View) convertView;
        }
        TextView title = (TextView) list.findViewById(R.id.drawer_item);
        ImageView icon = (ImageView) list.findViewById(R.id.drawer_icon);

        icon.setImageResource(icons[position]);
        title.setText(name[position]);
        return list;
    }
}
