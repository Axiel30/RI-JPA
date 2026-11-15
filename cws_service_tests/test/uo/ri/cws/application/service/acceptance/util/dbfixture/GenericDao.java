package uo.ri.cws.application.service.acceptance.util.dbfixture;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import uo.ri.cws.application.service.acceptance.util.db.DataSourceUtil;

public class GenericDao<T> {
	
	private final Class<T> type;
    private final String tableName;
    private Field[] fields;

    public GenericDao(Class<T> type) {
        this.type = type;
        this.tableName = type.getSimpleName().replace("Record", "").toUpperCase();
        this.fields = type.getFields();
    }

    public void insert(T obj) {
        String sql = composeInsertSql();
        execute(conn -> {
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                setParams(ps, obj, "");
                ps.executeUpdate();
                return null;
            }
        });
    }

	public void update(T obj) {
        String sql = composeUpdateSql();
		execute(conn -> {
			try (PreparedStatement ps = conn.prepareStatement(sql)) {
		        Field idField = type.getField("id");
		        Object idValue = idField.get(obj);

		        int i = setParams(ps, obj, "id"); // skip id
				ps.setObject(i + 1, idValue);
				ps.executeUpdate();
				return null;
			}
		});
    }

    public void delete(String id) {
        String sql = "DELETE FROM " + tableName + " WHERE ID=?";
        execute(conn -> {
	        try (PreparedStatement ps = conn.prepareStatement(sql)) {
	            ps.setString(1, id);
	            ps.executeUpdate();
	            return null;
	        }
		});
    }

    public T findById(String id) {
    	return findBy("id", id);
    }

	public T findBy(String field, Object value) {
        String sql = "SELECT * FROM " + tableName + " WHERE " + field +"=?";

        return execute(conn -> {
	        try (PreparedStatement ps = conn.prepareStatement(sql)) {
	            ps.setObject(1, value);
	            try (ResultSet rs = ps.executeQuery()) {
	                if (rs.next() == false) {
						return null;
					}
	                
                    return mapToObject(rs);
				}
	        }
        });
	}

	public List<T> findAll() {
        List<T> res = new ArrayList<>();
        
        String sql = "SELECT * FROM " + tableName;
        execute(conn -> {
	        try (PreparedStatement ps = conn.prepareStatement(sql)) {
	            try (ResultSet rs = ps.executeQuery()) {
	                while (rs.next() == true) {
	                	T object = mapToObject(rs);
						res.add( object);
					}
					return null;
				}
	        }
        });
        
        return res;
	}
	
	public List<T> findAllBy(String field, Object value) {
        List<T> res = new ArrayList<>();
        
        String sql = "SELECT * FROM " + tableName + " WHERE " + field +"=?";
        execute(conn -> {
	        try (PreparedStatement ps = conn.prepareStatement(sql)) {
	            ps.setObject(1, value);
	            try (ResultSet rs = ps.executeQuery()) {
	                while (rs.next() == true) {
	                	T object = mapToObject(rs);
						res.add( object);
					}
					return null;
				}
	        }
        });
        
        return res;
	}
	
	private int setParams(PreparedStatement ps, T obj, String skipField)
			throws SQLException, IllegalAccessException {
		int i = 0;
		for (Field f : fields) {
			if (skipField != null && f.getName().equals(skipField)) {
				continue;
			}
			
			i++;
			ps.setObject(i, f.get(obj));
		}
		return i;
	}

	private String composeInsertSql() {
		StringJoiner cols = new StringJoiner(", ");
        StringJoiner vals = new StringJoiner(", ");
        for (Field f : fields) {
            cols.add(f.getName().toUpperCase());
            vals.add("?");
        }

        return "INSERT INTO " + tableName + " (" + cols + ") VALUES (" + vals + ")";
	}

	private String composeUpdateSql() {
		StringJoiner setClause = new StringJoiner(", ");
        for (Field f : fields) {
            if (!f.getName().equals("id")) {
                setClause.add(f.getName().toUpperCase() + "=?");
            }
        }

        return "UPDATE " + tableName + " SET " + setClause + " WHERE ID=?";
	}

	private T mapToObject(ResultSet rs)
			throws InstantiationException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException, 
			SQLException {
		
		T obj = type.getDeclaredConstructor().newInstance();
		for (Field f : fields) {
		    Object val = rs.getObject(f.getName().toUpperCase());
		    f.set(obj, val);
		}
		return obj;
	}

	@FunctionalInterface
    public interface SqlCode<T> {
		T execute(Connection conn) throws Exception;
	}

    private T execute(SqlCode<T> code) {
		try (Connection conn = DataSourceUtil.createConnection() ) {
			return code.execute(conn);
		}
        catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void clearTables() {
        String sql = "TRUNCATE SCHEMA PUBLIC RESTART IDENTITY AND COMMIT NO CHECK";
        execute(conn -> {
	        try (PreparedStatement ps = conn.prepareStatement(sql)) {
	            ps.executeUpdate();
	            return null;
	        }
		});
	}

}
