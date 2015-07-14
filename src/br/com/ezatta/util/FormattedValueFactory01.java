package br.com.ezatta.util;

import java.text.SimpleDateFormat;

import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class FormattedValueFactory01<S, T> implements Callback<TableColumn<S, T>, TableCell<S, T>>  {
	
	private SimpleDateFormat formatter;

	public FormattedValueFactory01(String fieldName, String dateFormatPattern) {		
		this.formatter = new SimpleDateFormat(dateFormatPattern);
	}

	@Override
	public TableCell<S, T> call(TableColumn<S, T> param) {
		TableCell<S, T> cell = new TableCell<S, T>(){
			@Override
			protected void updateItem(T item, boolean empty) {
				super.updateItem(item, empty);				
				setText(empty ? "" : getDate(item));
			}
			
			private String getDate(T item){
				String formatted = "";				
				if (item != null) {				
					formatted = formatter.format(item);	
				}
				return formatted;
			}
		 };							
		cell.setAlignment(Pos.CENTER_RIGHT);		
		return cell;
	}

}
