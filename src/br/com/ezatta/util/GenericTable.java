/**
 * 
 */
package br.com.ezatta.util;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import br.com.ezatta.util.Colunas;

public class GenericTable<T extends Serializable>{
			
	private Map<Integer, Double> columnSizeMap = new HashMap<Integer, Double>();
	private Map<Integer, String> columnFieldMap = new HashMap<Integer, String>();
	private Map<Integer, String> columnNameMap = new HashMap<Integer, String>();
	private Map<Integer, String> columnTypeMap = new HashMap<Integer, String>();	
	private Map<Integer, Integer> columnFractionMap = new HashMap<Integer, Integer>();
	private CheckBox selectAllCheckBox;	
	private int numField; 
	
	private void processarClasse(Class<?> classeP) {
		for (Field field : classeP.getDeclaredFields()) {
			if (field.isAnnotationPresent(Colunas.class)) {				
				field.setAccessible(true);
				Colunas coluna = field.getAnnotation(Colunas.class);
				columnSizeMap.put(numField, coluna.size());
				columnFieldMap.put(numField, field.getName());	
				columnNameMap.put(numField, coluna.nome());
				columnTypeMap.put(numField, field.getType().getSimpleName());
				columnFractionMap.put(numField, coluna.numCasaDecimal());
				numField++;
			}			
		}
	}	
	
	public void processaClasse(Class<T> classe) {
		numField = 1;	
		Class<?> currentClass = classe;
		if (!classe.getSuperclass().getName().equals("java.lang.Object")){
			while (currentClass.getSuperclass() != null) {
				currentClass = currentClass.getSuperclass();
				processarClasse(currentClass);
			}
			processarClasse(classe);
		}else{
			processarClasse(classe);
		}
	}	
	
	public List<TableColumn<T, ?>> tableColunas(Class<T> cla){
		columnSizeMap.clear();
		columnFieldMap.clear();
		columnNameMap.clear();
		processaClasse(cla);		
		List<TableColumn<T, ?>> tableColunas = new ArrayList<TableColumn<T, ?>>();		
		String colunaNome = "";
		Double colunaSize = 0.0;	
		String colunaTipo = "";
		for(Map.Entry<Integer, String> ent1 : columnNameMap.entrySet()){
			colunaNome = ent1.getValue();			
			colunaSize = columnSizeMap.get(ent1.getKey());	
			colunaTipo = columnTypeMap.get(ent1.getKey());			
			if(colunaTipo.equals("Boolean") || colunaTipo.equals("boolean")){
				final TableColumn<T, Boolean> tcheck = new TableColumn<T, Boolean>();	
				tcheck.setPrefWidth(colunaSize);			
				String fieldNameCheck = columnFieldMap.get(ent1.getKey());
				tcheck.setCellValueFactory(new PropertyValueFactory<T, Boolean>(fieldNameCheck));
				if(getSelectAllCheckBox() != null){
					tcheck.setGraphic(getSelectAllCheckBox());					
				}
				tcheck.setResizable(false);
				tcheck.setEditable(true);
				tableColunas.add(tcheck);			
			}else if(colunaTipo.equals("BigDecimal")){
				TableColumn<T, BigDecimal> tc = new TableColumn<T, BigDecimal>(colunaNome);	
				tc.setPrefWidth(colunaSize);				
				String fieldName = columnFieldMap.get(ent1.getKey());	
				tc.setCellValueFactory(new PropertyValueFactory<T, BigDecimal>(fieldName));
				tc.setResizable(true);
				tableColunas.add(tc);
			} else if(colunaTipo.equals("Date")){
				TableColumn<T, Date> tc = new TableColumn<T, Date>(colunaNome);	
				tc.setPrefWidth(colunaSize);			
				String fieldName = columnFieldMap.get(ent1.getKey());
				tc.setCellFactory(new FormattedValueFactory01<T, Date>(fieldName, "dd/MM/yyyy"));
				tc.setCellValueFactory(new PropertyValueFactory<T, Date>(fieldName));
				tc.setResizable(true);
				tableColunas.add(tc);
			} else {
				TableColumn<T, String> tc = new TableColumn<T, String>(colunaNome);	
				tc.setPrefWidth(colunaSize);			
				String fieldName = columnFieldMap.get(ent1.getKey());
				tc.setCellValueFactory(new PropertyValueFactory<T, String>(fieldName));
				tc.setResizable(true);
				tableColunas.add(tc);
			}			
		}		
		return tableColunas;		
	}

	
	private CheckBox getSelectAllCheckBox() {
		return selectAllCheckBox;
	}
	public void setSelectAllCheckBox(CheckBox selectAllCheckBox) {
		this.selectAllCheckBox = selectAllCheckBox;
	}
}