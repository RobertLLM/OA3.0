package cn.invonate.ygoa3.Util.pingyin;

import java.util.Comparator;

import cn.invonate.ygoa3.Entry.CyContacts;

/**
 *
 *
 */
public class PinyinComparator2 implements Comparator<CyContacts.CyContactsBean> {

	@Override
	public int compare(CyContacts.CyContactsBean o1, CyContacts.CyContactsBean o2) {
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
