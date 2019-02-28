package com.study.android.CrimeFragment;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import database.CrimeDbSchema.CrimeBaseHelper;
import database.CrimeDbSchema.CrimeCursorWrapper;
import database.CrimeDbSchema.CrimeDbSchema;

/*
*
*
* */
public class CrimeLab {
    //注意 sCrimeLab 变量的 s 前缀。这是Android开发的命名约定，一看到此前缀，我们就
    //知道 sCrimeLab 是一个静态变量。
    private static CrimeLab sCrimeLab;
   // private LinkedHashMap<UUID,Crime> mCrimes;
    private Context mContext;
    private SQLiteDatabase mDatabase;


    private static ContentValues getContentValues(Crime crime){
        ContentValues contentValues = new ContentValues();
        contentValues.put(CrimeDbSchema.CrimeTable.Cols.UUID,crime.getId().toString());
        contentValues.put(CrimeDbSchema.CrimeTable.Cols.DATE,crime.getDate().getTime());
        contentValues.put(CrimeDbSchema.CrimeTable.Cols.SOLVED,crime.isSolved()?1:0);
        contentValues.put(CrimeDbSchema.CrimeTable.Cols.TITLE,crime.getTitle());
        contentValues.put(CrimeDbSchema.CrimeTable.Cols.SUSPECT,crime.getSupect());
        contentValues.put(CrimeDbSchema.CrimeTable.Cols.CALLCONTACT,crime.getCallContact());
        return contentValues;
    }
    public static CrimeLab get(Context context) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }
    public void addCrime(Crime crime){
      //  mCrimes.put(crime.getId(),crime);
        ContentValues contentValues = getContentValues(crime);
        mDatabase.insert(CrimeDbSchema.CrimeTable.NAME,null,contentValues);
    }


    //public Cursor query(
    //String table,
    //String[] columns,
    //String where,
    //String[] whereArgs,
    //String groupBy,
    //String having,
    //String orderBy,
    //String limit)
    private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs){
        Cursor cursor = mDatabase.query(
                CrimeDbSchema.CrimeTable.NAME,
                null,//not select all colums
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new CrimeCursorWrapper(cursor); //利用Warpper返回 Crime
    }


    public void updateCrime(Crime crime){
        String uuid = crime.getId().toString(); //防止SQL注入
        ContentValues values = getContentValues(crime);
        mDatabase.update(CrimeDbSchema.CrimeTable.NAME,values,CrimeDbSchema.CrimeTable.Cols.UUID+"=?",
                new String[]{uuid});
    }
    private CrimeLab(Context context){
     /*   mCrimes = new ArrayList<>();
        for (int i=0;i<100;i++){
            Crime crime = new Crime();
            crime.setTitle("Crime #"+i);
            crime.setSolved(i%2==0); //true and flase
            mCrimes.add(crime);*/
     //优化代码 LinkedHashMap<String,Crime> mCrimes;
        mContext = context.getApplicationContext();
        mDatabase = new CrimeBaseHelper(context).getWritableDatabase();
       // mCrimes = new LinkedHashMap<>();
       /* for (int i=0;i<100;i++){
            Crime crime = new Crime();
            crime.setTitle("Crime #"+i);
            crime.setSolved(i%2==0); //true and flase
            mCrimes.put(crime.getId(),crime);
        }*/


    }
    public List<Crime> getCrimes(){
        List<Crime> crimes = new ArrayList<>();
        CrimeCursorWrapper cursorWrapper = queryCrimes(null,null);
        try {
            cursorWrapper.moveToFirst();
            while (!cursorWrapper.isAfterLast()){
                crimes.add(cursorWrapper.getCrime());
                cursorWrapper.moveToNext();
            }
        }finally {
            cursorWrapper.close();
        }
        return crimes;
    }
    public Crime getCrime(UUID uuid){
        CursorWrapper cursorWrapper = queryCrimes(CrimeDbSchema.CrimeTable.Cols.UUID +
                "= ?",new String[] {uuid.toString()});
        try {
            if (cursorWrapper.getCount()==0){
                return null;
            }
            cursorWrapper.moveToFirst();
            return ((CrimeCursorWrapper) cursorWrapper).getCrime();
        }finally {
            cursorWrapper.close();
        }
    }
    public File getPhotoFile(Crime crime){
        File filesDir = mContext.getFilesDir();//获取/data/data/<包名>/files目录。
        return new File(filesDir, crime.getPhotoFileName());
    }

    public void removeCrime(Crime crime){
        String uuid = crime.getId().toString();
        mDatabase.delete(CrimeDbSchema.CrimeTable.NAME,
                CrimeDbSchema.CrimeTable.Cols.UUID +" = ?",new String[]{uuid});
    }
}
