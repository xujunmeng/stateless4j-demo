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

    private State state = State.OffHook;

    public Phone(String number) {
        this.number = number;
    }

    public void callDialed() {
        switch (state) {
            case OffHook:
                state = State.Ringing;
                break;
            default:
                throw new IllegalStateException("电话不在摘机状态，拨打无效");
        }
        System.out.println("电话当前状态：" + state);
    }

    public void hangup() {
        switch (state) {
            case Ringing:
                state = State.OffHook;
                break;
            case Connected:
                state = State.OffHook;
                break;
            case OnHold:
                state = State.OffHook;
                break;
            default:
                throw new IllegalStateException("电话尚未接通，挂断无效");
        }
        System.out.println("电话当前状态：" + state);
    }

    public void callConnected() {
        switch (state) {
            case Ringing:
                state = State.Connected;
                break;
            default:
                throw new IllegalStateException("电话不在振铃状态，接听无效");
        }
        System.out.println("电话当前状态：" + state);
    }
}
