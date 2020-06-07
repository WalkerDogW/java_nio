package site.javaee.java_nio;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;

/**
 * 选择器（Selector） 是 SelectableChannle 对象的多路复用器，
 * Selector 可以同时监控多个 SelectableChannel 的 IO 状况，
 * 利用 Selector可使一个单独的线程管理多个 Channel。Selector 是非阻塞 IO 的核心。
 *
 * @author shkstart
 * @create 2020-06-01 0:26
 */
public class A_04_Selector {
    @Test
    /**
     * 传统IO流是阻塞式的
     */
    void diff() {
        /*
         阻塞IO
            当一个线程调用 read() 或 write() 时，该线程被阻塞，直到有一些数据被读取或写入，
            该线程在此期间不 能执行其他任务。因此，在完成网络通信进行 IO 操作时，由于线程会 阻塞，
            服务器端必须为每个客户端都提供一个独立的线程进行处理。当服务器端需要处理大量客户端时，性能会急剧下降。
         非阻塞IO
            当线程从某通道进行读写数据时，若没有数 据可用时，该线程可以进行其他任务。
            线程通常将非阻塞 IO 的空闲时 间用于在其他通道上执行 IO 操作，所以单独的线程可以理多个输入 和输出通道。
            因此，NIO 可以让服务器端使用一个或有限几个线程来同 时处理连接到服务器端的所有客户端。

         使用NIO完成网络通信的三个核心
             通道（Channel）负责连接
             通道（Buffer）负责数据存取
             选择器（Selector）是SelectableChannel的多路复用器，用于监控SelectableChannel的IO状况

         可以监听的事件类型（可使用 SelectionKey 的四个常量表示）：
             读 : SelectionKey.OP_READ （1）
             写 : SelectionKey.OP_WRITE （4）
             连接 : SelectionKey.OP_CONNECT （8）
             接收 : SelectionKey.OP_ACCEPT （16）
            若注册时不止监听一个事件，则可以使用“位或”操作符连接
         */


    }

    /**
     * io阻塞客户端
     */
    @Test
    void ioBlockClient() throws IOException {
        // 要连接的服务端IP地址和端口
        String host = "127.0.0.1";
        int port = 55533;
        // 与服务端建立连接
        Socket socket = new Socket(host, port);
        // 建立连接后获得输出流
        OutputStream outputStream = socket.getOutputStream();
        String message = "你好  yiwangzhibujian";
        socket.getOutputStream().write(message.getBytes("UTF-8"));
        //通过shutdownOutput告诉服务器已经发送完数据，后续只能接受数据
        socket.shutdownOutput();

        InputStream inputStream = socket.getInputStream();
        byte[] bytes = new byte[1024];
        int len;
        StringBuilder sb = new StringBuilder();
        while ((len = inputStream.read(bytes)) != -1) {
            //注意指定编码格式，发送方和接收方一定要统一，建议使用UTF-8
            sb.append(new String(bytes, 0, len, "UTF-8"));
        }
        System.out.println("get message from server: " + sb);

        inputStream.close();
        outputStream.close();
        socket.close();

    }


    /**
     * io阻塞服务端
     */
    @Test
    void ioBlockServer() throws IOException {
        int port = 55533;
        ServerSocket server = new ServerSocket(port);

        // server将一直等待连接的到来
        System.out.println("server将一直等待连接的到来");
        Socket socket = server.accept();
        // 建立好连接后，从socket中获取输入流，并建立缓冲区进行读取
        InputStream inputStream = socket.getInputStream();
        byte[] bytes = new byte[1024];
        int len;
        StringBuilder sb = new StringBuilder();
        //只有当客户端关闭它的输出流的时候，服务端才能取得结尾的-1
        while ((len = inputStream.read(bytes)) != -1) {
            // 注意指定编码格式，发送方和接收方一定要统一，建议使用UTF-8
            sb.append(new String(bytes, 0, len, "UTF-8"));
        }
        System.out.println("get message from client: " + sb);

        OutputStream outputStream = socket.getOutputStream();
        outputStream.write("Hello Client,I get the message.".getBytes("UTF-8"));

        inputStream.close();
        outputStream.close();
        socket.close();
        server.close();

    }

