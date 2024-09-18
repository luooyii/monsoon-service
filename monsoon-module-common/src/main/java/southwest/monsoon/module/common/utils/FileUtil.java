package southwest.monsoon.module.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
public class FileUtil {
    private FileUtil() {
    }

    public static String getFileSuffix(String fileName) {
        if (StringUtils.isBlank(fileName)) {
            return "";
        }
        int index = fileName.lastIndexOf('.');
        if (index < 0) {
            return "";
        }
        return fileName.substring(index);
    }

    /**
     * byte[]数组转File
     *
     * @param bytes
     * @param fileFullPath
     * @return
     */
    public static File byte2file(byte[] bytes, String fileFullPath) throws IOException {
        if (bytes == null) {
            return null;
        }
        File file = new File(fileFullPath);
        if (file.exists()) {
            return file;
        }
        //判断文件是否存在
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdir();
        }
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            fileOutputStream.write(bytes);
            return file;
        }
    }

    public static String sizeMB(long size) {
        BigDecimal sizeMb = new BigDecimal(size);
        sizeMb = sizeMb.divide(new BigDecimal(1024 * 1024));
        sizeMb = sizeMb.setScale(2, RoundingMode.HALF_UP);
        return sizeMb + "MB";
    }

    public static void delete(File file) {
        if (file != null) {
            if (file.exists()) {
                if (!file.delete()) {
                    log.warn("Delete {} failed.", file);
                    try {
                        log.warn("Force deleting {} on exit.", file);
                        FileUtils.forceDeleteOnExit(file);
                    } catch (IOException e) {
                        log.error("Force deleting {} failed.", file);
                    }
                }
            } else {
                log.warn("File to be deleted does not exist.");
            }
        } else {
            log.warn("File to be deleted is null.");
        }
    }
}
