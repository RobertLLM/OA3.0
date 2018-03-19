package cn.invonate.ygoa3.Util.pingyin;

import java.util.Comparator;

import cn.invonate.ygoa3.Entry.Contacts;

/**
 *
 *
 */
public class PinyinComparator implements Comparator<Contacts> {

	@Override
	public int compare(Contacts o1, Contacts o2) {
		if (o1.getSortLetters().equals("@")
				|| o2.getSortLetters().equals("#")) {
			return -1;
		} else if (o1.getSortLetters().equals("#")
				|| o2.getSortLetters().equals("@")) {
			return 1;
		} else {
			return o1.getSortLetters().compareTo(o2.getSortLetters());
		}
	}

}
