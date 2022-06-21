package jpabook.jpashop.repository;

import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    public void save(Item item) {
        if (item.getId() == null) {
            em.persist(item);
        } else {
            // 찾아온 item의 값을 모두다 바꺼치기 해버림.
            // item은 영속성으로 바뀌지않지만  em.merge()의 반환값이 영속성임.
            // 만약 쓸일이있으면 변수 만들어서 사용.
            em.merge(item);// 나중에 설명 ( update 비스무리함)
        }
    }

    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }
}
