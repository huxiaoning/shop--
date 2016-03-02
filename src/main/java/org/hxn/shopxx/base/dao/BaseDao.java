package org.hxn.shopxx.base.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import org.hxn.shopxx.util.Condition;
import org.hxn.shopxx.util.Page;

public interface BaseDao<T> {

	/**
	 * 根据ID获取实体对象.
	 *
	 * @param id
	 *            记录ID
	 * @return 实体对象
	 */
	public T get(Serializable id);

	/**
	 * 保存实体
	 * 
	 * @param entity
	 *            实体对象
	 * @return 生成的主键的值
	 */
	Serializable save(T entity);

	/**
	 * 保存实体列表
	 * 
	 * @param entities
	 *            实体对象列表
	 * @return 生成的主键的值
	 */
	void save(List<T> entities);

	/**
	 * 保存实体列表
	 * 
	 * @param entities
	 *            实体对象列表
	 * @return 生成的主键的值
	 */
	void saveBySQL(List<T> entities);

	/**
	 * 根据主键id数组查询实体列表
	 * 
	 * @param ids
	 * @return
	 */
	List<T> get(Serializable[] ids);

	/**
	 * 根据主键id列表查询实体列表
	 * 
	 * @param ids
	 * @return
	 */
	List<T> get(List<? extends Serializable> ids);

	/**
	 * 根据主键id的Set集查询实体列表
	 * 
	 * @param ids
	 * @return
	 */
	List<T> get(Set<? extends Serializable> ids);

	/**
	 * 根据属性名-值对，查询满足条件的唯一实体
	 * 
	 * @param propertyName
	 * @param value
	 * @return
	 */
	T get(String propertyName, Object value);

	/**
	 * 根据属性名-值对，查询满足条件的实体列表 支持模糊查询
	 * 
	 * @param propertyName
	 * @param value
	 * @return
	 */
	List<T> getList(String propertyName, Object value);

	/**
	 * 获取满足某一属性值在指定数组中的实体列表
	 * 
	 * @param propertyName
	 * @param values
	 * @return
	 */
	List<T> getList(String propertyName, Object[] values);

	/**
	 * 获取满足某一属性值在指定数组中的实体列表
	 * 
	 * @param propertyName
	 * @param values
	 * @return
	 */
	List<T> getList(String propertyName, List<?> values);

	/**
	 * 无筛选条件查询所有实体
	 * 
	 * @return
	 */
	List<T> getAll();

	/**
	 * 无筛选条件查询所有实体的总数
	 * 
	 * @return
	 */
	Long getTotalCount();

	/**
	 * 无筛选条件，仅分页
	 * 
	 * @return
	 */
	List<T> getByPage(Page page);

	/**
	 * 根据属性名-值对，查询满足条件的实体列表 支持模糊查询
	 * 
	 * @param propertyName
	 * @param value
	 * @return
	 */
	List<T> getList(String propertyName, Object value, Page page);

	/**
	 * 单筛选条件查询所有实体的总数 支持模糊查询
	 * 
	 * @return
	 */
	Long getTotalCount(String propertyName, Object value);

	/**
	 * 更新实体
	 */
	void update(T entity);

	/**
	 * load 查询
	 * 
	 * @param id
	 * @return
	 */
	T load(Serializable id);

	/**
	 * 根据主键删除实体
	 * 
	 * @param id
	 */
	void delete(Serializable id);

	/**
	 * 删除实体
	 * 
	 * @param entity
	 */
	void delete(T entity);

	/**
	 * 根据id数组批量删除实体
	 * 
	 * @param ids
	 */
	int delete(Serializable[] ids);

	/**
	 * 根据id列表批量删除实体
	 * 
	 * @param ids
	 */
	int delete(List<? extends Serializable> ids);

	/**
	 * 根据id Set集批量删除实体
	 * 
	 * @param ids
	 */
	int delete(Set<? extends Serializable> ids);

	/**
	 * 根据属性名-值对删除实体 支持模糊删除
	 * 
	 * @param propertyName
	 * @param value
	 */
	void delete(String propertyName, Object value);

	/**
	 * 根据sql查询实体列表
	 * 
	 * @param sql
	 * @param c
	 * @return
	 */
	List<T> getAllBySql(String sql);

	/**
	 * 根据sql分页查询实体列表
	 * 
	 * @param sql
	 * @param page
	 * @return
	 */
	List<T> getAllBySqlPage(String sql, Page page);

	/**
	 * 万能模板查询
	 */
	List<T> getByCriteria(List<Condition> conditions);

	/**
	 * 万能模板查询 带分页、排序功能
	 */
	List<T> getByCriteria(List<Condition> conditions, Page page, List<String> orders);

}
