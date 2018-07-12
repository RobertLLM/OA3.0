package cn.invonate.ygoa3.Util

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class MD5 {
    companion object {

        private val strDigits = arrayOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f")

        private fun byteToArrayString(bByte: Byte): String {
            var iRet = bByte.toInt()
            // System.out.println("iRet="+iRet);
            if (iRet < 0) {
                iRet += 256
            }
            val iD1 = iRet / 16
            val iD2 = iRet % 16
            return strDigits[iD1] + strDigits[iD2]
        }

        private fun byteToNum(bByte: Byte): String {
            var iRet = bByte.toInt()
            if (iRet < 0) {
                iRet += 256
            }
            return iRet.toString()
        }

        private fun byteToString(bByte: ByteArray): String {
            val sBuffer = StringBuilder()
            for (aBByte in bByte) {
                sBuffer.append(byteToArrayString(aBByte))
            }
            return sBuffer.toString()
        }

        fun GetMD5Code(strObj: String): String {
            var resultString: String? = null
            try {
                resultString = strObj
                val md = MessageDigest.getInstance("MD5")
                resultString = byteToString(md.digest(strObj.toByteArray()))
            } catch (ex: NoSuchAlgorithmException) {
                ex.printStackTrace()
            }

            return resultString!!
        }
    }
}