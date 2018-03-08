package testview;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.InputStream;

/**
 * Created by ASUS on 2018/2/26.
 */

public class ShengshiquDBHelper extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 1;
    // 数据库名
    public static final String DATABASE_NAME = "pca2.db";
    // 个人信息数据表名，
    public static final String TABLE_NAME = "area";
    // 建表语句-province
    public static final String CREATE_Tables = "create table "
            + "if not exists "
            + "province"
            + " (_id INTEGER NOT NULL PRIMARY KEY,"
            + " name varchar(200),"
            + " pid INTEGER,"
            + " levelid INTEGER"
            + ")";
    // 建表语句-city
    public static final String CREATE_Tables2 = "create table "
            + "if not exists "
            + "city"
            + " (_id INTEGER NOT NULL PRIMARY KEY,"
            + " name varchar(200),"
            + " pid INTEGER,"
            + " levelid INTEGER"
            + ")";
    // 建表语句-area
    public static final String CREATE_Tables3 = "create table "
            + "if not exists "
            + "area"
            + " (_id INTEGER NOT NULL PRIMARY KEY,"
            + " name varchar(200),"
            + " pid INTEGER,"
            + " levelid INTEGER"
            + ")";
    public ShengshiquDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_Tables);
        db.execSQL(CREATE_Tables2);
        db.execSQL(CREATE_Tables3);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        onCreate(db);
    }

    public boolean deleteDatabase(Context context) {
        return context.deleteDatabase(DATABASE_NAME);
    }


}
