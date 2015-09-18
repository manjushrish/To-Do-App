public class DBHelper {
        private static final String LOGTAG = "DBHelper";
        private static final String DATABASE_NAME = "TasksList.db";
        private static final int DATABASE_VERSION = 4;
        private static final String TABLE_NAME = "TasksData";



        public static String KEY_TITLE = "title";
        public static String KEY_DATE = "createddate";
        public static String KEY_SHORTD = "shortd";
        public static String KEY_LONGD = "longd";
        public static String KEY_ID = "_id";
        public static String KEY_TAG = "tag";




        public static final int COLUMN_TITLE = 1;
        public static final int COLUMN_DATE = 2;
        public static final int COLUMN_SHORTD = 3;
        public static final int COLUMN_LONGD = 4;
        public static final int COLUMN_ID = 0;
        public static final int COLUMN_TAG = 5;

        private Context context;
        private SQLiteDatabase db;
        private SQLiteStatement insertStmt;
        private static final String INSERT = "INSERT INTO " + TABLE_NAME + "(" + KEY_TITLE + ", " +
                KEY_DATE + ", " +
                KEY_SHORTD + ", " + KEY_LONGD + ", "+ KEY_TAG + ") values (?, ?, ?, ?, ?)";

        public DBHelper(Context context) throws Exception {
            this.context = context;
            try {
                OpenHelper openHelper = new OpenHelper(this.context); // Open a database for reading and writing

                db = openHelper.getWritableDatabase();
                insertStmt = db.compileStatement(INSERT);
            } catch (Exception e) {
                Log.e(LOGTAG, " DBHelper constructor: could not get database " + e);
                throw (e);
            }
        }

        private static class OpenHelper extends SQLiteOpenHelper {
            private static final String LOGTAG = "OpenHelper";
            private static final String CREATE_TABLE =
                    "CREATE TABLE " +
                            TABLE_NAME +
                            " (" +
                            KEY_ID + " integer primary key autoincrement, " +
                            KEY_TITLE + " TEXT, " +
                            KEY_DATE + " TEXT, " +
                            KEY_SHORTD + " TEXT, " +
                            KEY_LONGD + " TEXT, "+
                            KEY_TAG + " TEXT);";

            OpenHelper(Context context) {
                super(context, DATABASE_NAME, null, DATABASE_VERSION);
            }

            public void onCreate(SQLiteDatabase db) {
                Log.d(LOGTAG, " onCreate");

                try {

                    db.execSQL(CREATE_TABLE);

                } catch (Exception e) {
                    Log.e(LOGTAG, " onCreate@: Could not create SQL database: " + e);
                }
            }

            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                Log.w(LOGTAG, "Upgrading database, this will drop tables and recreate.");
                try {
                    db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
                    onCreate(db);
                } catch (Exception e) {
                    Log.e(LOGTAG, " onUpgrade: Could not update SQL database: " + e);
                }
            }
        }


        public long insert(Task taskInfo) {
            insertStmt.bindString(COLUMN_TITLE, taskInfo.getTitle());
            insertStmt.bindString(COLUMN_DATE, taskInfo.getDate());
            insertStmt.bindString(COLUMN_SHORTD, taskInfo.getShortd());
            insertStmt.bindString(COLUMN_LONGD, taskInfo.getLongd());
            insertStmt.bindString(COLUMN_TAG,taskInfo.getTag());
            long value = -1;
            try {
                value = insertStmt.executeInsert();
            } catch (Exception e) {
                Log.e(LOGTAG, " executeInsert problem: " + e);
            }
            Log.d(LOGTAG, "value=" + value);
            return value;
        }

        public void deleteAll() {
            db.delete(TABLE_NAME, null, null); }
        // delete a row in the database
        public boolean deleteRecord(long rowId) {
            return db.delete(TABLE_NAME, KEY_ID + "=" + rowId, null) > 0;
        }
        public List<Task> selectAll()
        {
            List<Task> list = new ArrayList<Task>();
            Cursor cursor = db.query(TABLE_NAME,
                    new String[] { KEY_ID, KEY_TITLE, KEY_DATE, KEY_SHORTD, KEY_LONGD,KEY_TAG }, null, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do
                {
                    Task taskInfo = new Task();
                    taskInfo.setTitle(cursor.getString(COLUMN_TITLE));
                    taskInfo.setDate(cursor.getString(COLUMN_DATE));
                    taskInfo.setShortd(cursor.getString(COLUMN_SHORTD));
                    taskInfo.setLongd(cursor.getString(COLUMN_LONGD));
                    taskInfo.setId(cursor.getLong(COLUMN_ID));
                    taskInfo.setTag(cursor.getString(COLUMN_TAG));
                    list.add(taskInfo);
                }
                while (cursor.moveToNext()); }
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            return list;
        }


}
