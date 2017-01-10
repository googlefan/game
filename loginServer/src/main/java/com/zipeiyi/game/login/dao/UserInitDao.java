package com.zipeiyi.game.login.dao;

import com.zipeiyi.game.login.model.Card;
import com.zipeiyi.game.login.model.UserR2Card;
import com.zipeiyi.xpower.dao.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by zhuhui on 17-1-2.
 */
@Repository
public class UserInitDao {
    final IDao dao = DaoFactory.getIDao();
    final String bizName = "game";
    final Logger logger = LoggerFactory.getLogger(UserDao.class);

    public List<Card> getUnusedCards() {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * FROM card ")
                .append("WHERE ")
                .append("used <> 1 ")
                .append("LIMIT 3 ");
        return dao.queryList(new DefaultOpList<Card>(sql, bizName, (resultSet, i) -> {
            Card card = new Card();
            card.setId(resultSet.getLong("id"));
            card.setUpidId(resultSet.getString("upid_id"));
            return card;
        }));
    }

    public void markUsedCard(Card unusedCard) {
        StringBuffer sql = new StringBuffer();
        sql.append("UPDATE card ")
                .append("SET used = 1 ")
                .append("WHERE id = ? ");
        dao.update(new OpUpdate(sql, bizName).addParams(unusedCard.getId()));
    }

    public void createUserR2Card(UserR2Card ur) {
        StringBuffer sql = new StringBuffer();
        sql.append("INSERT INTO user_r2_card (user_id, card_id, create_time) VALUES(?, ?, ?)");
        dao.insert(new OpInsert<Integer>(sql, bizName, Integer.class).addParams(ur.getUserId(), ur.getCardId(), new Date()));
    }

    public void initUserProfile(long uid) {
        StringBuffer sql = new StringBuffer();
        sql.append("INSERT user_profile ")
                .append("(user_id, win_count, loss_count, escape_count, assets, sex, init_flg) ")
                .append("VALUES(?, 0, 0, 0, 100000, 1, 1) ");
        dao.insert(new OpInsert<>(sql, bizName, Integer.class).addParams(uid));
    }
}
