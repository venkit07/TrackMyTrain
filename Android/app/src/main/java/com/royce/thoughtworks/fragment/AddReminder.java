
package com.royce.thoughtworks.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;

import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.SaveCallback;
import com.royce.thoughtworks.R;
import com.royce.thoughtworks.datasource.DataSource;

public class AddReminder extends Fragment implements View.OnClickListener {

    EditText mNumber, mCode;
    Button mAdd;
    Spinner mDate;
    DataSource db;
    static boolean onTrain;

    // TODO: Rename and change types and number of parameters
    public static AddReminder newInstance(boolean ontrain) {
        AddReminder fragment = new AddReminder();
        onTrain = ontrain;
        return fragment;
    }

    public AddReminder() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mNumber = (EditText) view.findViewById(R.id.add_number);
        mCode = (EditText) view.findViewById(R.id.add_station_code);
        mDate = (Spinner) view.findViewById(R.id.add_date);
        mAdd = (Button) view.findViewById(R.id.add_reminder);

        if(onTrain) {
            mCode.setHint("Enter destination");
            mAdd.setText("Wake me up!");
        }

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.date_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mDate.setAdapter(adapter);

        mAdd.setOnClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_trip, container, false);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add_reminder) {

            final ProgressDialog dialog = new ProgressDialog(getActivity());
            dialog.setTitle("Please wait");
            dialog.setMessage("Adding your reminder");
            dialog.show();
            dialog.setCancelable(false);
            ParseObject reminder = new ParseObject("Reminder");
            reminder.put("trainNumber", mNumber.getText().toString());
            reminder.put("stationCode", mCode.getText().toString().toUpperCase());
            int day = mDate.getSelectedItemPosition();
            reminder.put("day", day);
            reminder.put("deviceId", Settings.Secure.getString(getActivity().getContentResolver(),
                    Settings.Secure.ANDROID_ID));
            switch (day) {
                default:
                case 0:
                    reminder.put("query",mNumber.getText().toString()+"-today");
                    break;
                case 1:
                    reminder.put("query",mNumber.getText().toString()+"-yesterday");
                    break;
                case 2:
                    reminder.put("query",mNumber.getText().toString()+"-tomorrow");
                    break;
            }
            mNumber.setText("");
            mCode.setText("");
            reminder.put("status", 0);
            reminder.put("isOnBoard", onTrain);
            reminder.pinInBackground();
            reminder.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    dialog.cancel();

                }
            });
            ParsePush.subscribeInBackground(Settings.Secure.getString(getActivity().getContentResolver(),
                    Settings.Secure.ANDROID_ID), new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if(e==null)
                        Toast.makeText(getActivity(),"YO",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


}
