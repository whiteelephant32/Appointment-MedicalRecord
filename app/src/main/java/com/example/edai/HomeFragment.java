package com.example.edai;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {

    private DatabaseHelper databaseHelper;
    private ListView listViewAppointments;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize UI elements
        listViewAppointments = view.findViewById(R.id.listViewAppointments);

        // Initialize database helper
        databaseHelper = new DatabaseHelper(getActivity());

        // Fetch appointments from database
        Cursor cursor = databaseHelper.getAllAppointments();

        // Define the columns to bind directly
        String[] fromColumns = {"name", "date", "time"};

        // Define the corresponding views to bind the data to
        int[] toViews = {R.id.tvName, R.id.tvDate, R.id.tvTime};

        // Create the SimpleCursorAdapter
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                getActivity(),
                R.layout.item_appointment,  // The layout for each item
                cursor,                     // Cursor with appointment data
                fromColumns,                // Columns from the cursor to bind
                toViews,                    // Views in item layout to bind the data
                0                           // Flags
        );

        // Set the adapter on the ListView
        listViewAppointments.setAdapter(adapter);

        return view;
    }
}
