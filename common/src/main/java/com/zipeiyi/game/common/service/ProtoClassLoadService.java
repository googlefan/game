package com.zipeiyi.game.common.service;

import com.google.common.collect.Maps;
import com.zipeiyi.game.common.ModuleCmd;
import com.zipeiyi.game.common.code.CmdClassManager;
import com.zipeiyi.game.common.util.PkgScanner;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ProtoClassLoadService {

    private static ProtoClassLoadService instance;

    public static ProtoClassLoadService getInstance() {
        if (instance == null) {
            instance = new ProtoClassLoadService();
        }
        return instance;
    }

    public void Init() throws IOException, ClassNotFoundException {

        PkgScanner scanner = new PkgScanner("com.zipeiyi.game.common.proto");
        List<String> list = scanner.scan();
        Map<Integer, Class> protoReqClass = Maps.newHashMap();
        Map<Integer, Class> protoAckClass = Maps.newHashMap();


        Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {

            String l = iterator.next();

            Class clazz = this.getClass().getClassLoader().loadClass(l);
            String simpleName = clazz.getSimpleName();

            if (simpleName.endsWith("Req")) {
                simpleName = simpleName.substring(0, simpleName.length() - 3);
                int cmdVal = ModuleCmd.moduleCmd.getValueByName(simpleName);
                if (cmdVal >= 0) {
                    protoReqClass.put(cmdVal, clazz);
                }

            } else if (simpleName.endsWith("Ack")) {
                simpleName = simpleName.substring(0, simpleName.length() - 3);
                int cmdVal = ModuleCmd.moduleCmd.getValueByName(simpleName);
                if (cmdVal >= 0) {
                    protoAckClass.put(cmdVal, clazz);
                }
            } else if (simpleName.endsWith("Push")) {
                simpleName = simpleName.substring(0, simpleName.length() - 4);
                int cmdVal = ModuleCmd.moduleCmd.getValueByName(simpleName);
                if (protoAckClass.get(cmdVal) == null) {
                    protoAckClass.put(cmdVal, clazz);
                }
            } else {
                continue;
            }
            System.out.println(simpleName);
        }
        CmdClassManager.setReq_obj_class(protoReqClass);
        CmdClassManager.setRes_obj_class(protoAckClass);
    }

//	public static void main(String args[]) {
//		ProtoClassLoadService instance1 = new ProtoClassLoadService();
//		try {
//			try {
//				instance1.Init();
//			} catch (ClassNotFoundException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
}
