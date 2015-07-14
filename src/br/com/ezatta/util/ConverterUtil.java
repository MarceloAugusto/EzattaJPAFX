/**
 * 
 */
package br.com.ezatta.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * @author marcos.fernando
 *
 */
public final class ConverterUtil {
	
	public static Date localDateToDate(LocalDate localDate){
		Instant instant = localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
		return Date.from(instant);
	}	
	
	public static LocalDate utilDateToLocalDate(Date date){		
		Instant instant = Instant.ofEpochMilli(date.getTime());
		LocalDateTime res = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
		return res.toLocalDate();
	}
	
	public static java.sql.Date stringToDateSql(String data){
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");		
		java.sql.Date dt = null;
		try {
			dt = new java.sql.Date(df.parse(data).getTime());
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Erro StringToDateSql: "+e.getMessage(), e.getCause());
		}
		return dt;
	}
	
	public static String localDateToStringDate(LocalDate data){		
		return data.toString().replace("-", ".");		
	}

}
