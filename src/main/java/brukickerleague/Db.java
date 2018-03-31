package brukickerleague;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

@AllArgsConstructor
class Db {

  private static final Logger LOGGER = LoggerFactory.getLogger(Db.class);

  private final EntityManagerFactory emf;

  public <T> T save(T entity) {
    return inTx(tx -> tx.save(entity));
  }

  public <T> List<T> all(Class<T> entityClass, String orderBy) {
    return inTx(tx -> tx.all(entityClass, orderBy));
  }

  public <T> List<T> all(Class<T> entityClass, String property, Object value, String orderBy) {
    return inTx(tx -> tx.all(entityClass, property, value, orderBy));
  }

  @SuppressWarnings("unchecked")
  public <T> Optional<T> by(Class<T> entityClass, String property, Object value) {
    return inTx(tx -> tx.by(entityClass, property, value));
  }

  public <T> List<T> query(Class<T> entityClass, String queryName, Object... values) {
    return inTx(tx -> tx.query(entityClass, queryName, values));
  }

  public <T> T raw(BiFunction<Tx, EntityManager, T> worker) {
    return inTx(tx -> worker.apply(tx, tx.em));
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
  static class Tx {

    private final EntityManager em;

    @SuppressWarnings("unchecked")
    public <T> Optional<T> by(Class<T> entityClass, String property, Object value) {
      LOGGER.info("by: " + entityClass.getSimpleName() + "#" + property + " = " + value);
      if ("id".equals(property) && value instanceof String) {
        value = Long.parseLong((String) value);
      }

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
      LOGGER.info("save: " + entity.getClass().getSimpleName());
      try {
        return em.merge(entity);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }

    public <T> List<T> all(Class<T> entityClass, String orderBy) {
      LOGGER.info("all: " + entityClass.getSimpleName() + " ORDER BY " + orderBy);
      TypedQuery<T> query = em.createQuery("from " + entityClass.getSimpleName() + " order by " + orderBy + " DESC", entityClass);

      return query.getResultList();
    }

    public <T> List<T> all(Class<T> entityClass, String property, Object value, String orderBy) {
      LOGGER.info("all: " + entityClass.getSimpleName() + "#" + property + " = " + value + " ORDER BY " + orderBy);

      TypedQuery<T> query = em.createQuery("from " + entityClass.getSimpleName() + " where " + property + "= :value order by " + orderBy + " DESC", entityClass);
      query.setParameter("value", value);

      return query.getResultList();
    }

    public <T> List<T> query(Class<T> entityClass, String queryName, Object... parameters) {
      LOGGER.info("query: " + queryName + " " + Arrays.toString(parameters));
      TypedQuery<T> query = em.createNamedQuery(queryName, entityClass);
      for (int i = 0; i < parameters.length; i++) {
        query.setParameter(i + 1, parameters[i]);
      }
      List<T> resultList = query.getResultList();
      return resultList != null ? resultList : Collections.emptyList();
    }
  }
}
