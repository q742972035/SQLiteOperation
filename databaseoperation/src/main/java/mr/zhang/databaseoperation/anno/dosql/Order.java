package mr.zhang.databaseoperation.anno.dosql;

import java.util.List;

import mr.zhang.databaseoperation.anno.dml.DML;
import mr.zhang.databaseoperation.anno.sqlbuilder.OrderByBuilder;

/**
 * Created by Mr. Zhang on 2016/10/25.
 */

public class Order<T> {
    private Class<T> mClass;
    private OrderByBuilder builder;
    /**
     * 上方创过来的sb
     */
    private StringBuilder mSb;
    private Order(Class<T> clazz,StringBuilder sb){
        mClass = clazz;
        mSb = sb;
    }

    public static<T> Order<T> create(StringBuilder sb,Class classes,String[] columnNames,OrderByBuilder.Operation[] operations){
        Order<T> order = new Order(classes,sb);
        order.builder = OrderByBuilder.create(columnNames, operations);
        return order;
    }

    public Limit<T> limit(int start,int counts){
        Limit<T> limit = Limit.create(new StringBuilder(getSql()), mClass, start, counts);
        return limit;
    }

    public List<T> findAll(){
        return DML.findAll(mClass,new StringBuilder(getSql()));
    }

    public String getSql() {
        return mSb+builder.getSql();
    }
}
