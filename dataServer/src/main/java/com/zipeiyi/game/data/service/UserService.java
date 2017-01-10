package com.zipeiyi.game.data.service;

import com.zipeiyi.game.common.proto.pojo.CardInfo;
import com.zipeiyi.game.common.proto.pojo.UserInfo;
import com.zipeiyi.game.data.NettyServerStart;
import com.zipeiyi.game.data.dao.CardDao;
import com.zipeiyi.game.data.dao.UserDao;
import com.zipeiyi.game.data.dao.impl.CardDaoImpl;
import com.zipeiyi.game.data.dao.impl.UserDaoImpl;
import com.zipeiyi.game.data.model.Card;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zhuhui on 16-12-28.
 */
@Service
public class UserService {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    public void initUserProfile(long uid) {
        UserDao userDao = NettyServerStart.factory.getBean(UserDaoImpl.class);
        userDao.createUserProfile(uid);
    }

    /**
     * 游戏中获取用户信息
     * 用户的基本属性
     * 用户的卡牌信息
     *
     * @param uid
     * @return
     */
    public UserInfo getUserInfo(long uid) {
        UserDao userDao = NettyServerStart.factory.getBean(UserDaoImpl.class);
        CardDao cardDao = NettyServerStart.factory.getBean(CardDaoImpl.class);
        UpidService upidService = NettyServerStart.factory.getBean(UpidService.class);
        UserInfo userInfo = userDao.getUserInfo(uid);//用户信息
        if (userInfo == null) {
            return null;
        }
        List<Card> cardList = cardDao.getUserCards(uid);//用户的卡牌信息
        List<CardInfo> cardInfoList = upidService.castCard2CardInfo(cardList);
        userInfo.setCardList(cardInfoList);
        userInfo.setDiamond(1);// 钻石暂时没业务使用，固定1
        return userInfo;
    }

    public Integer updateUserInitStats(long uid) {
        UserDao userDao = NettyServerStart.factory.getBean(UserDaoImpl.class);
        return userDao.updateUserInitStats(uid);
    }

    public String getUserTicket(long uid) {
        UserDao userDao = NettyServerStart.factory.getBean(UserDaoImpl.class);
        return userDao.getUserTicket(uid);
    }
}
