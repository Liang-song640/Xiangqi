package edu.sustech.xiangqi.model;

import java.io.Serializable;

/**
 * 用户类，用于存储用户信息
 */
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private String username;
    private String passwordHash; //存储密码哈希而非明文
    private boolean isGuest; //是否为游客

    /**
     * 创建普通用户
     * @param username 用户名
     * @param passwordHash 密码哈希值
     */
    public User(String username, String passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.isGuest = false;
    }

    /**
     * 创建游客用户
     */
    public User() {
        this.username = "Guest";
        this.passwordHash = "";
        this.isGuest = true;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public boolean isGuest() {
        return isGuest;
    }

    @Override
    public String toString() {
        return isGuest ? "Guest" : username;
    }
}