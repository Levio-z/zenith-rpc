package com.zenith.zenithrpc.server;

import com.zenith.zenithrpc.HttpServerHandler;
import io.vertx.core.Vertx;

public class VertxHttpServer implements HttpServer {
    /**
     * 创建 Vert.x 实例；
     * 使用 Vert.x 实例创建一个 HTTP 服务器；
     * 设置 HTTP 服务器的请求处理器，用于处理每个请求。在处理器中，打印请求方法和 URI，然后发送一个包含 "Hello from Vert.x HTTP server!" 字符串的 HTTP 响应；
     * 调用 server.listen 方法启动 HTTP 服务器并监听指定的端口；
     * 在监听端口的操作完成之后，根据操作结果打印相应的信息，成功则打印"Server is now listening on port {port}"，失败则打印"Failed to start server: {cause}"。
     *
     * @param port
     */
    @Override
    public void doStart(int port) {
        // 创建 Vert.x 实例
        Vertx vertx = Vertx.vertx();
        // 创建 HTTP 服务器
        io.vertx.core.http.HttpServer server = vertx.createHttpServer();

        // 监听端口并处理请求
        server.requestHandler(new HttpServerHandler());

        // 启动 HTTP 服务器并监听指定端口
        // 要监听的端口号，服务器将在这个端口上接收进来的连接请求。
        server.listen(port, result -> {
            // 监听处理器，是一个回调函数。当服务器实际开始监听指定端口（即成功启动并可以接受连接）时，或者监听失败时，这个处理器会被调用。如果监听成功，处理器通常会提供一个确认信息；如果失败，它可能会包含错误信息。这提供了一种异步处理方式，确保在服务器准备好服务之前不会继续执行后续代码。
            if (result.succeeded()) {
                System.out.println("Server is now listening on port " + port);
            } else {
                System.err.println("Failed to start server: " + result.cause());
            }
        });

    }
}
