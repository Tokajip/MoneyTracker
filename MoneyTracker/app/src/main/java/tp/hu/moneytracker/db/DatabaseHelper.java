package tp.hu.moneytracker.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Peti on 2015.03.24..
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context, String name) {
        super(context, name, null, DbConstants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DbConstants.DATABASE_CREATE_ALL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DbConstants.DATABASE_DROP_ALL);
        db.execSQL(DbConstants.DATABASE_CREATE_ALL);
    }
    public void delete(SQLiteDatabase db){
        db.execSQL(DbConstants.DATABASE_DROP_ALL);
    }
}
