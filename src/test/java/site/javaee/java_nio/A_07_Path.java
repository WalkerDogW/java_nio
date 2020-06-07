package site.javaee.java_nio;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

/**Path 接口代表一个平台无关的平台路径，描述了目 录结构中文件的位置。
 * @author shkstart
 * @create 2020-06-07 21:51
 */
public class A_07_Path {
    @Test
    void path(){
        //Paths 提供的 get() 方法用来获取 Path 对象
        Path path = Paths.get("./abc.txt");
        System.out.println(path);
        //判断是否以 path 路径结束
        System.out.println(path.endsWith("abc.txt"));
        //判断是否是绝对路径
        System.out.println(path.isAbsolute());
        // 返回与调用 Path 对象关联的文件名
        System.out.println(path.getFileName());
        //返回的指定索引位置 idx 的路径名称
        System.out.println(path.getName(1));
        // 返回Path 根目录后面元素的数量
        System.out.println(path.getNameCount());
        //返回Path对象包含整个路径，不包含 Path 对象指定的文件路径
        System.out.println(path.getParent());
        //返回调用 Path 对象的根路径
        System.out.println(path.getRoot());
        //将相对路径解析为绝对路径???
        System.out.println(path.resolve(path));
        //作为绝对路径返回调用 Path 对象
        System.out.println(path.toAbsolutePath());
    }
}
