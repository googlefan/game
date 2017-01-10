package com.zipeiyi.game.data.dao;

import com.zipeiyi.game.common.proto.pojo.UserInfo;
import com.zipeiyi.game.data.model.Card;
import com.zipeiyi.game.data.model.User;
import com.zipeiyi.game.data.model.UserR2Card;

import java.util.List;

/**
 * Created by zhuhui on 16-12-2.
 */
public interface UserDao {
    void createUserR2Card(UserR2Card userR2Card);

    List<Card> getUserCards(User user);

    void createUserProfile(long uid);

    UserInfo getUserInfo(long uid);

    Integer updateUserInitStats(long uid);

    String getUserTicket(long uid);
}
