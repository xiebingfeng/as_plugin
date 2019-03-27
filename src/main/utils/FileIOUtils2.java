package main.utils;

import com.intellij.openapi.actionSystem.AnAction;

import java.io.*;

public class FileIOUtils2 {

    /**
     * 读取模板文件中的字符内容
     *
     * @param fileName 模板文件名
     * @param anAction
     * @return
     */
    public static String readTemplateFile(String fileName, AnAction anAction) {
        InputStream in = null;
        in = anAction.getClass().getResourceAsStream("/Template/" + fileName);
        String content = "";
        content = new String(readStream(in));
        return content;
    }

    public static byte[] readStream(InputStream in) throws RuntimeException {
        try {
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            byte[] data = new byte[65536];

            int read;
            while ((read = in.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, read);
            }

            buffer.flush();
            return buffer.toByteArray();
        } catch (IOException var4) {
            throw new RuntimeException(var4);
        }
    }

    public static boolean writeFileFromString(final File file,
                                              final String content,
                                              final boolean append) {
        if (file == null || content == null) return false;
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(file, append));
            bw.write(content);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
