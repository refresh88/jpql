package jpql;

import javax.persistence.*;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Member member = new Member();
            member.setUsername("teamA");
            member.setAge(10);
            member.setType(MemberType.ADMIN);

            member.setTeam(team);

            em.persist(member);

            em.flush();
            em.clear();

//            String query ="select m from Member m left outer join m.team t";
//            String query = "select m from Member m, Team t where m.username = t.name";
//            String query = "select m from Member m left join m.team t on t.name = 'teamA'";
//            String query = "select m from Member m join Team t on m.username = t.name";
//            String query = "select (select avg(m1.age) from Member m1) as avgAge from Member m join Team t on m.username = t.name";
            String query = "select m .username, 'HELLO', TRUE from Member m " +
                    "where m.type = :userType";

            List<Object[]> result = em.createQuery(query)
                    .setParameter("userType", MemberType.ADMIN)
                    .getResultList();
            for (Object[] objects : result) {
                System.out.println("objects = " + objects[0]);
                System.out.println("objects = " + objects[1]);
                System.out.println("objects = " + objects[2]);
            }



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