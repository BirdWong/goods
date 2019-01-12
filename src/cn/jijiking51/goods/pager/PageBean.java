package cn.jijiking51.goods.pager;

import java.util.List;

/**
 * 分页类
 * @author  h4795
 * @param <T>
 */
public class PageBean<T> {
	/**
	 * 当前页码
	 */
	private int pc ;
	/**
	 *总记录数
	 */
	private int tr ;
	/**
	 * 当前记录数
	 */
	private int ps ;
	/**
	 * 请求路径 例如：/BookServlet?method=findxxx&cid=1&bname=2
	 */
	private String url;
	/**
	 * 列表内容
	 */
	private List<T> beanList ;
	
	public int getTp(){
		int tp = tr/ps;
		return tr%ps == 0 ? tp : tp+1; 
	}
	public int getPc() {
		return pc;
	}
	public void setPc(int pc) {
		this.pc = pc;
	}
	public int getTr() {
		return tr;
	}
	public void setTr(int tr) {
		this.tr = tr;
	}
	public int getPs() {
		return ps;
	}
	public void setPs(int ps) {
		this.ps = ps;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public List<T> getBeanList() {
		return beanList;
	}
	public void setBeanList(List<T> beanList) {
		this.beanList = beanList;
	}
	
}
