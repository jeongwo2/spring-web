package com.example.myweb.controller;

import com.example.myweb.domain.AttachFileDTO;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/** ex05: 첨부파일
 * Controller for handling file uploads and downloads.
 */
@Controller
//@Log4j2
public class UploadController {

    private static final Logger log = LogManager.getLogger(UploadController.class);

    /**
     * Displays the upload form page.
     */
    @GetMapping("/uploadForm")
    public void uploadForm() {
        log.info("upload form");
    }

    /**
     * Handles file uploads from the form.
     *
     * @param uploadFile the uploaded files
     * @param model the model for rendering views
     */
    @PostMapping("/uploadFormAction")
    public void uploadFormPost(MultipartFile[] uploadFile, Model model) {
        String uploadFolder = "C:\\upload";

        for (MultipartFile multipartFile : uploadFile) {
            log.info("-------------------------------------");
            log.info("Upload File Name: " + multipartFile.getOriginalFilename());
            log.info("Upload File Size: " + multipartFile.getSize());

            File saveFile = new File(uploadFolder, multipartFile.getOriginalFilename());

            try {
                multipartFile.transferTo(saveFile);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }

    /**
     * Displays the upload AJAX page.
     */
    @GetMapping("/uploadAjax")
    public void uploadAjax() {
        log.info("upload ajax");
    }

    /**
     * Handles file uploads using AJAX.
     *
     * @param uploadFile the uploaded files
     * @return a list of uploaded file details
     *  APPLICATION_JSON_UTF8_VALUE change to APPLICATION_JSON_VALUE
     */
    @PostMapping(value = "/uploadAjaxAction", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<AttachFileDTO>> uploadAjaxPost(MultipartFile[] uploadFile) {
        List<AttachFileDTO> list = new ArrayList<>();
        String uploadFolder = "C:\\upload";

        String uploadFolderPath = getFolder();
        File uploadPath = new File(uploadFolder, uploadFolderPath);
        // add
        if (!uploadPath.exists() &&!uploadPath.mkdirs()) {
            log.error("Failed to create directory: {}", uploadPath);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
/*        if (!uploadPath.exists()) {
            uploadPath.mkdirs();
        }
        */

        for (MultipartFile multipartFile : uploadFile) {
            AttachFileDTO attachDTO = new AttachFileDTO();

            String uploadFileName = multipartFile.getOriginalFilename();
            attachDTO.setFileName(uploadFileName);

            UUID uuid = UUID.randomUUID();
            uploadFileName = uuid.toString() + "_" + uploadFileName;

            try {
                File saveFile = new File(uploadPath, uploadFileName);
                multipartFile.transferTo(saveFile);

                attachDTO.setUuid(uuid.toString());
                attachDTO.setUploadPath(uploadFolderPath);

                if (checkImageType(saveFile)) {
                    attachDTO.setImage(true);

                    FileOutputStream thumbnail = new FileOutputStream(new File(uploadPath, "s_" + uploadFileName));
                    Thumbnailator.createThumbnail(multipartFile.getInputStream(), thumbnail, 100, 100);
                    thumbnail.close();
                }

                list.add(attachDTO);

            } catch (Exception e) {
                log.error("Error uploading file", e);
            }
        }

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    /**
     * Gets a file for display.
     *
     * @param fileName the name of the file
     * @return the file as a byte array with appropriate headers
     */
    @GetMapping("/display")
    @ResponseBody
    public ResponseEntity<byte[]> getFile(String fileName) {
        log.info("fileName: " + fileName);

        File file = new File("c:\\upload\\" + fileName);
        log.info("file: " + file);

        ResponseEntity<byte[]> result = null;

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", Files.probeContentType(file.toPath()));
            result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Downloads a file.
     *
     * @param userAgent the user agent string
     * @param fileName the name of the file
     * @return the file as a resource with appropriate headers
     */
    @GetMapping(value = "/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(@RequestHeader("User-Agent") String userAgent, String fileName) {
        Resource resource = new FileSystemResource("c:\\upload\\" + fileName);

        if (!resource.exists()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        String resourceName = resource.getFilename();
        String resourceOriginalName = resourceName.substring(resourceName.indexOf("_") + 1);

        HttpHeaders headers = new HttpHeaders();
        try {
            String downloadName = null;

            if (userAgent.contains("Trident")) {
                log.info("IE browser");
                downloadName = URLEncoder.encode(resourceOriginalName, "UTF-8").replaceAll("\\+", " ");
            } else if (userAgent.contains("Edge")) {
                log.info("Edge browser");
                downloadName = URLEncoder.encode(resourceOriginalName, "UTF-8");
            } else {
                log.info("Chrome browser");
                downloadName = new String(resourceOriginalName.getBytes("UTF-8"), "ISO-8859-1");
            }

            log.info("downloadName: " + downloadName);

            headers.add("Content-Disposition", "attachment; filename=" + downloadName);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

    /**
     * Deletes a file.
     *
     * @param fileName the name of the file
     * @param type the type of the file (image or other)
     * @return a response indicating success or failure
     */
    @PostMapping("/deleteFile")
    @ResponseBody
    public ResponseEntity<String> deleteFile(String fileName, String type) {
        log.info("deleteFile: " + fileName);

        File file = null;
        try {
            file = new File("c:\\upload\\" + URLDecoder.decode(fileName, "UTF-8"));

            if (!file.exists()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            file.delete();

            if (type.equals("image")) {
                String largeFileName = file.getAbsolutePath().replace("s_", "");
                log.info("largeFileName: " + largeFileName);

                file = new File(largeFileName);
                file.delete();
            }

        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        return new ResponseEntity<String>("deleted", HttpStatus.OK);
    }

    /**
     * Checks if a file is an image type.
     *
     * @param file the file to check
     * @return true if the file is an image, false otherwise
     */
    private boolean checkImageType(File file) {
        try {
            String contentType = Files.probeContentType(file.toPath());
            return contentType.startsWith("image");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Gets the current date folder for uploads.
     *
     * @return the date folder as a string
     */
    private String getFolder() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(new Date());
    }
}