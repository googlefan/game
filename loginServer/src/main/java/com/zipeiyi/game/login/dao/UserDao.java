package com.zipeiyi.game.login.dao;

import com.zipeiyi.game.login.model.User;
import com.zipeiyi.xpower.dao.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by zhangxiaoqiang on 16/12/14.
 */
@Repository
public class UserDao {

    final IDao dao = DaoFactory.getIDao();
    final String bizName = "game";
    final Logger logger = LoggerFactory.getLogger(UserDao.class);

    public void insert(User user){
        String sql = "insert ignore into user(id,account,name,figure,auth,state,reg_time,reg_ip,site_connected) values (?,?,?,?,?,?,?,?,?)";
        dao.update(new OpUpdate(sql, bizName).addParams(user.getId(), user.getAccount(), user.getName(), user.getFigure(),user.getAuth(), user.getState(), user.getRegTime(), user.getRegIp(),user.getSiteConnected()));
    }


    public void updateLoginTime(User user){
        String sql = "update user set last_login_ip = ? ,last_login_time = now() where id = ?";
        dao.update(new OpUpdate(sql,bizName).addParams(user.getLastLoginIp(),user.getId()));
    }

    public User fetchUserByAccount(String account){
        String sql = "select * from user where account = ?";
        return dao.queryUniq(new DefaultOpUniq<User>(sql,bizName,new UserMapper()).addParams(account));
    }

    public User getUser(long userId){
        String sql = "select * from user where id = ?";
        return dao.queryUniq(new DefaultOpUniq<User>(sql,bizName,new UserMapper()).addParams(userId));
    }

    public void modifyUserState(long id,int state){
        String sql = "update user set state = ? where id = ?";
        dao.update(new OpUpdate(sql,bizName).addParams(state,id));
    }

    public void addLoginNum(long id){
        String sql = "update user set login_num = login_num + 1 where id = ?";
        dao.update(new OpUpdate(sql,bizName).addParams(id));
    }

    class UserMapper implements IRowMapper<User>{

        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getLong("id"));
            user.setAccount(rs.getString("account"));
            user.setName(rs.getString("name"));
            user.setAuth(rs.getInt("auth"));
            user.setState(rs.getInt("state"));
            Timestamp regTime = rs.getTimestamp("reg_time");
            if(regTime != null){
                user.setRegTime(new Date(regTime.getTime()));
            }else{
                user.setRegTime(null);
            }
            user.setRegIp(rs.getString("reg_ip"));
            user.setLastLoginIp(rs.getString("last_login_ip"));
            Timestamp lastLoginTime = rs.getTimestamp("last_login_time");
            if(lastLoginTime != null){
                user.setLastLoginTime(new Date(lastLoginTime.getTime()));
            }else{
                user.setLastLoginTime(null);
            }
            user.setDirect(rs.getBoolean("is_direct"));
            user.setSiteConnected(rs.getInt("site_connected"));
            user.setLoginNum(rs.getInt("login_num"));
            user.setFigure(rs.getInt("figure"));
            return user;
        }
    }
}
