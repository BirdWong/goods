package cn.jijiking51.goods.category.domain;

import java.util.List;

public class Category {
	//主键
	private String cid;
	//分类名称
	private String cname;
	//父分类的
	private Category parent;
	//分类描述
	private String desc;
	//子分类
	private List<Category> children;
	@Override
	public String toString() {
		return "Category [cid=" + cid + ", cname=" + cname + ", parent="
				+ parent + ", desc=" + desc + ", children=" + children + "]";
	}
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}
	public Category getParent() {
		return parent;
	}
	public void setParent(Category parent) {
		this.parent = parent;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public List<Category> getChildren() {
		return children;
	}
	public void setChildren(List<Category> children) {
		this.children = children;
	}
}
