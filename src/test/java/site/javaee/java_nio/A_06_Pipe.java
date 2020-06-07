package site.javaee.java_nio;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;

/**
 * 管道是2个线程之间的单向数据连接。Pipe有一个source通道和一个sink通道。数据会被写到sink通道，从source通道读取。
 *
 * @author shkstart
 * @create 2020-06-06 20:03
 */
public class A_06_Pipe {
    @Test
    void pipe() throws IOException {
        //1.获取管道
        Pipe pipe = Pipe.open();

        //缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.put("通过单向管道发送数据".getBytes());
        byteBuffer.flip();

        //2.将缓冲区中的数据写入管道
        Pipe.SinkChannel sinkChannel = pipe.sink();
        sinkChannel.write(byteBuffer);

        //3.读取管道中的数据
        Pipe.SourceChannel sourceChannel = pipe.source();
        byteBuffer.clear();
        int len = sourceChannel.read(byteBuffer);
        System.out.println(new String(byteBuffer.array(),0,len));

        sourceChannel.close();
        sinkChannel.close();
    }
}
