package mr.zhang.databaseoperation.anno.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * 列名
 * Created by Mr. Zhang on 2016/10/24.
 */
@Target(ElementType.FIELD)
public @interface DBColumn {
    /**
     * 列名
     */
    String columnName();

    /**
     * 是否是id
     */
    boolean isId() default false;

    /**
     * 是否有唯一约束
     */
    boolean isUnique() default  false;

    /**
     * 是否自动增长
     */
    boolean isAutoCreatment() default false;
}
