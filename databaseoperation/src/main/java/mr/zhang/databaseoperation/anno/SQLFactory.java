package mr.zhang.databaseoperation.anno;

/**
 * Created by Mr. Zhang on 2016/10/24.
 */

public class SQLFactory {
    public static DbControl getDbControl(DBManager.DaoConfig daoConfig){
        return DbControl.getDB(daoConfig);
    }
}
