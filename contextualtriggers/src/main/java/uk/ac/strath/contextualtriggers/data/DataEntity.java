package uk.ac.strath.contextualtriggers.data;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverter;

@Entity(tableName = "data_cache")
public class DataEntity {


    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo
    public Data data;




}