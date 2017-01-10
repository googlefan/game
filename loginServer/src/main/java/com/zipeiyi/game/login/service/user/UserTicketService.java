package com.zipeiyi.game.login.service.user;


import com.zipeiyi.game.login.model.UserTicket;

/**
 * 票
 * @author zhangxiaoqiang
 */
public interface UserTicketService {

    public void createOrUpdateTicket(UserTicket userTicket);

    public String fetchUserTicket(String userId);
}