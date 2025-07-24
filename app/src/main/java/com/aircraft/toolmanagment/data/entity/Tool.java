
package com.aircraft.toolmanagment.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tools")
public class Tool {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String name;
    public String model;
    public String specification;
    public int quantity;
    public String manufacturer;
    public long purchaseDate;
    public String storageLocation;
    public String barcode;
    public String status;
    public long updatedAt;

    public Tool() {}
}