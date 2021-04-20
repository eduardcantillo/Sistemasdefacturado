package com.bolsadeideas.spring.horario.datajpa.app.view;

import java.awt.Color;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.bolsadeideas.spring.horario.datajpa.app.models.Factura;
import com.bolsadeideas.spring.horario.datajpa.app.models.ItemFactura;
import com.lowagie.text.Document;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

@Component("factura/ver")
public class FacturaPdfView extends AbstractPdfView {

	@Autowired
	@Qualifier("messageSource")
	private MessageSource message;

	@Autowired
	LocaleResolver localeResolve;
	
	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		Factura factura = (Factura) model.get("factura");
		System.out.println(factura.getId());
		PdfPTable tabla = new PdfPTable(1);
		tabla.setSpacingAfter(20);
		
		Locale locale=localeResolve.resolveLocale(request);
		MessageSourceAccessor mensaje=getMessageSourceAccessor();
		
		 message.getMessage("text.cliente.form.titulo", null, locale);

		PdfPCell cell=null;
		
		cell=new PdfPCell(new Phrase(message.getMessage("text.factura.ver.datos.cliente", null, locale)));
		cell.setBackgroundColor(new Color(184,218,255));
		cell.setPadding(8f);
		
		tabla.addCell(cell);
		tabla.addCell(factura.getCliente().toString());
		tabla.addCell(factura.getCliente().getEmail());

		PdfPTable tabla2 = new PdfPTable(1);
		
		cell=new PdfPCell(new Phrase(message.getMessage("text.factura.ver.datos.factura", null, locale)));
		cell.setBackgroundColor(new Color(195,230,203));
		cell.setPadding(8f);
		
		
		tabla2.addCell(cell);
		tabla2.setSpacingAfter(20);
		tabla2.addCell(message.getMessage("text.cliente.factura.folio", null, locale).concat(factura.getId().toString()));
		tabla2.addCell(message.getMessage("text.cliente.factura.descripcion", null, locale).concat(factura.getDescripcion()));
		tabla2.addCell(message.getMessage("text.cliente.factura.fecha", null, locale).concat(factura.getCreateAt().toString()));

		document.add(tabla);
		document.add(tabla2);
		
		PdfPTable table3=new PdfPTable(4);
		table3.setWidths(new float[] {3.5f,1,1,1});
		table3.addCell(mensaje.getMessage("text.factura.form.item.nombre"));
		table3.addCell(mensaje.getMessage("text.factura.form.item.precio"));
		table3.addCell(mensaje.getMessage("text.factura.form.item.cantidad"));
		table3.addCell(mensaje.getMessage("text.factura.form.item.total"));
		
		for(ItemFactura items: factura.getItems()) {
			table3.addCell(items.getProducto().getNombre());
			table3.addCell(items.getProducto().getPrecio()+"");
			
			cell=new PdfPCell(new Phrase(items.getCantidad()+""));
			cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
			
			table3.addCell(cell);
			table3.addCell(items.calcularImporte()+"");
		}
		
		 cell=new PdfPCell(new Phrase(message.getMessage("text.factura.form.total", null, locale)));
		cell.setColspan(3);
		cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
		table3.addCell(cell);
		table3.addCell(factura.getTotal()+"");
		document.add(table3);
		
	}

}
