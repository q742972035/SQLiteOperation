package mr.zhang.databaseoperation.anno.dosql;

import java.util.List;

import mr.zhang.databaseoperation.anno.dml.DML;
import mr.zhang.databaseoperation.anno.sqlbuilder.OrderByBuilder;
import mr.zhang.databaseoperation.anno.sqlbuilder.WhereBuilder;
import mr.zhang.databaseoperation.anno.util.Relfection;

/**
 * Created by Mr. Zhang on 2016/10/25.
 */

public class Selector<T> {
    private StringBuilder sb ;
    private Class<T> mClazz;
    private Selector(Class<T> clazz){
        sb = new StringBuilder();
        mClazz = clazz;
    }
    public static<T> Selector<T> select(Class<T> clazz){
        Selector<T> find = new Selector<>(clazz);
        final StringBuilder sb = find.sb;
        sb.append("SELECT * FROM "+ Relfection.getTableName(clazz)+" ");
        return find;
    }

    public Where<T> where(String columnName,WhereBuilder.Operation operation,
                        String...assignment){
        Where where = Where.where(sb,mClazz,columnName,operation,assignment);
        return where;
    }

    public Order<T> order(String[] columnNames,OrderByBuilder.Operation[] operations){
        Order order = Order.create(sb, mClazz, columnNames, operations);
        return order;
    }

    public Limit<T> limit(int start,int counts){
        Limit limit = Limit.create(sb, mClazz, start, counts);
        return limit;
    }

    public List<T> findAll(){
        return DML.findAll(mClazz,sb);
    }

    public String getSql(){
        return sb.toString();
    }
}
