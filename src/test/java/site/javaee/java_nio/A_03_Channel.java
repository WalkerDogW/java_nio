package site.javaee.java_nio;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.Set;

/**
 * @author shkstart
 * @create 2020-05-27 4:50
 */
@SpringBootTest
public class A_03_Channel {
    /**
     *  Channel 表示 IO 源与目标打开的连接，类似于传统的“流”，在Java NIO中负责缓冲区数据的传输。
     */
    @Test
    void what(){
        /*
        Channel本身不能直接访问数据，只能与 Buffer 进行交互。
        Channel是一个独立的处理器，拥有自己的命令，专门用于IO操作。

        Java 为 Channel 接口提供的最主要实现类如下：
            •FileChannel：用于读取、写入、映射和操作文件的通道。
            •DatagramChannel：通过 UDP 读写网络中的数据通道。
            •SocketChannel：通过 TCP 读写网络中的数据。
            •ServerSocketChannel：可以监听新进来的 TCP 连接，对每一个新进来的连接都会创建一个 SocketChannel。

        获取通道的三种方法：
        一、对支持通道的对象调用getChannel() 方法。支持通道的类如下：
        本地IO：
             FileInputStream
             FileOutputStream
             RandomAccessFile
        网络IO：
             DatagramSocket
             Socket
             ServerSocket
        二、使用 Files 类的静态方法 newByteChannel() 获取字节通道。(jdk1.7的nio2)
        三、通过通道的静态方法 open() 打开并返回指定通道。(jdk1.7的nio2)


        通道之间的数据传输(直接缓冲区)
           transferFrom()
           transferTo()


        分散（Scatter）与聚集（Gather）
           分散读取（Scattering Reads）：将通道中的数据分散到多个缓冲区中去，
                按照缓冲区的顺序，从Channel中读取的数据依次将Buffer填满
           聚集写入（Gathering Writes）：将多个缓冲区中的数据聚集到通道中，
                按照缓冲区等顺序，写入position和limit之间的数据到Channel


        字符集：Charset
            编码：String  --> Byte[]
            解码：Byte[]  --> String
         */
    }

    /**
     * 使用通道完成文件复制
     */
    @Test
    void copyFile() throws IOException {
        long start = System.currentTimeMillis();

        //创建输入输出流
        FileInputStream fis = new FileInputStream("src/main/resources/1.jpg");
        FileOutputStream fos = new FileOutputStream("src/main/resources/2.jpg");

        //获取通道，对支持通道的对象调用getChannel() 方法
        FileChannel inChannel = fis.getChannel();
        FileChannel outChannel = fos.getChannel();

        //分配缓冲区
        ByteBuffer buf = ByteBuffer.allocate(1024);

        //将通道中的数据存入缓冲区中
        while(inChannel.read(buf) != -1){
            //将缓冲区切换成读取数据模式
            buf.flip();
            //将缓冲区中的数据写入通道
            outChannel.write(buf);
            //清空缓冲区
            buf.clear();
        }
        //关闭资源
        inChannel.close();
        outChannel.close();
        fis.close();
        fos.close();


        long end = System.currentTimeMillis();
        System.out.println("耗费时间："+(end-start));
    }

    /**
     * 使用通道和直接缓冲区完成文件复制
     */
    @Test
    void copyFileDirect() throws IOException {
        long start = System.currentTimeMillis();

        //获取通道，使用 Files 类的静态方法 newByteChannel()
        FileChannel inChannel = FileChannel.open(Paths.get("src/main/resources/1.jpg"), StandardOpenOption.READ);
        FileChannel outChannel = FileChannel.open(Paths.get("src/main/resources/2.jpg"),StandardOpenOption.READ,StandardOpenOption.WRITE,StandardOpenOption.CREATE);
        //内存映射文件，与allocateDirect一样用来创建直接缓冲区
        MappedByteBuffer inMapBuf =  inChannel.map(FileChannel.MapMode.READ_ONLY, 0, inChannel.size());
        MappedByteBuffer outMapBuf = outChannel.map(FileChannel.MapMode.READ_WRITE, 0, inChannel.size());
        //直接对缓冲区进行数据的读写，无需经过通道
        byte[] dst =new byte[inMapBuf.limit()];
        inMapBuf.get(dst);
        outMapBuf.put(dst);
        //关闭通道
        inChannel.close();
        outChannel.close();


        long end = System.currentTimeMillis();
        System.out.println("耗费时间："+(end-start));
    }

