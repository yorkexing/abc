package com.team.common.http;

import java.util.concurrent.TimeUnit;

import org.apache.http.conn.HttpClientConnectionManager;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 清理httpclient的连接池
 *
 * @author Administrator
 */
public class IdleConnectionMonitorThread extends Thread {

    private static Logger logger = LoggerFactory.getLogger(IdleConnectionMonitorThread.class.getName());

    private final HttpClientConnectionManager connMgr;
    private volatile boolean shutdown;

    public IdleConnectionMonitorThread(HttpClientConnectionManager connMgr) {
        super();
        this.connMgr = connMgr;
    }

    @Override
    public void run() {
        try {
            while (!shutdown) {
                synchronized (this) {
                    wait(HttpConstans.IDLECONNECTIONMONITORTHREAD_START_TIME);
                    logger.info("step into IdleConnectionMonitorThread.....清空失效连接...");
                    // 关闭失效连接
                    connMgr.closeExpiredConnections();
                    // 关闭空闲超过配置时间的连接
                    connMgr.closeIdleConnections(HttpConstans.CLOSE_IDLE_CONNECTIONS, TimeUnit.SECONDS);
                }
            }
        } catch (InterruptedException e) {
            logger.error("IdleConnectionMonitorThread is error" + e);
        }
    }

    public void shutdown() {
        shutdown = true;
        synchronized (this) {
            notifyAll();
        }
    }
}
