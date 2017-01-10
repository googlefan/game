package com.zipeiyi.game.login.service.user;


import com.zipeiyi.game.login.model.UserConnect;

public interface UserConnectService {

    public UserConnect findByUserId(long userId);
    public void addUserConnect(long siteId, int site, long userId, String email, String name);
    public UserConnect getBySiteAndSiteEmail(int site, String email);
    public UserConnect getBySiteAndSiteId(int site, long siteId);
}