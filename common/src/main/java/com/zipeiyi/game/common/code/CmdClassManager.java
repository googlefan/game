/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.zipeiyi.game.common.code;

import java.util.Map;

import com.google.common.collect.Maps;

/**
 * @author tpp
 */
public class CmdClassManager {
	
        public static Map<Integer, Class> req_obj_class = Maps.newHashMap();
        public static Map<Integer, Class> res_obj_class = Maps.newHashMap();
        
		public static Map<Integer, Class> getReq_obj_class() {
			return req_obj_class;
		}
		public static void setReq_obj_class(Map<Integer, Class> req_obj_class) {
			CmdClassManager.req_obj_class = req_obj_class;
		}
		public static Map<Integer, Class> getRes_obj_class() {
			return res_obj_class;
		}
		public static void setRes_obj_class(Map<Integer, Class> res_obj_class) {
			CmdClassManager.res_obj_class = res_obj_class;
		}
        
        
}
