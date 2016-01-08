package com.example.ipcam.camer.util;


import com.example.ipcam.camer.entity.wifi;

import java.util.Comparator;


/**
 * wifi信号强度排序
 * 
 * @author Administrator
 *
 */
public class SortComparator implements Comparator {

	@Override
	public int compare(Object lhs, Object rhs) {
		// TODO Auto-generated method stub
		wifi a = (wifi) lhs;
		wifi b = (wifi) rhs;
		return a.getDbm0() - b.getDbm0();
	}

}
