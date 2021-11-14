package myproject.eumfruit.dto;

import lombok.Getter;
import lombok.Setter;
import myproject.eumfruit.entity.Itemimg;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class ItemImgDto {
    private Long id;

    private String imgName;

    private String imgUrl;

    private String repImgYn;

    private static ModelMapper modelMapper = new ModelMapper(); // ModelMapper 객체 추가

    public static ItemImgDto of(Itemimg itemImg) {
        return modelMapper.map(itemImg, ItemImgDto.class);
        /* itemImg 엔티티 객체를 파라미터로 바아 itemImg 객체의 자료형과 멤버변수의 이름이 같을 때 ItemImgDto로 값을 복사해서 반환한다.
        static 메소드로 선언해 ItemImgDto객체를 생성하지 않아도 호출할 수 있도록 한다.
         */
    }
}
