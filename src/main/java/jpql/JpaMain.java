package jpql;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            Team teamA = new Team();
            teamA.setName("팀A");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("팀B");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setUsername("회원1");
            member1.setAge(0);
            member1.setTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("회원2");
            member2.setAge(0);
            member2.setTeam(teamA);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("회원3");
            member3.setAge(0);
            member3.setTeam(teamB);
            em.persist(member3);


            //벌크연산
            //FLUSH 자동 호출 flush는 커밋, query나가거나, 강제 호출 될때 호출됨.
            int resultCount = em.createQuery("update Member m set m.age = 20")
                    .executeUpdate();
            em.clear();

            Member findMember = em.find(Member.class, member1.getId());
            System.out.println("findMember = " + findMember.getAge());

//            // Named Query
//            List<Member> resultList = em.createNamedQuery("Member.findByUsername", Member.class)
//                    .setParameter("username", "회원1")
//                    .getResultList();
//
//            for (Member member : resultList) {
//                System.out.println("member = " + member);
//            }


//            String query = "select m from Member m where m = :member";
//            Member result = em.createQuery(query, Member.class)
//                    .setParameter("member", member1)
//                    .getSingleResult();
//
//            System.out.println("result = " + result);



//            String query = "select t from Team t";

//            List<Team> result = em.createQuery(query, Team.class)
//                    .setFirstResult(0)
//                    .setMaxResults(2)
//                    .getResultList();
//
//            System.out.println("result = " + result.size());
//
//            for (Team team : result) {
//                System.out.println("team = " + team.getName() + "|members =" + team.getMembers());
//                for (Member member : team.getMembers()) {
//                    System.out.println("-> member = " + member);
//                }
//            }

//            String query = "select m from Member m";
//
//            List<Member> result = em.createQuery(query, Member.class)
//                    .getResultList();
//
//            for (Member member : result) {
//                System.out.println("member = " + member.getUsername() + ", " + member.getTeam().getName());
//            }
            // 회원1, 팀A(SQL)
            // 회원2, 팀A(1차캐시)
            // 회원3, 팀B(SQL)

            // 회원 100명 -> N + 1 : 여기서 1이란 첫번째 날린 쿼리, 즉 회원 1명을 조회하기 위해 날린 쿼리 한개를 말하고
            // N은 나머지 값을 얻기 위해 여기서는 99번만큼 날리는 쿼리 횟수를 말한다.
            // 이 문제는 지연로딩이든 즉시로딩이든 발생한다. 이문제를 해결하기 위해서 fetch join을 사용하ㅣㄴ다.
//            String query = "select m from Member m join fetch m.team";
//
//            List<Member> result = em.createQuery(query, Member.class)
//                    .getResultList();
//
//            for (Member member : result) {
//                System.out.println("member = " + member.getUsername() + ", " + member.getTeam().getName());
//            }

//            String query = "select function('group_concat', m.username) from Member m";
//            String query = "select group_concat(m.username) from Member m";
            //여기서 m.username은 상태 필드임 경로탐색의 끝, 탐색X
//            String query = "select m.username from Member m";

            //여기서 m.team은 묵시적 내부 조인(inner join)이 발생함, 탐색이 더 가능 m.team.name 이런식으로
//            String query = "select m.team from Member m";

            // 여기서 members는 컬렉션임. 컬렉션은 묵시적 내부 조인이 발생하지만 탐색은 불가하다.
//            String query = "select t.members from Team t";
//            Collection result = em.createQuery(query, Collection.class)
//                    .getResultList();
            // FROM 절에서 명시적 조인을 통해 별칭을 얻으면 별칭을 통해 탐색이 가능함.
//            String query = "select m.username from Team t join t.members m";
//
//            Collection result = em.createQuery(query, Collection.class)
//                    .getResultList();
//
//            for (Object s : result) {
//                System.out.println("s = " + s);
//            }


//            Team team = new Team();
//            team.setName("teamA");
//            em.persist(team);
//
//            Member member = new Member();
//            member.setUsername("teamA");
//            member.setAge(10);
//            member.setType(MemberType.ADMIN);
//
//            member.setTeam(team);
//
//            em.persist(member);
//
//            em.flush();
//            em.clear();

//            String query ="select m from Member m left outer join m.team t";
//            String query = "select m from Member m, Team t where m.username = t.name";
//            String query = "select m from Member m left join m.team t on t.name = 'teamA'";
//            String query = "select m from Member m join Team t on m.username = t.name";
//            String query = "select (select avg(m1.age) from Member m1) as avgAge from Member m join Team t on m.username = t.name";
//            String query = "select m .username, 'HELLO', TRUE from Member m " +
//                    "where m.type = :userType";
//
//            List<Object[]> result = em.createQuery(query)
//                    .setParameter("userType", MemberType.ADMIN)
//                    .getResultList();
//            for (Object[] objects : result) {
//                System.out.println("objects = " + objects[0]);
//                System.out.println("objects = " + objects[1]);
//                System.out.println("objects = " + objects[2]);
//            }



//            System.out.println("result = " + result.size());
//            for (Member member1 : result) {
//                System.out.println("member1 = " + member1);
//            }


//            List<MemberDTO> result = em.createQuery("select new jpql.MemberDTO(m.username, m.age) from Member m", MemberDTO.class)
//                    .getResultList();
//
//            MemberDTO memberDTO = result.get(0);
//            System.out.println("memberDTO = " + memberDTO.getUsername());
//            System.out.println("memberDTO = " + memberDTO.getAge());


            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }

        emf.close();
    }

}