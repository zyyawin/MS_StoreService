package com.mysongktv.cn.tms.utils.electionUtils;

import com.mysongktv.cn.tms.RunApp;
import com.mysongktv.cn.tms.StartTask;
import com.mysongktv.cn.tms.utils.Constant;
import com.mysongktv.cn.tms.utils.MacUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author ZhangYang
 * @date 2020/1/4 12:54
 */
public class HeartBeatServerHandler extends SimpleChannelInboundHandler<String> {
    private static Logger logger = LoggerFactory.getLogger(HeartBeatServer.class);

    List<LinkedBlockingQueue<ConcurrentHashMap<String, String>>> linkedBlockingQueues=null;
    // 失败计数器：未收到client端发送的ping请求
    private int unRecPingTimes = 0;
    // 定义服务端没有收到心跳消息的最大次数
    private static final int MAX_UN_REC_PING_TIMES = 3;
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {

        String macAddrLocal=null;
        String macAddrClient=null;
//        boolean flag = false;
        if (msg!=null && msg.startsWith(Constant.HEARTBEAT_MSG)){
            macAddrLocal = MacUtil.getMACAddr();
            macAddrClient = msg.split("\\|")[1];
            logger.info("==========> 本地服务器MAC地址为："+macAddrLocal+";客户端MAC地址为："+macAddrClient);
            ctx.writeAndFlush(Constant.HEARTBEAT_MSG+"|"+macAddrLocal+"|server");
            if (MacUtil.maxMac(macAddrLocal,macAddrClient)){
                linkedBlockingQueues = StartTask.startDownFile(Constant.STORE_ID);
            }
        }
    }




    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state()== IdleState.READER_IDLE){
                logger.info("=====服务端=====(READER_IDLE 读超时)");
                // 失败计数器次数大于等于3次的时候，关闭链接，等待client重连
                if (unRecPingTimes >= MAX_UN_REC_PING_TIMES) {
                    logger.info("=====服务端=====(读取超时超过3次，关闭通道等待连接)");
                    // 连续超过N次未收到client的ping消息，那么关闭该通道，等待client重连
                    ctx.close();
                } else {
                    // 失败计数器加1
                    unRecPingTimes++;
                }
            }else {
                super.userEventTriggered(ctx,evt);
            }
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);

        logger.info("==========>一个服务器已连接");
        //System.out.println("一个服务器已连接");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        logger.info("==========客户端已经断开连接，服务端开始执行任务");
    }

    /**
     * Do nothing by default, sub-classes may override this method.
     *
     * @param ctx
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        super.handlerRemoved(ctx);
    }
}
