package com.example.myweb.domain;

import lombok.Data;

/**Part6 업로드 된 파일의 데이터 반환
 * This class represents a data transfer object (DTO) for an attached file.
 * It contains properties for the file name, upload path, UUID, and whether the file is an image.
 */
@Data
public class AttachFileDTO {

	private String fileName;
	private String uploadPath;
	private String uuid;
	private boolean image;

}
