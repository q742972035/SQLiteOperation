package mr.zhang.databaseoperation.anno;

import android.content.Context;

import java.util.List;

import mr.zhang.databaseoperation.anno.dosql.Selector;

/**
 * 处理接口
 * Created by Mr. Zhang on 2016/10/24.
 */

public interface DBManager {
    String getCreateSql(Class clazz) throws Exception;
    /**
     * 新增或者更新
     */
    void createOrUpdate(Object object) throws Exception;

    <T> Selector<T> selector(Class<T> clazz);

    void delete(Object object) throws Exception;


    class DaoConfig{
        private String mDbName;
        private int mDbVersion;
        private List<Class> mClasses;
        private DbUpdateCallBack mCallBack;
        private Context mContext;

        public DbUpdateCallBack getCallBack() {
            return mCallBack;
        }

        public String getDbName() {
            return mDbName;
        }

        public Context getContext() {
            return mContext;
        }

        public int getDbVersion() {
            return mDbVersion;
        }

        public List<Class> getClasses() {
            return mClasses;
        }

        public DaoConfig(){}

        public DaoConfig setDbName(String dbName){
            mDbName = dbName;
            return this;
        }

        public DaoConfig setDbVersion(int dbVersion){
            mDbVersion = dbVersion;
            return this;
        }

        public DaoConfig setDbClass(List<Class> classes){
            mClasses = classes;
            return this;
        }

        public DaoConfig setUpdateCallBack(DbUpdateCallBack callBack){
            mCallBack = callBack;
            return this;
        }

        public DaoConfig setContext(Context context){
            mContext = context;
            return this;
        }
    }
}
