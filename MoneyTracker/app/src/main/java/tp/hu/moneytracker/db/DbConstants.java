package tp.hu.moneytracker.db;

/**
 * Created by Peti on 2015.03.24..
 */
public class DbConstants {

    // Broadcast Action, amely az adatbazis modosulasat jelzi
//    public static final String ACTION_DATABASE_CHANGED = "hu.bute.daai.amorg.examples.DATABASE_CHANGED";
    public static final String ACTION_DATABASE_CHANGED = "tp.hu.moneytracker.DATABASE_CHANGED";

    // fajlnev, amiben az adatbazis lesz
    public static final String DATABASE_NAME = "data.db";
    // verzioszam
    public static final int DATABASE_VERSION = 1;
    // osszes belso osztaly DATABASE_CREATE szkriptje osszefuzve
    public static String DATABASE_CREATE_ALL = Transaction.DATABASE_CREATE;
    // osszes belso osztaly DATABASE_DROP szkriptje osszefuzve
    public static String DATABASE_DROP_ALL = Transaction.DATABASE_DROP;

    /* Todo osztaly DB konstansai */
    public static class Transaction {
        // tabla neve
        public static final String DATABASE_TABLE = "transantion";
        // oszlopnevek
        public static final String KEY_ROWID = "_id";
        public static final String KEY_TITLE = "title";
        public static final String KEY_DATE = "date";
        public static final String KEY_PRICE= "price";
        // sema letrehozo szkript
        public static final String DATABASE_CREATE =
                "create table if not exists "+DATABASE_TABLE+" ( "
                        + KEY_ROWID +" integer primary key autoincrement, "
                        + KEY_TITLE + " text not null, "
                        + KEY_DATE + " long, "
                        + KEY_PRICE +" integer"
                        + "); ";
        // sema torlo szkript
        public static final String DATABASE_DROP =
                "drop table if exists " + DATABASE_TABLE + "; ";
    }
}
