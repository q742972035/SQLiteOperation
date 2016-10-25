package com.example.mrzhang.databasedemo.db;

import mr.zhang.databaseoperation.anno.anno.DBColumn;
import mr.zhang.databaseoperation.anno.anno.TableName;

/**
 * Created by Mr. Zhang on 2016/10/25.
 */
@TableName(tableName = "RESULT")
public class Result {
    @DBColumn(columnName = "ID_CARD",isId = true)
    public String idCard;
    @DBColumn(columnName = "NAME")
    public String name;
    @DBColumn(columnName = "AGE")
    public String age;
    @DBColumn(columnName = "CHINESE")
    public String chinese;
    @DBColumn(columnName = "MATH")
    public String math;

    public Result(){

    }

    public Result(String idCard, String name, String age, String chinese, String math) {
        this.idCard = idCard;
        this.name = name;
        this.age = age;
        this.chinese = chinese;
        this.math = math;
    }

    @Override
    public String toString() {
        return "Result{" +
                "idCard='" + idCard + '\'' +
                ", name='" + name + '\'' +
                ", age='" + age + '\'' +
                ", chinese='" + chinese + '\'' +
                ", math='" + math + '\'' +
                '}';
    }
}
