package mr.zhang.databaseoperation.anno;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import mr.zhang.databaseoperation.anno.db.Dian;
import mr.zhang.databaseoperation.anno.db.MyDataBaseHelper;
import mr.zhang.databaseoperation.anno.dml.DML;
import mr.zhang.databaseoperation.anno.dosql.Selector;
import mr.zhang.databaseoperation.anno.spilt.CreateTable;
import mr.zhang.databaseoperation.anno.util.Relfection;

/**
 * 数据库的控制层
 * Created by Mr. Zhang on 2016/10/24.
 */

public final class DbControl implements DBManager {
    private static MyDataBaseHelper sDataBaseHelper;

    private static DbControl mDbcontrol;

    /**
     * 获取数据表实体类
     */
    private List<Class> mClasses;
    private Context mContext;

    /**
     *  数据表创建语句
     */
    private static Map<Class,String> mTableCreateSqls = new HashMap<>();
    private static SQLiteDatabase sDatabases;

    private DbControl(List<Class> classes,Context context){
        mClasses = classes;
        mContext = context;
    }

    public static DbControl getDB(DaoConfig daoConfig){
        if (mDbcontrol == null) {
            synchronized (DbControl.class) {
                if (mDbcontrol == null) {
                    mDbcontrol = new DbControl(daoConfig.getClasses(), daoConfig.getContext());
                    // 不包含，才初始化
                    for (Class aClass : daoConfig.getClasses()) {
                        if (!mTableCreateSqls.containsKey(aClass)) {
                            mTableCreateSqls.put(aClass, CreateTable.getCreateSql(aClass));
                        }
                    }
                    // 初始化sDataBaseHelper
                    if (sDataBaseHelper == null) {
                        Set<String> createSqls = new HashSet<>();
                        for (Map.Entry<Class, String> classStringEntry : mTableCreateSqls.entrySet()) {
                            createSqls.add(classStringEntry.getValue());
                        }
                        sDataBaseHelper = new MyDataBaseHelper(daoConfig.getContext(), daoConfig.getDbName(),
                                daoConfig.getDbVersion(), createSqls,daoConfig);
                        sDatabases = sDataBaseHelper.getWritableDatabase();
                        DML.sDatabase = sDatabases;
                    }
                }
            }
        }

        return mDbcontrol;
    }

    /**
     * 判断是否存在
     */
    private boolean isCreated(Object object) throws SQLException{
        // 找到表名
        String tableName = Relfection.getTableName(object);
        // 获取id列名
        List<String> idColumnsNames = new ArrayList<>();
        // 获取id字段名
        List<String> idFieldNames = new ArrayList<>();

        Relfection.getAllIds(object,idColumnsNames,idFieldNames);

        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM "+tableName+" where ");

        for (int i = 0; i < idColumnsNames.size(); i++) {
            // id的列名
            String idColumnName = idColumnsNames.get(i);
            // id的字段名
            String idFieldName = idFieldNames.get(i);
            // id的值
            Object value = Relfection.getValueByFieldName(object, idFieldName);
            if (i != idColumnsNames.size() - 1) {
                sb.append(idColumnName + " = " + Dian.DIAN + value + Dian.DIAN +" AND ");
            } else {
                sb.append(idColumnName + " = " + Dian.DIAN + value + Dian.DIAN +" ; ");
            }
        }
        Cursor cursor = sDatabases.rawQuery(sb.toString(),null);
        int count = cursor.getCount();
        cursor.close();
        return count>0;
    }



    @Override
    public String getCreateSql(Class clazz) throws SQLException {
        return mTableCreateSqls.get(clazz);
    }

    @Override
    public void createOrUpdate(Object object) throws SQLException {
        if (object instanceof  List){
            List<Object> objects = (List<Object>) object;
            for (Object o : objects) {
                if (!isCreated(o)) {
                    DML.insert(o);
                }else {
                    DML.update(o);
                }
            }
            return;
        }


        if (!isCreated(object)) {
            DML.insert(object);
        }else {
            DML.update(object);
        }
    }

    @Override
    public void delete(Object object) throws Exception {
        if (object instanceof  List){
            List<Object> objects = (List<Object>) object;
            for (Object o : objects) {
                DML.delete(o);
            }
            return;
        }
        DML.delete(object);
    }


    public Selector selector(Class clazz) {
        return Selector.select(clazz);
    }

}
