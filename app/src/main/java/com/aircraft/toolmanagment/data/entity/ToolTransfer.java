package com.example.toolmagapp.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.sql.Timestamp;
import java.sql.Timestamp;
import java.sql.Timestamp;
import java.sql.Timestamp;

@Entity
public class ToolTransfer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "tool_id")
    private Tool tool;
    @ManyToOne
    @JoinColumn(name = "old_borrower_id")
    private User oldBorrower;
    @ManyToOne
    @JoinColumn(name = "new_borrower_id")
    private User newBorrower;
    private Timestamp transferTime;
    private String transferReason;
    // 因 Java 无直接的 POINT 类型，可使用字符串或自定义类型替代，此处暂用字符串
    private String location;

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Tool getTool() {
        return tool;
    }

    public void setTool(Tool tool) {
        this.tool = tool;
    }

    public User getOldBorrower() {
        return oldBorrower;
    }

    public void setOldBorrower(User oldBorrower) {
        this.oldBorrower = oldBorrower;
    }

    public User getNewBorrower() {
        return newBorrower;
    }

    public void setNewBorrower(User newBorrower) {
        this.newBorrower = newBorrower;
    }

    public Timestamp getTransferTime() {
        return transferTime;
    }

    public void setTransferTime(Timestamp transferTime) {
        this.transferTime = transferTime;
    }

    public String getTransferReason() {
        return transferReason;
    }

    public void setTransferReason(String transferReason) {
        this.transferReason = transferReason;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}