package kr.co.green.common.paging;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
//Setter가 필요 없기 때문에 VO에 가까움 
//->readOnly 특징을 가짐 
public class PageInfo {
	private int listCount;
	private int cpage;
	private int pageLimit;
	private int boardLimit;
	
	private int maxPage;
	private int startPage;
	private int endPage;
	private int offset;
}
