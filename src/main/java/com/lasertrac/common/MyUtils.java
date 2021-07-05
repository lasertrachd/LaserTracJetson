package com.lasertrac.common;

import java.text.SimpleDateFormat;

public class MyUtils {

	public final static SimpleDateFormat recordingDTFormatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
	
	public final static SimpleDateFormat recFileNameDTFormatter = new SimpleDateFormat("dd.MM.yyyy HH.mm.ss");
	public final static SimpleDateFormat directoryDTFormatter = new SimpleDateFormat("dd_MM_yyyy");
	
	public final static SimpleDateFormat logTimeFormatter = new SimpleDateFormat("HH_mm_ss_SSS");
	
}
