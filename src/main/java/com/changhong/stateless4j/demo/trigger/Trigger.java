package com.changhong.stateless4j.demo.trigger;

/**
 * 来电触发器
 *
 * @author L.X <xu1.luo@changhong.com>
 */
public enum Trigger {

    /**
     * 来电
     */
    CallDialed,
    /**
     * 语音留言
     */
    HungUp,
    /**
     * 接听
     */
    CallConnected,
    /**
     * 语音留言
     */
    LeftMessage,
    /**
     * 保持
     */
    PlacedOnHold,
    /**
     * 取消保持
     */
    TakenOffHold
}
