package org.hxn.shopxx.base.service.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import org.hxn.shopxx.base.dao.BaseDao;
import org.hxn.shopxx.base.service.BaseService;
import org.hxn.shopxx.util.Condition;
import org.hxn.shopxx.util.Page;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseServiceImpl<T> implements BaseService<T> {

	@Autowired
	private BaseDao<T> baseDao;

	public BaseDao<T> getBaseDao() {
		return baseDao;
	}

	public void setBaseDao(BaseDao<T> baseDao) {
		this.baseDao = baseDao;
	}

	@Override
	public void save(List<T> entities) {
		baseDao.save(entities);
	}

	@Override
	public void saveBySQL(List<T> entities) {
		baseDao.saveBySQL(entities);
	}

	@Override
	public Serializable save(T entity) {
		return baseDao.save(entity);
	}

	@Override
	public T get(Serializable id) {
		return baseDao.get(id);
	}

	@Override
	public List<T> get(Serializable[] ids) {
		return baseDao.get(ids);
	}

	@Override
	public List<T> get(List<? extends Serializable> ids) {
		return baseDao.get(ids);
	}

	@Override
	public List<T> get(Set<? extends Serializable> ids) {
		return baseDao.get(ids);
	}

	@Override
	public T get(String propertyName, Object value) {
		return baseDao.get(propertyName, value);
	}

	@Override
	public List<T> getList(String propertyName, Object value) {
		return baseDao.getList(propertyName, value);
	}

	@Override
	public List<T> getList(String propertyName, Object[] values) {
		return baseDao.getList(propertyName, values);
	}

	@Override
	public List<T> getList(String propertyName, List<?> values) {
		return baseDao.getList(propertyName, values);
	}

	@Override
	public List<T> getAll() {
		return baseDao.getAll();
	}

	@Override
	public Long getTotalCount() {
		return baseDao.getTotalCount();
	}

	@Override
	public List<T> getByPage(Page page) {
		return baseDao.getByPage(page);
	}

	@Override
	public List<T> getList(String propertyName, Object value, Page page) {
		return baseDao.getList(propertyName, value, page);
	}

	@Override
	public Long getTotalCount(String propertyName, Object value) {
		return baseDao.getTotalCount(propertyName, value);
	}

	@Override
	public void update(T entity) {
		baseDao.update(entity);
	}

	@Override
	public T load(Serializable id) {
		return baseDao.load(id);
	}

	@Override
	public void delete(Serializable id) {
		baseDao.delete(id);
	}

	@Override
	public void delete(T entity) {
		baseDao.delete(entity);
	}

	@Override
	public int delete(Serializable[] ids) {
		return baseDao.delete(ids);
	}

	@Override
	public int delete(List<? extends Serializable> ids) {
		return baseDao.delete(ids);
	}

	@Override
	public int delete(Set<? extends Serializable> ids) {
		return baseDao.delete(ids);
	}

	@Override
	public void delete(String propertyName, Object value) {
		baseDao.delete(propertyName, value);
	}

	@Override
	public List<T> getAllBySql(String sql) {
		return baseDao.getAllBySql(sql);
	}

	@Override
	public List<T> getAllBySqlPage(String sql, Page page) {
		return baseDao.getAllBySqlPage(sql, page);
	}


	@Override
	public List<T> getByCriteria(List<Condition> conditions) {
		return baseDao.getByCriteria(conditions);
	}

	@Override
	public List<T> getByCriteria(List<Condition> conditions, Page page, List<String> orders) {
		return baseDao.getByCriteria(conditions,page,orders);
	}

}
