package brukickerleague;

import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@AllArgsConstructor
class Db {

  private final EntityManagerFactory emf;

  public <T> T save(T entity) {
    return inTx(tx -> tx.save(entity));
  }

  @SuppressWarnings("unchecked")
  public <T> Optional<T> by(Class<T> entityClass, String property, Object value) {
    return inTx(tx -> tx.by(entityClass, property, value));
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

    public <T> List<T> all(Class<T> entityClass, String orderBy) {
      TypedQuery<T> query = em.createQuery("from " + entityClass.getSimpleName() + " order by :orderBy DESC", entityClass);
      query.setParameter("orderBy", orderBy);

      return query.getResultList();
    }
  }
}