package com.example.parkingappfrags_v2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MyDatabaseHelper extends SQLiteOpenHelper {


    private Context context;
    private static final String DATABASE_NAME = "ParkingLotFrags.db";
    private static final int DATABASE_VERSION = 1;

    // Dimensions Table
    private static final String DIM_TABLE_NAME = "dimensions";
    private static final String DIM_ID = "dim_id";
    private static final String DIM_ROWS = "dim_rows";
    private static final String DIM_COLS = "dim_columns";
    private static final String DIM_NUM = "dim_numberoflots";

    // Price-Per-Hour Table
    private static final String PRICE_TABLE_NAME = "price_per_hour";
    private static final String PRICE_ID = "price_id";
    private static final String PRICE_AMT = "price_amt";

    // Parking Details Table
    private static final String PARKING_TABLE_NAME = "parking";
    private static final String PARKING_CUSTOMER_ID = "parking_customer_id";
    private static final String PARKING_CUSTOMER_NAME = "parking_customer_name";
    private static final String PARKING_CUSTOMER_PH_NO = "parking_customer_ph_no";
    private static final String PARKING_CUSTOMER_VEHICLE_NO = "parking_customer_vehicle_no";
    private static final String PARKING_LOT_NO = "parking_lot_no";
    private static final String PARKING_START_TIME = "parking_start_time";
    private static final String PARKING_END_TIME = "parking_end_time";

    // Master Details Table
    private static final String MASTER_TABLE_NAME = "master";
    private static final String MASTER_CUSTOMER_ID = "master_customer_id";
    private static final String MASTER_CUSTOMER_NAME = "master_customer_name";
    private static final String MASTER_CUSTOMER_PH_NO = "master_customer_ph_no";
    private static final String MASTER_CUSTOMER_VEHICLE_NO = "master_customer_vehicle_no";
    private static final String MASTER_LOT_NO = "master_lot_no";
    private static final String MASTER_START_TIME = "master_start_time";
    private static final String MASTER_LEAVE_TIME = "master_leave_time";

    // Free-Lots Table
    private static final String FREELOTS_TABLE_NAME = "freelots";
    private static final String FREELOTS_ID = "fl_id";
    private static final String FREELOTS_LOT_NO = "freelots_lot_no";
    private static final String FREELOTS_STATUS = "freelots_status";

    // UPI-ID Table
    private static final String UPI_TABLE_NAME = "upi";
    private static final String UPI_TABLE_ID = "upi_table_id";
    private static final String UPI_ID = "upi_id";

    // Price-Accumulation Table
    private static final String PRICE_ACCUMULATION_TABLE_NAME = "price_accumulation";
    private static final String PRICE_ACCUMULATION_TABLE_ID = "price_accumulation_table_id";
    private static final String PRICE_ACCUMULATION_DATE = "price_accumulation_date";
    private static final String PRICE_ACCUMULATION_DAY = "price_accumulation_day";
    private static final String PRICE_ACCUMULATION_TOTALPRICE = "price_accumulation_total_price";

    // Singleton support
    private static MyDatabaseHelper mInstance = null;
    public MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public static MyDatabaseHelper getInstance(Context context){
        if (mInstance == null){
            mInstance = new MyDatabaseHelper(context.getApplicationContext());
        }
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Creating Dimensions Table
        String query1 =
                "CREATE TABLE " + DIM_TABLE_NAME +
                        " (" + DIM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        DIM_ROWS + " INTEGER, " +
                        DIM_COLS + " INTEGER, " +
                        DIM_NUM + " INTEGER);";
        db.execSQL(query1);

        // Creating Price-Per-Hour Table
        String query2 = "CREATE TABLE " + PRICE_TABLE_NAME +
                " (" + PRICE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PRICE_AMT + " INTEGER);";
        db.execSQL(query2);

        // Creating Parking Table
        String query3 = "CREATE TABLE " + PARKING_TABLE_NAME +
                " (" + PARKING_CUSTOMER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PARKING_CUSTOMER_NAME + " TEXT, " +
                PARKING_CUSTOMER_PH_NO + " TEXT, " +
                PARKING_CUSTOMER_VEHICLE_NO + " TEXT, " +
                PARKING_LOT_NO + " TEXT, " +
                PARKING_START_TIME + " TEXT, " +
                PARKING_END_TIME + " TEXT);";
        db.execSQL(query3);

        // Creating Free-Lots Table
        String query4 =
                "CREATE TABLE " + FREELOTS_TABLE_NAME +
                        " (" + FREELOTS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        FREELOTS_LOT_NO + " INTEGER, " +
                        FREELOTS_STATUS + " INTEGER);";
        db.execSQL(query4);

        // Creating Master Table
        String query5 = "CREATE TABLE " + MASTER_TABLE_NAME +
                " (" + MASTER_CUSTOMER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MASTER_CUSTOMER_NAME + " TEXT, " +
                MASTER_CUSTOMER_PH_NO + " TEXT, " +
                MASTER_CUSTOMER_VEHICLE_NO + " TEXT, " +
                MASTER_LOT_NO + " TEXT, " +
                MASTER_START_TIME + " TEXT, " +
                MASTER_LEAVE_TIME + " TEXT);";
        db.execSQL(query5);

        // Creating UPI Table
        String query6 = "CREATE TABLE " + UPI_TABLE_NAME +
                " (" + UPI_TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                UPI_ID + " TEXT);";
        db.execSQL(query6);

        // Creating Price-Accumulation Table
        String query7 = "CREATE TABLE " + PRICE_ACCUMULATION_TABLE_NAME +
                " (" + PRICE_ACCUMULATION_TABLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PRICE_ACCUMULATION_DATE + " TEXT, " +
                PRICE_ACCUMULATION_DAY + " TEXT, " +
                PRICE_ACCUMULATION_TOTALPRICE + " TEXT);";
        db.execSQL(query7);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + DIM_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PRICE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PARKING_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + FREELOTS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MASTER_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + UPI_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PRICE_ACCUMULATION_TABLE_NAME);
        onCreate(db);
    }

    // Functions for Dimensions Table
    void addDimensions(Integer rows, Integer cols, Integer numberoflots){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(DIM_ROWS, rows);
        cv.put(DIM_COLS, cols);
        cv.put(DIM_NUM, numberoflots);

        long result = db.insert(DIM_TABLE_NAME, null, cv);
        if (result == -1){
            Toast.makeText(context, "Failed to insert to dimensions db", Toast.LENGTH_SHORT).show();
        }
    }

    public int retrieveDimensions_id(){
        int ID = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        Cursor res = db.rawQuery("select " + DIM_ID + " from " + DIM_TABLE_NAME + ";",null);
        res.moveToFirst();
        while (res.isAfterLast() == false){
            //arrayList.add(res.getInt(res.getColumnIndex(DIM_ID)));
            arrayList.add(res.getInt(res.getColumnIndexOrThrow(DIM_ID)));
            res.moveToNext();
        }
        if (arrayList.size()!=0)
            ID = arrayList.get(arrayList.size() - 1);
        res.close();
        return ID;
    }

    public int retrieveDimensions_rows(){
        int rows = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        Cursor res = db.rawQuery("select " + DIM_ROWS + " from " + DIM_TABLE_NAME + ";",null);
        res.moveToFirst();
        while (res.isAfterLast() == false){
            //arrayList.add(res.getInt(res.getColumnIndex(DIM_ROWS)));
            arrayList.add(res.getInt(res.getColumnIndexOrThrow(DIM_ROWS)));
            res.moveToNext();
        }
        if (arrayList.size()!=0)
            rows = arrayList.get(arrayList.size() - 1);
        res.close();
        return rows;
    }

    public int retrieveDimensions_cols(){
        int cols = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        Cursor res = db.rawQuery("select " + DIM_COLS + " from " + DIM_TABLE_NAME + ";",null);
        res.moveToFirst();
        while (res.isAfterLast() == false){
            //arrayList.add(res.getInt(res.getColumnIndex(DIM_COLS)));
            arrayList.add(res.getInt(res.getColumnIndexOrThrow(DIM_COLS)));
            res.moveToNext();
        }
        if (arrayList.size()!=0)
            cols = arrayList.get(arrayList.size() - 1);
        res.close();
        return cols;
    }

    public int retrieveDimensions_numberoflots(){
        int num = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        Cursor res = db.rawQuery("select " + DIM_NUM + " from " + DIM_TABLE_NAME + ";",null);
        res.moveToFirst();
        while (res.isAfterLast() == false){
            //arrayList.add(res.getInt(res.getColumnIndex(DIM_NUM)));
            arrayList.add(res.getInt(res.getColumnIndexOrThrow(DIM_NUM)));
            res.moveToNext();
        }
        if (arrayList.size()!=0)
            num = arrayList.get(arrayList.size() - 1);
        res.close();
        return num;
    }

    // Functions for Price-Per-Hour Table
    void addPrice(Integer amt){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(PRICE_AMT, amt);

        long result = db.insert(PRICE_TABLE_NAME, null, cv);
        if (result == -1){
            Toast.makeText(context, "Failed to insert to prices db", Toast.LENGTH_SHORT).show();
        }
    }

    public int retrievePrices(){
        int price = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        Cursor res = db.rawQuery("select " + PRICE_AMT + " from " + PRICE_TABLE_NAME + ";",null);
        res.moveToFirst();
        while (res.isAfterLast() == false){
            //arrayList.add(res.getInt(res.getColumnIndex(PRICE_AMT)));
            arrayList.add(res.getInt(res.getColumnIndexOrThrow(PRICE_AMT)));
            res.moveToNext();
        }
        if (arrayList.size()!=0)
            price = arrayList.get(arrayList.size() - 1);
        res.close();
        return price;
    }

    // Functions for Parking-Details Table
    void addParking(String cus_name, String cus_ph_no, String cus_vehc_no, String lot_no, String start_time){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(PARKING_CUSTOMER_NAME, cus_name);
        cv.put(PARKING_CUSTOMER_PH_NO, cus_ph_no);
        cv.put(PARKING_CUSTOMER_VEHICLE_NO, cus_vehc_no);
        cv.put(PARKING_LOT_NO, lot_no);
        cv.put(PARKING_START_TIME, start_time);

        long result = db.insert(PARKING_TABLE_NAME, null, cv);
        if (result == -1){
            Toast.makeText(context, "Failed to insert to parking db", Toast.LENGTH_SHORT).show();
        }
    }

    void deleteAllParking(){
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DELETE FROM " + PARKING_TABLE_NAME + ";");
        db.execSQL("DELETE FROM sqlite_sequence WHERE name = '" + PARKING_TABLE_NAME + "' ; ");

        db.close();
    }

    void addParkingEndTime(String lot_no, String end_time){
        SQLiteDatabase db = this.getWritableDatabase();
        /*
        ContentValues cv = new ContentValues();

        cv.put(PARKING_END_TIME, end_time);

        db.update(PARKING_TABLE_NAME, cv, "where parking_lot_no = "+lot_no, new String[]{PARKING_LOT_NO});
        */

        String query = "UPDATE "+ PARKING_TABLE_NAME + " SET " + PARKING_END_TIME + " = " + end_time + " WHERE " + PARKING_LOT_NO + " = " + lot_no +";";
        db.execSQL(query);
    }

    public Cursor readAllData_Parking(){
        String query = "SELECT * FROM " + PARKING_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query,null);
        }
        return cursor;
    }

    public int countParking(){
        int count = 0;
        String query = "SELECT * FROM " + PARKING_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query,null);
        }

        count = cursor.getCount();
        return count;
    }

    public int retrieveParking_id_individual(Integer lot_no){
        int id = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        Cursor res = db.rawQuery("select " + PARKING_CUSTOMER_ID + " from " + PARKING_TABLE_NAME + " where " + PARKING_LOT_NO + "=" + lot_no + ";",null);
        res.moveToFirst();
        while (res.isAfterLast() == false){
            //arrayList.add(res.getInt(res.getColumnIndex(DIM_ID)));
            arrayList.add(res.getInt(res.getColumnIndexOrThrow(PARKING_CUSTOMER_ID)));
            res.moveToNext();
        }
        if (arrayList.size()!=0)
            id = arrayList.get(0);
        res.close();
        return id;
    }

    public String retrieveParking_name_individual(Integer lot_no){
        String name = "";
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> arrayList = new ArrayList<String>();
        Cursor res = db.rawQuery("select " + PARKING_CUSTOMER_NAME + " from " + PARKING_TABLE_NAME + " where " + PARKING_LOT_NO + "=" + lot_no + ";",null);
        res.moveToFirst();
        while (res.isAfterLast() == false){
            //arrayList.add(res.getInt(res.getColumnIndex(DIM_ID)));
            arrayList.add(res.getString(res.getColumnIndexOrThrow(PARKING_CUSTOMER_NAME)));
            res.moveToNext();
        }
        if (arrayList.size()!=0)
            name = String.valueOf(arrayList.get(0));
        res.close();
        return name;
    }

    public String retrieveParking_phno_individual(Integer lot_no){
        String phno = "";
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> arrayList = new ArrayList<String>();
        Cursor res = db.rawQuery("select " + PARKING_CUSTOMER_PH_NO + " from " + PARKING_TABLE_NAME + " where " + PARKING_LOT_NO + "=" + lot_no + ";",null);
        res.moveToFirst();
        while (res.isAfterLast() == false){
            //arrayList.add(res.getInt(res.getColumnIndex(DIM_ID)));
            arrayList.add(res.getString(res.getColumnIndexOrThrow(PARKING_CUSTOMER_PH_NO)));
            res.moveToNext();
        }
        if (arrayList.size()!=0)
            phno = String.valueOf(arrayList.get(0));
        res.close();
        return phno;
    }

    public String retrieveParking_vehcno_individual(Integer lot_no){
        String vehcno = "";
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> arrayList = new ArrayList<String>();
        Cursor res = db.rawQuery("select " + PARKING_CUSTOMER_VEHICLE_NO + " from " + PARKING_TABLE_NAME + " where " + PARKING_LOT_NO + "=" + lot_no + ";",null);
        res.moveToFirst();
        while (res.isAfterLast() == false){
            //arrayList.add(res.getInt(res.getColumnIndex(DIM_ID)));
            arrayList.add(res.getString(res.getColumnIndexOrThrow(PARKING_CUSTOMER_VEHICLE_NO)));
            res.moveToNext();
        }
        if (arrayList.size()!=0)
            vehcno = String.valueOf(arrayList.get(0));
        res.close();
        return vehcno;
    }

    public String retrieveParking_starttime_individual(Integer lot_no){
        String time = "";
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> arrayList = new ArrayList<String>();
        Cursor res = db.rawQuery("select " + PARKING_START_TIME + " from " + PARKING_TABLE_NAME + " where " + PARKING_LOT_NO + "=" + lot_no + ";",null);
        res.moveToFirst();
        while (res.isAfterLast() == false){
            //arrayList.add(res.getInt(res.getColumnIndex(DIM_ID)));
            arrayList.add(res.getString(res.getColumnIndexOrThrow(PARKING_START_TIME)));
            res.moveToNext();
        }
        if (arrayList.size()!=0)
            time = String.valueOf(arrayList.get(0));
        res.close();
        return time;
    }

    void delete1Record(String row_id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(PARKING_TABLE_NAME,"parking_customer_id=?", new String[]{row_id});
        if (result == -1){
            Toast.makeText(context, "Error in deleting", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context, "Deleted successfully", Toast.LENGTH_SHORT).show();
        }
    }

    void modParkingName(String name, String cus_parking_id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(PARKING_CUSTOMER_NAME, name);

        long result = db.update(PARKING_TABLE_NAME, cv, "parking_customer_id=?", new String[]{cus_parking_id});

        if (result == -1){
            Toast.makeText(context, "Failed to Update", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context, "Update success", Toast.LENGTH_SHORT).show();
        }

    }

    void modParkingVehicleNumber(String vehc_no, String cus_parking_id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(PARKING_CUSTOMER_VEHICLE_NO, vehc_no);

        long result = db.update(PARKING_TABLE_NAME, cv, "parking_customer_id=?", new String[]{cus_parking_id});

        if (result == -1){
            Toast.makeText(context, "Failed to Update", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context, "Update success", Toast.LENGTH_SHORT).show();
        }
    }

    void modParkingPhoneNumber(String ph_no, String cus_parking_id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(PARKING_CUSTOMER_PH_NO, ph_no);

        long result = db.update(PARKING_TABLE_NAME, cv, "parking_customer_id=?", new String[]{cus_parking_id});

        if (result == -1){
            Toast.makeText(context, "Failed to Update", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context, "Update success", Toast.LENGTH_SHORT).show();
        }
    }

    // Functions for Master Table
    void addMaster(String master_name, String master_ph_no, String master_vehc_no, String master_lot_no, String master_start_time, String master_leave_time){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(MASTER_CUSTOMER_NAME, master_name);
        cv.put(MASTER_CUSTOMER_PH_NO, master_ph_no);
        cv.put(MASTER_CUSTOMER_VEHICLE_NO, master_vehc_no);
        cv.put(MASTER_LOT_NO, master_lot_no);
        cv.put(MASTER_START_TIME, master_start_time);
        cv.put(MASTER_LEAVE_TIME, master_leave_time);

        long result = db.insert(MASTER_TABLE_NAME, null, cv);
        if (result == -1){
            Toast.makeText(context, "Failed to insert to master db", Toast.LENGTH_SHORT).show();
        }
    }

    void addMaster_leaveTime(String master_id , String master_leave_time){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(MASTER_LEAVE_TIME, master_leave_time);

        long result = db.update(MASTER_TABLE_NAME, cv, "master_customer_id=?", new String[]{master_id});

        if (result == -1){
            Toast.makeText(context, "Failed to Update", Toast.LENGTH_SHORT).show();
        }
    }

    void deleteAllMaster(){
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DELETE FROM " + MASTER_TABLE_NAME + ";");
        db.execSQL("DELETE FROM sqlite_sequence WHERE name = '" + MASTER_TABLE_NAME + "' ; ");

        db.close();
    }

    public Cursor readAllData_Master(){
        String query = "SELECT * FROM " + MASTER_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query,null);
        }
        return cursor;
    }

    void modMasterName(String name, String master_cus_id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(MASTER_CUSTOMER_NAME, name);

        long result = db.update(MASTER_TABLE_NAME, cv, "master_customer_id=?", new String[]{master_cus_id});

        if (result == -1){
            Toast.makeText(context, "Failed to Update", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context, "Update success", Toast.LENGTH_SHORT).show();
        }
    }

    void modMasterVehicleNumber(String vehc_no, String master_cus_id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(MASTER_CUSTOMER_VEHICLE_NO, vehc_no);

        long result = db.update(MASTER_TABLE_NAME, cv, "master_customer_id=?", new String[]{master_cus_id});

        if (result == -1){
            Toast.makeText(context, "Failed to Update", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context, "Update success", Toast.LENGTH_SHORT).show();
        }
    }

    void modMasterPhoneNumber(String ph_no, String master_cus_id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(MASTER_CUSTOMER_PH_NO, ph_no);

        long result = db.update(MASTER_TABLE_NAME, cv, "master_customer_id=?", new String[]{master_cus_id});

        if (result == -1){
            Toast.makeText(context, "Failed to Update", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context, "Update success", Toast.LENGTH_SHORT).show();
        }
    }

    // Functions for Free-Lots Table
    void addFreeLot(Integer lotno, Integer status){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(FREELOTS_LOT_NO, lotno);
        cv.put(FREELOTS_STATUS, status);

        long result = db.insert(FREELOTS_TABLE_NAME, null, cv);
        if (result == -1){
            Toast.makeText(context, "Failed to insert to free-lots db", Toast.LENGTH_SHORT).show();
        }
    }

    void deleteAllFreeLots(){
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DELETE FROM " + FREELOTS_TABLE_NAME + ";");
        db.execSQL("DELETE FROM sqlite_sequence WHERE name = '" + FREELOTS_TABLE_NAME + "' ; ");

        db.close();
    }

    public int retrieveFreeLot_id(){
        int id = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        Cursor res = db.rawQuery("select " + FREELOTS_ID + " from " + FREELOTS_TABLE_NAME + " where " + FREELOTS_STATUS + "=0;",null);
        if( res !=null && res.moveToFirst()) {
            while (res.isAfterLast() == false) {
                //arrayList.add(res.getInt(res.getColumnIndex(FREELOTS_ID)));
                arrayList.add(res.getInt(res.getColumnIndexOrThrow(FREELOTS_ID)));
                res.moveToNext();
            }
            if (arrayList.size() != 0)
                id = arrayList.get(0);
            res.close();
        }
        return id;
    }

    public int retrieveFreeLot_status(){
        int status = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        Cursor res = db.rawQuery("select " + FREELOTS_STATUS + " from " + FREELOTS_TABLE_NAME + " where " + FREELOTS_STATUS + "=0;",null);
        res.moveToFirst();
        while (res.isAfterLast() == false){
            //arrayList.add(res.getInt(res.getColumnIndex(DIM_ID)));
            arrayList.add(res.getInt(res.getColumnIndexOrThrow(FREELOTS_STATUS)));
            res.moveToNext();
        }
        if (arrayList.size()!=0)
            status = arrayList.get(0);
        res.close();
        return status;
    }

    public int retrieveFreeLot_status_individual(Integer lot_no){
        int status = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Integer> arrayList = new ArrayList<Integer>();
        Cursor res = db.rawQuery("select " + FREELOTS_STATUS + " from " + FREELOTS_TABLE_NAME + " where " + FREELOTS_ID + "=" + lot_no + ";",null);
        res.moveToFirst();
        while (res.isAfterLast() == false){
            //arrayList.add(res.getInt(res.getColumnIndex(DIM_ID)));
            arrayList.add(res.getInt(res.getColumnIndexOrThrow(FREELOTS_STATUS)));
            res.moveToNext();
        }
        if (arrayList.size()!=0)
            status = arrayList.get(0);
        res.close();
        return status;
    }

    void modFreeLotStatus_1(String lot_no){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(FREELOTS_STATUS, 1);

        long result = db.update(FREELOTS_TABLE_NAME, cv, "fl_id=?", new String[]{lot_no});

        if (result == -1){
            Toast.makeText(context, "Failed to Update", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context, "Update success", Toast.LENGTH_SHORT).show();
        }
    }

    void modFreeLotStatus_0(String lot_no){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(FREELOTS_STATUS, 0);

        long result = db.update(FREELOTS_TABLE_NAME, cv, "fl_id=?", new String[]{lot_no});

        if (result == -1){
            Toast.makeText(context, "Failed to Update", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context, "Update success", Toast.LENGTH_SHORT).show();
        }
    }

    // Functions for UPI Table
    void addUPI(String upi_id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(UPI_ID, upi_id);

        long result = db.insert(UPI_TABLE_NAME, null, cv);
        if (result == -1){
            Toast.makeText(context, "Failed to insert to UPI db", Toast.LENGTH_SHORT).show();
        }
    }

    public String retrieveUPIID(){
        String upiid = "";
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> arrayList = new ArrayList<String>();
        Cursor res = db.rawQuery("select " + UPI_ID + " from " + UPI_TABLE_NAME + ";",null);
        res.moveToFirst();
        while (res.isAfterLast() == false){
            //arrayList.add(res.getInt(res.getColumnIndex(PRICE_AMT)));
            arrayList.add(res.getString(res.getColumnIndexOrThrow(UPI_ID)));
            res.moveToNext();
        }
        if (arrayList.size()!=0)
            upiid = arrayList.get(arrayList.size() - 1);
        res.close();
        return upiid;
    }

    // Functions for Price-Accumulation Table
    void addPriceAccumulation(String date, String day, String value){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(PRICE_ACCUMULATION_DATE, date);
        cv.put(PRICE_ACCUMULATION_DAY, day);
        cv.put(PRICE_ACCUMULATION_TOTALPRICE, value);

        long result = db.insert(PRICE_ACCUMULATION_TABLE_NAME, null, cv);
        if (result == -1){
            Toast.makeText(context, "Failed to insert to price-accumulation db", Toast.LENGTH_SHORT).show();
        }
    }

    public String retrievePriceAccumulation_TotalPrice(){
        String price = "";
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> arrayList = new ArrayList<String>();
        Cursor res = db.rawQuery("select " + PRICE_ACCUMULATION_TOTALPRICE + " from " + PRICE_ACCUMULATION_TABLE_NAME + ";",null);
        res.moveToFirst();
        while (res.isAfterLast() == false){
            //arrayList.add(res.getInt(res.getColumnIndex(PRICE_AMT)));
            arrayList.add(res.getString(res.getColumnIndexOrThrow(PRICE_ACCUMULATION_TOTALPRICE)));
            res.moveToNext();
        }
        if (arrayList.size()!=0)
            price = arrayList.get(arrayList.size() - 1);
        res.close();
        return price;
    }

    public String retrievePriceAccumulation_Date(){
        String date = "";
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> arrayList = new ArrayList<String>();
        Cursor res = db.rawQuery("select " + PRICE_ACCUMULATION_DATE + " from " + PRICE_ACCUMULATION_TABLE_NAME + ";",null);
        res.moveToFirst();
        while (res.isAfterLast() == false){
            //arrayList.add(res.getInt(res.getColumnIndex(PRICE_AMT)));
            arrayList.add(res.getString(res.getColumnIndexOrThrow(PRICE_ACCUMULATION_DATE)));
            res.moveToNext();
        }
        if (arrayList.size()!=0)
            date = arrayList.get(arrayList.size() - 1);
        res.close();
        return date;
    }

    void modPriceAccumulation_TotalPrice(String price, String date){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(PRICE_ACCUMULATION_TOTALPRICE, price);

        long result = db.update(PRICE_ACCUMULATION_TABLE_NAME, cv, "price_accumulation_date=?", new String[]{date});

        if (result == -1){
            Toast.makeText(context, "Failed to Update", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context, "Update success", Toast.LENGTH_SHORT).show();
        }
    }

    public Cursor readAll_DATE_PRICE_Data_priceAccumulation(){
        String query = "SELECT " + PRICE_ACCUMULATION_DATE  + " , " + PRICE_ACCUMULATION_TOTALPRICE + " FROM " + PRICE_ACCUMULATION_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query,null);
        }
        return cursor;
    }

    public Cursor readAll_DAY_PRICE_Data_priceAccumulation(){
        String query = "SELECT " + PRICE_ACCUMULATION_DAY  + " , " + PRICE_ACCUMULATION_TOTALPRICE + " FROM " + PRICE_ACCUMULATION_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query,null);
        }
        return cursor;
    }
}
