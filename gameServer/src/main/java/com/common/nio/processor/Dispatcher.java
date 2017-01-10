package com.common.nio.processor;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.common.nio.socket.Session;
import com.zipeiyi.game.common.message.MessageReq;
import com.zipeiyi.game.common.message.MessageRes;

public class Dispatcher extends AbsDisPatcher{

	private static final Logger logger = LoggerFactory.getLogger(Dispatcher.class);

    private static volatile Dispatcher instance;
    
    public static Dispatcher getInstance(){
    	
    	if(instance == null){
    			instance = new Dispatcher();
    			return instance;
    	}
    	return instance;
    }
    
	
	public void dispatcher(Session session, MessageReq request) throws Exception {
		// TODO Auto-generated method stub
	    if ((session == null) || (request == null)) {
            return;
        }
		
		int module = request.getModuleId();
		int cmd = request.getCmd();
		MessageRes response = new MessageRes();
		response.setCmd(cmd);
		response.setSeque(request.getSeque());
		response.setUid(request.getUid());
		response.setModuleId(request.getModuleId());
//		response.setObj(obj);
		Processor processor = this.processors.get(Integer.valueOf(cmd));
        if(processor != null){
        	processor.process(session, request, response);
        }else{
        	if(logger.isDebugEnabled())
                logger.info(String.format("No Processor for module:[%d], cmd:[%d]", new Object[]{Integer.valueOf(module), Integer.valueOf(cmd)}));
               
                session.write(response);
        }
		
	}
	

}
