package com.example.myweb.task;

import com.example.myweb.domain.BoardAttachVO;
import com.example.myweb.mapper.BoardAttachMapper;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/** ex05
 * This class is responsible for checking and removing old files from the server.
 * It uses Spring's Scheduled annotation to run the checkFiles method at 2 AM every day.
 * The method retrieves a list of files from the database and compares them with the files in the server's upload directory.
 * If a file in the directory is not in the database, it is considered old and will be deleted.
 *
 * @author YourName
 * @since 2023-01-01
 */
@Log4j2
@Component
public class FileCheckTask {

    /**
     * The BoardAttachMapper interface is used to interact with the database.
     * It provides methods to retrieve file information.
     */
    @Setter(onMethod_ = { @Autowired })
    private BoardAttachMapper attachMapper;

    /**
     * This method generates the path to the directory of files uploaded one day ago.
     *
     * @return The path to the directory of files uploaded one day ago.
     */
    private String getFolderYesterDay() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        String str = sdf.format(cal.getTime());
        return str.replace("-", File.separator);
    }

    /**
     * This method is scheduled to run at 2 AM every day.
     * It checks for old files in the server's upload directory and removes them if they are not in the database.
     *
     * @throws Exception If an error occurs during the file deletion process.
     */
    @Scheduled(cron = "0 0 2 * * *")
    public void checkFiles() throws Exception {
        log.debug("File Check Task run.................");
        log.debug(new Date());

        // Retrieve a list of files from the database
        List<BoardAttachVO> fileList = attachMapper.getOldFiles();

        // Prepare a list of paths for the files in the database
        List<Path> fileListPaths = fileList.stream()
                .map(vo -> Paths.get("C:\\upload", vo.getUploadPath(), vo.getUuid() + "_" + vo.getFileName()))
                .collect(Collectors.toList());

        // Add paths for thumbnail files of image files to the list
        fileList.stream()
                .filter(vo -> vo.isFileType() == true)
                .map(vo -> Paths.get("C:\\upload", vo.getUploadPath(), "s_" + vo.getUuid() + "_" + vo.getFileName()))
                .forEach(p -> fileListPaths.add(p));

        log.debug("===========================================");

        // Log the paths of the files in the database
        fileListPaths.forEach(p -> log.debug(p));

        // Get the directory of files uploaded one day ago
        File targetDir = Paths.get("C:\\upload", getFolderYesterDay()).toFile();

        // Retrieve a list of files in the directory that are not in the database
        File[] removeFiles = targetDir.listFiles(file -> fileListPaths.contains(file.toPath()) == false);

        log.debug("-----------------------------------------");

        // Delete the old files
        for (File file : removeFiles) {
            log.debug(file.getAbsolutePath());
            file.delete();
        }
    }
}
