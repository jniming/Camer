package com.example.ipcam.camer.util;

import com.example.ipcam.camer.entity.AlarmMsg;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;




/**
 * 报警数据排序
 * 
 * @author Administrator
 *
 */
public class AlarmSortComparator implements Comparator {

	@Override
	public int compare(Object lhs, Object rhs) {
		// TODO Auto-generated method stub
		AlarmMsg a = (AlarmMsg) lhs;
		AlarmMsg b = (AlarmMsg) rhs;
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");

		long a_date = 0;
		long b_date = 0;
		try {
			a_date = (dateFormat.parse(a.getTime()).getTime());
			b_date = (dateFormat.parse(b.getTime()).getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		long temp = b_date - a_date;

		if (temp > 0) {
			return 1;
		} else if (temp == 0) {
			return 0;
		} else {
			return -1;
		}

	}

}
