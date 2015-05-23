package com.royce.thoughtworks.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseObject;
import com.royce.thoughtworks.R;

import java.util.List;
import java.util.Random;

/**
 * Created by RRaju on 2/4/2015.
 */
public class HistoryAdapter extends BaseAdapter {
    Context mContext;
    List<ParseObject> reminders;

    public HistoryAdapter(List<ParseObject> reminders, Context mContext) {
        this.reminders = reminders;
        this.mContext = mContext;
    }

    @Override

    public int getCount() {
        return reminders.size();
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
        ParseObject reminder = reminders.get(position);
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.row_track, null);
        TextView number = (TextView) convertView.findViewById(R.id.track_number);
        TextView station = (TextView) convertView.findViewById(R.id.track_station);
        ImageView submit = (ImageView) convertView.findViewById(R.id.track_submit);

        number.setText(reminder.getString("trainNumber"));
        station.setText(reminder.getString("stationCode"));
        final boolean isOnBoard = reminder.getBoolean("isOnBoard");
        if (!reminder.getBoolean("isOnBoard"))
            submit.setImageResource(R.drawable.ic_directions_transit_grey600_48dp);
        else
            submit.setImageResource(R.drawable.ic_schedule_grey600_48dp);
        if (reminder.getInt("status") == 1)
            submit.setImageResource(R.drawable.ic_verified_user_grey600_48dp);

        Random random = new Random();
        final int choice = random.nextInt(2);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(choice, isOnBoard);
            }
        });
        return convertView;
    }

    private void showDialog(int choice, boolean isOnBoard) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        alertDialogBuilder.setTitle("Status");

        String message = new String();
        if (isOnBoard) {
            if (choice == 1)
                message = mContext.getResources().getString(R.string.location_1);
            else
                message = mContext.getResources().getString(R.string.location_2);
        } else {
            if (choice == 1)
                message = mContext.getResources().getString(R.string.status_1);
            else
                message = mContext.getResources().getString(R.string.status_2);
        }


        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setNegativeButton("Ok, I got it ", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }
}
