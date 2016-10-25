package mr.zhang.databaseoperation.anno.dosql;

import java.util.List;

import mr.zhang.databaseoperation.anno.dml.DML;
import mr.zhang.databaseoperation.anno.sqlbuilder.OrderByBuilder;
import mr.zhang.databaseoperation.anno.sqlbuilder.WhereBuilder;

/**
 * Created by Mr. Zhang on 2016/10/25.
 */

public class Where<T> {
    private Class<T> mClass;
    private WhereBuilder builder;
    /**
     * Selector创过来的sb
     */
    private StringBuilder mSb;
    private Where(Class<T> clazz,StringBuilder sb){
        mClass = clazz;
        mSb = sb;
    }

    public static<T> Where<T> where(StringBuilder sb,Class clazz,String columnName,WhereBuilder.Operation operation,
                               String...assignment){
        Where<T> where = new Where<>(clazz,sb);
        where.builder = WhereBuilder.create(columnName, operation, assignment);
        return where;
    }

    public Where<T> next(WhereBuilder.Symbol symbol, String columnName, WhereBuilder.Operation operation,
                      String...assignment){
        builder.next(symbol, columnName, operation, assignment);
        return this;
    }

    public Limit<T> limit(int start,int counts){
        Limit limit = Limit.create(new StringBuilder(getSql()), mClass, start, counts);
        return limit;
    }

    public Order<T> order(String[] columnNames,OrderByBuilder.Operation[] operations){
        Order<T> order = Order.create(new StringBuilder(getSql()), mClass, columnNames, operations);
        return order;
    }

    public List<T> findAll(){
        return DML.findAll(mClass,new StringBuilder(getSql()));
    }

    public String getSql() {
        return mSb+builder.getSql();
    }
}
