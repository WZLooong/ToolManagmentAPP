
package com.aircraft.toolmanagment.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String phone;
    public String email;
    public String password;
    public String name;
    public String employee_id;
    public String team;
    public String role;

    // Room 不建议使用 Timestamp，建议用 long 或 String
    public long createdAt;
}