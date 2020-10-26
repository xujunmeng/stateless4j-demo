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
    来电事件,
    /**
     * 语音留言
     */
    挂断电话事件,
    /**
     * 接听
     */
    接听事件,
    /**
     * 语音留言
     */
    语音留言事件,
    /**
     * 保持
     */
    通话保持事件,
    /**
     * 取消保持
     */
    取消通话保持事件
}
