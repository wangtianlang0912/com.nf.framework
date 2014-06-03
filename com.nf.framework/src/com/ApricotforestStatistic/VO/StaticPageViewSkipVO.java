package com.ApricotforestStatistic.VO;
/**
 * 访问页面跳转 权重统计
 * @author niufei
 *
 */
public class StaticPageViewSkipVO {

	String currentViewCode;
	String nextViewCode;
	int count;
	String weight;
	public String getCurrentViewCode() {
		return currentViewCode;
	}
	public void setCurrentViewCode(String currentViewCode) {
		this.currentViewCode = currentViewCode;
	}
	public String getNextViewCode() {
		return nextViewCode;
	}
	public void setNextViewCode(String nextViewCode) {
		this.nextViewCode = nextViewCode;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((currentViewCode == null) ? 0 : currentViewCode.hashCode());
		result = prime * result
				+ ((nextViewCode == null) ? 0 : nextViewCode.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StaticPageViewSkipVO other = (StaticPageViewSkipVO) obj;
		if (currentViewCode == null) {
			if (other.currentViewCode != null)
				return false;
		} else if (!currentViewCode.equals(other.currentViewCode))
			return false;
		if (nextViewCode == null) {
			if (other.nextViewCode != null)
				return false;
		} else if (!nextViewCode.equals(other.nextViewCode))
			return false;
		return true;
	}
	public StaticPageViewSkipVO(String currentViewCode, String nextViewCode) {
		super();
		this.currentViewCode = currentViewCode;
		this.nextViewCode = nextViewCode;
	}
	public StaticPageViewSkipVO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
