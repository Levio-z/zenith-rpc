package com.zenith.zenithrpc.server;


/**
 * HTTP 服务器接口
 *
 * @author zenith
 * @date 2024/06/06
 */
public interface HttpServer {
    /**
     * 启动服务器
     *
     * @param port
     */
    void doStart(int port);
}
