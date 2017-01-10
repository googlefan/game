package com.zipeiyi.game.common.proto.pojo;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

import java.util.List;

/**
 * question::用户信息 需要从哪些地方获取？
 *
 * @author Administrator
 */
public class GameUserInfo {

    @Protobuf(fieldType = FieldType.INT64, required = true,order=1)
    Long userID;
    @Protobuf(fieldType = FieldType.STRING, required = true,order=2)
    String userName;
    @Protobuf(fieldType = FieldType.INT32, required = true,order=3)
    Integer sex;
    @Protobuf(fieldType = FieldType.STRING, required = true,order=4)
    String icon;

    @Protobuf(fieldType = FieldType.INT32, required = true,order=5)
    int chip;

    @Protobuf(fieldType = FieldType.INT32,order = 6)
    Integer status;

    @Protobuf(fieldType = FieldType.OBJECT,order = 7)
    List<CardInfo> cardList;

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public int getChip() {
        return chip;
    }

    public void setChip(int chip) {
        this.chip = chip;
    }

    public List<CardInfo> getCardList() {
        return cardList;
    }

    public void setCardList(List<CardInfo> cardList) {
        this.cardList = cardList;
    }

}
