package com.tsj2023.todobucketlist.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class  DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "myapp.db";
    private static final int DATABASE_VERSION = 1;

    // 생성자
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // 데이터베이스가 최초로 생성될 때 호출됩니다.
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 테이블을 생성하는 SQL 쿼리를 실행합니다.
        String createTableQuery = "CREATE TABLE IF NOT EXISTS todo_bucket_list " +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT, msg TEXT, checked INTEGER, category TEXT)";
        db.execSQL(createTableQuery);
    }

    // 데이터베이스의 버전이 변경될 때 호출됩니다.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 데이터베이스 스키마 업그레이드를 처리하는 코드를 여기에 작성합니다.
    }
}
