package com.zipeiyi.game.login.service.user.impl;

import com.zipeiyi.game.login.dao.UserConnectDao;
import com.zipeiyi.game.login.model.UserConnect;
import com.zipeiyi.game.login.service.user.UserConnectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value="userConnectService")
public class UserConnectServiceImpl implements UserConnectService {

    @Autowired
    private UserConnectDao userConnectDao;

    @Override
    public UserConnect findByUserId(long userId) {
        return userConnectDao.findByUserId(userId);
    }

    @Override
    public UserConnect getBySiteAndSiteEmail(int site, String email) {
        return userConnectDao.getBySiteAndEmail(site, email);
    }

    @Override
    public void addUserConnect(long siteId, int site, long userId, String email, String name){
        UserConnect userConnect = new UserConnect();
        userConnect.setName(name);
        userConnect.setSite(site);
        userConnect.setSiteId(siteId);
        userConnect.setUserId(userId);
        userConnect.setSiteEmail(email);
        userConnectDao.insert(userConnect);
    }

    @Override
    public UserConnect getBySiteAndSiteId(int site, long siteId) {
        UserConnect uc =  userConnectDao.getBySiteAndSiteId(site, siteId);
        return uc;
    }
}
