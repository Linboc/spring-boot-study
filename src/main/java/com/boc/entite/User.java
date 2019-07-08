package com.boc.entite;

/**
 * @author 我什么都不知道
 *
 */
public class User {

    private Integer id;
    
    private String name;
    
    private String pass;
    
    private String nickName;
    
    private String createDate;
    
    private String premission;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getPremission() {
        return premission;
    }

    public void setPremission(String premission) {
        this.premission = premission;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", name=" + name + ", pass=" + pass + ", nickName=" + nickName + ", createDate="
                + createDate + ", premission=" + premission + "]";
    }
    
}
