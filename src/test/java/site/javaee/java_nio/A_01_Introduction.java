package site.javaee.java_nio;

import org.apiguardian.api.API;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.Buffer;

@SpringBootTest
class A_01_Introduction {

    /**
     * 什么是NIO
     */
    @Test
    void what() {
        /*
        Java NIO（New IO | Non Blocking IO）是从Java 1.4版本开始引入的 一个新的IO API，可以替代标准的Java IO API。
        NIO与原来的IO有同样的作用和目的，但是使用 的方式完全不同，NIO支持面向缓冲区的、基于 通道的IO操作。
        NIO将以更加高效的方式进行文 件的读写操作。

         */
    }

    /**
     * 与传统IO的区别
     */
    @Test
    void difference() {
        /*
         IO:面向流(Stream Oriented)、属于阻塞IO(Blocking IO）。
         NIO:面向缓冲区(Buffer Oriented)、属于非阻塞IO(Non Blocking IO)、且有选择器(Selectors)。
         IO的流可以理解为水流，NIO的通道可以理解为负责交通运输的铁路，缓冲区类比火车，通道本身不负责传输数据，需要依赖于缓冲区传输数据。
         IO的流是单向的，缓冲区是双向的。

         ps：阻塞与选择器是针对socket而言的
         */
    }

    /**
     * NIO的核心
     */
    @Test
    void core(){
        /*
        Java NIO系统的核心在于：通道(Channel)和缓冲区 (Buffer)。
        通道表示打开到 IO 设备(例如：文件、 套接字)的连接。
        若需要使用 NIO 系统，需要获取 用于连接 IO 设备的通道以及用于容纳数据的缓冲 区。
        然后操作缓冲区，对数据进行处理。
        简而言之，Channel 负责传输， Buffer 负责存储

         */
    }

}
