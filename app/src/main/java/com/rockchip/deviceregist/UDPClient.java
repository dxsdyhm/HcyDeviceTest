package com.rockchip.deviceregist;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * @Author: dxs
 * @time: 2020/5/13
 * @Email: duanxuesong12@126.com
 */
public class UDPClient implements Runnable{
    final static int udpPort = 33334;
    final static String hostIp = "127.0.0.1";
    private static DatagramSocket socket = null;
    private static DatagramPacket packetSend,packetRcv;
    private boolean udpLife = true; //udp生命线程
    private byte[] msgRcv = new byte[1024]; //接收消息
    private Handler handler;
    private int CLIENT_ERROR_SERVER=-20;
    private int CLIENT_ERROR_SEND=-21;
    private int CLIENT_ERROR_REC=-22;

    public UDPClient(Handler handler){
        super();
        this.handler=handler;
    }

    //返回udp生命线程因子是否存活
    public boolean isUdpLife(){
        if (udpLife){
            return true;
        }
        return false;
    }

    //更改UDP生命线程因子
    public void setUdpLife(boolean b){
        udpLife = b;
    }

    //发送消息
    public String send(String msgSend){
        InetAddress hostAddress = null;
        try {
            hostAddress = InetAddress.getByName(hostIp);
        } catch (UnknownHostException e) {
            Log.i("udpClient","未找到服务器");
            e.printStackTrace();
            handler.sendEmptyMessage(CLIENT_ERROR_SERVER);
        }
        packetSend = new DatagramPacket(msgSend.getBytes() , msgSend.getBytes().length,hostAddress,udpPort);

        try {
            socket.send(packetSend);
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("udpClient","发送失败");
            handler.sendEmptyMessage(CLIENT_ERROR_SEND);
        }
        //   socket.close();
        return msgSend;
    }

    @Override
    public void run() {
        try {
            socket = new DatagramSocket();
            socket.setSoTimeout(3000);//设置超时为3s
        } catch (SocketException e) {
            Log.i("udpClient","建立接收数据报失败");
            e.printStackTrace();
            handler.sendEmptyMessage(CLIENT_ERROR_REC);
        }
        packetRcv = new DatagramPacket(msgRcv,msgRcv.length);
        while (udpLife){
            try {
                Log.i("udpClient", "UDP监听");
                socket.receive(packetRcv);
                String RcvMsg = new String(packetRcv.getData(),packetRcv.getOffset(),packetRcv.getLength());
                //将收到的消息发给主界面
                Log.i("Rcv",RcvMsg);
                handler.sendMessage(handler.obtainMessage(1,RcvMsg));
                udpLife=false;
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        socket.close();
    }
}
