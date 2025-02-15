package com.example.edai;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.Calendar;

public class AddFragment extends Fragment {

    private EditText etName, etDOB, etAddress, etContact, etEmail;
    private EditText etUserName, etDate, etTime;
    private Button btnAddUser, btnAddAppointment;
    private DatabaseHelper databaseHelper;

    // Variables to store selected date and time
    private int selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        // Initialize UI elements for Users
        etName = view.findViewById(R.id.etName);
        etDOB = view.findViewById(R.id.etDOB);
        etAddress = view.findViewById(R.id.etAddress);
        etContact = view.findViewById(R.id.etContact);
        etEmail = view.findViewById(R.id.etEmail);
        btnAddUser = view.findViewById(R.id.btnAddUser);

        // Initialize UI elements for Appointments
        etUserName = view.findViewById(R.id.etUserName);
        etDate = view.findViewById(R.id.etDate);
        etTime = view.findViewById(R.id.etTime);
        btnAddAppointment = view.findViewById(R.id.btnAddAppointment);

        // Initialize DatabaseHelper
        databaseHelper = new DatabaseHelper(getActivity());

        // Set Click Listeners
        btnAddUser.setOnClickListener(v -> addUser());
        btnAddAppointment.setOnClickListener(v -> addAppointment());

        // Set Date and Time pickers for EditText fields
        etDate.setOnClickListener(v -> showDatePickerDialog());
        etTime.setOnClickListener(v -> showTimePickerDialog());

        return view;
    }

    // Method to show DatePickerDialog
    private void showDatePickerDialog() {
        // Get the current date
        Calendar calendar = Calendar.getInstance();
        selectedYear = calendar.get(Calendar.YEAR);
        selectedMonth = calendar.get(Calendar.MONTH);
        selectedDay = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                (view, year, monthOfYear, dayOfMonth) -> {
                    // Set the selected date to the EditText
                    etDate.setText(String.format("%02d-%02d-%04d", dayOfMonth, monthOfYear + 1, year));
                },
                selectedYear, selectedMonth, selectedDay);

        datePickerDialog.show();
    }

    // Method to show TimePickerDialog
    private void showTimePickerDialog() {
        // Get the current time
        Calendar calendar = Calendar.getInstance();
        selectedHour = calendar.get(Calendar.HOUR_OF_DAY);
        selectedMinute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                (view, hourOfDay, minute) -> {
                    // Set the selected time to the EditText
                    etTime.setText(String.format("%02d:%02d", hourOfDay, minute));
                },
                selectedHour, selectedMinute, true);

        timePickerDialog.show();
    }

    private void addUser() {
        String name = etName.getText().toString().trim();
        String dob = etDOB.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String contact = etContact.getText().toString().trim();
        String email = etEmail.getText().toString().trim();

        if (name.isEmpty() || dob.isEmpty() || address.isEmpty() || contact.isEmpty() || email.isEmpty()) {
            Toast.makeText(getActivity(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean result = databaseHelper.insertUser(name, dob, address, contact, email);
        if (result) {
            Toast.makeText(getActivity(), "User added successfully!", Toast.LENGTH_SHORT).show();
            clearUserFields();
        } else {
            Toast.makeText(getActivity(), "Error adding user. Try again!", Toast.LENGTH_SHORT).show();
        }
    }

    private void addAppointment() {
        String userName = etUserName.getText().toString().trim();
        String date = etDate.getText().toString().trim();
        String time = etTime.getText().toString().trim();

        int userId = getUserIdByName(userName);

        if (userName.isEmpty() || date.isEmpty() || time.isEmpty() || userId == -1) {
            Toast.makeText(getActivity(), "Invalid data! Make sure user exists.", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean result = databaseHelper.insertAppointment(date, time, userId);
        if (result) {
            Toast.makeText(getActivity(), "Appointment added successfully!", Toast.LENGTH_SHORT).show();
            clearAppointmentFields();
        } else {
            Toast.makeText(getActivity(), "Error adding appointment", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to get User ID by Name
    private int getUserIdByName(String name) {
        Cursor cursor = databaseHelper.getAllUsers();
        while (cursor.moveToNext()) {
            if (cursor.getString(1).equals(name)) {
                return cursor.getInt(0); // User ID
            }
        }
        return -1;
    }

    private void clearUserFields() {
        etName.setText("");
        etDOB.setText("");
        etAddress.setText("");
        etContact.setText("");
        etEmail.setText("");
    }

    private void clearAppointmentFields() {
        etUserName.setText("");
        etDate.setText("");
        etTime.setText("");
    }
}
