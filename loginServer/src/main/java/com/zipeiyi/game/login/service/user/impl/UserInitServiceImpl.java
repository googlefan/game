package com.zipeiyi.game.login.service.user.impl;

import com.zipeiyi.game.login.dao.UserInitDao;
import com.zipeiyi.game.login.model.Card;
import com.zipeiyi.game.login.model.UserR2Card;
import com.zipeiyi.game.login.service.user.UserInitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by zhuhui on 17-1-2.
 */
@Service(value="userInitService")
public class UserInitServiceImpl implements UserInitService {

    @Autowired
    UserInitDao userInitDao;

    @Override
    public void initUser(long uid) {
        // 初始化用户筹码
        userInitDao.initUserProfile(uid);
        // 注册赠送3张卡牌
        List<Card> unUsedCards = userInitDao.getUnusedCards();
        for (Card unUsedCard : unUsedCards) {
            UserR2Card userR2Card = new UserR2Card();
            userR2Card.setUserId(uid);
            userR2Card.setCardId(unUsedCard.getId());
            userR2Card.setCreateTime(new Date());
            userInitDao.createUserR2Card(userR2Card);
            unUsedCard.setUsed(true);
            userInitDao.markUsedCard(unUsedCard);
        }
    }
}
