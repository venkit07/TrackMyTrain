package com.royce.thoughtworks.fragment;

import android.app.Activity;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.royce.thoughtworks.R;
import com.royce.thoughtworks.datasource.DataSource;
import com.royce.thoughtworks.model.Station;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class StationCodesFragment extends Fragment {

    EditText search_text;
    ImageView search;
    ListView listView;
    List<String> station,codes;
    // TODO: Rename and change types and number of parameters
    public static StationCodesFragment newInstance() {
        StationCodesFragment fragment = new StationCodesFragment();
        return fragment;
    }

    public StationCodesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initStationDb();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.frag_codes, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        search_text = (EditText) view.findViewById(R.id.search);
        search = (ImageView) view.findViewById(R.id.done);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    private void initStationDb() {

        station = new ArrayList<String>();
        codes = new ArrayList<String>();

        InputStream fis;
        StringBuffer fileContent = new StringBuffer();
        try {
            fis = getActivity().getAssets().open("stationList.txt");
            byte[] buffer = new byte[1024];
            int n;
            while ((n = fis.read(buffer)) != -1) {
                fileContent.append(new String(buffer, 0, n));
            }
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] items = fileContent.toString().split(",");
        for (String item : items) {
            station.add(item.substring(0, item.indexOf("-")));
            codes.add(item.substring(item.indexOf("-") + 2));
        }
    }



}
