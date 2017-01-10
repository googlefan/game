package com.zipeiyi.game.login.dao;

import com.zipeiyi.game.login.model.UserTicket;
import com.zipeiyi.xpower.dao.DaoFactory;
import com.zipeiyi.xpower.dao.IDao;
import com.zipeiyi.xpower.dao.OpUniq;
import com.zipeiyi.xpower.dao.OpUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by zhangxiaoqiang on 16/12/14.
 */
@Repository
public class UserTicketDao {

    final IDao dao = DaoFactory.getIDao();
    final String bizName = "game";
    final Logger logger = LoggerFactory.getLogger(UserTicketDao.class);

    public void createOrUpdateTicket(UserTicket userTicket){
        String sql = "insert into user_ticket (user_id,ticket,login_time) values (?,?,?) on duplicate key update ticket = values(ticket),login_time = values(login_time)";
        dao.update(new OpUpdate(sql,bizName).addParams(userTicket.getUserId(),userTicket.getTicket(),userTicket.getLoginTime()));
    }

    public String getUserTicket(String userId){
        String sql = "select ticket from user_ticket where user_id = ?";
        return dao.queryUniq(new OpUniq<String>(sql,bizName) {
            @Override
            protected String parse(ResultSet rs) throws SQLException {
                return rs.getString("ticket");
            }
        }.addParams(userId));
    }
}
