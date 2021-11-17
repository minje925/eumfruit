package myproject.eumfruit.service;

import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

@Service
@Log
public class FileService {

    public String uploadFile(String uploadPath, String originalFileName,
                             byte[] fileData) throws Exception {
        UUID uuid = UUID.randomUUID();  // UUID는 서로 다른 개체들을 구별하기 위해서 이름을 부여할 때 사용한다. => 파일명 중복문제 해결
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String savedFileName = uuid.toString()+extension;   // UUID와 원래 파일의 이름의 확장자를 조합해서 파일이름을 만든다.
        String fileUploadFullUrl = uploadPath + "/" + savedFileName;
        FileOutputStream fos = new FileOutputStream(fileUploadFullUrl); // FileOutputStream 클래스는 바이트 단위의 출력을 내보내는 클래스, 생성자로 파일이 저장될 위치와 파일의 이름을 넘겨 파일에 쓸 파일 출력 스트림을 만든다.
        fos.write(fileData);    // 파일을 출력 스트림에 입력한다.
        fos.close();    // 업로드된 파일의 이름을 반환한다.
        return savedFileName;
    }

    public void deleteFile(String filePath) throws Exception {
        File deleteFile = new File(filePath);   // 파일이 저장된 경로를 이용하여 파일 객체를 생성한다.

        if(deleteFile.exists()) {
            deleteFile.delete();    // 해당 파일이 존재하면 파일을 삭제한다.
            log.info("파일을 삭제했습니다.");
        } else{
            log.info("파일이 존재하지 않습니다.");
        }
    }
}
