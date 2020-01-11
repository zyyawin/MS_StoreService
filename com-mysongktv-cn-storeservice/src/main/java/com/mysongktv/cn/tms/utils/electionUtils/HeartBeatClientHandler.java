package com.mysongktv.cn.tms.utils.electionUtils;

import com.mysongktv.cn.tms.utils.MacUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoop;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;


/**
 * @author ZhangYang
 * @date 2020/1/4 13:01
 */
public class HeartBeatClientHandler extends SimpleChannelInboundHandler<String> {

    private static Logger logger = LoggerFactory.getLogger(HeartBeatClientHandler.class);
    private HeartBeatClient client;

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:dd");
    //通过心跳把Mac传入进去
    private static final ByteBuf HEARTBEAT_SEQUENCE;


    static {
        String msg = "HeartBeat" + "|" + MacUtil.getMACAddr() + "| Client";
        ByteBuf heartbeat = Unpooled.copiedBuffer(msg, CharsetUtil.UTF_8);
        HEARTBEAT_SEQUENCE = Unpooled.unreleasableBuffer(
                heartbeat);
    }

    public HeartBeatClientHandler(HeartBeatClient client) {
        this.client = client;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        ctx.write("has read message from server");
        ctx.flush();

        //接收服务器返回信息
        String macAddrServer = null;
        String macAddrLocal = null;

        if (msg.startsWith("HeartBeat")) {

            System.out.println("收到服务端回复：" + msg);
            macAddrServer = msg.split("\\|")[1];
            macAddrLocal = MacUtil.getMACAddr();
            if (MacUtil.maxMac(macAddrLocal, macAddrServer)) {
                System.out.println("==========================执行程序==================================");
            }
        }

        ReferenceCountUtil.release(msg);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.WRITER_IDLE) {
                ctx.writeAndFlush(HEARTBEAT_SEQUENCE.duplicate());
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        System.err.println("客户端与服务端断开连接,断开的时间为：" + format.format(new Date()));
        // 定时线程 断线重连
        final EventLoop eventLoop = ctx.channel().eventLoop();
        eventLoop.schedule(new Runnable() {
            @Override
            public void run() {
                client.doConncet();
            }
        }, 10, TimeUnit.SECONDS);
    }


}
