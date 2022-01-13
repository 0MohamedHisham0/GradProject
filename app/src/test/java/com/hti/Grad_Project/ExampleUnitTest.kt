package com.hti.Grad_Project

import org.junit.Test




/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val link : String = ""
        val idStr: String = link.substring(link.lastIndexOf("/media/") + 7)
        val last = idStr.substring(idStr.lastIndexOf("/"))
        val done = idStr.replace(last, "")
        print(done)
        print(idStr)
    }

}