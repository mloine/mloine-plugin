package com.mloine;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @title: JAVA类描述
 * @fileName MloineMojo.java
 * @description: TODO  maven插件需要继承AbstractMojo
 * @author mloine
 * @date 2019/5/3 11:22
 */
@Mojo(name = "mloine",defaultPhase = LifecyclePhase.PACKAGE)
public class MloineMojo extends AbstractMojo {

    // 这个集合石用来放文件的
    private static List<String> fileList = new ArrayList<String>();

    private int allLines = 0;
    /**
     * 引入需要的参数数据
      */
    @Parameter(property = "currentBaseDir",defaultValue = "src/main")
    private String currentBaseDir;  // 这个参数是引入本插件的工程传进来的文件夹

    @Parameter(property = "suffix",defaultValue = ".java")
    private String suffix;  // 这个参数是引入本插件的工程传入进来的文件类型 (例如.java)



    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        List<String> fileList = scanFile(currentBaseDir);
        System.out.println("mloine plugins start .....");
        System.out.println("FilePath:" + currentBaseDir);
        System.out.println("FileSuffix:" + suffix);
        System.out.println("FileTotal:"+fileList.size());
        System.out.println("allLines:"+allLines);
        System.out.println("mloine plugins end .....");
    }

    /**
     * 递归统计文件,将所有符合条件的文件放入集合中
     */
    private List<String> scanFile(String filePath) {
        File dir = new File(filePath);
        // 递归查找所有的class文件
        for(File file : dir.listFiles()) {
            if(file.isDirectory()) {  // 如果是文件夹
                scanFile(file.getAbsolutePath());  // 进入递归 ,参数是遍历到的当前文件夹的绝对路径
            } else {
                if(file.getName().endsWith(suffix)) {
                    fileList.add(file.getName());  // 符合条件 添加到集合中
                    allLines+=countLines(file);    // 统计行数
                }
            }
        }
        return fileList;
    }
    private int countLines(File file) {
        int lines = 0;
        try {
            // 转换成高级流  可以按行读
            BufferedReader reader =  new BufferedReader(new FileReader(file));
            while(reader.ready()) {
                reader.readLine();
                lines++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }
}
