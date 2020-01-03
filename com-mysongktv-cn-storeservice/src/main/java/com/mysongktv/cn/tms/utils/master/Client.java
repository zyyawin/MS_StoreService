package com.mysongktv.cn.tms.utils.master;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.util.Date;


//@Component
public class Client {
    private Logger logger = LoggerFactory.getLogger(Client.class);

    private Socket server = null;

// 服务器是否可用


    private boolean SERVER_ALIAVE;

// 服务器给与的最后一次心跳回执时间


    private Date SERVER_HEART_RECEIVE_TIME;

// 心跳间隔（你的脉搏多久跳动一次）


    private static final long HEARTBEAT_SLEEP = 2 * 1000;

    //服务器宕机的宽限时间，超过了，就视作挂了


    private static final long TIMEOUT = 10 * 1000;

    //@Scheduled(cron = "0 0/1 * * * ?")
    public void start() throws InterruptedException {
        try {
            logger.info("==================================");
            // 连接服务器
            server = new Socket("127.0.0.1", 5555);


            SERVER_ALIAVE = true;
//            logger.info("连接主服务器成功IP+PORT" + server.getLocalSocketAddress());
//            System.out.println("连接主服务器成功，IP+PORT" + server.getLocalSocketAddress());
            // 启动接受消息的线程
            new ReceiveMessageListener().start();
            // 启动发送消息的线程
            new SendMessageListener().start();
            // 启动发送心跳的线程
            new HeartbeatListener().start();
        } catch (IOException e) {
            logger.info(e.toString());

        }
    }


    class SendMessageListener extends Thread {
        @Override
        public void run() {
            try {
                // 监听idea、eclipse的console输入
                //Scanner scanner = new Scanner(System.in);
                // 不再使用while(true)了，避免在服务器宕机了之后，线程还依旧在无限循环
                Date date = new Date();
                while (SERVER_ALIAVE) {
                    sendMsg("从服务器发送请求到主服务器" + date);
                    Thread.sleep(10000);//休眠时间
                }
            } catch (IOException | InterruptedException e) {
                logger.info("错误信息:" + e.toString());
            }
        }
    }


    class ReceiveMessageListener extends Thread {
        @Override
        public void run() {
            try {
                // 不再使用while(true)了，避免在服务器宕机了之后，线程还依旧在无限循环
                String msg;
                while (SERVER_ALIAVE) {
                    msg = receiveMsg();

                    // 如果服务器已经断开连接，但是心跳回执的间隔时间又还没到的这一段时间（本案例中是2秒），会导致接收消息的readLine()一直为null
                    if (msg == null) {
                        continue;
                    }

                    if ("heartbeat_receipt".equals(msg)) {
                        SERVER_HEART_RECEIVE_TIME = new Date();// 记录每一次的心跳回执时间

                    } else if ("exit_receipt".equals(msg)) {
                        System.exit(0);// 必须在收到回执消息之后再退出，否则会导致消息丢失

                    } else {
                        System.out.println(msg);
                    }
                }
            } catch (IOException e) {
                logger.info(e.toString());
            }
        }
    }

// 发送心跳的线程


    private static boolean configDone = false;

    class HeartbeatListener extends Thread {
        @Override
        public void run() {
            try {
                // 不再使用while(true)了，避免在服务器宕机了之后，线程还依旧在无限循环
                Date now;
                while (true) {
                    // 每次发送心跳之前，都需要检测一下服务器是否健康，不做无用功
                    now = new Date();

                    // 连接到服务器之后，不会立即发送心跳（虽然本案例中服务器端是做了处理，第一次连接默认记录当前时间），所以需要判断是否为null
                    if (SERVER_HEART_RECEIVE_TIME != null && now.getTime() - SERVER_HEART_RECEIVE_TIME.getTime() > TIMEOUT) {
                        //SERVER_ALIAVE = false;

                        System.out.println("请求主服务器超时,主服务器" + "[" + server.getRemoteSocketAddress() + "]宕机无法请求；从服务器接受任务处理");

//                        while (!configDone) {
//                            configDone = requestConfig();
//                            Thread.sleep(10 * 1000);
//                        }


                        System.out.println("请求断开运行任务");

                        //   InitUtil.startRequestLoop();
                        // InitUtil.startExecuteLoop();
                       // RunApp.St();
                        // 服务器宕机了，客户端也自动退出
                        //System.exit(0);


                    }

                    // 发送心跳
                    sendMsg("heartbeat");
                    // 限定发送频率
                    try {
                        Thread.sleep(HEARTBEAT_SLEEP);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                logger.info(e.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 发送消息
     *
     * @param msg 消息内容
     * @throws IOException
     */

    private void sendMsg(String msg) throws IOException {
        OutputStream os = server.getOutputStream();
        PrintWriter pw = new PrintWriter(os);
        pw.println(msg);// 这里需要特别注意，对方用readLine获取消息，就必须用print而不能用write，否则会导致消息获取不了
        pw.flush();
    }

    /**
     * 接受消息
     *
     * @return 消息内容
     * @throws IOException
     */

    private String receiveMsg() throws IOException {
        InputStream is = server.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        return br.readLine();
    }

    public static void main(String[] args) throws InterruptedException {
        new Client().start();
    }

}
