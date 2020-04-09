package com.taobao.qa.ruleengine.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExpressFileReader {
    private static final Logger logger  = LoggerFactory.getLogger(ExpressFileReader.class);

    /**
     * 直接从文件系统读取文件
     * @throws IOException
     */
    public static Map<String,String> loadRuleExpressFromFile(String filename) throws IOException {
        logger.debug("[Enter]loadRuleExpressFromFile()");
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        return loadFromBufferedReader(reader);
    }

    /**
     * 处理从前端上传的文件
     * @throws IOException
     */
    public static Map<String,String> loadRuleExpressFromMultipartFile(MultipartFile multipartFile) throws IOException {
        logger.debug("[Enter]loadRuleExpressFromMultipartFile()");
        InputStreamReader is = new InputStreamReader(multipartFile.getInputStream());
        BufferedReader br = new BufferedReader(is);
        return loadFromBufferedReader(br);
    }

    private static Map<String,String> loadFromBufferedReader(BufferedReader reader) throws IOException {
        Pattern sectionPattern = Pattern.compile("^\\s*\\[(.+)\\]");
        Map<String,String> output = new HashMap<String, String>();

        StringBuffer sectionContent = new StringBuffer("");
        String sectionName = null;
        String line;

        try {
            while ((line = reader.readLine()) != null) {
                // remove BOM
                line = line.replace("\uFEFF","");
                String lineTrimed = line.trim();

                // 忽略空白行或#开头的注释行
                if(lineTrimed.isEmpty() || lineTrimed.startsWith("#")) {
                    continue;
                }

                // 判断是否是Section Line
                Matcher m = sectionPattern.matcher(lineTrimed);

                // 发现新的section
                if (m.find()) {
                    // 第一个section
                    if(sectionName == null) {
                        // 获取sectionName(大小写不敏感)
                        sectionName = m.group(1).toLowerCase();

                        // sectionContent清零
                        sectionContent.setLength(0);
                        continue;
                    }

                    // 非第一个section
                    // 记录最近的sectionContent(trim 结尾的\n)
                    output.put(sectionName, sectionContent.toString().trim());

                    // sectionContent清零
                    sectionContent.setLength(0);

                    // 获取新的sectionName(大小写不敏感)
                    sectionName = m.group(1).toLowerCase();

                    // 如果sectionName有重复，则抛异常
                    if (output.containsKey(sectionName)) {
                        String errMsg = String.format("存在重名Section \"%s\"", sectionName);
                        throw new IllegalStateException(errMsg);
                    }
                }
                // 当前line是sectionContent
                else {
                    if (sectionName == null) {
                        String errMsg = "解析Rule Express失败:sectionName 还未初始化";
                        throw new IllegalStateException(errMsg);
                    }
                    sectionContent.append(line + "\n");
                }
            }

            // 记录最后一个sectionContent(trim 结尾的\n)
            output.put(sectionName, sectionContent.toString().trim());
            return output;
        }
        finally {
            reader.close();
        }
    }

    public static List<String> getFileListWithGlobPattern(String glob,String location) throws IOException {
        final PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher(glob);
        List<String> outputFileList = new Vector<String>();

        Files.walkFileTree(Paths.get(location),new FileVisitor(pathMatcher,outputFileList));

        return outputFileList;
    }

    private static class FileVisitor extends SimpleFileVisitor<Path> {
        private PathMatcher pathMatcher;
        private List<String> outputFileList;

        public FileVisitor(PathMatcher matcher, List<String> outputFileList) {
            if(outputFileList == null || !outputFileList.isEmpty()) {
                throw new IllegalArgumentException("outputFileList is null or is not empty");
            }
            this.pathMatcher = matcher;
            this.outputFileList = outputFileList;
        }

        public List<String> getOutputFileList() {
            return this.outputFileList;
        }

        @Override
        public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) throws IOException {
            if(pathMatcher.matches(path)) {
                //System.out.println(path);
                outputFileList.add(path.toString());
            }
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc) {
            return FileVisitResult.CONTINUE;
        }
    }


    ////// Test
    public static void main(String[] args) throws IOException {
        System.out.println("Enter main...");

        List<String> output = getFileListWithGlobPattern("glob:**/DemoRule*.ql",".");

        System.out.println("Output:");
        System.out.println("=======");
        for(String item: output) {
            System.out.println(item);
        }

        System.out.println("Exit main...");
    }
}
