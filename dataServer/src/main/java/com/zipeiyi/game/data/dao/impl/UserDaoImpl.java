package com.zipeiyi.game.data.dao.impl;

import com.zipeiyi.game.common.proto.pojo.UserInfo;
import com.zipeiyi.game.data.dao.UserDao;
import com.zipeiyi.game.data.model.Card;
import com.zipeiyi.game.data.model.User;
import com.zipeiyi.game.data.model.UserR2Card;
import com.zipeiyi.xpower.dao.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by zhuhui on 16-12-2.
 */
@Repository
public class UserDaoImpl implements UserDao {
    final IDao dao = DaoFactory.getIDao();
    final String bizName = "game";
    final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);

    @Override
    public void createUserR2Card(UserR2Card ur) {
        StringBuffer sql = new StringBuffer();
        sql.append("INSERT INTO user_r2_card (user_id, card_id, create_time) VALUES(?, ?, ?)");
        dao.insert(new OpInsert<Integer>(sql, bizName, Integer.class).addParams(ur.getUserId(), ur.getUserId(), new Date()));
    }

    @Override
    public List<Card> getUserCards(User user) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * FROM ")
                .append("user_r2_card ")
                .append("WHERE ")
                .append("user_id = ? ");
        dao.queryList(new DefaultOpList<>(sql, bizName).addParams());
        return null;
    }

    @Override
    public void createUserProfile(long uid) {
        StringBuffer sql = new StringBuffer();
        sql.append("INSERT INTO user_profile (user_id, win_count, loss_count, escape_count, assets) VALUES(?, 0, 0, 0, 100000)");
        dao.insert(new OpInsert<>(sql, bizName, Integer.class).addParams(uid));
    }

    @Override
    public UserInfo getUserInfo(long uid) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT u.name AS name, u.figure AS icon, p.sex AS sex, p.assets AS chip, p.init_flg AS isNew FROM ")
                .append("user u ")
                .append("LEFT JOIN ")
                .append("user_profile p ")
                .append("ON ")
                .append("u.id = p.user_id ")
                .append("WHERE ")
                .append("u.id = ? ");
        return dao.queryUniq(new DefaultOpUniq<>(sql, bizName, (resultSet, i) -> {
            UserInfo userInfo = new UserInfo();
            userInfo.setUserID(uid);
            userInfo.setUserName(resultSet.getString("name"));
            userInfo.setIcon(resultSet.getString("icon"));
            userInfo.setSex(resultSet.getInt("sex"));
            userInfo.setChip(resultSet.getInt("chip"));
            userInfo.setNew(resultSet.getBoolean("isNew"));
            return userInfo;
        }).addParams(uid));
    }

    @Override
    public Integer updateUserInitStats(long uid) {
        StringBuffer sql = new StringBuffer();
        sql.append("UPDATE user_profile ")
                .append("SET ")
                .append("init_flg = 1 ")
                .append("WHERE ")
                .append("user_id = ? ");
        return dao.update(new OpUpdate(sql, bizName).addParams(uid));
    }

    @Override
    public String getUserTicket(long uid) {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ticket FROM ")
                .append("user_ticket ")
                .append("WHERE ")
                .append("user_id = ? ");
        return dao.queryUniq(new DefaultOpUniq<>(sql, bizName, (resultSet, i) -> resultSet.getString("ticket")).addParams(uid));
    }

}
