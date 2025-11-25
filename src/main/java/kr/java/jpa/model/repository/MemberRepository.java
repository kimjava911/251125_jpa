package kr.java.jpa.model.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import kr.java.jpa.model.entity.Member;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MemberRepository {
    private final EntityManagerFactory emf; // bean에 등록한 내용을 불러옴
    public MemberRepository(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public void delete(long id) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Member member = em.find(Member.class, id);
            em.remove(member);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new RuntimeException("멤버 삭제 실패");
        } finally {
            em.close();
        }
    }

    public void updatePassword(long id, String password) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Member member = em.find(Member.class, id); // 멤버를 불러옴 -> 영속성 컨텍스트
            // <- 스냅샷. 이 상태와 이후 상태가 어떻게 변했는지를 체크.
            member.setPassword(password); // 속성을 바꾸면 -> 바꾼 상태에서 영속성 컨텍스트.
            tx.commit(); // 반영이 됨. -> 변경된 필드에 대한 update문이 작성됨.
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new RuntimeException("멤버 수정 실패");
        } finally {
            em.close();
        }
    }

    // 1) id -> 수정할 개별 값
    // 2) id가 동일한 객체를 넣어서 나머지 값을 덮어씌우기
    public void update(Member member) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(member); // id가 일치하는 엔터티에 대해 나머지 값들을 덮어씌우는 형태
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new RuntimeException("멤버 수정 실패");
        } finally {
            em.close();
        }
    }

    public Member findById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            // '객체'를 키로 집어넣어야함 (JPA -> PK. int, long -> Integer, Long)
            return em.find(Member.class, id);
            // Member -> @Entity
            // id -> @Id
        } catch (Exception e) {
            throw new RuntimeException("멤버 개별 조회 실패 : %d".formatted(id));
        } finally {
            em.close();
        }
    }

    public List<Member> findAll() {
        EntityManager em = emf.createEntityManager();
        // JPA -> 단위가 Entity 1개 -> Table.
        try {
            return em.createQuery("SELECT m FROM Member m", Member.class)
                    .getResultList(); // JPQL
        } catch (Exception e) {
            throw new RuntimeException("멤버 전체 조회 실패");
        } finally {
            em.close();
        }
    }

    public void save(Member member) {
        // 1단계 : EntityManager <- EntityManagerFactory
        // 새로운 EntityManager
        EntityManager em = emf.createEntityManager();
        // 2단계 : 트랜잭션 객체
        EntityTransaction tx = em.getTransaction(); // manager -> tx.
        try {
            // 3단계 : 트랜잭션 시작
            tx.begin(); // auto commit false
            // 데이터변경 작업 허용하는 옵션
            // 4단계 : 엔터티를 영속성 컨텍스트 저장 (임시저장)
            em.persist(member); // Transient -> Managed (자바 객체 -> DB 연결)
            // 아직 SQL문은 실행된 것이 아니라, 1차 캐시. (임시저장)
            // 5단계 : 트랜잭션 커밋 시도
            tx.commit(); // flush -> 구동을 시켜보는데... fail -> throw -> catch
            // 성공시 커밋되는 개념 (커밋 시도)
        } catch (Exception e) {
            // 예외 발생 시에
            if (tx.isActive()) { // 트랜잭션 활성화 상태라면...
                tx.rollback(); // 롤백
            }
            throw new RuntimeException("멤버 저장 실패");
        } finally {
            // 6단계: 리소스 정리
            em.close(); // em 반환.
            // em -> JDBC Connection
        }
    }
}
