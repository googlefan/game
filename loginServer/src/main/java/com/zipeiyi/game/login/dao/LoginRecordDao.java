package com.zipeiyi.game.login.dao;

import com.zipeiyi.game.login.model.LoginRecord;
import com.zipeiyi.xpower.dao.DaoFactory;
import com.zipeiyi.xpower.dao.IDao;
import com.zipeiyi.xpower.dao.OpInsert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

/**
 * Created by zhangxiaoqiang on 16/12/14.
 */
@Repository
public class LoginRecordDao {

    final IDao dao = DaoFactory.getIDao();
    final String bizName = "game";
    final Logger logger = LoggerFactory.getLogger(LoginRecordDao.class);


    public void insertLoginRecord(LoginRecord loginRecord){
        String sql = "insert into login_record (user_id,type,time,ip,user_agent) values (?,?,now(),?,?)";
        dao.insert(new OpInsert(sql,bizName,Long.class).addParams(loginRecord.getUserId(),loginRecord.getType(),loginRecord.getIp(),loginRecord.getUserAgent()));
    }
}