    /**
     * 通道之间的数据传输
     */
    @Test
    void channelTrans() throws IOException {
        long start = System.currentTimeMillis();

        //获取通道，使用 Files 类的静态方法 newByteChannel()
        FileChannel inChannel = FileChannel.open(Paths.get("src/main/resources/1.jpg"), StandardOpenOption.READ);
        FileChannel outChannel = FileChannel.open(Paths.get("src/main/resources/2.jpg"),StandardOpenOption.READ,StandardOpenOption.WRITE,StandardOpenOption.CREATE);

        //通道之间的数据传输
        inChannel.transferTo(0, inChannel.size(), outChannel);

        inChannel.close();
        outChannel.close();

        long end = System.currentTimeMillis();
        System.out.println("耗费时间："+(end-start));
    }

    /**
     * 分散读取与聚集写入
     */
    @Test
    void scatter() throws IOException {
        //由于可以自由访问文件的任意位置，所以如果需要访问文件的部分内容，RandomAccessFile将是更好的选择。
        RandomAccessFile raf1 = new RandomAccessFile("src/main/resources/蜀道难.txt", "rw");
        //获取通道
        FileChannel channel1 = raf1.getChannel();
        //分配指定大小的缓冲区
        ByteBuffer buf1 = ByteBuffer.allocate(100);
        ByteBuffer buf2 = ByteBuffer.allocate(1024);
        //分散读取
        ByteBuffer[] bufs = {buf1,buf2};
        channel1.read(bufs);
        //切换成读数据模式
        for(ByteBuffer byteBuffer:bufs){
            byteBuffer.flip();
        }

        System.out.println(new String(bufs[0].array() ,0, bufs[0].limit()));
        System.out.println("----------------");
        System.out.println(new String(bufs[1].array() ,0, bufs[1].limit()));


        //聚集写入
        RandomAccessFile raf2 = new RandomAccessFile("src/main/resources/蜀道难2.txt", "rw");
        FileChannel channel2 = raf2.getChannel();
        channel2.write(bufs);
    }

    /**
     * 字符集
     */
    @Test
    void charset() throws CharacterCodingException {
        //所有字符集
        Map<String,Charset> map = Charset.availableCharsets();
        Set<Map.Entry<String,Charset>> set=map.entrySet();
        for(Map.Entry<String,Charset> entry:set){
          //System.out.println(entry.getKey()+"="+entry.getValue());
        }
        //默认字符集
        System.out.println(Charset.defaultCharset());


        Charset charset1 = Charset.forName("UTF-8");
        //获取编码器
        CharsetEncoder charsetEncoder = charset1.newEncoder();
        //获取解码器
        CharsetDecoder  charsetDecoder = charset1.newDecoder();
        //缓冲区
        CharBuffer charBuffer = CharBuffer.allocate(1024);
        charBuffer.put("测试字符集!");
        charBuffer.flip();
        //编码
        ByteBuffer byteBuffer = charsetEncoder.encode(charBuffer);
        System.out.println(byteBuffer.limit());
        for(int i=0; i<16; i++){
            System.out.println( byteBuffer.get());
        }
        byteBuffer.flip();
        //解码
        CharBuffer charBuffer2 = charsetDecoder.decode(byteBuffer);
        System.out.println(charBuffer2.toString());
    }
}
