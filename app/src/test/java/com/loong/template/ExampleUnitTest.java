package com.loong.template;

import org.junit.Test;

import static org.junit.Assert.*;

import android.util.Log;

import com.loong.common.utils.PasswordUtil;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);

    }


    @Test
    public void passwordCheck() throws Exception {
        String phone = "18515970756";
        String encode = PasswordUtil.encrypt2(phone,Constants.SECRET_KEY);
        String decode = PasswordUtil.decrypt2(encode,Constants.SECRET_KEY);

        assertEquals(phone, decode);
    }
}