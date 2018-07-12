package cn.invonate.ygoa3.Util.pingyin

import cn.invonate.ygoa3.Entry.Contacts
import java.util.*

/**
 *
 *
 */
class PinyinComparator : Comparator<Contacts> {

    override fun compare(o1: Contacts, o2: Contacts): Int {
        return if (o1.sortLetters == "@" || o2.sortLetters == "#") {
            -1
        } else if (o1.sortLetters == "#" || o2.sortLetters == "@") {
            1
        } else {
            o1.sortLetters.compareTo(o2.sortLetters)
        }
    }

}
