package com.mysongktv.cn.tms.utils.master;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Date;

import static com.mysongktv.cn.tms.utils.Constant.*;

/**
 * @author ZhangYang
 * @date 2020/1/3 16:08
 */
public class UDPSenderAndReceive {
    private static Logger logger = LoggerFactory.getLogger(UDPSenderAndReceive.class);

    public static void main(String[] args) {
        new SendMessage().start();
        new ReceiverMessage().start();
    }

    //广播发送消息
    private static String sendMsg(
            String host,
            int sendPort,
            String message) {

        String receiveMsg = null;
        try {
            InetAddress adds = InetAddress.getByName(host);
            DatagramSocket sendDs = new DatagramSocket();
            DatagramPacket sendDp = new DatagramPacket(message.getBytes(), message.length(), adds, sendPort);

            for (int i = 0; i < 10; i++) {
                sendDs.send(sendDp);
                System.out.println("发送消息");
                Thread.sleep(1000);
            }


            //暂时不不关闭
            //  ds.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return receiveMsg;
    }


    static class SendMessage extends Thread {
        //发送消息

        //接收消息

        String msg = "Broadcast message " + System.currentTimeMillis();

        @Override
        public void run() {
            try {

                sendMsg(BROADCAST_HOST, BROADCAST_SEND_PORT, msg);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static class ReceiverMessage extends Thread {
        //接收消息
        @Override
        public void run() {
            try {

                receiveMsg(BROADCAST_RECEIVER_PORT);
                Thread.sleep(1000);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void receiveMsg(int revport) {
        DatagramSocket receiveDs = null;
        DatagramPacket receiveDp = null;
        byte[] buf = new byte[1024];//存储发来的消息
        StringBuffer sbuf = new StringBuffer();
        try {
            //绑定端口的
            receiveDs = new DatagramSocket(revport);
            receiveDp = new DatagramPacket(buf, buf.length);
            logger.info("Receiver message ====>" + "进入接收消息");

            for (int i = 0; i < 100; i++) {
                receiveDs.receive(receiveDp);
                boolean broadcast = receiveDs.getBroadcast();
                System.out.println(broadcast);
                int j;
                //接收消息
//                for (j = 0; j < 1024; j++) {
//                    if (buf[j] == 0) {
//                        break;
//                    }else {
//                        System.out.println( sbuf.append((char) buf[j]));
//                    }
//
//                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
