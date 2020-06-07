package site.javaee.java_nio;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.ByteBuffer;

@SpringBootTest
class A_02_Buffer {



    /**
     * 缓冲区（Buffer）：在Java NIO 中负责数据的存取。
     * 缓冲区底层就是数组，用于存储不同数据类型的数据
     */
    @Test
    void intro(){
        /*
         缓冲区（Buffer）：一个用于特定基本数据类型的容器。
        由 java.nio 包定义的所有缓冲区 都是 Buffer 抽象类的子类。
        Java NIO 中的 Buffer 主要用于与 NIO 通道进行 交互，数据是从通道读入缓冲区，从缓冲区写 入通道中的。

         根据数据类型不同(除了boolean除外)，NIO提供了相应类型的缓冲区。
        以下缓冲区的管理方式几乎一致，通过allocate()获取缓冲区。
        ByteBuffer（最常用，不管是磁盘还是网络传输的时候都是字节），
        CharBuffer，
        ShortBuffer，
        IntBuffer，
        LongBuffer，
        FloatBuffer，
        DoubleBuffer
         */
    }

    /**
     * 缓冲区(Buffer)核心
     */
    @Test
    void core(){
        /*
        一：缓冲区存取数据的两个核心方法：
             put(): 存入数据到缓冲区
             get(): 获取缓冲区数据

             flip():写模式==>读模式，将缓冲区的界限设置为当前位置，并将当前位置重置为 0
             clear():读模式==>写模式，清空缓冲区，缓冲区中的数据依然存在，但处于“被遗忘”的状态


        二：缓冲区的四个核心属性
             容量 (capacity) ：表示 Buffer 最大数据容量，缓冲区容量不能为负，并且创 建后不能更改。
             限制 (limit)：第一个不应该读取或写入的数据的索引，即位于 limit 后的数据 不可读写。
                缓冲区的限制不能为负，并且不能大于其容量。
             位置 (position)：下一个要读取或写入的数据的索引。缓冲区的位置不能为 负，并且不能大于其限制
             标记 (mark)与重置 (reset)：标记是一个索引，通过 Buffer 中的 mark() 方法
                指定 Buffer 中一个特定的 position，之后可以通过调用 reset() 方法恢复到这 个 position.

             标记、位置、限制、容量遵守以下不变式： 0 <= mark <= position <= limit <= capacity
             可以用 Buffer limit(int n)、Buffer position(int n) 修改属性
         */
    }

    /**
     * 基本使用
     */
    @Test
    void use(){
        String str = "abcde";

        //1、allocate(): 分配一个指定大小的缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        bufferStatus("allocate",byteBuffer);

        //2、put(): 存入数据到缓冲区
        byteBuffer.put(str.getBytes());
        bufferStatus("put",byteBuffer);

        //3、flip(): 切换写模式到读模式，limit=position;position=0;
        byteBuffer.flip();
        bufferStatus("flip",byteBuffer);

        //4、get()：读取缓冲区数据
        byte[] dst = new byte[byteBuffer.limit()];
        byteBuffer.get(dst);
        bufferStatus("get",byteBuffer);
        System.out.println(new String(dst,0,dst.length));

        //5、rewind()：可重复读数据，即重置position指针，取消设置的 mark
        byteBuffer.rewind();
        bufferStatus("rewind",byteBuffer);

        //6、clear(): 清空缓冲区，缓冲区中的数据依然存在，但处于“被遗忘”的状态，用于将读模式切换成写模式
        byteBuffer.clear();
        bufferStatus("clear",byteBuffer);
        System.out.println((char)byteBuffer.get());

        //7、hasRemaining(): 判断缓冲区中是否还有剩余
        if (byteBuffer.hasRemaining()) {
            bufferStatus("hasRemaining",byteBuffer);
        }


    }


    /**
     * 标记缓冲区位置
     */
    @Test
    void mark(){
        String str = "abcde";

        //1、allocate(): 分配一个指定大小的缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        bufferStatus("allocate",byteBuffer);

        //2、put(): 存入数据到缓冲区
        byteBuffer.put(str.getBytes());
        bufferStatus("put",byteBuffer);

        //3、flip(): 切换读取数据模式
        byteBuffer.flip();
        bufferStatus("flip",byteBuffer);

        //4、get()：读取缓冲区数据
        byte[] dst = new byte[byteBuffer.limit()];
        byteBuffer.get(dst,0,2);
        bufferStatus("get",byteBuffer);
        System.out.println(new String(dst,0,2));

        //5、mark(): 标记缓冲区位置
        byteBuffer.mark();
        bufferStatus("mark", byteBuffer);

        //6、继续读取数据
        byteBuffer.get(dst,2,2);
        bufferStatus("get",byteBuffer);
        System.out.println(new String(dst,2,2));

        //7、reset()：恢复到mark的位置
        byteBuffer.reset();
        bufferStatus("reset",byteBuffer);
    }

    /**
     * 缓冲区分为直接缓冲区与非直接缓冲区，字节缓冲区要么是直接的，要么是非直接的。
     */
    @Test
    void bufferType(){
        /*
         如果为直接字节缓冲区，则 Java 虚拟机会尽最大努力直接在 此缓冲区上执行本机 I/O 操作。
        也就是说，在每次调用基础操作系统的一个本机 I/O 操作之前（或之后），
        虚拟机都会尽量避免将缓冲区的内容复制到中间缓冲区中（或从中间缓冲区中复制内容）。

         直接字节缓冲区可以通过调用此类的 allocateDirect() 工厂方法来创建。
        此方法返回的缓冲区进行分配和取消分配所需成本通常高于非直接缓冲区。
        直接缓冲区的内容可以驻留在常规的垃圾回收堆之外，它们对应用程序的内存需求量造成的影响可能并不明显。
        建议将直接缓冲区主要分配给那些易受基础系统的 本机 I/O 操作影响的大型、持久的缓冲区。
        一般情况下，最好仅在直接缓冲区能在程序性能方面带来明显好 处时分配它们。

         直接字节缓冲区还可以通过 FileChannel 的MappedByteBuffer map()将文件区域直接映射到内存中来创建。
        Java 平台的实现有助于通过 JNI 从本机代码创建直接字节缓冲区。
        如果以上这些缓冲区中的某个缓冲区实例指的是不可访问的内存区域，则试图访问该区域不会更改该缓冲区的内容，
        并且将会在访问期间或稍后的某个时间导致抛出不确定的异常。

         字节缓冲区是直接缓冲区还是非直接缓冲区可通过调用其 isDirect() 方法来确定。
        提供此方法是为了能够在 性能关键型代码中执行显式缓冲区管理。

         */
    }


    /**
     * 直接缓冲区
     */
    @Test
    void directBuffer(){
        //分配直接缓冲区
        ByteBuffer buf = ByteBuffer.allocateDirect(1024);
        //判断是否为直接缓冲区
        System.out.println("isDirect: "+buf.isDirect());
    }


    /**
     * 输出缓冲区的状态
     * @param method 对缓冲区进行的操作
     * @param byteBuffer 缓冲区
     */
    private void bufferStatus(String method,ByteBuffer byteBuffer) {
        System.out.println("-----------------------------"+method+"()-----------------------------");
        System.out.println("capacity: "+byteBuffer.capacity());
        System.out.println("limit: "+byteBuffer.limit());
        System.out.println("position: "+byteBuffer.position());
        System.out.println("remaining: "+byteBuffer.remaining());
    }
}
