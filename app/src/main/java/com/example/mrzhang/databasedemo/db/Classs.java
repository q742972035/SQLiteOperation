package com.example.mrzhang.databasedemo.db;

import mr.zhang.databaseoperation.anno.anno.DBColumn;
import mr.zhang.databaseoperation.anno.anno.TableName;

/**
 * Created by Mr. Zhang on 2016/10/24.
 */
@TableName(tableName = "CLASS")
public class Classs {
    @DBColumn(columnName = "ID_CARD" ,isId = true)
    public String idCrad;
    @DBColumn(columnName = "NAME")
    public String name;
    @DBColumn(columnName = "GRADE")
    public String grade;
    @DBColumn(columnName = "CLASS")
    public String clazz;
    @DBColumn(columnName = "FINGER",isUnique = true)
    public String finger;

    public Classs(){}

    public Classs(String idCrad, String name, String grade, String clazz, String finger) {
        this.idCrad = idCrad;
        this.name = name;
        this.grade = grade;
        this.clazz = clazz;
        this.finger = finger;
    }

    public interface ColumnName {
        String idCrad = "ID_CARD";
        String name = "NAME";
        String grade = "GRADE";
        String clazz = "CLASS";
        String finger = "FINGER";
    }
}
