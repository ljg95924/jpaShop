package jpabook.jpashop.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Member {
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "member") // order 테이블에 있는 member 필드에 의해 매핑된 것
    private List<Order> orders = new ArrayList<>(); // 컬렉션은 필드에서 초기화하는 것이 안전하다. (생성자에서 초기화 하는 것 보다!)
    // 하이버네이트는 엔티티를 영속할 때 컬렉션을 감싸서 하이버네이트가 제공하는 내장 컬렉션으로 변경한다
    // 예제 코드
    // Member member = new Member();
    // System.out.println("member.getOrders().getClass());
    // em.persist(team);
    // System.out.println("member.getOrders().getClass());
    // 출력 결과
    // Class java.util.ArrayList
    // Class org.hibernate.collection.internal.PersistentBag




    // SpringPhysicalNamingStrategy 가 엔티티의 필드명을 그대로 테이블의 컬럼명으로 사용하게 해줌
    // 1. 카멜케이스 -> 언더스코어로 변경해줌
    // 2. .(점) -> _(언더스코어)로 변경해줌
    // 3. 대문자 -> 소문자로 변경해줌
    
}
