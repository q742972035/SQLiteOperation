package mr.zhang.databaseoperation.anno.dosql;

import java.util.List;

import mr.zhang.databaseoperation.anno.dml.DML;
import mr.zhang.databaseoperation.anno.sqlbuilder.LimitBuilder;

/**
 * Created by Mr. Zhang on 2016/10/25.
 */

public class Limit<T> {
    private Class<T> mClass;
    private LimitBuilder builder;

    /**
     * 上方创过来的sb
     */
    private StringBuilder mSb;
    private Limit(Class<T> clazz,StringBuilder sb){
        mClass = clazz;
        mSb = sb;
    }

    public static<T> Limit<T> create(StringBuilder sb,Class classes,int start,int counts){
        Limit limit = new Limit(classes,sb);
        limit.builder = LimitBuilder.create(start, counts);
        return limit;
    }

    public List<T> findAll(){
        return DML.findAll(mClass,new StringBuilder(getSql()));
    }

    public String getSql() {
        return mSb+builder.getSql();
    }
}
