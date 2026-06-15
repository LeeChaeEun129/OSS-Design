package com.example.subwise;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "subwise.db";
    private static final int DB_VERSION = 2;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "email TEXT, " +
                "password TEXT, " +
                "job TEXT)");

        db.execSQL("CREATE TABLE subscriptions (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "serviceName TEXT, " +
                "price INTEGER, " +
                "billingDate TEXT, " +
                "category TEXT, " +
                "partyName TEXT, " +
                "partyCount INTEGER, " +
                "isEssential INTEGER, " +
                "isShared INTEGER)");
    }

    // ✅ 로그인 검증
    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE email=? AND password=?",
                new String[]{email, password});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // ✅ 회원가입
    public boolean insertUser(String email, String password, String job) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("email", email);
        values.put("password", password);
        values.put("job", job);
        long result = db.insert("users", null, values);
        return result != -1;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS subscriptions");
        onCreate(db);
    }

    // ✅ 전체 구독 목록 조회
    public List<Subscription> getAllSubscriptions() {
        List<Subscription> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM subscriptions", null);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String serviceName = cursor.getString(cursor.getColumnIndexOrThrow("serviceName"));
            int price = cursor.getInt(cursor.getColumnIndexOrThrow("price"));
            String billingDate = cursor.getString(cursor.getColumnIndexOrThrow("billingDate"));
            String category = cursor.getString(cursor.getColumnIndexOrThrow("category"));
            String partyName = cursor.getString(cursor.getColumnIndexOrThrow("partyName"));
            int partyCount = cursor.getInt(cursor.getColumnIndexOrThrow("partyCount"));
            boolean essential = cursor.getInt(cursor.getColumnIndexOrThrow("isEssential")) == 1;
            boolean shared = cursor.getInt(cursor.getColumnIndexOrThrow("isShared")) == 1;

            // ✅ 9개 인자만 넘기기
            Subscription sub = new Subscription(id, serviceName, price, billingDate,
                    category, partyName, partyCount, essential, shared);
            list.add(sub);
        }
        cursor.close();
        return list;
    }

    // ✅ 구독 추가
    public void insertSubscription(String serviceName, int price, String billingDate,
                                   String category, String partyName, int partyCount,
                                   boolean isEssential, boolean isShared) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("serviceName", serviceName);
        cv.put("price", price);
        cv.put("billingDate", billingDate);
        cv.put("category", category);
        cv.put("partyName", partyName);
        cv.put("partyCount", partyCount);
        cv.put("isEssential", isEssential ? 1 : 0);
        cv.put("isShared", isShared ? 1 : 0);
        db.insert("subscriptions", null, cv);
    }

    // ✅ 구독 수정
    public void updateSubscription(int id, String serviceName, int price, String billingDate,
                                   String category, String partyName, int partyCount,
                                   boolean isEssential, boolean isShared) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("serviceName", serviceName);
        cv.put("price", price);
        cv.put("billingDate", billingDate);
        cv.put("category", category);
        cv.put("partyName", partyName);
        cv.put("partyCount", partyCount);
        cv.put("isEssential", isEssential ? 1 : 0);
        cv.put("isShared", isShared ? 1 : 0);
        db.update("subscriptions", cv, "id=?", new String[]{String.valueOf(id)});
    }

    // ✅ 특정 구독 조회
    public Subscription getSubscriptionById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM subscriptions WHERE id=?", new String[]{String.valueOf(id)});
        Subscription sub = null;
        if (cursor.moveToFirst()) {
            sub = new Subscription(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("serviceName")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("price")),
                    cursor.getString(cursor.getColumnIndexOrThrow("billingDate")),
                    cursor.getString(cursor.getColumnIndexOrThrow("category")),
                    cursor.getString(cursor.getColumnIndexOrThrow("partyName")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("partyCount")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("isEssential")) == 1,
                    cursor.getInt(cursor.getColumnIndexOrThrow("isShared")) == 1
            );
        }
        cursor.close();
        return sub;
    }

    // ✅ 구독 삭제
    public void deleteSubscription(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("subscriptions", "id=?", new String[]{String.valueOf(id)});
    }

    public Subscription getSubscriptionByDate(String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("subscriptions",
                null,
                "billingDate = ?",
                new String[]{date},
                null, null, null);

        if (cursor.moveToFirst()) {
            Subscription sub = new Subscription(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    cursor.getString(cursor.getColumnIndexOrThrow("serviceName")), // ✅ 수정
                    cursor.getInt(cursor.getColumnIndexOrThrow("price")),          // ✅ 수정
                    cursor.getString(cursor.getColumnIndexOrThrow("billingDate")),
                    cursor.getString(cursor.getColumnIndexOrThrow("category")),
                    cursor.getString(cursor.getColumnIndexOrThrow("partyName")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("partyCount")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("isEssential")) == 1,
                    cursor.getInt(cursor.getColumnIndexOrThrow("isShared")) == 1
            );
            cursor.close();
            return sub;
        }
        return null;
    }
}
