package uo.ri.cws.persistence.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import uo.ri.cws.domain.base.BaseEntity;

public class UnitOfWork {

	private EntityManagerFactory factory;

	public static UnitOfWork over(EntityManagerFactory factory) {
		return new UnitOfWork( factory );
	}

	private UnitOfWork(EntityManagerFactory factory) {
		this.factory = factory;
	}

	public void persist(Object... objects) {
		EntityManager mapper = factory.createEntityManager();
		EntityTransaction trx = mapper.getTransaction();
		trx.begin();

		for(Object obj: objects) {
			mapper.persist( obj );
		}

		trx.commit();
		mapper.close();
	}

	public <T> T findById(Class<T> classType, String id) {
		EntityManager mapper = factory.createEntityManager();
		EntityTransaction trx = mapper.getTransaction();
		trx.begin();

		T res = mapper.find(classType, id);

		trx.commit();
		mapper.close();
		return res;
	}

	public void execute(Consumer<EntityManager> action) {
		EntityManager mapper = factory.createEntityManager();
		EntityTransaction trx = mapper.getTransaction();
		try {
			trx.begin();
			action.accept(mapper);
			trx.commit();
		} catch (RuntimeException ex) {
			if (trx.isActive()) {
				trx.rollback();
			}
			throw ex;
		} finally {
			mapper.close();
		}
	}

	public void remove(BaseEntity... detachedObjects) {
		EntityManager mapper = factory.createEntityManager();
		EntityTransaction trx = mapper.getTransaction();
		trx.begin();

		List<Object> persistentObjects = new ArrayList<>();
		for(BaseEntity detached: detachedObjects) {
//			persistentObjects.add( mapper.merge( detached ) );
			persistentObjects.add(
				mapper.find( detached.getClass(), detached.getId() )
			);
		}
		for(Object persistent: persistentObjects) {
			mapper.remove( persistent );
		}

		trx.commit();
		mapper.close();
	}

}