    /**
     * nio阻塞服务端
     */
    @Test
    void nioBlockClient() throws IOException {
        //1：获取套接字通道
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9898));

        //2：读取本地文件并发送到服务端
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        FileChannel fileChannelIn = FileChannel.open(Paths.get("src/main/resources/蜀道难.txt"), StandardOpenOption.READ);
        while (fileChannelIn.read(byteBuffer) != -1) {
            byteBuffer.flip();
            socketChannel.write(byteBuffer);
            byteBuffer.clear();
        }

        //客户端结束数据发送
        socketChannel.shutdownOutput();

        //接收服务端的反馈
        int len = 0;
        while ((len = socketChannel.read(byteBuffer)) != -1) {
            byteBuffer.flip();
            System.out.println(new String(byteBuffer.array(), 0, len));
            byteBuffer.clear();
        }

        //3:关闭通道
        fileChannelIn.close();
        socketChannel.close();
    }

    /**
     * nio阻塞服务端
     */
    @Test
    void nioBlockserver() throws IOException {
        //1：获取套接字通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //2：绑定端口
        serverSocketChannel.bind(new InetSocketAddress(9898));
        //3：获取客户端连接的通道
        SocketChannel socketChannel = serverSocketChannel.accept();
        //4：接收客户端的数据，并保存到本地
        FileChannel fileChannelOut = FileChannel.open(Paths.get("src/main/resources/2.jpg"), StandardOpenOption.WRITE, StandardOpenOption.CREATE);
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        while (socketChannel.read(byteBuffer) != -1) {
            byteBuffer.flip();
            fileChannelOut.write(byteBuffer);
            byteBuffer.clear();
        }


        //发送反馈给客户端
        byteBuffer.put("服务端接收数据成功!".getBytes());
        byteBuffer.flip();
        socketChannel.write(byteBuffer);

        //服务端结束数据发送
        socketChannel.shutdownOutput();

        //5：通道
        fileChannelOut.close();
        socketChannel.close();
        serverSocketChannel.close();
    }


    @Test
    void nioServer() throws IOException {
        //1:获取 通道
        ServerSocketChannel socketServerChannel = ServerSocketChannel.open();

        //2:切换成非阻塞模式
        socketServerChannel.configureBlocking(false);

        //3:绑定链接
        socketServerChannel.bind(new InetSocketAddress(9898));

        //4:获取选择器
        Selector selector = Selector.open();

        //5:将通道注册到选择器上，并且指定"监听接收事件"
        socketServerChannel.register(selector, SelectionKey.OP_ACCEPT);

        //6:轮询式的获取选择器上已经“准备就绪”的事件
        while(selector.select() > 0){
            //7:获取当前选择器中所有注册的"选择键(已就绪的监听事件)"
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()){
                //8:获取“准备就绪”的事件
                SelectionKey selectionKey = iterator.next();
                //9:判断具体是什么事件准备就绪
                if(selectionKey.isAcceptable()){
                    //10:如果"接收就绪",就获取客户端连接
                    SocketChannel socketChannel = socketServerChannel.accept();
                    //11:切客户端换成非阻塞模式
                    socketChannel.configureBlocking(false);
                    //12:将客户端通道注册到选择器上
                    socketChannel.register(selector, SelectionKey.OP_READ);
                }else if(selectionKey.isReadable()){
                    //13:获取当前选择器上"读就绪"状态的通道
                    SocketChannel socketChannel =(SocketChannel) selectionKey.channel();
                    //14:读取数据
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                    int len=0;
                    while((len=socketChannel.read(byteBuffer))>0){
                        byteBuffer.flip();
                        System.out.println(new String(byteBuffer.array(),0,len));
                        byteBuffer.clear();
                    }
                }

                //15:取消选择键
                iterator.remove();

            }
        }
    }

    @Test
    void nioClient() throws IOException {
        //1：获取套接字通道
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9898));

        //2：切换成非阻塞模式
        socketChannel.configureBlocking(false);

        //3：读取本地文件并发送到服务端
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        FileChannel fileChannelIn = FileChannel.open(Paths.get("src/main/resources/蜀道难.txt"), StandardOpenOption.READ);
        /*
        while (fileChannelIn.read(byteBuffer) != -1) {
            byteBuffer.flip();
            socketChannel.write(byteBuffer);
            byteBuffer.clear();
        }*/

        //持续发送数据
        Scanner scanner = new Scanner(System.in);
        while(scanner.hasNext()){
            String string = scanner.next();
            byteBuffer.put((new Date().toString() + "\n" + string).getBytes());
            byteBuffer.flip();
            socketChannel.write(byteBuffer);
            byteBuffer.clear();
        }

        //3:关闭通道
        fileChannelIn.close();
        socketChannel.close();
    }
}
