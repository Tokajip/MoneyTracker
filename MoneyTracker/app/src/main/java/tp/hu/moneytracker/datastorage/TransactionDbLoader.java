package tp.hu.moneytracker.datastorage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import tp.hu.moneytracker.db.DatabaseHelper;
import tp.hu.moneytracker.db.DbConstants;

/**
 * Created by Peti on 2015.03.24..
 */
public class TransactionDbLoader {

    private Context ctx;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase mDb;

    public TransactionDbLoader(Context ctx) {
        this.ctx = ctx;
    }

    public void open() throws SQLException {
        // DatabaseHelper objektum
        dbHelper = new DatabaseHelper(
                ctx, DbConstants.DATABASE_NAME);
        // adatbázis objektum
        mDb = dbHelper.getWritableDatabase();
        // ha nincs még séma, akkor létrehozzuk
        dbHelper.onCreate(mDb);
    }

    public void close() {
        dbHelper.close();
    }

    // CRUD és egyéb metódusok
    // INSERT
    public long createTransition(tp.hu.moneytracker.data.Transaction transition) {
        ContentValues values = new ContentValues();
        values.put(DbConstants.Transaction.KEY_TITLE, transition.getTitle());
        values.put(DbConstants.Transaction.KEY_DATE, transition.getDate());
        values.put(DbConstants.Transaction.KEY_PRICE, (Integer) transition.getPrice());

        return mDb.insert(DbConstants.Transaction.DATABASE_TABLE, null, values);
    }

    // DELETE
    public boolean deleteTransition(long rowId) {
        return mDb.delete(
                DbConstants.Transaction.DATABASE_TABLE,
                DbConstants.Transaction.KEY_ROWID + "=" + rowId,
                null) > 0;
    }

    // UPDATE
    public boolean updateTransition(long rowId, tp.hu.moneytracker.data.Transaction newTransaction) {
        ContentValues values = new ContentValues();
        values.put(DbConstants.Transaction.KEY_TITLE, newTransaction.getTitle());
        values.put(DbConstants.Transaction.KEY_DATE, newTransaction.getDate());
        values.put(DbConstants.Transaction.KEY_PRICE, newTransaction.getPrice());
        return mDb.update(
                DbConstants.Transaction.DATABASE_TABLE,
                values,
                DbConstants.Transaction.KEY_ROWID + "=" + rowId,
                null) > 0;
    }

    // minden Transaction lekérése
    public Cursor fetchAll() {
        // cursor minden rekordra (where = null)
        return mDb.query(
                DbConstants.Transaction.DATABASE_TABLE,
                new String[]{
                        DbConstants.Transaction.KEY_ROWID,
                        DbConstants.Transaction.KEY_TITLE,
                        DbConstants.Transaction.KEY_DATE,
                        DbConstants.Transaction.KEY_PRICE
                }, null, null, null, null, DbConstants.Transaction.KEY_TITLE);
    }

    // egy Transaction lekérése
    public tp.hu.moneytracker.data.Transaction fetchTodo(long rowId) {
        // a Transaction-re mutato cursor
        Cursor c = mDb.query(
                DbConstants.Transaction.DATABASE_TABLE,
                new String[]{
                        DbConstants.Transaction.KEY_ROWID,
                        DbConstants.Transaction.KEY_TITLE,
                        DbConstants.Transaction.KEY_DATE,
                        DbConstants.Transaction.KEY_PRICE
                }, DbConstants.Transaction.KEY_ROWID + "=",
                new String[]{"" + rowId}, null, null, DbConstants.Transaction.KEY_TITLE);
        // ha van rekord amire a Cursor mutat
        if (c.moveToFirst())
            return getTransationByCursor(c);
        // egyebkent null-al terunk vissza
        return null;
    }

    public static tp.hu.moneytracker.data.Transaction getTransationByCursor(Cursor c) {
        return new tp.hu.moneytracker.data.Transaction(
                c.getString(c.getColumnIndex(DbConstants.Transaction.KEY_TITLE)), // title
                c.getString(c.getColumnIndex(DbConstants.Transaction.KEY_DATE)), // date
                c.getInt(c.getColumnIndex(DbConstants.Transaction.KEY_PRICE)) // price
        );
    }

    public void deleteAll() {
        dbHelper.delete(mDb);
    }
}
