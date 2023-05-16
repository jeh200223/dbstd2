package com.example.sqlite2;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import androidx.annotation.Nullable;

public class DB extends SQLiteOpenHelper {
    private Context context;
    String TBLName = "products";

    public DB(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS " + TBLName + "(CODE TEXT PRIMARY KEY, " + "name TEXT, price FLOAT);";
        db.execSQL(sql);
    }

    public void deleteTable() {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "DROP TABLE IF EXISTS " + TBLName + ";";
        database.execSQL(sql);
        database.close();
    }

    public long insertData(String code, String name, float price) {
        SQLiteDatabase database = getWritableDatabase();
        database.beginTransaction();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("CODE", code);
            contentValues.put("name", name);
            contentValues.put("price", price);

            long insertedRowId = database.insertWithOnConflict(TBLName, null, contentValues, SQLiteDatabase.CONFLICT_NONE);
            database.setTransactionSuccessful();
            return insertedRowId;
        } catch (Exception e) {
            return -1L;
        } finally {
            database.endTransaction();
        }

    }

    public Cursor searchData() {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "SELECT * FROM " + TBLName + ";";
        Cursor cursor = database.rawQuery(sql, null);
        return cursor;
    }

    public Cursor keywordSearchData(String keyword) {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "SELECT * FROM " + TBLName + " WHERE name = '" + keyword + "';";
        Cursor cursor = database.rawQuery(sql, null);
        return cursor;
    }

    public void deleteData(int index) {
        SQLiteDatabase database = getWritableDatabase();
        database.beginTransaction();
        try {
            String sql = "DELETE FROM " + TBLName + " WHERE CODE = ?";
            String[] whereArgs = new String[]{"CD-010" + index};
            database.execSQL(sql, whereArgs);
            database.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            database.endTransaction();
        }

    }

    public void deleteAll() {
        SQLiteDatabase database = getWritableDatabase();
        database.beginTransaction();
        try {
            String sql = "DELETE FROM " + TBLName;
            database.execSQL(sql);
            database.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            database.endTransaction();
        }
    }

    @SuppressLint("Range")
    public void onUpdate(float price) {
        SQLiteDatabase database = getWritableDatabase();
        database.beginTransaction();
        SQLiteStatement statement = null;
        try {
            String sql = "INSERT OR REPLACE INTO " + TBLName + " (CODE, name, price) VALUES (?, ?, ?)";
            statement = database.compileStatement(sql);

            String selectSql = "SELECT CODE, name, price FROM " + TBLName;
            Cursor cursor = database.rawQuery(selectSql, null);

            while (cursor.moveToNext()) {
                String code = cursor.getString(cursor.getColumnIndex("CODE"));
                String name = cursor.getString(cursor.getColumnIndex("name"));

                statement.bindString(1, code);
                statement.bindString(2, name);
                statement.bindDouble(3, Double.parseDouble(String.valueOf(price)));
                statement.execute();
                statement.clearBindings();
            }

            cursor.close();
            database.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (statement != null) {
                statement.close();
            }
            database.endTransaction();
        }

    }

    public void onDelete(String keyword) {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "DELETE FROM " + TBLName + " WHERE name = '" + keyword + "';";
        database.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + TBLName + ";";
        db.execSQL(sql);
        onCreate(db);
    }
}