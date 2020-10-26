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
    private final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    @Getter
    private int callTime = 0;
    private volatile boolean stopTimer = false;

    /**
     * 状态机上下文构造函数
     */
    public PhoneCallStateMachineContext() {
        executor.setCorePoolSize(8);
        executor.setMaxPoolSize(16);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("common-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
    }

    /**
     * @return 返回一个状态机实例
     */
    public StateMachine<State, Trigger> stateMachine() {
        StateMachineConfig<State, Trigger> phoneCallConfig = new StateMachineConfig<>();

        phoneCallConfig.configure(State.摘机状态)
                .onEntry(this::resetCallTimer)
                .permit(Trigger.来电事件, State.振铃状态);

        phoneCallConfig.configure(State.振铃状态)
                .permit(Trigger.挂断电话事件, State.摘机状态)
                .permit(Trigger.接听事件, State.通话中状态);

        phoneCallConfig.configure(State.通话中状态)
                .onEntry(this::startCallTimer)
                .onExit(this::stopCallTimer)
                .permit(Trigger.语音留言事件, State.摘机状态)
                .permit(Trigger.挂断电话事件, State.摘机状态)
                .permit(Trigger.通话保持事件, State.通话保持状态);

        phoneCallConfig.configure(State.通话保持状态)
                .substateOf(State.通话中状态)
                .permit(Trigger.取消通话保持事件, State.通话中状态)
                .permit(Trigger.挂断电话事件, State.摘机状态);

        return new StateMachine<>(State.摘机状态, phoneCallConfig);
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
