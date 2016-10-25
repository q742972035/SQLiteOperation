package mr.zhang.databaseoperation.anno.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * 表名
 * Created by Mr. Zhang on 2016/10/24.
 */
@Target(ElementType.TYPE)
public @interface TableName {
    String tableName();
}
