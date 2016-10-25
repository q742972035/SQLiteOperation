package mr.zhang.databaseoperation.anno.dml;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import mr.zhang.databaseoperation.anno.anno.DBColumn;
import mr.zhang.databaseoperation.anno.anno.TableName;
import mr.zhang.databaseoperation.anno.db.Dian;
import mr.zhang.databaseoperation.anno.util.Relfection;

/**
 * Created by Mr. Zhang on 2016/10/25.
 */

public class DML {
    public static SQLiteDatabase sDatabase;

    public static void insert(Object object){
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO ");
        Class clazz = object.getClass();
        // 遍历成员变量
        Field[] declaredFields = clazz.getDeclaredFields();
        // 获取表名
        if (clazz.isAnnotationPresent(TableName.class)){
            TableName tableName = (TableName) clazz.getAnnotation(TableName.class);
            String table = tableName.tableName();
            sb.append(table+"(");
        }

        List<String> keys = new ArrayList<>();
        List<Object> valueses = new ArrayList<>();
        for (int i = 0; i < declaredFields.length; i++) {
            Field field = declaredFields[i];
            field.setAccessible(true);
            if (field.isAnnotationPresent(DBColumn.class)){
                // 获取列名
                DBColumn dbColumn = field.getAnnotation(DBColumn.class);
                String columnName = dbColumn.columnName();
                keys.add(columnName);

                // 获取该字段的值
                try {
                    Object value = field.get(object);
                    valueses.add(value);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        for (int i = 0; i < keys.size(); i++) {
            // 不是最后一个
            if (i != keys.size() - 1) {
                sb.append(keys.get(i)+",");
            } else {
                sb.append(keys.get(i)+") VALUES (");
            }
        }

        for (int i = 0; i < valueses.size(); i++) {
            // 不是最后一个
            if (i != valueses.size() - 1) {
                sb.append(Dian.DIAN+valueses.get(i)+Dian.DIAN+",");
            } else {
                sb.append(Dian.DIAN+valueses.get(i)+Dian.DIAN+");");
            }
        }
        sDatabase.execSQL(sb.toString());
    }

    public static void delete(Object object){
        StringBuilder sb = new StringBuilder();
        // 获取表名
        String tableName = Relfection.getTableName(object);
        sb.append("DELETE FROM "+tableName+" WHERE ");
        List<String> idColumnsNames = new ArrayList<>();
        List<String> idFieldNames = new ArrayList<>();
        Relfection.getAllIds(object,idColumnsNames,idFieldNames);
        for (int i = 0; i < idColumnsNames.size(); i++) {
            // Id的列名
            String idColumnName = idColumnsNames.get(i);
            // id的字段名
            String idFieldName = idFieldNames.get(i);
            // 获取字段值
            Object value = Relfection.getValueByFieldName(object,idFieldName);

            if (i!=idColumnsNames.size()-1){
                sb.append(idColumnName+" = "+Dian.DIAN+value+Dian.DIAN+" AND ");
            }else {
                sb.append(idColumnName+" = "+Dian.DIAN+value+Dian.DIAN+";");
            }
        }
        sDatabase.execSQL(sb.toString());
    }

    public static void update(Object object){
        StringBuilder sb = new StringBuilder();
        // 获取表名
        String tableName = Relfection.getTableName(object);
        sb.append("UPDATE "+tableName+" SET ");
        List<String> notIdColumnsNames = new ArrayList<>();
        List<String> notIdFieldNames = new ArrayList<>();
        Relfection.getAllNotIds(object,notIdColumnsNames,notIdFieldNames);

        for (int i = 0; i < notIdColumnsNames.size(); i++) {
            // 不是Id的列名
            String notIdColumnName = notIdColumnsNames.get(i);
            // 不是id的字段名
            String notIdFieldName = notIdFieldNames.get(i);
            // 获取字段值
            Object value = Relfection.getValueByFieldName(object,notIdFieldName);

            if (i!=notIdColumnsNames.size()-1){
                sb.append(notIdColumnName+" = "+Dian.DIAN+value+Dian.DIAN+" , ");
            }else {
                sb.append(notIdColumnName+" = "+Dian.DIAN+value+Dian.DIAN+" WHERE ");
            }
        }

        List<String> idColumnsNames = new ArrayList<>();
        List<String> idFieldNames = new ArrayList<>();
        Relfection.getAllIds(object,idColumnsNames,idFieldNames);


        for (int i = 0; i < idColumnsNames.size(); i++) {
            // Id的列名
            String idColumnName = idColumnsNames.get(i);
            // id的字段名
            String idFieldName = idFieldNames.get(i);
            // 获取字段值
            Object value = Relfection.getValueByFieldName(object,idFieldName);

            if (i!=idColumnsNames.size()-1){
                sb.append(idColumnName+" = "+Dian.DIAN+value+Dian.DIAN+" AND ");
            }else {
                sb.append(idColumnName+" = "+Dian.DIAN+value+Dian.DIAN+";");
            }
        }
        sDatabase.execSQL(sb.toString());



    }

    public static <T> List<T> findAll(Class<T> tClass,StringBuilder sb){
        List<T> list = new ArrayList<>();
        sb.append(";");
        String selectSql = sb.toString();
        Cursor cursor = sDatabase.rawQuery(selectSql,null);
        List<String> columnsNames = new ArrayList<>();
        List<String> fieldNames = new ArrayList<>();
        Relfection.getAlls(tClass,columnsNames,fieldNames);
        while (cursor.moveToNext()){
            // 反射创建对象
            T o = null;
            try {
                o = tClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (int i = 0; i < columnsNames.size(); i++) {
                String value = cursor.getString(cursor.getColumnIndex(columnsNames.get(i)));
                String fieldName = fieldNames.get(i);
                Field field = null;
                try {

                    field = tClass.getField(fieldName);
                    field.setAccessible(true);
                    field.set(o,value);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            list.add(o);
        }
        cursor.close();
        return list;
    }
}
