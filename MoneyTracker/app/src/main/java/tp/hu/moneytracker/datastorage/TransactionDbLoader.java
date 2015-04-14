package tp.hu.moneytracker.datastorage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import tp.hu.moneytracker.data.Transaction;
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
        values.put(DbConstants.Transaction.KEY_PRICE, transition.getPrice());
        values.put(DbConstants.Transaction.KEY_CATEGORY, transition.getCategory());

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
        values.put(DbConstants.Transaction.KEY_CATEGORY, newTransaction.getCategory());
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
                        DbConstants.Transaction.KEY_PRICE,
                        DbConstants.Transaction.KEY_CATEGORY
                }, null, null, null, null, DbConstants.Transaction.KEY_DATE+" DESC");
    }

    public Cursor fetchByTitle(String parameter) {
        Cursor c = mDb.query(DbConstants.Transaction.DATABASE_TABLE,
                new String[]{
                        DbConstants.Transaction.KEY_ROWID,
                        DbConstants.Transaction.KEY_TITLE,
                        DbConstants.Transaction.KEY_DATE,
                        DbConstants.Transaction.KEY_PRICE,
                        DbConstants.Transaction.KEY_CATEGORY
                }, DbConstants.Transaction.KEY_TITLE + " like ?",
                new String[]{parameter}, null, null, DbConstants.Transaction.KEY_DATE+" DESC"
        );
        return c;
    }
    public Cursor fetchIncomes() {
        Cursor c = mDb.query(DbConstants.Transaction.DATABASE_TABLE,
                new String[]{
                        DbConstants.Transaction.KEY_ROWID,
                        DbConstants.Transaction.KEY_TITLE,
                        DbConstants.Transaction.KEY_DATE,
                        DbConstants.Transaction.KEY_PRICE,
                        DbConstants.Transaction.KEY_CATEGORY
                }, DbConstants.Transaction.KEY_PRICE + "> 0",
                null, null, null, DbConstants.Transaction.KEY_DATE+" DESC"
        );
        return c;
    }
    public Cursor fetchOutgoes() {
        Cursor c = mDb.query(DbConstants.Transaction.DATABASE_TABLE,
                new String[]{
                        DbConstants.Transaction.KEY_ROWID,
                        DbConstants.Transaction.KEY_TITLE,
                        DbConstants.Transaction.KEY_DATE,
                        DbConstants.Transaction.KEY_PRICE,
                        DbConstants.Transaction.KEY_CATEGORY
                }, DbConstants.Transaction.KEY_PRICE + "< 0",
                null, null, null, DbConstants.Transaction.KEY_DATE+" DESC"
        );
        return c;
    }
    // egy Transaction lekérése

    public tp.hu.moneytracker.data.Transaction fetchTransaction(long rowId) {
        // a Transaction-re mutato cursor
        Cursor c = mDb.query(
                DbConstants.Transaction.DATABASE_TABLE,
                new String[]{
                        DbConstants.Transaction.KEY_ROWID,
                        DbConstants.Transaction.KEY_TITLE,
                        DbConstants.Transaction.KEY_DATE,
                        DbConstants.Transaction.KEY_PRICE,
                        DbConstants.Transaction.KEY_CATEGORY
                }, DbConstants.Transaction.KEY_ROWID + "=?",
                new String[]{"" + rowId}, null, null, DbConstants.Transaction.KEY_DATE+" DESC");
        // ha van rekord amire a Cursor mutat
        if (c.moveToFirst())
            return getTransationByCursor(c);
        // egyebkent null-al terunk vissza
        return null;
    }

    public static tp.hu.moneytracker.data.Transaction getTransationByCursor(Cursor c) {
        return new tp.hu.moneytracker.data.Transaction(
                c.getString(c.getColumnIndex(DbConstants.Transaction.KEY_TITLE)), // title
                c.getLong(c.getColumnIndex(DbConstants.Transaction.KEY_DATE)), // date
                c.getInt(c.getColumnIndex(DbConstants.Transaction.KEY_PRICE)), // price
                c.getString(c.getColumnIndex(DbConstants.Transaction.KEY_CATEGORY)) // category
        );
    }

    public void deleteAll() {
        dbHelper.delete(mDb);
    }

    public Cursor fetchByCategory(String parameter) {
            Cursor c = mDb.query(DbConstants.Transaction.DATABASE_TABLE,
                    new String[]{
                            DbConstants.Transaction.KEY_ROWID,
                            DbConstants.Transaction.KEY_TITLE,
                            DbConstants.Transaction.KEY_DATE,
                            DbConstants.Transaction.KEY_PRICE,
                            DbConstants.Transaction.KEY_CATEGORY
                    }, DbConstants.Transaction.KEY_CATEGORY + " like ?",
                    new String[]{parameter}, null, null, DbConstants.Transaction.KEY_DATE+" DESC"
            );
            return c;
    }
    public boolean update(Transaction t){
        ContentValues values = new ContentValues();
        values.put(DbConstants.Transaction.KEY_TITLE, t.getTitle());
        values.put(DbConstants.Transaction.KEY_DATE, t.getDate());
        values.put(DbConstants.Transaction.KEY_PRICE, t.getPrice());
        values.put(DbConstants.Transaction.KEY_CATEGORY, t.getCategory());
        return mDb.update(
                DbConstants.Transaction.DATABASE_TABLE,
                values,
                DbConstants.Transaction.KEY_DATE + "=" + t.getDate(),
                null) > 0;
    }

    public Cursor fetchByDate(String mindate,String  maxdate) {
        Cursor c = mDb.query(DbConstants.Transaction.DATABASE_TABLE,
                new String[]{
                        DbConstants.Transaction.KEY_ROWID,
                        DbConstants.Transaction.KEY_TITLE,
                        DbConstants.Transaction.KEY_DATE,
                        DbConstants.Transaction.KEY_PRICE,
                        DbConstants.Transaction.KEY_CATEGORY
                }, DbConstants.Transaction.KEY_DATE + " BETWEEN ? and ?",
                new String[]{mindate,maxdate}, null, null, DbConstants.Transaction.KEY_DATE
        );
        return c;
    }
}