package cn.invonate.ygoa3.Util.pingyin

import cn.invonate.ygoa3.Entry.LocalContacts
import java.util.*

/**
 *
 *
 */
class PinyinComparator3 : Comparator<LocalContacts> {

    override fun compare(o1: LocalContacts, o2: LocalContacts): Int {
        return if (o1.sortLetters == "@" || o2.sortLetters == "#") {
            -1
        } else if (o1.sortLetters == "#" || o2.sortLetters == "@") {
            1
        } else {
            o1.sortLetters.compareTo(o2.sortLetters)
        }
    }

}
