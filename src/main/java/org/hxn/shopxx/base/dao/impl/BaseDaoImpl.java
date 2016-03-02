package org.hxn.shopxx.base.dao.impl;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hxn.shopxx.base.dao.BaseDao;
import org.hxn.shopxx.util.Condition;
import org.hxn.shopxx.util.Operator;
import org.hxn.shopxx.util.Page;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseDaoImpl<T> implements BaseDao<T> {

	private Class<T> clazz;

	@Autowired
	protected SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	public BaseDaoImpl() {
		this.clazz = null;
		Class<?> c = getClass();
		Type type = c.getGenericSuperclass();
		if (type instanceof ParameterizedType) {
			Type[] parameterizedType = ((ParameterizedType) type).getActualTypeArguments();
			this.clazz = (Class<T>) parameterizedType[0];
		}
	}

	protected Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	public Class<T> getClazz() {
		return clazz;
	}

	@Override
	public Serializable save(T entity) {
		return getSession().save(entity);
	}

	@Override
	public void save(List<T> entities) {
		Session session = getSession();
		for (int i = 0; i < entities.size(); i++) {
			session.save(entities.get(i));
			if (i / 30 == 0) {
				getSession().flush();
				getSession().clear();
			}
		}
	}

	@Override
	public void saveBySQL(List<T> entities) {
		String tableName = null;
		Table table = clazz.getAnnotation(Table.class);
		if (table == null) {
			tableName = clazz.getSimpleName();
		} else if ("".equals(table.name()) || null == table.name()) {
			tableName = clazz.getSimpleName();
		} else {
			tableName = table.name();
		}
		String sql = "insert into " + tableName + " (";
		Field[] fields = clazz.getDeclaredFields();
		List<StringBuilder> entitiesStr = new ArrayList<StringBuilder>();
		for (int i = 0; i < fields.length; i++) {
			fields[i].setAccessible(true);
			if (!fields[i].getName().equals("serialVersionUID") && fields[i].getAnnotation(Id.class) == null) {
				Column column = fields[i].getAnnotation(Column.class);
				String columnName = null;
				if (column == null) {
					columnName = fields[i].getName();
				} else if ("".equals(column.name()) || null == column.name()) {
					columnName = fields[i].getName();
				} else {
					columnName = column.name();
				}
				sql += columnName;
				if (i < fields.length - 1) {
					sql += ",";
				}
			}
		}
		for (int j = 0; j < entities.size(); j++) {
			entitiesStr.add(new StringBuilder());
			entitiesStr.get(entitiesStr.size() - 1).append("(");
			T entity = entities.get(j);
			for (int i = 0; i < fields.length; i++) {
				fields[i].setAccessible(true);
				if (!fields[i].getName().equals("serialVersionUID") && fields[i].getAnnotation(Id.class) == null) {
					try {
						Object value = fields[i].get(entity);
						entitiesStr.get(entitiesStr.size() - 1)
								.append((value instanceof String) ? "'" + value + "'" : value);
						if (i < fields.length - 1) {
							entitiesStr.get(entitiesStr.size() - 1).append(",");
						}
						if (i == fields.length - 1) {
							entitiesStr.get(entitiesStr.size() - 1).append(")");
						}
					} catch (IllegalArgumentException | IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		}
		sql += ") values ";
		for (int i = 0; i < entitiesStr.size(); i++) {
			// for (StringBuilder bd : entitiesStr) {
			sql += entitiesStr.get(i).toString();
			if (i < entitiesStr.size() - 1) {
				sql += ",";
			}
		}

		getSession().createSQLQuery(sql).executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public T get(Serializable id) {
		return (T) getSession().get(clazz, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> get(Serializable[] ids) {
		String hql = "from " + clazz.getName() + " as model where model.id in(:ids)";
		return getSession().createQuery(hql).setParameterList("ids", ids).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> get(List<? extends Serializable> ids) {
		String hql = "from " + clazz.getName() + " as model where model.id in(:ids)";
		return getSession().createQuery(hql).setParameterList("ids", ids).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> get(Set<? extends Serializable> ids) {
		String hql = "from " + clazz.getName() + " as model where model.id in(:ids)";
		return getSession().createQuery(hql).setParameterList("ids", ids).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public T get(String propertyName, Object value) {
		String hql = "from " + clazz.getName() + " as model where model." + propertyName + " = :propertyValue";
		return (T) getSession().createQuery(hql).setParameter("propertyValue", value).uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> getList(String propertyName, Object value) {
		String hql = "";
		if (value instanceof String && (((String) value).indexOf("_") >= 0 || ((String) value).indexOf("%") >= 0)) {
			hql = "from " + clazz.getName() + " as model where model." + propertyName + " like :propertyValue";
		} else {
			hql = "from " + clazz.getName() + " as model where model." + propertyName + " = :propertyValue";
		}
		return getSession().createQuery(hql).setParameter("propertyValue", value).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> getList(String propertyName, Object[] values) {
		String hql = "from " + clazz.getName() + " as model where model." + propertyName + "  in ( :propertyList)";
		return getSession().createQuery(hql).setParameterList("propertyList", values).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> getList(String propertyName, List<?> values) {
		String hql = "from " + clazz.getName() + " as model where model." + propertyName + "  in ( :propertyList)";
		return getSession().createQuery(hql).setParameterList("propertyList", values).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> getAll() {
		String hql = "from " + clazz.getName();
		return getSession().createQuery(hql).list();
	}

	@Override
	public Long getTotalCount() {
		String hql = "select count(*) from " + clazz.getName();
		return (Long) getSession().createQuery(hql).uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> getByPage(Page page) {
		String hql = "from " + clazz.getName();
		return getSession().createQuery(hql).setFirstResult((page.getPageNum() - 1) * page.getPageSize())
				.setMaxResults(page.getPageSize()).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> getList(String propertyName, Object value, Page page) {
		String hql = "";
		if (value instanceof String && (((String) value).indexOf("_") >= 0 || ((String) value).indexOf("%") >= 0)) {
			hql = "from " + clazz.getName() + " as model where model." + propertyName + " like :propertyValue";
		} else {
			hql = "from " + clazz.getName() + " as model where model." + propertyName + " = :propertyValue";
		}
		return getSession().createQuery(hql).setParameter("propertyValue", value)
				.setFirstResult((page.getPageNum() - 1) * page.getPageSize()).setMaxResults(page.getPageSize()).list();
	}

	@Override
	public Long getTotalCount(String propertyName, Object value) {
		String hql = "";
		if (value instanceof String && (((String) value).indexOf("_") >= 0 || ((String) value).indexOf("%") >= 0)) {
			hql = "select count(*) from " + clazz.getName() + " as model where model." + propertyName
					+ " like :propertyValue";
		} else {
			hql = "select count(*) from " + clazz.getName() + " as model where model." + propertyName
					+ " = :propertyValue";
		}
		return (Long) getSession().createQuery(hql).setParameter("propertyValue", value).uniqueResult();
	}

	@Override
	public void update(T entity) {
		getSession().update(entity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public T load(Serializable id) {
		return (T) getSession().load(clazz, id);
	}

	@Override
	public void delete(Serializable id) {
		T entity = load(id);
		getSession().delete(entity);
	}

	@Override
	public void delete(T entity) {
		getSession().delete(entity);
	}

	@Override
	public int delete(Serializable[] ids) {
		String id = null;
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			Id annotation = field.getAnnotation(Id.class);
			if (null != annotation) {
				id = field.getName();
				break;
			}
		}
		Session session = getSession();
		String hql = "delete " + clazz.getSimpleName() + " t where t." + id + " in :ids";
		Query query = session.createQuery(hql);
		query.setParameterList("ids", ids);
		int i = query.executeUpdate();
		return i;
	}

	@Override
	public int delete(List<? extends Serializable> ids) {
		String id = null;
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			Id annotation = field.getAnnotation(Id.class);
			if (null != annotation) {
				id = field.getName();
				break;
			}
		}
		Session session = getSession();
		String hql = "delete " + clazz.getSimpleName() + " t where t." + id + " in :ids";
		Query query = session.createQuery(hql);
		query.setParameterList("ids", ids);
		int i = query.executeUpdate();
		return i;
	}

	@Override
	public int delete(Set<? extends Serializable> ids) {
		String id = null;
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			Id annotation = field.getAnnotation(Id.class);
			if (null != annotation) {
				id = field.getName();
				break;
			}
		}
		Session session = getSession();
		String hql = "delete " + clazz.getSimpleName() + " t where t." + id + " in :ids";
		Query query = session.createQuery(hql);
		query.setParameterList("ids", ids);
		int i = query.executeUpdate();
		return i;
	}

	@Override
	public void delete(String propertyName, Object value) {
		String hql = null;
		if (value instanceof String && (((String) value).indexOf("_") >= 0 || ((String) value).indexOf("%") >= 0)) {
			hql = "delete from " + clazz.getName() + " as t where t." + propertyName + " like :propertyValue";
		} else {
			hql = "delete from " + clazz.getName() + " as t where t." + propertyName + " = :propertyValue";
		}
		getSession().createQuery(hql).setParameter("propertyValue", value).executeUpdate();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<T> getAllBySql(String sql) {
		List<T> entities = getSession().createSQLQuery(sql).addEntity(clazz).list();
		return entities;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> getAllBySqlPage(String sql, Page page) {
		if (page == null) {
			page = new Page();
		}
		List<T> entities = getSession().createSQLQuery(sql).addEntity(clazz)
				.setFirstResult((page.getPageNum() - 1) * page.getPageSize()).setMaxResults(page.getPageSize()).list();
		return entities;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<T> getByCriteria(List<Condition> conditions) {
		Criteria criteria = getSession().createCriteria(clazz);
		Set<String> aliases = new HashSet<String>();// 所有的别名都要放进来，防止重复别名
		if (null != conditions && 0 != conditions.size()) {
			for (Condition condition : conditions) {
				if (null == condition.getProperty()) {
					continue;
				}
				String property = condition.getProperty();
				String[] props = property.split("\\.");
				for (int i = 0; i < props.length - 1; i++) {
					StringBuilder prop = new StringBuilder();
					StringBuilder alias = new StringBuilder();
					for (int j = 0; j <= i; j++) {
						prop.append(props[j]);
						alias.append(props[j]);
						if (j < i) {
							prop.append(".");
						}
					}
					boolean flag = aliases.add(prop.toString());
					if (flag) {// 如果添加进入别名集合成功，则说明别名没有创建，即可以创建此别名
						criteria.createAlias(prop.toString(), alias.toString());
					}
					if (i == props.length - 2) {
						property = alias.toString() + "." + props[props.length - 1];
					}
				}
				// 别名创建完毕
				Operator operator = condition.getOperator();
				switch (operator) {
				case EQ:
					criteria.add(Restrictions.eq(property, condition.getObj()));
					break;
				case GT:
					criteria.add(Restrictions.gt(property, condition.getObj()));
					break;
				case LT:
					criteria.add(Restrictions.lt(property, condition.getObj()));
					break;
				case GE:
					criteria.add(Restrictions.ge(property, condition.getObj()));
					break;
				case LE:
					criteria.add(Restrictions.le(property, condition.getObj()));
					break;
				case NE:
					criteria.add(Restrictions.ne(property, condition.getObj()));
					break;
				case ISNULL:
					criteria.add(Restrictions.isNull(property));
					break;
				case ISNOTNULL:
					criteria.add(Restrictions.isNotNull(property));
					break;
				case LIKE:
					criteria.add(Restrictions.like(property, condition.getObj()));
					break;
				case IN:
					criteria.add(Restrictions.in(property, (Object[]) condition.getObj()));
					break;
				case BETWEEN:
					criteria.add(Restrictions.between(property, ((Object[]) (condition.getObj()))[0],
							((Object[]) (condition.getObj()))[0]));
					break;
				case IDEQ:
					criteria.add(Restrictions.idEq(condition.getObj()));
					break;
				case EQORISNULL:
					criteria.add(Restrictions.eqOrIsNull(property, condition.getObj()));
					break;
				case NEORISNOTNULL:
					criteria.add(Restrictions.neOrIsNotNull(property, condition.getObj()));
					break;
				case ILIKE:
					criteria.add(Restrictions.ilike(property, condition.getObj()));
					break;
				case INCOLLECTION:
					criteria.add(Restrictions.in(property, (Collection) (condition.getObj())));
					break;
				case EQPROPERTY:
					criteria.add(Restrictions.eqProperty(property, (String) (condition.getObj())));
					break;
				case NEPROPERTY:
					criteria.add(Restrictions.neProperty(property, (String) (condition.getObj())));
					break;
				case LTPROPERTY:
					criteria.add(Restrictions.ltProperty(property, (String) (condition.getObj())));
					break;
				case LEPROPERTY:
					criteria.add(Restrictions.leProperty(property, (String) (condition.getObj())));
					break;
				case GTPROPERTY:
					criteria.add(Restrictions.gtProperty(property, (String) (condition.getObj())));
					break;
				case GEPROPERTY:
					criteria.add(Restrictions.geProperty(property, (String) (condition.getObj())));
					break;
				case AND:
					criteria.add(Restrictions.and((Criterion[]) (condition.getObj())));
					break;
				case OR:
					criteria.add(Restrictions.or((Criterion[]) (condition.getObj())));
					break;
				case NOT:
					criteria.add(Restrictions.not((Criterion) (condition.getObj())));
					break;
				case SQLRESTRICTION:
					criteria.add(Restrictions.sqlRestriction((String) (((Object[]) (condition.getObj()))[0]),
							(Object[]) (((Object[]) (condition.getObj()))[1]),
							(org.hibernate.type.Type[]) (((Object[]) (condition.getObj()))[2])));
					break;
				case CONJUNCTION:
					criteria.add(Restrictions.conjunction((Criterion[]) (condition.getObj())));
					break;
				case ALLEQ:
					criteria.add(Restrictions.allEq((Map<String, ?>) (condition.getObj())));
					break;
				case ISEMPTY:
					criteria.add(Restrictions.isEmpty(property));
					break;
				case ISNOTEMPTY:
					criteria.add(Restrictions.isNotEmpty(property));
					break;
				case SIZEEQ:
					criteria.add(Restrictions.sizeEq(property, (int) (condition.getObj())));
					break;
				case SIZENE:
					criteria.add(Restrictions.sizeNe(property, (int) (condition.getObj())));
					break;
				case SIZEGT:
					criteria.add(Restrictions.sizeGt(property, (int) (condition.getObj())));
					break;
				case SIZELT:
					criteria.add(Restrictions.sizeLt(property, (int) (condition.getObj())));
					break;
				case SIZEGE:
					criteria.add(Restrictions.sizeGe(property, (int) (condition.getObj())));
					break;
				case SIZELE:
					criteria.add(Restrictions.sizeLe(property, (int) (condition.getObj())));
					break;
				case NATURALID:
					criteria.add(Restrictions.naturalId());
					break;
				default:
					break;
				}
			}
		}

		return criteria.list();
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<T> getByCriteria(List<Condition> conditions, Page page, List<String> orders) {
		Criteria criteria = getSession().createCriteria(clazz);
		Set<String> aliases = new HashSet<String>();// 所有的别名都要放进来，防止重复别名
		if (null != conditions && 0 != conditions.size()) {
			for (Condition condition : conditions) {
				if (null == condition.getProperty()) {
					continue;
				}
				String property = condition.getProperty();
				String[] props = property.split("\\.");
				for (int i = 0; i < props.length - 1; i++) {
					StringBuilder prop = new StringBuilder();
					StringBuilder alias = new StringBuilder();
					for (int j = 0; j <= i; j++) {
						prop.append(props[j]);
						alias.append(props[j]);
						if (j < i) {
							prop.append(".");
						}
					}
					boolean flag = aliases.add(prop.toString());
					if (flag) {// 如果添加进入别名集合成功，则说明别名没有创建，即可以创建此别名
						criteria.createAlias(prop.toString(), alias.toString());
					}
					if (i == props.length - 2) {
						property = alias.toString() + "." + props[props.length - 1];
					}
				}
				// 别名创建完毕
				Operator operator = condition.getOperator();
				switch (operator) {
				case EQ:
					criteria.add(Restrictions.eq(property, condition.getObj()));
					break;
				case GT:
					criteria.add(Restrictions.gt(property, condition.getObj()));
					break;
				case LT:
					criteria.add(Restrictions.lt(property, condition.getObj()));
					break;
				case GE:
					criteria.add(Restrictions.ge(property, condition.getObj()));
					break;
				case LE:
					criteria.add(Restrictions.le(property, condition.getObj()));
					break;
				case NE:
					criteria.add(Restrictions.ne(property, condition.getObj()));
					break;
				case ISNULL:
					criteria.add(Restrictions.isNull(property));
					break;
				case ISNOTNULL:
					criteria.add(Restrictions.isNotNull(property));
					break;
				case LIKE:
					criteria.add(Restrictions.like(property, condition.getObj()));
					break;
				case IN:
					criteria.add(Restrictions.in(property, (Object[]) condition.getObj()));
					break;
				case BETWEEN:
					criteria.add(Restrictions.between(property, ((Object[]) (condition.getObj()))[0],
							((Object[]) (condition.getObj()))[0]));
					break;
				case IDEQ:
					criteria.add(Restrictions.idEq(condition.getObj()));
					break;
				case EQORISNULL:
					criteria.add(Restrictions.eqOrIsNull(property, condition.getObj()));
					break;
				case NEORISNOTNULL:
					criteria.add(Restrictions.neOrIsNotNull(property, condition.getObj()));
					break;
				case ILIKE:
					criteria.add(Restrictions.ilike(property, condition.getObj()));
					break;
				case INCOLLECTION:
					criteria.add(Restrictions.in(property, (Collection) (condition.getObj())));
					break;
				case EQPROPERTY:
					criteria.add(Restrictions.eqProperty(property, (String) (condition.getObj())));
					break;
				case NEPROPERTY:
					criteria.add(Restrictions.neProperty(property, (String) (condition.getObj())));
					break;
				case LTPROPERTY:
					criteria.add(Restrictions.ltProperty(property, (String) (condition.getObj())));
					break;
				case LEPROPERTY:
					criteria.add(Restrictions.leProperty(property, (String) (condition.getObj())));
					break;
				case GTPROPERTY:
					criteria.add(Restrictions.gtProperty(property, (String) (condition.getObj())));
					break;
				case GEPROPERTY:
					criteria.add(Restrictions.geProperty(property, (String) (condition.getObj())));
					break;
				case AND:
					criteria.add(Restrictions.and((Criterion[]) (condition.getObj())));
					break;
				case OR:
					criteria.add(Restrictions.or((Criterion[]) (condition.getObj())));
					break;
				case NOT:
					criteria.add(Restrictions.not((Criterion) (condition.getObj())));
					break;
				case SQLRESTRICTION:
					criteria.add(Restrictions.sqlRestriction((String) (((Object[]) (condition.getObj()))[0]),
							(Object[]) (((Object[]) (condition.getObj()))[1]),
							(org.hibernate.type.Type[]) (((Object[]) (condition.getObj()))[2])));
					break;
				case CONJUNCTION:
					criteria.add(Restrictions.conjunction((Criterion[]) (condition.getObj())));
					break;
				case ALLEQ:
					criteria.add(Restrictions.allEq((Map<String, ?>) (condition.getObj())));
					break;
				case ISEMPTY:
					criteria.add(Restrictions.isEmpty(property));
					break;
				case ISNOTEMPTY:
					criteria.add(Restrictions.isNotEmpty(property));
					break;
				case SIZEEQ:
					criteria.add(Restrictions.sizeEq(property, (int) (condition.getObj())));
					break;
				case SIZENE:
					criteria.add(Restrictions.sizeNe(property, (int) (condition.getObj())));
					break;
				case SIZEGT:
					criteria.add(Restrictions.sizeGt(property, (int) (condition.getObj())));
					break;
				case SIZELT:
					criteria.add(Restrictions.sizeLt(property, (int) (condition.getObj())));
					break;
				case SIZEGE:
					criteria.add(Restrictions.sizeGe(property, (int) (condition.getObj())));
					break;
				case SIZELE:
					criteria.add(Restrictions.sizeLe(property, (int) (condition.getObj())));
					break;
				case NATURALID:
					criteria.add(Restrictions.naturalId());
					break;
				default:
					break;
				}
			}
		}
		//排序
		if(null != orders && 0 != orders.size()){
			for(int k=0;k<orders.size();k++){
				String order = orders.get(k);
				String[] strs = order.split(":");
				String field = strs[0];
				String sc = strs[1];
				String[] props = field.split("\\.");
				for (int i = 0; i < props.length - 1; i++) {
					StringBuilder prop = new StringBuilder();
					StringBuilder alias = new StringBuilder();
					for (int j = 0; j <= i; j++) {
						prop.append(props[j]);
						alias.append(props[j]);
						if (j < i) {
							prop.append(".");
						}
					}
					boolean flag = aliases.add(prop.toString());
					if (flag) {// 如果添加进入别名集合成功，则说明别名没有创建，即可以创建此别名
						criteria.createAlias(prop.toString(), alias.toString());
					}
					if (i == props.length - 2) {
						field = alias.toString() + "." + props[props.length - 1];
					}
				}
				if ("asc".equals(sc)) {
					criteria.addOrder(Order.asc(field));
				} else if ("desc".equals(sc)) {
					criteria.addOrder(Order.desc(field));
				}
			}
		}
		//分页
		if(null != page){
			criteria.setFirstResult(page.getPageSize()*(page.getPageNum()-1));
			criteria.setMaxResults(page.getPageSize());
		}
		return criteria.list();
	}

}
