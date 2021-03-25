package com.bomu.game.runnable;

import com.bomu.game.service.TableService;
import com.bomu.game.socket.config.SpringContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public abstract class AbstractRunnable implements Runnable {
    public static ScheduledExecutorService threadPool = Executors.newSingleThreadScheduledExecutor();

    protected ScheduledFuture<?> timeoutFuture;
    protected TableService tableService = SpringContext.getBean("tableService");
    protected final Logger logger = LogManager.getLogger(getClass());

    /**
     * 关掉线程
     */
    protected void stopTimeoutThread() {
        if (timeoutFuture != null && !timeoutFuture.isCancelled()) {
            timeoutFuture.cancel(true);
        }
    }

    /**
     * 隔段时间继续运行
     *
     * @param timeout
     */
    protected void startTimeoutThread(int timeout) {
        this.timeoutFuture = threadPool.schedule(this, timeout, TimeUnit.SECONDS);
    }

    /**
     * 隔段时间继续运行
     *
     * @param timeout
     */
    protected void startTimeoutMilliThread(int timeout) {
        this.timeoutFuture = threadPool.schedule(this, timeout, TimeUnit.MILLISECONDS);
    }
}
