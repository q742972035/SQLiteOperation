package mr.zhang.databaseoperation.anno.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Set;

import mr.zhang.databaseoperation.anno.DBManager;

/**
 * Created by Mr. Zhang on 2016/10/24.
 */

public class MyDataBaseHelper extends SQLiteOpenHelper{

    private Context mContext;
    private Set<String> mCreateSQLs;
    private DBManager.DaoConfig mDaoConfig;

    private MyDataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public MyDataBaseHelper(Context context, String name, int version, Set<String> createSqls, DBManager.DaoConfig daoConfig){
        this(context,name,null,version);
        mContext = context;
        mCreateSQLs = createSqls;
        mDaoConfig = daoConfig;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        for (String mCreateSQL : mCreateSQLs) {
            db.execSQL(mCreateSQL);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (String mCreateSQL : mCreateSQLs) {
            db.execSQL(mCreateSQL);
        }

        if (mDaoConfig.getCallBack() != null) {
            mDaoConfig.getCallBack().onUpgrade(db, oldVersion, newVersion);
        }
    }
}
