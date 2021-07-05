package com.lasertrac.entity;

//import com.book81.dao.impl.Autowired;
//import com.book81.dao.impl.HibernateDaoSupport;
//import com.book81.dao.impl.PostConstruct;
//import com.book81.dao.impl.Session;
//import com.book81.dao.impl.SessionFactory;
import org.hibernate.Session;
//import org.hibernate.SessionFactory;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
public class HibernateUtil {//extends HibernateDaoSupport{
	//public class HibernateUtil {

//		@Autowired
//		private SessionFactory sessionFactory;
//
//		@PostConstruct
//		public void init() {
//			setSessionFactory(sessionFactory);
//		}	
//		
//		public Session getCurrentSession() {
//			return sessionFactory.getCurrentSession();
//		}
	private static final SessionFactory sessionFactory = buildSessionFactory();

	@SuppressWarnings("deprecation")
	private static SessionFactory buildSessionFactory() {
		try {
			// Use hibernate.cfg.xml to get a SessionFactory
			return new Configuration().configure().buildSessionFactory();
		} catch (Throwable ex) {
			System.err.println("SessionFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public static void shutdown() {
		getSessionFactory().close();
	}
}
