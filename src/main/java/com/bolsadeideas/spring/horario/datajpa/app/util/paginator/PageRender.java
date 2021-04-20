package com.bolsadeideas.spring.horario.datajpa.app.util.paginator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;

public class PageRender<T> {

	private String url;
	private Page<T> page;
	private int totalPaginas;
	private int numElementoPorPagina;
	private int pagActual;

	private List<PageItem> items;

	public PageRender(String url, Page<T> page) {

		this.items = new ArrayList<PageItem>();
		this.url = url;
		this.page = page;
		numElementoPorPagina = page.getSize();
		totalPaginas = page.getTotalPages();
		pagActual = page.getNumber() + 1;
		int desde, hasta;
		if (totalPaginas <= numElementoPorPagina) {
			desde = 1;
			hasta = totalPaginas;
		} else {

			if (pagActual <= numElementoPorPagina / 2) {

				desde = 1;
				hasta = numElementoPorPagina;
			} else if (pagActual >= totalPaginas - numElementoPorPagina / 2) {

				desde = totalPaginas - numElementoPorPagina + 1;
				hasta = numElementoPorPagina;

			} else {

				desde = pagActual - numElementoPorPagina / 2;
				hasta = numElementoPorPagina;
			}

		}

		for (int i = 0; i < hasta; i++) {
			items.add(new PageItem(desde + i, pagActual == desde + i));

		}

	}

	public String getUrl() {
		return url;
	}

	public int getTotalPaginas() {
		return totalPaginas;
	}

	public int getPagActual() {
		return pagActual;
	}

	public boolean isFirst() {
		return page.isFirst();
	}

	public boolean isLast() {
		return page.isLast();
	}

	public boolean isHasNext() {
		return page.hasNext();
	}

	public List<PageItem> getItems() {
		return items;
	}

	public boolean isHasPrevious() {
		return page.hasPrevious();
	}

}
