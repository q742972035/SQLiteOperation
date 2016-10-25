package com.example.mrzhang.databasedemo;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.mrzhang.databasedemo.db.Classs;
import com.example.mrzhang.databasedemo.db.House;
import com.example.mrzhang.databasedemo.db.Result;

import java.util.ArrayList;
import java.util.List;

import mr.zhang.databaseoperation.anno.DBManager;
import mr.zhang.databaseoperation.anno.DbUpdateCallBack;
import mr.zhang.databaseoperation.anno.SQLFactory;
import mr.zhang.databaseoperation.anno.sqlbuilder.OrderByBuilder;
import mr.zhang.databaseoperation.anno.sqlbuilder.WhereBuilder;

public class MainActivity extends AppCompatActivity {
    private static final List<Class> classes = new ArrayList<Class>(){
        {
            add(Classs.class);
            add(Result.class);
            add(House.class);
        }
    };

    DBManager.DaoConfig daoConfig = new DBManager.DaoConfig()
            .setDbVersion(2)
            .setDbName("test.db")
            .setDbClass(classes)
            .setContext(this)
            .setUpdateCallBack(new DbUpdateCallBack() {
                @Override
                public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                }
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DBManager manager = SQLFactory.getDbControl(daoConfig);

        List<Result> results = new ArrayList<>();
        results.add(new Result("12110","大小","29","54","120"));
        results.add(new Result("12111","小大","26","76","19"));
        results.add(new Result("12112","大大","25","44","2"));
        results.add(new Result("12113","大三","25","97","99"));
        results.add(new Result("12114","小妞","23","76","100"));
        results.add(new Result("12115","小三","22","60","60"));
        results.add(new Result("12116","小张","20","100","120"));
        results.add(new Result("12117","大张","25","54","20"));
        results.add(new Result("12118","大大","20","44","120"));
        try {
            manager.createOrUpdate(results);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //查找名字等于大小的全部House
        List<Result> all = manager
                .selector(Result.class)
                .where("NAME", WhereBuilder.Operation.EQUALS, "大小")
                .findAll();

        // 根据语文成绩降序，从第三条开始共查找5条。
        List<Result> all1 = manager.selector(Result.class)
                .order(new String[]{"CHINESE"}, new OrderByBuilder.Operation[]{OrderByBuilder.Operation.DESC})
                .limit(2, 5).findAll();
//        for (Result result : all1) {
//            Log.d("MainActivity", result.toString());
//        }

        // 按照插入顺序，从第二条开始查询，共查询4条
        List<Result> all2 = manager.selector(Result.class).limit(1, 4).findAll();
//        for (Result result : all2) {
//            Log.d("MainActivity", result.toString());
//        }

        // 查询语文成绩在50和99直接，数学成绩大于60，并且 并且以“大”开始，并且“大”后面仅有一个字符的结果。
        List<Result> all3 = manager.selector(Result.class)
                .where("CHINESE", WhereBuilder.Operation.BETWEEN, "50", "99")
                .next(WhereBuilder.Symbol.AND, "MATH", WhereBuilder.Operation.MORE, "60")
                .next(WhereBuilder.Symbol.AND, "NAME", WhereBuilder.Operation.LIKE, "大_").findAll();
        for (Result result : all3) {
            Log.d("MainActivity", result.toString());
        }
    }
}
