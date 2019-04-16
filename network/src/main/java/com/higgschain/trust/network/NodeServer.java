package com.higgschain.trust.network;

import com.higgschain.trust.network.codec.MessageDecoder;
import com.higgschain.trust.network.codec.MessageEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

/**
 * The type Node server.
 *
 * @author duhongming
 * @date 2018 /9/5
 */
public class NodeServer {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final EventLoopGroup serverGroup;
    private final EventLoopGroup clientGroup;
    private final NetworkConfig config;
    private final Address localAddress;
    private final ChannelHandler messageDispatcher;

    private Channel serverChannel;

    /**
     * Instantiates a new Node server.
     *
     * @param localAddress      the local address
     * @param messageDispatcher the message dispatcher
     * @param serverGroup       the server group
     * @param clientGroup       the client group
     * @param config            the config
     */
    public NodeServer(
            final Address localAddress,
            final ChannelHandler messageDispatcher,
            final EventLoopGroup serverGroup,
            final EventLoopGroup clientGroup, final NetworkConfig config) {
        this.messageDispatcher =messageDispatcher;
        this.serverGroup = serverGroup;
        this.clientGroup = clientGroup;
        this.localAddress = localAddress;
        this.config = config;
    }

    /**
     * Start completable future.
     *
     * @return the completable future
     */
    public CompletableFuture<Void> start() {
        CompletableFuture<Void> future = new CompletableFuture<>();
        ServerBootstrap b = new ServerBootstrap();
        b.option(ChannelOption.SO_REUSEADDR, true);
        b.option(ChannelOption.SO_BACKLOG, 128);
        b.childOption(ChannelOption.WRITE_BUFFER_WATER_MARK,
                new WriteBufferWaterMark(8 * 1024, 32 * 1024));
        b.childOption(ChannelOption.SO_RCVBUF, 1024 * 1024);
        b.childOption(ChannelOption.SO_SNDBUF, 1024 * 1024);
        b.childOption(ChannelOption.SO_KEEPALIVE, true);
        b.childOption(ChannelOption.TCP_NODELAY, true);
        b.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        b.group(serverGroup, clientGroup);
        b.channel(NioServerSocketChannel.class);
        b.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel channel) {
                ChannelPipeline pipeline = channel.pipeline();
                pipeline.addLast("encoder", new MessageEncoder(localAddress));
                pipeline.addLast("decoder", new MessageDecoder());
                pipeline.addLast("messageHandler", messageDispatcher);
            }
        });

        // Bind and start to accept incoming connections.
        b.bind(config.port()).addListener((ChannelFutureListener) f -> {
            if (f.isSuccess()) {
                log.info("{} accepting incoming connections on port {}", config.host(), config.port());
                serverChannel = f.channel();
                future.complete(null);
            } else {
                log.warn("{} failed to bind to port {} due to {}", config.host(), config.port(), f.cause());
                future.completeExceptionally(f.cause());
            }
        });
        return future;
    }

    /**
     * Close channel.
     *
     * @throws InterruptedException the interrupted exception
     */
    public void closeChannel() throws InterruptedException {
        serverChannel.close().sync();
    }
}
