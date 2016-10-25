package com.example.mrzhang.databasedemo.db;

import java.util.UUID;

import mr.zhang.databaseoperation.anno.anno.DBColumn;
import mr.zhang.databaseoperation.anno.anno.TableName;

/**
 * Created by Mr. Zhang on 2016/10/25.
 */
@TableName(tableName = "HOUSE")
public class House {
    @DBColumn(columnName = "_ID" ,isId = true)
    private String _id;
    @DBColumn(columnName = "HOUST_ID",isUnique = true)
    private String houseId;
    @DBColumn(columnName = "MASTER")
    private String master;

    public House(){}

    public House(String houseId, String master) {
        this._id = UUID.randomUUID().toString();
        this.houseId = houseId;
        this.master = master;
    }

    public String getId() {
        return _id;
    }

    public String getHouseId() {
        return houseId;
    }

    public void setHouseId(String houseId) {
        this.houseId = houseId;
    }

    public String getMaster() {
        return master;
    }

    public void setMaster(String master) {
        this.master = master;
    }
}
