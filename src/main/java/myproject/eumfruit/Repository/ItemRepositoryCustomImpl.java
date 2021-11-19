package myproject.eumfruit.Repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import myproject.eumfruit.constant.ItemKind;
import myproject.eumfruit.constant.ItemSellStatus;
import myproject.eumfruit.dto.ItemSearchDto;
import myproject.eumfruit.dto.ProductItemDto;
import myproject.eumfruit.dto.QProductItemDto;
import myproject.eumfruit.entity.Item;
import myproject.eumfruit.entity.QItem;
import myproject.eumfruit.entity.QItemimg;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

public class ItemRepositoryCustomImpl implements ItemRepositoryCustom {
    /* ItemRepositoryCustom 인터페이스를 구현하는 ItemRepositoryCustomImpl 클래스를 작성한다.
    이때 주의할 점은 클래스명 끝에 "Impl"을 붙여야 정상적으로 동작한다.
    Querydsl에서는 BooleanExpression 이라는 where절에서 사용할 수 있는 값을 지원합니다.
    BooleanExpression을 반환하는 메소드를 만들고 해당 조건들을 다른 쿼리를 생성할 때 사용할 수 있기 때문에 중복 코드르 줄일 수 있는 장점이 있다.
     */

    private JPAQueryFactory queryFactory;

    public ItemRepositoryCustomImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

    private BooleanExpression searchSellStatusEq(ItemSellStatus searchSellStatus){
        return searchSellStatus == null ? null : QItem.item.itemSellStatus.eq(searchSellStatus);
    }

    private BooleanExpression regDtsAfter(String searchDateType){

        LocalDateTime dateTime = LocalDateTime.now();

        if(StringUtils.equals("all", searchDateType) || searchDateType == null){
            return null;
        } else if(StringUtils.equals("1d", searchDateType)){
            dateTime = dateTime.minusDays(1);
        } else if(StringUtils.equals("1w", searchDateType)){
            dateTime = dateTime.minusWeeks(1);
        } else if(StringUtils.equals("1m", searchDateType)){
            dateTime = dateTime.minusMonths(1);
        } else if(StringUtils.equals("6m", searchDateType)){
            dateTime = dateTime.minusMonths(6);
        }

        return QItem.item.regTime.after(dateTime);
    }

    private BooleanExpression searchByLike(String searchBy, String searchQuery){

        if(StringUtils.equals("itemNm", searchBy)){
            return QItem.item.itemNm.like("%" + searchQuery + "%");
        } else if(StringUtils.equals("createdBy", searchBy)){
            return  QItem.item.createBy.like("%" + searchQuery + "%");
        }

        return null;
    }

    @Override
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {

        System.out.println("쿼리조건 : "+itemSearchDto.getSearchBy()+itemSearchDto.getSearchQuery()+itemSearchDto.getSearchDateType());
        QueryResults<Item> results = queryFactory
                .selectFrom(QItem.item)
                .where(regDtsAfter(itemSearchDto.getSearchDateType()),
                        searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
                        searchByLike(itemSearchDto.getSearchBy(),
                                itemSearchDto.getSearchQuery()))
                .orderBy(QItem.item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();



        List<Item> content = results.getResults();

        long total = results.getTotal();
        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public List<ProductItemDto> getProductItemList(ItemKind itemKind) {
        QItem item = QItem.item;
        QItemimg itemImg = QItemimg.itemimg;

        QueryResults<ProductItemDto> results = queryFactory
                .select(
                        new QProductItemDto(
                                item.id,
                                item.itemNm,
                                item.itemDetail,
                                itemImg.imgUrl,
                                item.price)
                )
                .from(itemImg)
                .join(itemImg.item, item)
                .where(itemImg.repimgYn.eq("Y"))
                .where(item.itemKind.eq(itemKind))
                .fetchResults();

        List<ProductItemDto> content = results.getResults();
        return content;
    }
}
