package com.zipeiyi.game.login.service.user.impl;

import com.zipeiyi.game.login.dao.UserTicketDao;
import com.zipeiyi.game.login.model.UserTicket;
import com.zipeiyi.game.login.service.user.UserTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by zhangxiaoqiang on 16/12/14.
 */
@Service(value="userTicketService")
public class UserTicketServiceImpl implements UserTicketService {

    @Autowired
    protected UserTicketDao userTicketDao;

    @Override
    public void createOrUpdateTicket(UserTicket userTicket) {
        userTicketDao.createOrUpdateTicket(userTicket);
    }

    @Override
    public String fetchUserTicket(String userId) {
         return userTicketDao.getUserTicket(userId);
    }

}
