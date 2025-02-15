package com.example.edai;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MyAppDatabase.db";
    private static final int DATABASE_VERSION = 2; // Incremented version

    // Table Names
    private static final String TABLE_USERS = "Users";
    private static final String TABLE_DOCTORS = "Doctors";
    private static final String TABLE_APPOINTMENTS = "Appointments";
    private static final String TABLE_BILLS = "Bills";

    // Common Columns
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_ADDRESS = "address";
    private static final String COLUMN_CONTACT = "contact";
    private static final String COLUMN_EMAIL = "email";

    // Users Table Columns
    private static final String COLUMN_DOB = "dob";

    // Doctors Table Columns
    private static final String COLUMN_DEGREE = "degree";

    // Appointments Table Columns
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_TIME = "time";
    private static final String COLUMN_USER_ID = "user_id"; // Foreign Key to Users Table

    // Bills Table Columns
    private static final String COLUMN_MEDICINE_NAME = "medicine_name";
    private static final String COLUMN_QUANTITY = "quantity";
    private static final String COLUMN_PRICE = "price";

    // Create Users Table Query
    private static final String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_NAME + " TEXT NOT NULL, "
            + COLUMN_DOB + " TEXT NOT NULL, "
            + COLUMN_ADDRESS + " TEXT NOT NULL, "
            + COLUMN_CONTACT + " TEXT UNIQUE NOT NULL, "
            + COLUMN_EMAIL + " TEXT UNIQUE NOT NULL);";

    // Create Doctors Table Query
    private static final String CREATE_DOCTORS_TABLE = "CREATE TABLE " + TABLE_DOCTORS + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_NAME + " TEXT NOT NULL, "
            + COLUMN_ADDRESS + " TEXT NOT NULL, "
            + COLUMN_CONTACT + " TEXT UNIQUE NOT NULL, "
            + COLUMN_EMAIL + " TEXT UNIQUE NOT NULL, "
            + COLUMN_DEGREE + " TEXT NOT NULL);";

    // Create Appointments Table Query
    private static final String CREATE_APPOINTMENTS_TABLE = "CREATE TABLE " + TABLE_APPOINTMENTS + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_DATE + " TEXT NOT NULL, "
            + COLUMN_TIME + " TEXT NOT NULL, "
            + COLUMN_USER_ID + " INTEGER NOT NULL, "
            + "FOREIGN KEY (" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + "));";

    // Create Bills Table Query
    private static final String CREATE_BILLS_TABLE = "CREATE TABLE " + TABLE_BILLS + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_MEDICINE_NAME + " TEXT NOT NULL, "
            + COLUMN_QUANTITY + " INTEGER NOT NULL, "
            + COLUMN_PRICE + " REAL NOT NULL, "
            + COLUMN_USER_ID + " INTEGER NOT NULL, "
            + "FOREIGN KEY (" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + "));";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create tables
        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_DOCTORS_TABLE);
        db.execSQL(CREATE_APPOINTMENTS_TABLE);
        db.execSQL(CREATE_BILLS_TABLE);

        // Insert default data only if the table is empty
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_USERS, null);
        cursor.moveToFirst();
        if (cursor.getInt(0) == 0) {
            // Insert default data for Users
            db.execSQL("INSERT INTO " + TABLE_USERS + " (name, dob, address, contact, email) VALUES ('John Doe', '1990-01-01', '123 Main St', '1234567890', 'johndoe@example.com')");
            db.execSQL("INSERT INTO " + TABLE_USERS + " (name, dob, address, contact, email) VALUES ('Jane Smith', '1985-05-15', '456 Oak St', '0987654321', 'janesmith@example.com')");
        }
        cursor.close();
    }

    public String getColumnName() {
        return COLUMN_NAME;
    }

    public String getColumnDate() {
        return COLUMN_DATE;
    }

    public String getColumnTime() {
        return COLUMN_TIME;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOCTORS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_APPOINTMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BILLS);
        onCreate(db);
    }
    // Insert User
    public boolean insertUser(String name, String dob, String address, String contact, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_DOB, dob);
        values.put(COLUMN_ADDRESS, address);
        values.put(COLUMN_CONTACT, contact);
        values.put(COLUMN_EMAIL, email);

        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;
    }


    // Insert Appointment
    public boolean insertAppointment(String date, String time, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_TIME, time);
        values.put(COLUMN_USER_ID, userId);

        long result = db.insert(TABLE_APPOINTMENTS, null, values);
        return result != -1;
    }

    // Insert Bill
    public boolean insertBill(String medicineName, int quantity, double price, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MEDICINE_NAME, medicineName);
        values.put(COLUMN_QUANTITY, quantity);
        values.put(COLUMN_PRICE, price);
        values.put(COLUMN_USER_ID, userId);

        long result = db.insert(TABLE_BILLS, null, values);
        return result != -1;
    }

    // Fetch All Users
    public Cursor getAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_USERS, null);
    }


    // Fetch All Appointments Ordered by Date and Time
    public Cursor getAllAppointments() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT a." + COLUMN_ID + " AS _id, a." + COLUMN_DATE + ", a." + COLUMN_TIME + ", u." + COLUMN_NAME + " " +
                "FROM " + TABLE_APPOINTMENTS + " a " +
                "JOIN " + TABLE_USERS + " u ON a." + COLUMN_USER_ID + " = u." + COLUMN_ID + " " +
                "ORDER BY a." + COLUMN_DATE + " ASC, a." + COLUMN_TIME + " ASC";

        return db.rawQuery(query, null);
    }




    // Fetch All Bills
    public Cursor getAllBills() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_BILLS, null);
    }
}
