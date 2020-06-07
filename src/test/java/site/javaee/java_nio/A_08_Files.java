package site.javaee.java_nio;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.channels.Pipe;
import java.nio.file.Files;

/**Files 是用于操作文件或目录的工具类。
 * @author shkstart
 * @create 2020-06-07 22:03
 */
public class A_08_Files {

    @Test
    void files(){
        /*
        Files常用方法：操作文件
             Path copy(Path src, Path dest, CopyOption … how) : 文件的复制
             Path createDirectory(Path path, FileAttribute<?> … attr) : 创建一个目录
             Path createDirectories(Path path, FileAttribute<?> … attr) : 创建多级
             Path createFile(Path path, FileAttribute<?> … arr) : 创建一个文件
             void delete(Path path) : 删除一个文件
             Path move(Path src, Path dest, CopyOption…how) : 将 src 移动到 dest 位置
             long size(Path path) : 返回 path 指定文件的大小


        Files常用方法：用于判断
 boolean exists(Path path, LinkOption … opts) : 判断文件是否存在
 boolean isDirectory(Path path, LinkOption … opts) : 判断是否是目录
 boolean isExecutable(Path path) : 判断是否是可执行文件
 boolean isHidden(Path path) : 判断是否是隐藏文件
 boolean isReadable(Path path) : 判断文件是否可读
 boolean isWritable(Path path) : 判断文件是否可写
 boolean notExists(Path path, LinkOption … opts) : 判断文件是否不存在
 public static <A extends BasicFileAttributes> A readAttributes(Path path,Class<A> type,LinkOption...options) : 获取与 path 指定的文件相关联的属性。



Files常用方法：用于操作内容
 SeekableByteChannel newByteChannel(Path path, OpenOption…how) : 获取与指定文件的连接，
how 指定打开方式。
 DirectoryStream newDirectoryStream(Path path) : 打开 path 指定的目录
 InputStream newInputStream(Path path, OpenOption…how):获取 InputStream 对象
 OutputStream newOutputStream(Path path, OpenOption…how) : 获取 OutputStream 对象

         */

    }

    /**
     * 自动资源管理
     */
    @Test
    void  autoCloseable() throws IOException {
        //当 try 代码块结束时，自动释放资源。因此不需要显示的调用 close() 方法。该形式也称为“带资源的 try 语句”。
        try (Pipe.SinkChannel sinkChannel = Pipe.open().sink()) {
            //可能发生异常的语句
            System.out.println("try");
        }catch (Exception e){
            //异常的处理语句
            System.out.println("catch");
        }finally {
            //一定执行的语句
            System.out.println("finally");
        }

        /*
        注意：
            1/try 语句中声明的资源被隐式声明为 final ，资源的作用局限于带资源的 try 语句
            2/可以在一条 try 语句中管理多个资源，每个资源以“;” 隔开即可。
            3/需要关闭的资源，必须实现了 AutoCloseable 接口或其自接口 Closeable
         */

    }

}
