package uo.ri.cws.application.service.acceptance.util.dbfixture;

import java.util.List;

public class Db {

	public static <T> void insert(T record) {
		@SuppressWarnings("unchecked")
		Class<T> type = (Class<T>) record.getClass();
		new GenericDao<T>(type).insert( record );
	}

	public static <T> void update(T record) {
		@SuppressWarnings("unchecked")
		Class<T> type = (Class<T>) record.getClass();
		new GenericDao<T>(type).update( record );
	}

	public static <T> void delete(Class<T> type, String id) {
		new GenericDao<T>(type).delete( id );
	}

	public static <T> T findById(Class<T> type, String id) {
		return new GenericDao<T>(type).findById( id );
	}

	public static <T> T findBy(Class<T> type, String field, Object value) {
		return new GenericDao<T>(type).findBy( field, value );
	}

	public static <T> List<T> findAll(Class<T> type) {
		return new GenericDao<T>(type).findAll();
	}
	
	public static <T> List<T> findAllBy(Class<T> type, String field, Object value) {
		return new GenericDao<T>(type).findAllBy( field, value );
	}

	public static void clearTables() {
		new GenericDao<Void>(Void.class).clearTables();
	}
}
