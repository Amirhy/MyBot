package Dao;

import javax.transaction.Transaction;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import Model.Person;

public class DataBase {
	static SessionFactory sessionFactory;

	public DataBase() {
		// TODO Auto-generated constructor stub
		getSession();
	}

	static public SessionFactory getSession() {
		if (sessionFactory == null) {
			Configuration configuration = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Person.class);
			StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
					.applySettings(configuration.getProperties()).build();
			sessionFactory = configuration.buildSessionFactory(registry);
		}
		return sessionFactory;
	}

	public Person UserExist(long chatID) {
		Session session = sessionFactory.openSession();
		org.hibernate.Transaction transaction = null;
		Person p = null;
		try {
			transaction = session.beginTransaction();
			p = (Person) session.get(Person.class, chatID);

			transaction.commit();
		} catch (Exception e) {
			// TODO: handle exception
			transaction.rollback();
			e.printStackTrace();
		}

		return p;
	}


	
	public void InsertUser(Person p)
	{
		org.hibernate.Transaction transaction = null;
		try {		
			Session session = sessionFactory.openSession();
			transaction = session.beginTransaction();
			session.saveOrUpdate(p);
			transaction.commit();

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			transaction.rollback();
		}
	}
}
