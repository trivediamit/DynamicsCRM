package com.dynamics.crm.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.dynamics.crm.model.Attachment;
import com.dynamics.crm.model.Lead;
import com.dynamics.crm.model.LeadLocation;
import com.dynamics.crm.model.SalesPerson;
import com.dynamics.crm.model.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class DatabaseUtil {

    DatabaseHelper helper;

    public DatabaseUtil(Context context) {
        helper = new DatabaseHelper(context);
    }

    public void insertDummyData() {
        try {
            SQLiteDatabase db = helper.getWritableDatabase();

            Log.i("DatabaseUtil: ", "Insertion in Login table");

            // User login table
            // User
            ContentValues contentValuesUser = new ContentValues();
            contentValuesUser.put(DatabaseHelper.LOGIN_COLUMN_USERNAME, "vibhorm@winjit.com");
            contentValuesUser.put(DatabaseHelper.LOGIN_COLUMN_PASSWORD, "winjit");
            contentValuesUser.put(DatabaseHelper.LOGIN_COLUMN_IS_ADMIN, false);
            db.insert(DatabaseHelper.LOGIN_TABLE, null, contentValuesUser);

            // Admin
            ContentValues contentValuesAdmin = new ContentValues();
            contentValuesAdmin.put(DatabaseHelper.LOGIN_COLUMN_USERNAME, "admin@winjit.com");
            contentValuesAdmin.put(DatabaseHelper.LOGIN_COLUMN_PASSWORD, "admin");
            contentValuesAdmin.put(DatabaseHelper.LOGIN_COLUMN_IS_ADMIN, true);
            db.insert(DatabaseHelper.LOGIN_TABLE, null, contentValuesAdmin);

            ContentValues contentValuesSalesPerson = new ContentValues();
            contentValuesSalesPerson.put(DatabaseHelper.SALES_PERSON_NAME, "Vibhor Mehta");
            contentValuesSalesPerson.put(DatabaseHelper.SALES_PERSON_EMAIL, "vibhorm@winjit.com");
            db.insert(DatabaseHelper.SALES_PERSON_TABLE, null, contentValuesSalesPerson);

            Log.i("DatabaseUtil: ", "Insertion in Lead table");

            insertLeadEntry("Mailed an interest card back  (sample)", "Susanna", "Stubberod (sample)", "Litware, Inc. (sample)", "someonel2@example.com", "555-0127", "Vibhor Mehta", "New");
            insertLeadEntry("New store opened this year - follow up (sample)", "Nancy", "Anderson (sample)", "Adventure Works (sample)", "someonel3@example.com", "555-0134", "Vibhor Mehta", "New");
            insertLeadEntry("Interested in online only store (sample)", "Maria", "Campbell (sample)", "Fabrikam, Inc. (sample)", "someonel4@example.com", "555-0165", "Vibhor Mehta", "New");
            insertLeadEntry("Good prospect (sample)", "Peter", "Houston (sample)", "Tailspin Toys", "someonel5@example.com", "555-0156", "Vibhor Mehta", "New");


        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
    }

    public void insertLeadEntry(String topic, String firstName, String lastName, String companyName, String emailId, String businessPhone, String owner, String status) {

        try {
            SQLiteDatabase db = helper.getWritableDatabase();

            // Lead table

            ContentValues contentValues = new ContentValues();
            contentValues.put(DatabaseHelper.LEAD_TOPIC, topic);
            contentValues.put(DatabaseHelper.LEAD_FIRST_NAME, firstName);
            contentValues.put(DatabaseHelper.LEAD_LAST_NAME, lastName);
            contentValues.put(DatabaseHelper.LEAD_COMPANY_NAME, companyName);
            contentValues.put(DatabaseHelper.LEAD_EMAIL_ID, emailId);
            contentValues.put(DatabaseHelper.LEAD_BUSINESS_PHONE, businessPhone);
            contentValues.put(DatabaseHelper.LEAD_OWNER, owner);
            contentValues.put(DatabaseHelper.LEAD_STATUS, status);

            db.insert(DatabaseHelper.LEAD_TABLE, null, contentValues);
        } catch (SQLException sqlEx) {
            {
                sqlEx.printStackTrace();
            }
        }
    }

    public User getUserDetails(final String strUsername, final String strPassword) {
        String[] columns = {DatabaseHelper.LOGIN_COLUMN_USERNAME, DatabaseHelper.LOGIN_COLUMN_IS_ADMIN};
        String strSelectionCriteria = DatabaseHelper.LOGIN_COLUMN_USERNAME + "= ? AND " +
                DatabaseHelper.LOGIN_COLUMN_PASSWORD + "= ?";
        String strSelectionArgs[] = {strUsername, strPassword};

        User user = null;
        try {
            SQLiteDatabase db = helper.getWritableDatabase();
            Cursor cursor = db.query(DatabaseHelper.LOGIN_TABLE, columns, strSelectionCriteria, strSelectionArgs, null, null, null, null);
            if (cursor.moveToNext()) {
                user = new User();
                user.setUserName(cursor.getString(0));
                user.setIsAdmin(cursor.getString(1));
                cursor.close();
            }
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
        return user;
    }

    public ArrayList<Lead> getLeadsList() {
        ArrayList<Lead> leadArrayList = new ArrayList<>();
        try {
            SQLiteDatabase db = helper.getWritableDatabase();
            String[] columns = {"iId", "cFirstName", "cLastName", "cTopic"};
            Cursor cursor = db.query(true, DatabaseHelper.LEAD_TABLE, columns, null, null, null, null, null, null);

            while (cursor.moveToNext()) {
                String id = String.valueOf(cursor.getInt(0));
                String firstName = cursor.getString(1);
                String lastName = cursor.getString(2);
                String topic = cursor.getString(3);

                Lead lead = new Lead();
                lead.setId(id);
                lead.setFirstName(firstName);
                lead.setLastName(lastName);
                lead.setTopic(topic);

                leadArrayList.add(lead);
            }
            cursor.close();
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
        return leadArrayList;
    }

    public ArrayList<String> getLeadDetails(String leadId) {
        ArrayList<String> leadValueStrings = new ArrayList<>();
        try {
            SQLiteDatabase db = helper.getWritableDatabase();

            String[] columns = {"cTopic", "cFirstName", "cLastName", "cCompanyName", "cEmailId", "cBusinessPhone", "cOwner", "cStatus"};
            String strSelectionCriteria = DatabaseHelper.LEAD_ID + "= ? ";
            String strSelectionArgs[] = {leadId};

            Cursor cursor = db.query(true, DatabaseHelper.LEAD_TABLE, columns, strSelectionCriteria, strSelectionArgs, null, null, null, null);

            if (cursor.moveToNext()) {
                leadValueStrings.add(cursor.getString(0));
                leadValueStrings.add(cursor.getString(1));
                leadValueStrings.add(cursor.getString(2));
                leadValueStrings.add(cursor.getString(3));
                leadValueStrings.add(cursor.getString(4));
                leadValueStrings.add(cursor.getString(5));
                leadValueStrings.add(cursor.getString(6));
                leadValueStrings.add(cursor.getString(7));
            }
            cursor.close();
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
        return leadValueStrings;
    }

    public void storeAttachment(String leadId, String attachmentPath, String attachmentType) {
        try {
            SQLiteDatabase db = helper.getWritableDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues.put(DatabaseHelper.ATTACHMENT_LEAD_ID, leadId);
            contentValues.put(DatabaseHelper.ATTACHMENT_PATH, attachmentPath);
            contentValues.put(DatabaseHelper.ATTACHMENT_TYPE, attachmentType);

            db.insert(DatabaseHelper.ATTACHMENT_TABLE, null, contentValues);
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
    }

    public ArrayList<Attachment> getAttachments(String leadId) {
        ArrayList<Attachment> attachmentArrayList = new ArrayList<>();
        try {
            SQLiteDatabase db = helper.getWritableDatabase();

            String[] columns = {"iId", "cPath", "cType"};
            String strSelectionCriteria = DatabaseHelper.ATTACHMENT_LEAD_ID + "= ? ";
            String strSelectionArgs[] = {leadId};

            Cursor cursor = db.query(false, DatabaseHelper.ATTACHMENT_TABLE, columns, strSelectionCriteria, strSelectionArgs, null, null, null, null);

            while (cursor.moveToNext()) {
                Attachment attachment = new Attachment();

                attachment.setAttachmentId(cursor.getString(0));
                attachment.setAttachmentPath(cursor.getString(1));
                attachment.setAttachmentType(cursor.getString(2));

                attachmentArrayList.add(attachment);
            }
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
        return attachmentArrayList;
    }

    public void storeLocation(String leadId, String leadName, String latitude, String longitude) {
        try {
            SQLiteDatabase db = helper.getWritableDatabase();

            ContentValues contentValues = new ContentValues();

            contentValues.put(DatabaseHelper.LOCATION_LEAD_ID, leadId);
            contentValues.put(DatabaseHelper.LOCATION_LEAD_NAME, leadName);
            contentValues.put(DatabaseHelper.LOCATION_LAT, latitude);
            contentValues.put(DatabaseHelper.LOCATION_LONG, longitude);

            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            String currentDate = sdf.format(date);
            contentValues.put(DatabaseHelper.LOCATION_DATE, currentDate);

            db.insert(DatabaseHelper.LOCATION_TABLE, null, contentValues);
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
    }

    public ArrayList<SalesPerson> getSalesPersonList() {
        ArrayList<SalesPerson> salesPersonArrayList = new ArrayList<>();
        try {
            SQLiteDatabase db = helper.getWritableDatabase();

            String[] columns = {"cName", "cEmailId"};

            Cursor cursor = db.query(true, DatabaseHelper.SALES_PERSON_TABLE, columns, null, null, null, null, null, null);

            while (cursor.moveToNext()) {
                SalesPerson salesPerson = new SalesPerson();

                salesPerson.setName(cursor.getString(0));
                salesPerson.setEmailId(cursor.getString(1));

                salesPersonArrayList.add(salesPerson);
            }
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
        return salesPersonArrayList;
    }

    public ArrayList<LeadLocation> getLeadLocationHistory(String leadId) {
        ArrayList<LeadLocation> leadLocationArrayList = new ArrayList<>();
        try {
            SQLiteDatabase db = helper.getWritableDatabase();

            String[] columns = {"cDate", "cLatitude", "cLongitude"};

            String strSelectionCriteria = DatabaseHelper.LOCATION_LEAD_ID + "= ? ";
            String strSelectionArgs[] = {leadId};

            Cursor cursor = db.query(false, DatabaseHelper.LOCATION_TABLE, columns, strSelectionCriteria, strSelectionArgs, null, null, DatabaseHelper.LOCATION_DATE + " DESC", null);

            while (cursor.moveToNext()) {
                LeadLocation leadLocation = new LeadLocation();

                leadLocation.setDate(cursor.getString(0));
                leadLocation.setLatitude(cursor.getString(1));
                leadLocation.setLongitude(cursor.getString(2));

                leadLocationArrayList.add(leadLocation);
            }
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
        }
        return leadLocationArrayList;
    }

    public boolean updateData(ContentValues contentValues, String leadId) {
        try {
            SQLiteDatabase db = helper.getWritableDatabase();

            String strSelectionCriteria = DatabaseHelper.LEAD_ID + "= ? ";
            String strSelectionArgs[] = {leadId};

            int i = db.update(DatabaseHelper.LEAD_TABLE, contentValues, strSelectionCriteria, strSelectionArgs);

            return i >= 1;
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
            return false;
        }
    }

    static class DatabaseHelper extends SQLiteOpenHelper {

        public static final String DATABASE_NAME = "DynamicsCRM.sqlite";
        private static final int DATABASE_VERSION = 1;

        private final static String LOGIN_TABLE = "m_login";
        private final static String LOGIN_COLUMN_ID = "iId";
        private final static String LOGIN_COLUMN_USERNAME = "cUsername";
        private final static String LOGIN_COLUMN_PASSWORD = "cPassword";
        private final static String LOGIN_COLUMN_IS_ADMIN = "bIsAdmin";

        private final static String LEAD_TABLE = "t_lead";
        private final static String LEAD_ID = "iId";
        private final static String LEAD_TOPIC = "cTopic";
        private final static String LEAD_FIRST_NAME = "cFirstName";
        private final static String LEAD_LAST_NAME = "cLastName";
        private final static String LEAD_COMPANY_NAME = "cCompanyName";
        private final static String LEAD_EMAIL_ID = "cEmailId";
        private final static String LEAD_BUSINESS_PHONE = "cBusinessPhone";
        private final static String LEAD_OWNER = "cOwner";
        private final static String LEAD_STATUS = "cStatus";

        private final static String ATTACHMENT_TABLE = "t_attachments";
        private final static String ATTACHMENT_ID = "iId";
        private final static String ATTACHMENT_LEAD_ID = "iLeadID";
        private final static String ATTACHMENT_PATH = "cPath";
        private final static String ATTACHMENT_TYPE = "cType";

        private final static String LOCATION_TABLE = "t_location";
        private final static String LOCATION_ID = "iId";
        private final static String LOCATION_LEAD_ID = "cLeadId";
        private final static String LOCATION_LEAD_NAME = "cLeadName";
        private final static String LOCATION_LAT = "cLatitude";
        private final static String LOCATION_LONG = "cLongitude";
        private final static String LOCATION_DATE = "cDate";

        private final static String SALES_PERSON_TABLE = "m_sales_person";
        private final static String SALES_PERSON_ID = "iId";
        private final static String SALES_PERSON_NAME = "cName";
        private final static String SALES_PERSON_EMAIL = "cEmailId";

        private final String QUERY_CREATE_TABLE_USER_LOGIN =
                "CREATE TABLE " + LOGIN_TABLE + "(" +
                        LOGIN_COLUMN_ID + " INTEGER PRIMARY KEY  NOT NULL  DEFAULT (null) ," +
                        LOGIN_COLUMN_USERNAME + " TEXT NOT NULL  DEFAULT (null) ," +
                        LOGIN_COLUMN_PASSWORD + " TEXT NOT NULL  DEFAULT (null) ," +
                        LOGIN_COLUMN_IS_ADMIN + " BOOL DEFAULT (null));";

        private final String QUERY_CREATE_TABLE_LEAD =
                "CREATE TABLE " + LEAD_TABLE + "(" +
                        LEAD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL  DEFAULT (null) ," +
                        LEAD_TOPIC + " TEXT, " +
                        LEAD_FIRST_NAME + " TEXT, " +
                        LEAD_LAST_NAME + " TEXT, " +
                        LEAD_COMPANY_NAME + " TEXT, " +
                        LEAD_EMAIL_ID + " TEXT, " +
                        LEAD_BUSINESS_PHONE + " TEXT, " +
                        LEAD_OWNER + " TEXT, " +
                        LEAD_STATUS + " TEXT); ";

        private final String QUERY_CREATE_TABLE_ATTACHMENTS =
                "CREATE TABLE " + ATTACHMENT_TABLE + "(" +
                        ATTACHMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL  DEFAULT (null) ," +
                        ATTACHMENT_LEAD_ID + " INTEGER, " +
                        ATTACHMENT_PATH + " TEXT, " +
                        ATTACHMENT_TYPE + " TEXT ); ";

        private final String QUERY_CREATE_TABLE_LOCATION =
                "CREATE TABLE " + LOCATION_TABLE + "(" +
                        LOCATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL  DEFAULT (null) ," +
                        LOCATION_LEAD_ID + " INTEGER, " +
                        LOCATION_LEAD_NAME + " TEXT, " +
                        LOCATION_LAT + " TEXT, " +
                        LOCATION_LONG + " TEXT, " +
                        LOCATION_DATE + " TEXT ); ";

        private final String QUERY_CREATE_TABLE_SALES_PERSON =
                "CREATE TABLE " + SALES_PERSON_TABLE + "(" +
                        SALES_PERSON_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL  DEFAULT (null) ," +
                        SALES_PERSON_NAME + " TEXT, " +
                        SALES_PERSON_EMAIL + " TEXT ); ";

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            Log.i("DatabaseUtil: ", "Creation");
            try {
                /*
                Create table: User Login
                CREATE TABLE "m_login_user" ("iId" INTEGER PRIMARY KEY  NOT NULL  DEFAULT (null) ,"cUsername" TEXT NOT NULL  DEFAULT (null) ,"cPassword" TEXT NOT NULL  DEFAULT (null) ,"bIsAdmin" BOOL DEFAULT (null) ,"dtAuditDate" DATETIME DEFAULT (null) )
                 */
                sqLiteDatabase.execSQL(QUERY_CREATE_TABLE_USER_LOGIN);


                /*
                Create table: Lead
                CREATE  TABLE "main"."t_lead" ("iId" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , "cTopic" TEXT, "cFirstName" TEXT, "cLastName" TEXT, "cCompanyName" TEXT, "cEmail" TEXT, "cBusinessPhone" TEXT, "cOwner" TEXT, "cStatus" TEXT)
                 */

                sqLiteDatabase.execSQL(QUERY_CREATE_TABLE_LEAD);

                /*
                Create table: Attachment
                CREATE TABLE t_attachments(iId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL  DEFAULT (null) ,iLeadID INTEGER, cPath TEXT, cType TEXT )
                 */
                sqLiteDatabase.execSQL(QUERY_CREATE_TABLE_ATTACHMENTS);

                /*
                Create table: Location

                 */
                sqLiteDatabase.execSQL(QUERY_CREATE_TABLE_LOCATION);

                /*
                Create table: Sales
                 */
                sqLiteDatabase.execSQL(QUERY_CREATE_TABLE_SALES_PERSON);

            } catch (SQLException sqlEx) {
                sqlEx.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        }


    }
}


