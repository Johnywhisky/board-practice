package net.scit.spring7.utility;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class Pagination {
	private final int pageGroupSize = 10;
	private int pageSize;
	private int page;
	private int totalPages;
	private int groupSize;
	private int startPageGroup;
	private int endPageGroup;
	private int currentGroup;

	public Pagination(int pageSize, int page, int totalPages) {
		this.pageSize = pageSize;
		this.page = page;
		this.totalPages = totalPages;
		this.groupSize = totalPages % this.pageGroupSize == 0 ?
			totalPages / this.pageGroupSize :
			totalPages / this.pageGroupSize + 1;
		this.startPageGroup = (int) (Math.ceil((double) page / pageGroupSize) - 1) * pageGroupSize + 1;
		this.endPageGroup = (startPageGroup + pageGroupSize - 1) >= totalPages ?
			totalPages == 0 ? 1 : totalPages :
			(startPageGroup + pageGroupSize - 1);
		this.currentGroup = ((page - 1) / pageGroupSize) + 1;
	}
}
