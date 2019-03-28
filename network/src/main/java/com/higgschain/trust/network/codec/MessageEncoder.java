package com.higgschain.trust.network.codec;

import com.higgschain.trust.network.Address;
import com.higgschain.trust.network.NetworkManage;
import com.higgschain.trust.network.message.NetworkMessage;
import com.higgschain.trust.network.message.NetworkRequest;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author duhongming
 * @date 2018/8/21
 */
public class MessageEncoder extends MessageToByteEncoder<NetworkMessage> {

    private static final int VERSION = 1;
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final Address address;

    public MessageEncoder(Address address) {
        this.address = address;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, NetworkMessage rawMessage, ByteBuf out) {
        out.writeShort(VERSION);
        // sender ip
        writeContentWithLength(out, this.address.getHost());
        // sender port
        out.writeInt(this.address.getPort());

        // message id
        out.writeLong(rawMessage.id());

        // message type
        out.writeByte(rawMessage.type().id());

        if (rawMessage.isRequest()) {
            final byte[] actonName = ((NetworkRequest) rawMessage).actionName().getBytes();
            out.writeInt(actonName.length);
            out.writeBytes(actonName);
        }

        final byte[] payload = rawMessage.payload();
        out.writeInt(payload.length);
        out.writeBytes(payload);
        NetworkManage.getTrafficReporter().outbound(payload.length + 30);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        try {
            if (cause instanceof IOException) {
                log.debug("IOException inside channel handling pipeline.", cause);
            } else {
                log.error("Non-IOException inside channel handling pipeline.", cause);
            }
        } finally {
            ctx.close();
        }
    }

    @Override
    public boolean acceptOutboundMessage(Object msg) {
        return msg instanceof NetworkMessage;
    }

    private void writeContentWithLength(ByteBuf buf, String content) {
        final byte[] data = content.getBytes();
        buf.writeInt(data.length);
        buf.writeBytes(data);
    }
}
