package com.changhong.stateless4j.demo.state;

/**
 * 来电状态
 *
 * @author L.X <xu1.luo@changhong.com>
 */
public enum State {

    /**
     * 摘机
     */
    OffHook,
    /**
     * 振铃
     */
    Ringing,
    /**
     * 通话中
     */
    Connected,
    /**
     * 通话保持
     */
    OnHold
}
