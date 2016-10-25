package mr.zhang.databaseoperation.anno.spilt;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import mr.zhang.databaseoperation.anno.anno.DBColumn;
import mr.zhang.databaseoperation.anno.anno.TableName;

/**
 * 根据实体类的class反射生成对应的可执行String
 * Created by Mr. Zhang on 2016/10/24.
 */

public class CreateTable {
    /**
     * 生成执行“生成数据表”的语句
     * @param clazz
     * @return
     */
    public static String getCreateSql(Class clazz){
        StringBuilder sb = new StringBuilder();
        // 判断注解
        boolean annotationPresent = clazz.isAnnotationPresent(TableName.class);
        if (annotationPresent){
            TableName annotation = (TableName) clazz.getAnnotation(TableName.class);
            String tableName = annotation.tableName();
            sb.append("CREATE TABLE IF NOT EXISTS "+tableName+" (\n");
        }

        Field[] fields = clazz.getDeclaredFields();
        // 是id的列
        List<String> isIdColumn = new ArrayList<>();
        for (Field field : fields) {
            if (field.isAnnotationPresent(DBColumn.class)){
                DBColumn annotation = field.getAnnotation(DBColumn.class);
                // 是不是id
                boolean isId = annotation.isId();
                // 是否自动增长
                boolean isAutoCreatment = annotation.isAutoCreatment();
                // 是否唯一约束
                boolean isUnique = annotation.isUnique();
                // 列名
                String columnName = annotation.columnName();
                if (isAutoCreatment){
                    sb.append(columnName+" integer AUTOINCREMENT");
                }else {
                    sb.append(columnName+" text");
                }

                if (isId){
                    isIdColumn.add(columnName);
                }

                if (isUnique){
                    sb.append(" UNIQUE,\n");
                }else {
                    sb.append(",\n");
                }
            }
        }

        sb.append("PRIMARY KEY(");

        for (int i = 0; i < isIdColumn.size(); i++) {
            String columnName = isIdColumn.get(i);
            if (i != isIdColumn.size() - 1) {
                sb.append(columnName+",");
            } else {
                // 最后一项
                sb.append(columnName+")\n");
            }
        }
        sb.append(");");
        return sb.toString();
    }
}
