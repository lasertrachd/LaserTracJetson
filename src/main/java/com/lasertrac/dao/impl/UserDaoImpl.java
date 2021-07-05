package com.lasertrac.dao.impl;

import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;

import com.lasertrac.dao.UserDao;
import com.lasertrac.entity.FileInfo;
import com.lasertrac.entity.HibernateUtil;
import com.lasertrac.entity.Violations;
import com.lasertrac.settings.TrafficViolationsHelper;

public class UserDaoImpl extends HibernateUtil implements UserDao {

	
	public  List<Violations> loadViolations()
    {
		Session session =getSessionFactory().openSession();
		
		String hql = "from Violations";
		
		Query query = session.createQuery(hql);
//		query.setString("bookTitle", "%" + bookTitle + "%");
//		query.setFirstResult(pageNumber * Data.recordsPerPage);
//		
		@SuppressWarnings("unchecked")
		List<Violations> violationList = query.list();
		
		return violationList;
//        try
//        {
//            String error;
//            List<Violations> violationsList = dao.getViolationsList();
//            //voilationsList = violationsList;
//        }
//        catch (Exception ex)
//        {
//             System.out.println("loadViolations="+ex.toString());
//        }

    }

	@Override
	public boolean addFileInfo(FileInfo fileInfo) {
		
		Session session =  getSessionFactory().openSession();
		
		session.save(fileInfo);
		
		return true;
	}
	
//	public boolean insert(String table, Map values) throws SQLException {
//	     
//	    StringBuilder columns = new StringBuilder();
//	    StringBuilder vals = new StringBuilder();
//	     
//	    for (String col : values.keySet()) {
//	        columns.append(col).append(",");
//	         
//	        if (values.get(col) instanceof String) {
//	            vals.append("'").append(values.get(col)).append("', ");
//	        }
//	        else vals.append(values.get(col)).append(",");
//	    }
//	     
//	    columns.setLength(columns.length()-1);
//	    vals.setLength(vals.length()-1);
//	 
//	    String query = String.format("INSERT INTO %s (%s) VALUES (%s)", table,
//	            columns.toString(), vals.toString());
//	     
//	    Session session =getSessionFactory().openSession();
//	    Query hquery = session.createQuery(query);
//		int result=hquery.executeUpdate();
//		if(result>0){
//			return true;	
//		}else{
//			return false;
//		}
//	    return this.conn.createStatement().executeUpdate(query);
//	}

}
