package com.game.db.redis;


import java.util.Map;

import com.game.vo.ChairVo;
import com.game.vo.TableVo;
import com.game.vo.UserVo;

public interface RedisDao {

	public TableVo getTableVo(String tableID);	
	public void getAllTableVos();	
	public TableVo getNotFullTable(); //维持count：tableID队列
	public void saveTableVo(TableVo tableVo);
	public void delTableVo(String tableID);
	
	public ChairVo getChairVo();
	public void saveChairVo(ChairVo chairVo);
	public void delChairVo();
	
	public UserVo getUserVoById(long uid);	
	public void saveUserVo(UserVo user);
	public void delUserVo(long uid);
	
	
	public Map<String, Double> getNoFullTableIds() ;
	
}
