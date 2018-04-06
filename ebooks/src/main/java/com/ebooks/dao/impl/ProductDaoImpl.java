package com.ebooks.dao.impl;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ebooks.dao.ProductDao;
import com.ebooks.model.Product;

@Service(value="prodimplm")
@Transactional
public class ProductDaoImpl implements ProductDao {
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void addProduct(Product product) {
		// TODO Auto-generated method stub
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(product);
	}

	@Override
	public Product getProductById(String id) {
		// TODO Auto-generated method stub
		Session session = sessionFactory.getCurrentSession();
		Product product = (Product) session.get(Product.class, id);
		return product;

	}

	@Override
	public List<Product> getAllProducts() {
		// TODO Auto-generated method stub
		Session session = sessionFactory.getCurrentSession();
		Query query = session.createQuery("from Product");
		List<Product> products = query.list();
		/*for(Product product:products){
			System.out.println(product);
		}
		//Iterator<Product> iterator = products.iterator();
		for (Product product : products) {
			System.out.println(product);
		}*/
		session.flush();
		return products;
	}

	@Override
	public void deleteProduct(String id) {
		// TODO Auto-generated method stub
		Session session = sessionFactory.getCurrentSession();
		session.delete(getProductById(id));
		session.flush();
	}
	public void editProduct(Product product) {
		// TODO Auto-generated method stub
		Session session = sessionFactory.getCurrentSession();
		session.saveOrUpdate(product);
	}
}
