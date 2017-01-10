package com.zipeiyi.game.login.dao;

import com.zipeiyi.game.login.model.UserConnect;
import com.zipeiyi.xpower.dao.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * Created by zhangxiaoqiang on 16/12/15.
 */
@Repository
public class UserConnectDao {

    final IDao dao = DaoFactory.getIDao();
    final String bizName = "game";
    final Logger logger = LoggerFactory.getLogger(UserConnectDao.class);

    public void insert(UserConnect userConnect){
        String sql = "insert into user_connect(site,site_id,site_email,user_id,name,connect_date) values (?,?,?,?,?,now())";
        dao.update(new OpUpdate(sql, bizName).addParams(userConnect.getSite(),userConnect.getSiteId(),userConnect.getSiteEmail(),userConnect.getUserId(),userConnect.getName()));
    }

    public UserConnect getBySiteAndEmail(int site, String email){
        String sql = "select * from user_connect where site = ? and site_email = ?";
        return dao.queryUniq(new DefaultOpUniq<UserConnect>(sql,bizName,new UserConnectMapper()).addParams(site,email));
    }

    public UserConnect findByUserId(long userId){
        String sql = "select * from user_connect where user_id = ?";
        return dao.queryUniq(new DefaultOpUniq<UserConnect>(sql,bizName,new UserConnectMapper()).addParams(userId));
    }

    public UserConnect getBySiteAndSiteId(int site, long siteId){
        String sql = "select * from user_connect where site = ? and site_id = ?";
        return dao.queryUniq(new DefaultOpUniq<UserConnect>(sql,bizName,new UserConnectMapper()).addParams(site,siteId));
    }

    class UserConnectMapper implements IRowMapper<UserConnect>{

        @Override
        public UserConnect mapRow(ResultSet rs, int rowNum) throws SQLException {
            UserConnect userConnect = new UserConnect();
            userConnect.setName(rs.getString("name"));
            userConnect.setSiteEmail(rs.getString("site_email"));
            userConnect.setUserId(rs.getLong("user_id"));
            userConnect.setSiteId(rs.getLong("site_id"));
            userConnect.setSite(rs.getInt("site"));
            userConnect.setId(rs.getLong("id"));
            userConnect.setConnectDate(new Date(rs.getTimestamp("connect_date").getTime()));
            return userConnect;
        }
    }

}
