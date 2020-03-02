package com.android.viacheslavtimecounter.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;

import com.android.viacheslavtimecounter.model.database.namesDoings.NamesBaseHelper;
import com.android.viacheslavtimecounter.model.database.namesDoings.NamesCursorWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.android.viacheslavtimecounter.model.database.namesDoings.NamesDbSchema.*;

public class DoingNameLab {
    private static DoingNameLab sDoingNameLab;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    private DoingNameLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new NamesBaseHelper(mContext)
                .getWritableDatabase(); 
//        mDoingNames = new ArrayList<>(); //delete

        // add random doing names
        for (int i = 0; i < 7; i++) {
            DoingName doingName = new DoingName();
            doingName.setTitle(("Doing number " + i));
            Random rnd = new Random();
            int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            doingName.setColor(color);
            addDoingName(doingName);
        }

    }
    public static DoingNameLab getDoingNameLab(Context context) {
        if (sDoingNameLab == null) {
            sDoingNameLab = new DoingNameLab(context);
        }
        return sDoingNameLab;
    }

    public void addDoingName(DoingName doingName) {
//        mDoingNames.add(doingName);
        ContentValues values = getValues(doingName);
        mDatabase.insert(NamesTable.NAME, null, values);
    }

    public DoingName getDoingName(String title) {
        /*for (DoingName doingName : mDoingNames
        ) {
            if (doingName.getTitle().equals(title)) {
                return doingName;
            }
        }
        return null;*/

        NamesCursorWrapper cursor = queryItems(
                NamesTable.Cols.TITLE + " = ?",
                new String[]{title});
        try {
            if (cursor.getCount() == 0) {
                return null;
            }
            cursor.moveToFirst();
            return cursor.getDoingName();
        } finally {
            cursor.close();
        }
    }
    public void updateDoingName(DoingName doingName) {
        /*for (int i = 0; i < mDoingNames.size(); i++) {
            if (mDoingNames.get(i).getTitle().equals(doingName.getTitle())) {
                mDoingNames.set(i, doingName);
            }
        }*/

        ContentValues values = getValues(doingName);
        mDatabase.update(NamesTable.NAME, values,
                NamesTable.Cols.UUID + " = ?",
                new String[]{doingName.getUUID().toString()});

    }

    public void deleteDoingName(DoingName doingName) {
//        mDoingNames.remove(doingName);
        String title = doingName.getTitle();
        mDatabase.delete(NamesTable.NAME, NamesTable.Cols.TITLE + " = ?", new String[]{title});

    }

    public List<DoingName> getDoingNames() {
//        return mDoingNames;

        List<DoingName> doingNames = new ArrayList<>();
        NamesCursorWrapper cursor = queryItems(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                doingNames.add(cursor.getDoingName());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return doingNames;
    }

    private ContentValues getValues(DoingName doingName) {
        ContentValues values = new ContentValues();
        values.put(NamesTable.Cols.UUID, doingName.getUUID().toString());
        values.put(NamesTable.Cols.TITLE, doingName.getTitle());
        values.put(NamesTable.Cols.COLOR, doingName.getColor());
        return values;
    }

    private NamesCursorWrapper queryItems(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                NamesTable.NAME,
                null, // columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null  // orderBy
        );
        return new NamesCursorWrapper(cursor);
    }

}
