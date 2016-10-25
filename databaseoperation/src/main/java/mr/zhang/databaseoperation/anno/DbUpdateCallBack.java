package mr.zhang.databaseoperation.anno;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Mr. Zhang on 2016/10/25.
 */

public interface DbUpdateCallBack {
    void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);
}
