package myproject.eumfruit.service;

import lombok.RequiredArgsConstructor;
import myproject.eumfruit.Repository.ItemImgRepository;
import myproject.eumfruit.entity.Itemimg;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemImgService {

    @Value(("${itemImgLocation}"))  // application.properties 파일에 등록한 itemImgLocation 값을 불러와 itemImgLocation에 넣는다.
    private String itemImgLocation;

    private final ItemImgRepository itemImgRepository;

    private final FileService fileService;

    public void saveItemImg(Itemimg itemImg, MultipartFile itemImgFile) throws Exception {
        String oriImgName = itemImgFile.getOriginalFilename();
        String imgName = "";
        String imgUrl = "";

        // 파일 업로드
        if(!StringUtils.isEmpty(oriImgName)) {
            imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
            // 사용자가 상품의 이미지를 등록했다면 저장할 경로와 파일의 이름, 파일을 파일의 바이트 배열을 파일 업로드 파라미터로 uploadFile 메소드를 호출한다.
            // 호출 결과 로컬에 저장된 파일의 이름을 imgName변수에 저장한다.
            imgUrl = "/images/item/" + imgName;
            /*
            저장한 상품 이미지를 불러올 경로를 지정한다. 외부 리소스를 불러오는 urlPatterns로 WebMvcConfig클래스에서 "/images/**"를 설정해주었다.
            또한 aplication.properties에서 설정한 uploadPath 프로퍼티 경로인 "C:/eumfruit/" 아래 item 폴더에 이미지를 저장하므로 상품 이미지를 불러오는
            경로로 "/images/item/"을 붙여준다.
             */
        }

        // 상품 이미지 정보 저장
        itemImg.updateItemImg(oriImgName, imgName, imgUrl); // 입력받은 상품 이미지 정보를 저장한다.
        itemImgRepository.save(itemImg);    // imgName : 실제 로컬에 저장된 상품 이미지 파일 이름, oriImgName : 업로드했던 상품 이지미 파일의 원래 이름
        // imgUrl : 업로드 결과 로컬에 저장된 상품 이미지 파일을 불러오는 경로
    }
}
