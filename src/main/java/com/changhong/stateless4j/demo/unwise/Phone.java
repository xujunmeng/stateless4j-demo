package com.changhong.stateless4j.demo.unwise;

import com.changhong.stateless4j.demo.state.State;
import lombok.Getter;

/**
 * 电话  包含以下状态
 * CallDialed,
 * HungUp,
 * CallConnected,
 * LeftMessage,
 * PlacedOnHold,
 * TakenOffHold
 *
 * @author L.X <xu1.luo@changhong.com>
 */
public class Phone {

    @Getter
    private String number; // 电话号码

    private State state = State.摘机状态;

    public Phone(String number) {
        this.number = number;
    }

    public void callDialed() {
        switch (state) {
            case 摘机状态:
                state = State.振铃状态;
                break;
            default:
                throw new IllegalStateException("电话不在摘机状态，拨打无效");
        }
        System.out.println("电话当前状态：" + state);
    }

    public void hangup() {
        switch (state) {
            case 振铃状态:
                state = State.摘机状态;
                break;
            case 通话中状态:
                state = State.摘机状态;
                break;
            case 通话保持状态:
                state = State.摘机状态;
                break;
            default:
                throw new IllegalStateException("电话尚未接通，挂断无效");
        }
        System.out.println("电话当前状态：" + state);
    }

    public void callConnected() {
        switch (state) {
            case 振铃状态:
                state = State.通话中状态;
                break;
            default:
                throw new IllegalStateException("电话不在振铃状态，接听无效");
        }
        System.out.println("电话当前状态：" + state);
    }
}
