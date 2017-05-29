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
 */

public class ConversationDB {
    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "Conversation.db";
    private static final String CONVO_TABLE = "Conversation";
    private CourseDBHelper mCourseDBHelper;
    private SQLiteDatabase mSQLiteDatabase;

//    private final String CREAT_MESSAGE_SQL;

    public ConversationDB(Context context) {
        mCourseDBHelper = new CourseDBHelper(context, DB_NAME, null, DB_VERSION);
        mSQLiteDatabase = mCourseDBHelper.getWritableDatabase();
    }

    public boolean insertConvo(String myId, String senderId, String recipientId, String message, String username, String profileImgPath) {
        ContentValues cv = new ContentValues();
        cv.put("id", myId);
        cv.put("senderId", senderId);
        cv.put("recipientId", recipientId);
        cv.put("message", message);
        cv.put("username", username);
        cv.put("profileImgPath", profileImgPath);

        long rowId = mSQLiteDatabase.insertWithOnConflict("Conversation", null, cv, SQLiteDatabase.CONFLICT_IGNORE);

        return rowId !=-1;
    }

    public void closeDB() {
        mSQLiteDatabase.close();
    }

    public List<Conversation> getConvos() {

        String [] columns = {
                "id", "senderId", "recipientId", "message", "username", "profileImgPath"
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
            String profileImgPath = c.getString(5);
            Conversation convo = new Conversation();

            if(myId.equals(senderId)) {
                convo.setRecipientId(recipientId);
                convo.setUsername(username);
                convo.setMessage(message);
                convo.setRecipientProfileImgPath(profileImgPath);
            } else {
                convo.setUsername(username);
                convo.setSenderId(senderId);
                convo.setMessage(message);
                convo.setSenderProfileImgPath(profileImgPath);
            }

//            convo.setUsername(username);
//            convo.setMessage(message);
//            convo.setSenderProfileImgPath(profileImgPath);

            list.add(convo);
            c.moveToNext();
//            convo.setRecipientId(i\;
//            convo.setM
//            list.add(c);
        }

        return list;

    }



    class CourseDBHelper extends SQLiteOpenHelper{
        private String CREATE_CONVERSATION_SQL;
        private String DROP_CONVERSATION_SQL;


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
