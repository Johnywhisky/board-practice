package net.scit.spring7.utility;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class FileService {
	public static String saveFileAndgetSavedFileName(MultipartFile uploadFile, String uploadFilePath) {
		checkPath(uploadFilePath);
		String fullPath = null;
		String savedFileName = null;

		if (!uploadFile.isEmpty()) {
			String originalFileName = uploadFile.getOriginalFilename();
			String[] fileNameAndExt = getFileNameAndExtension(originalFileName);

			savedFileName = fileNameAndExt[1].equals("") ?
				fileNameAndExt[0] + "_" + getRandomString():
				fileNameAndExt[0] + "_" + getRandomString() + "." + fileNameAndExt[1];
			fullPath = uploadFilePath + "/" + savedFileName;
		}
		File savedFile = new File(fullPath);

		try {
			uploadFile.transferTo(savedFile);
		} catch (IOException e) {
			e.printStackTrace();
			savedFileName = null;
		}
		return savedFileName;
	}
	
	private static File checkPath(String uploadFilePath) {
		File path = new File(uploadFilePath);
		if (!path.isDirectory()){
			path.mkdir();
		}
		
		return path;
	}

	public static Boolean deleteFile(String fullPath) {
		File file = new File(fullPath);
		log.info("isFile: {}", file.isFile());
		if (file.isFile()) {

			return file.delete();
		}

		return false;
	}

	// Private Method
	private static String[] getFileNameAndExtension(String originalFileName) {
		String fileName;
		String ext;
		int indexOf = originalFileName.lastIndexOf(".");

		if (indexOf == -1) {
			fileName = originalFileName;
			ext = "";
		} else {
			fileName = originalFileName.substring(0, indexOf);
			ext = originalFileName.substring(indexOf+1);
		}
		
		String[] result = {fileName, ext};

		return result;
	}

	private static String getRandomString() {

		return UUID.randomUUID().toString();
	}
}
