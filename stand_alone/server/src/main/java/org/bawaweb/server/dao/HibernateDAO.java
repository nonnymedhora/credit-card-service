package org.bawaweb.server.dao;

import javax.transaction.UserTransaction;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import org.bawaweb.server.EntityManagerUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author medhoran
 * All DAOImpl classes must extend this
 * provides crud functionality for jpa using entitymanager and hibernate using session
 */
public class HibernateDAO {
	
	
	@SuppressWarnings("unused")
    // Logger.
    private final static Logger logger = LoggerFactory.getLogger(HibernateDAO.class);


    ////hibernate session style
	@SuppressWarnings("unchecked")	
	private static final ThreadLocal session = new ThreadLocal();
	private static final SessionFactory sessionFactory = null;//new Configuration().configure(new File("./config/hibernate.cfg.xml")).buildSessionFactory();
	
	// jpa style  -- note this is set to static, so all subclasses will
	@PersistenceContext
	protected final static EntityManager entityManager = EntityManagerUtil.getEntityManager();
	
	@Resource
	protected UserTransaction tx;
	
	public HibernateDAO() {
	}
	
	public EntityManager getEntityManager() {
		return entityManager;
	}

	@SuppressWarnings("unchecked")
	public static Session getSession() {
		Session session = (Session) HibernateDAO.session.get();
		if (session == null) {
			session = sessionFactory.openSession();
			HibernateDAO.session.set(session);
		}
		return session;
	}

	public void begin() {
		entityManager.getTransaction().begin();
	}
	
	public void beginAndFlush() {
		entityManager.getTransaction().begin();
		entityManager.flush();
	}
	
	public void beginAndClear() {System.out.println("in hibernatedao beginAndClear -- entityManager.getTransaction().isActive() = " + entityManager.getTransaction().isActive());
		if (!entityManager.getTransaction().isActive()) {
			entityManager.getTransaction().begin();
			entityManager.clear();
		}
		System.out.println("AFTER if in hibernatedao beginAndClear -- entityManager.getTransaction().isActive() = " + entityManager.getTransaction().isActive());
	}
	
	/*protected void beginAndClearRollbackOnly() {
		if (!entityManager.getTransaction().isActive()) {
			entityManager.getTransaction().begin();
			entityManager.getTransaction().setRollbackOnly();
			entityManager.clear();
		}
	}*/
	
	public void flush() {
		entityManager.flush();
	}
	
	public void commit() {System.out.println("in commit hibernatedao1");
		if ( isActive() ) {System.out.println("in commit - isactive hibernatedao2");
			entityManager.getTransaction().commit();
		}
	}
	
	public void rollback() {
		if ( isActive() ) {
			entityManager.getTransaction().rollback();
		}
	}
	
	public boolean isActive() {
		return entityManager.getTransaction().isActive();
	}
	
	public void close() {
		if ( isActive() ) {
			entityManager.close();
		}
	}
	
	public void detach(Object obj) {
		entityManager.detach(obj);
	}


	/*trying to extract the lowlevel stacktraceelements - l8r substitute with above commit
	 * protected void commit() {
		try {
			entityManager.getTransaction().commit();
		} catch (IllegalStateException ie) {//(com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException me) {
			// 
			ie.printStackTrace();
		} catch (RollbackException re) {
			System.out.println("Message is " + re.getMessage());
			System.out.println("Throwable is " + re.getCause());
			StackTraceElement[] ss = re.getStackTrace();
			for(int i = 0; i < ss.length; i++) {
				System.out.println( );
				StackTraceElement ste = ss[i];
				System.out.println("StackCLass " + ste.getClassName());
				System.out.println("Method name is " + ste.getMethodName());
				
			}
			re.printStackTrace();
		}
	}*/
	
	
	
/*   -- hibernate
	@SuppressWarnings("unchecked")
	protected void rollback() {
		try {
			getSession().getTransaction().rollback();
		} catch (HibernateException e) {
			System.out.println(e.getMessage());
		}
		try {
			getSession().close();
		} catch (HibernateException e) {
			System.out.println(e.getMessage());
		}
		HibernateDAO.session.set(null);
	}

	@SuppressWarnings("unchecked")
	public static void close() {
		getSession().close();
		HibernateDAO.session.set(null);
	}
	-- ends hibernate*/

}
