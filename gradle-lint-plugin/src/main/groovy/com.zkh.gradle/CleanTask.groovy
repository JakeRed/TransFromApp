package com.zkh.gradle;

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

/**
 * 任务执行逻辑代码
 */
public class CleanTask extends DefaultTask {

    final String UNUSED_RESOURCES_ID = "UnusedResources"
    final String ISSUE_XML_TAG = "issue"
    HashSet<String> mFilePaths = new HashSet<>()
    StringBuilder mDelLogSb = new StringBuilder()

    public CleanTask() {
        group = "lintCleaner"
        description = "删除未使用的资源文件"
    }

    @TaskAction
    def start() {
        // 读取编译产物的lint结果
        def ext = project.extensions.findByName('lintCleaner') as PluginExtension

        def file = new File(ext.lintXmlPath)
        if (!file.exists()) {
            println '找不到lint的xml文件，请检查路径是否正确! '
            return
        }

        // 解析xml，添加无用文件的路径到容器中
        new XmlSlurper().parse(file).'**'.findAll { node ->
            if (node.name() == ISSUE_XML_TAG && node.@id == UNUSED_RESOURCES_ID) {
                mFilePaths.add(node.location.@file)
            }
        }

        def num = mFilePaths.size()
        if (num > 0) {
            // 可以观察是否遗漏文件夹，便于后续迭代
            mDelLogSb.append("未使用文件总个数:${num}\n")
            mDelLogSb.append("=====删除文件列表=====\n")
            for (String path : mFilePaths) {
                println path
                // 删除文件
                deleteFileByPath(path)
            }
            println mDelLogSb
        } else {
            println '不存在无用资源！'
        }
    }

    /**
     * 删除并记录
     * @param path
     * @return
     */
    def deleteFileByPath(String path) {
        if (isDelFile(path)) {
            if (new File(path).delete()){
                mDelLogSb.append('\n\t' + path)
            }
        }
    }

    /**
     * drawable,mipmap,menu 文件夹下文件
     * @param path
     */
    def isDelFile(String path) {
        String dir = path
        (dir.contains('layout')
                ||dir.contains('drawable')
                || dir.contains('mipmap')
                || dir.contains('menu')) && (dir.endsWith('.png')
                || dir.endsWith('.jpg')
                || dir.endsWith('.jpeg')
                || dir.endsWith('.svg')
                || dir.endsWith('.webp')
                || dir.endsWith('.xml'))
    }

}