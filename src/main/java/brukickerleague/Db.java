package brukickerleague;

import lombok.AllArgsConstructor;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.util.Optional;
import java.util.function.Function;

@AllArgsConstructor
class Db {

  private final EntityManagerFactory emf;

  public <T> T save(T entity) {
    EntityManager em = emf.createEntityManager();
    EntityTransaction tx = em.getTransaction();
    try {
      tx.begin();
      T mergedEntity = new Tx(em).save(entity);
      tx.commit();
      return mergedEntity;
    } finally {
      em.close();
    }
  }

  @SuppressWarnings("unchecked")
  public <T> Optional<T> by(Class<T> entityClass, String property, Object value) {
    EntityManager em = emf.createEntityManager();
    EntityTransaction tx = em.getTransaction();
    try {
      tx.begin();
      Query query = em.createQuery("from " + entityClass.getSimpleName() + " where " + property + "= :value");
      query.setParameter("value", value);
      Optional<T> entity = new Tx(em).by(entityClass, property, value);
      tx.commit();
      return entity;
    } catch (Exception e) {
      tx.rollback();
      return Optional.empty();
    } finally {
      em.close();
    }
  }

  public <T> T inTx(Function<Tx, T> worker) {
    EntityManager em = emf.createEntityManager();
    EntityTransaction tx = em.getTransaction();
    try {
      tx.begin();
      T result = worker.apply(new Tx(em));
      tx.commit();
      return result;
    } catch (Exception e) {
      tx.rollback();
      return null;
    } finally {
      em.close();
    }
  }

  @AllArgsConstructor
  class Tx {

    private final EntityManager em;

    @SuppressWarnings("unchecked")
    public <T> Optional<T> by(Class<T> entityClass, String property, Object value) {
      Query query = em.createQuery("from " + entityClass.getSimpleName() + " where " + property + "= :value");
      query.setParameter("value", value);
      try {
        Object entity = query.getSingleResult();
        return Optional.ofNullable((T) entity);
      } catch (Exception e) {
        return Optional.empty();
      }
    }

    public <T> T save(T entity) {
      try {
        return em.merge(entity);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
  }
}
