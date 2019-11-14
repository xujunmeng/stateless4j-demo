package com.changhong.stateless4j.demo;

import com.changhong.stateless4j.demo.context.PhoneCallStateMachineContext;
import com.changhong.stateless4j.demo.state.State;
import com.changhong.stateless4j.demo.trigger.Trigger;
import com.github.oxo42.stateless4j.StateMachine;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

@Slf4j
@SpringBootTest
class DemoApplicationTests {

    @Test
    void dial() throws Exception {
        PhoneCallStateMachineContext context = new PhoneCallStateMachineContext();
        StateMachine<State, Trigger> phoneCall = context.stateMachine();

        log.info("开始拨号");
        phoneCall.fire(Trigger.CallDialed);
        Assertions.assertEquals(State.Ringing, phoneCall.getState());

        log.info("接听");
        phoneCall.fire(Trigger.CallConnected);
        Assertions.assertEquals(State.Connected, phoneCall.getState());
        TimeUnit.MILLISECONDS.sleep(3100);

        log.info("挂起");
        phoneCall.fire(Trigger.PlacedOnHold);
        Assertions.assertEquals(State.OnHold, phoneCall.getState());
        TimeUnit.MILLISECONDS.sleep(3100);

        log.info("解除挂起");
        phoneCall.fire(Trigger.TakenOffHold);
        Assertions.assertEquals(State.Connected, phoneCall.getState());
        TimeUnit.MILLISECONDS.sleep(3100);

        log.info("挂断");
        phoneCall.fire(Trigger.HungUp);
        Assertions.assertEquals(State.OffHook, phoneCall.getState());
        Assertions.assertEquals(0, context.getCallTime());
        log.info("通话结束");
    }

    @Test
    void leftMessage() throws Exception {
        PhoneCallStateMachineContext context = new PhoneCallStateMachineContext();
        StateMachine<State, Trigger> phoneCall = context.stateMachine();

        log.info("开始拨号");
        phoneCall.fire(Trigger.CallDialed);
        Assertions.assertEquals(State.Ringing, phoneCall.getState());

        log.info("接听");
        phoneCall.fire(Trigger.CallConnected);
        Assertions.assertEquals(State.Connected, phoneCall.getState());
        TimeUnit.MILLISECONDS.sleep(3100);
        // log.info("当前通话时间: {}", context.getCallTime());

        log.info("留言");
        phoneCall.fire(Trigger.LeftMessage);
        Assertions.assertEquals(State.OffHook, phoneCall.getState());
        log.info("通话结束");
    }
}
