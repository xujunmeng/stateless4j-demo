package com.changhong.stateless4j.demo.context;

import com.changhong.stateless4j.demo.unwise.Phone;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * 电话使用者
 *
 * @author L.X <xu1.luo@changhong.com>
 */
@Slf4j
public class PhoneUser {

    public static void main(String[] args) {
        Phone phone = new Phone("2266007");
        try {
            phone.callConnected();
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        try {
            TimeUnit.SECONDS.sleep(3);
            phone.callDialed();
            TimeUnit.SECONDS.sleep(3);
            phone.callConnected();
            TimeUnit.SECONDS.sleep(3);
            phone.hangup();
        } catch (InterruptedException ex) {
            log.error(ex.getMessage());
        }
    }
}
