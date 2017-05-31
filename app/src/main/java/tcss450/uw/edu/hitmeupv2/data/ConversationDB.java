package tcss450.uw.edu.hitmeupv2.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import tcss450.uw.edu.hitmeupv2.R;
import tcss450.uw.edu.hitmeupv2.WebService.Conversation;

/**
 * Created by Shema on 5/29/2017.
 *
 * Create SQLite DB to hold user information.
 */

public class ConversationDB {
    /** DB version */
    public static final int DB_VERSION = 1;
    /** The name of our db */
    public static final String DB_NAME = "Conversation.db";
    /** The name of our table */
    private static final String CONVO_TABLE = "Conversation";
    /** Helper */
    private CourseDBHelper mCourseDBHelper;
    /** The sqlLite db */
    private SQLiteDatabase mSQLiteDatabase;

    /**
     * Constructor. Sets the instance fields,
     * gets the db.
     * @param context the activity
     */
    public ConversationDB(Context context) {
        mCourseDBHelper = new CourseDBHelper(context, DB_NAME, null, DB_VERSION);
        mSQLiteDatabase = mCourseDBHelper.getWritableDatabase();
    }

    /**
     * Inserts a conversation into the DB.
     * @param myId the curr user id
     * @param senderId the last sender id
     * @param recipientId the recipient of the last message
     * @param message the message shown on homepage
     * @param username the username shown on list in homepage
     * @param profileImgPath the friends profile pic
     * @return a boolean
     */
    public boolean insertConvo(String myId, String senderId, String recipientId, String message, String username) {
        ContentValues cv = new ContentValues();
        cv.put("id", myId);
        cv.put("senderId", senderId);
        cv.put("recipientId", recipientId);
        cv.put("message", message);
        cv.put("username", username);
        System.out.println("the username being stored " + username);

        long rowId = mSQLiteDatabase.insert("Conversation", null, cv);

        return rowId !=-1;
    }


    /**
     * Closes the db.
     */
    public void closeDB() {
        mSQLiteDatabase.close();
    }


    /**
     * Retrieves the conversations from the DB, for display.
     * @return conversation
     */
    public List<Conversation> getConvos() {

        String [] columns = {
                "id", "senderId", "recipientId", "message", "username"
        };

        Cursor c = mSQLiteDatabase.query(
                CONVO_TABLE,
                columns,
                null,
                null,
                null,
                null,
                null
        );

        c.moveToFirst();
        List<Conversation> list = new ArrayList<Conversation>();
        for(int i = 0; i < c.getCount(); i++) {
            String myId = c.getString(0);
            String senderId = c.getString(1);
            String recipientId = c.getString(2);
            String message = c.getString(3);
            String username = c.getString(4);
            Conversation convo = new Conversation();

            System.out.println("getCount " + c.getCount());
//
            if(myId.equals(senderId)) {
                convo.setSenderId(senderId);
                convo.setSenderName(username);
                convo.setMessage(message);
////                convo.setRecipientProfileImgPath(profileImgPath);
            } else {
                convo.setRecipientId(recipientId);
                convo.setRecipientName(username);
                convo.setMessage(message);
//                convo.setSenderProfileImgPath(profileImgPath);
            }


            list.add(convo);
            c.moveToNext();

        }

        return list;

    }


    /**
     * Delete all the data from the CONVERSATION_TABLE
     */
    public void deleteCourses() {
        System.out.println("Delete");
        mSQLiteDatabase.delete(CONVO_TABLE, null, null);
    }


    /**
     * Creating the db
     */
    class CourseDBHelper extends SQLiteOpenHelper{
        /** The string that creates the table */
        private String CREATE_CONVERSATION_SQL;
        /** String that will drop the table */
        private String DROP_CONVERSATION_SQL;

        /**
         * Constructs the db
         * @param context the activity
         * @param name the name of db
         * @param factory the factory
         * @param version the db version
         */
        public CourseDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
            CREATE_CONVERSATION_SQL = context.getString(R.string.CREATE_CONVERSATION_SQL);
            DROP_CONVERSATION_SQL = context.getString(R.string.DROP_CONVERSATION_SQL);

        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_CONVERSATION_SQL);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(DROP_CONVERSATION_SQL);
            onCreate(db);

        }
    }
}
