package com.changhong.stateless4j.demo.context;

import com.changhong.stateless4j.demo.state.State;
import com.changhong.stateless4j.demo.trigger.Trigger;
import com.github.oxo42.stateless4j.StateMachine;
import com.github.oxo42.stateless4j.StateMachineConfig;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 来电状态机上下文
 *
 * @author L.X <xu1.luo@changhong.com>
 */
@Slf4j
public class PhoneCallStateMachineContext {

    @Getter
    private int callTime = 0;
    @Getter
    private ThreadPoolTaskExecutor executor;
    private volatile boolean stopTimer = false;

    public PhoneCallStateMachineContext() {
        this.init();
        StateMachineConfig<State, Trigger> phoneCallConfig = new StateMachineConfig<>();

        phoneCallConfig.configure(State.OffHook)
                .permit(Trigger.CallDialed, State.Ringing);

        phoneCallConfig.configure(State.Ringing)
                .permit(Trigger.HungUp, State.OffHook)
                .permit(Trigger.CallConnected, State.Connected);

        phoneCallConfig.configure(State.Connected)
                .onEntry(this::startCallTimer)
                .onExit(this::stopCallTimer)
                .permit(Trigger.LeftMessage, State.OffHook)
                .permit(Trigger.HungUp, State.OffHook)
                .permit(Trigger.PlacedOnHold, State.OnHold);
    }

    private void init() {
        executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(8);
        executor.setMaxPoolSize(16);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("common-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
    }

    public StateMachine<State, Trigger> stateMachine() {
        StateMachineConfig<State, Trigger> phoneCallConfig = new StateMachineConfig<>();

        phoneCallConfig.configure(State.OffHook)
                .onEntry(this::resetCallTimer)
                .permit(Trigger.CallDialed, State.Ringing);

        phoneCallConfig.configure(State.Ringing)
                .permit(Trigger.HungUp, State.OffHook)
                .permit(Trigger.CallConnected, State.Connected);

        phoneCallConfig.configure(State.Connected)
                .onEntry(this::startCallTimer)
                .onExit(this::stopCallTimer)
                .permit(Trigger.LeftMessage, State.OffHook)
                .permit(Trigger.HungUp, State.OffHook)
                .permit(Trigger.PlacedOnHold, State.OnHold);

        phoneCallConfig.configure(State.OnHold)
                .substateOf(State.Connected)
                .permit(Trigger.TakenOffHold, State.Connected)
                .permit(Trigger.HungUp, State.OffHook);

        return new StateMachine<>(State.OffHook, phoneCallConfig);
    }

    private void startCallTimer() {
        stopTimer = false;
        executor.execute(() -> {
            while (!stopTimer) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                log.info("当前通话时间: {}s", ++callTime);
            }
        });
    }

    private void stopCallTimer() {
        stopTimer = true;
    }

    private void resetCallTimer() {
        stopTimer = true;
        callTime = 0;
    }
}
