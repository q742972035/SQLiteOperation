package mr.zhang.databaseoperation.anno.util;

import java.lang.reflect.Field;
import java.util.List;

import mr.zhang.databaseoperation.anno.anno.DBColumn;
import mr.zhang.databaseoperation.anno.anno.TableName;

/**
 * Created by Mr. Zhang on 2016/10/25.
 */

public class Relfection {
    public static String getTableName(Object object){
        Class<?> aClass = object.getClass();
        TableName tableName = aClass.getAnnotation(TableName.class);
        return tableName.tableName();
    }

    public static String getTableName(Class clazz){
        TableName tableName = (TableName) clazz.getAnnotation(TableName.class);
        return tableName.tableName();
    }

    /**
     * 查找该类的所有id列名和字段名
     * @param object 所要查找的类
     * @param idColumnsNames id列名
     * @param idFieldNames id字段
     */
    public static void getAllIds(Object object ,List<String> idColumnsNames,List<String> idFieldNames){
        Class<?> aClass = object.getClass();
        Field[] declaredFields = aClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            if (declaredField.isAnnotationPresent(DBColumn.class)){
                DBColumn dbColumn = declaredField.getAnnotation(DBColumn.class);
                if (dbColumn.isId()){
                    idColumnsNames.add(dbColumn.columnName());
                    idFieldNames.add(declaredField.getName());
                }
            }
        }
    }

    /**
     * 查找该类的所有列名和字段名
     *
     * @param aClass       所要查找的类
     * @param columnsNames id列名
     * @param fieldNames id字段
     */
    public static void getAlls(Class aClass, List<String> columnsNames, List<String> fieldNames) {
        Field[] declaredFields = aClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            if (declaredField.isAnnotationPresent(DBColumn.class)) {
                DBColumn dbColumn = declaredField.getAnnotation(DBColumn.class);
                columnsNames.add(dbColumn.columnName());
                fieldNames.add(declaredField.getName());
            }
        }
    }

    /**
     * 查找该类的所有非id列名和字段名
     * @param object 所要查找的类
     * @param notIdColumnsNames 非id列名
     * @param notIdFieldNames 非id字段
     */
    public static void getAllNotIds(Object object ,List<String> notIdColumnsNames,List<String> notIdFieldNames){
        Class<?> aClass = object.getClass();
        Field[] declaredFields = aClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            if (declaredField.isAnnotationPresent(DBColumn.class)) {
                DBColumn dbColumn = declaredField.getAnnotation(DBColumn.class);
                if (!dbColumn.isId()) {
                    notIdColumnsNames.add(dbColumn.columnName());
                    notIdFieldNames.add(declaredField.getName());
                }
            }
        }
    }

    public static Object getValueByFieldName(Object object,String fileName){
        Object o = null;
        try {
            o = object.getClass().getField(fileName).get(object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return o;
    }


}
