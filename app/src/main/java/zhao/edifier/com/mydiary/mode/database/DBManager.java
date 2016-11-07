package zhao.edifier.com.mydiary.mode.database;

import org.xutils.DbManager;
import org.xutils.db.Selector;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.File;
import java.util.List;

/**
 * Created by tech57 on 2016/9/2.
 */
public class DBManager {

    protected static DBManager dbManager;
    protected DbManager db;

    public static DBManager newInstance() {
        if(dbManager==null)dbManager = new DBManager();
        return dbManager;
    }

    public DBManager() {

        DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
                .setDbName("notepaper.db")
                        // 不设置dbDir时, 默认存储在app的私有目录.
//                .setDbDir(new File("/sdcard")) // "sdcard"的写法并非最佳实践, 这里为了简单, 先这样写了.
//                .setDbDir(new File("/sdcard")) // "sdcard"的写法并非最佳实践, 这里为了简单, 先这样写了.
                .setDbVersion(1)
                .setDbOpenListener(new DbManager.DbOpenListener() {
                    @Override
                    public void onDbOpened(DbManager db) {
                        // 开启WAL, 对写入加速提升巨大
                        db.getDatabase().enableWriteAheadLogging();
                    }
                })
                .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                    @Override
                    public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                        // TODO: ...
                        // db.addColumn(...);
                        // db.dropTable(...);
                        // ...
                        // or
                        // db.dropDb();
                    }
                });
        db = x.getDb(daoConfig);
    }


    public void saveObj(Object object){

        try {
            db.saveOrUpdate(object);
        } catch (DbException e) {
            e.printStackTrace();
        }

    }

    public void deleteObj(Object object){
        try {
            db.delete(object);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public List<Object> findAllESC(Class cla,String parame){
        List<Object> list = null;
        try {
//            list = db.selector(cla).orderBy(parame, false).getOrderByList();
            list = db.selector(cla).orderBy(parame, true).limit(1000).findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }

        return list;
    }


}
