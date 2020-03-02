package com.android.viacheslavtimecounter.model.database.namesDoings;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.android.viacheslavtimecounter.model.DoingName;
import com.android.viacheslavtimecounter.model.database.doings.DoingDbSchema;

import java.util.UUID;

import static com.android.viacheslavtimecounter.model.database.namesDoings.NamesDbSchema.*;


public class NamesCursorWrapper extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public NamesCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public DoingName getDoingName() {
        String uuidString = getString(getColumnIndex(NamesTable.Cols.UUID));
        String title = getString(getColumnIndex(NamesTable.Cols.TITLE));
        int color = getInt(getColumnIndex(NamesTable.Cols.COLOR));

        DoingName doingName = new DoingName();
        doingName.setUUID(UUID.fromString(uuidString));
        doingName.setTitle(title);
        doingName.setColor(color);
        return doingName;
    }
}
