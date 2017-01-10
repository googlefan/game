package com.zipeiyi.game.login.dao;

import com.zipeiyi.game.login.model.RegRecord;
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
public class RegRecordDao {

    final IDao dao = DaoFactory.getIDao();
    final String bizName = "game";
    final Logger logger = LoggerFactory.getLogger(RegRecordDao.class);

    public void insertRegRecord(RegRecord regRecord){
        String sql = "insert into reg_record (user_id,type,time,ip,user_agent) values (?,?,now(),?,?)";
        dao.insert(new OpInsert(sql,bizName,Long.class).addParams(regRecord.getUserId(),regRecord.getType(),regRecord.getIp(),regRecord.getUserAgent()));
    }
}
