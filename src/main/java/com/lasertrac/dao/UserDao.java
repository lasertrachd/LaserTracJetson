package com.lasertrac.dao;

import java.util.List;

import com.lasertrac.entity.FileInfo;
import com.lasertrac.entity.Violations;

public interface UserDao {
	
	public  List<Violations> loadViolations();
	public  boolean addFileInfo(FileInfo fileInfo);
	
}
