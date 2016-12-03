package com.team.common.mybatis.page;

import java.util.*;

/**
 * 
 * @author 10075673
 *
 */
public class PageDataSet {
  
	PageCondition pageCondition;

    @SuppressWarnings("rawtypes")
	List dataset;// 数据

	/**
	 * @return the pageCondition
	 */
	public PageCondition getPageCondition() {
		return pageCondition;
	}

	/**
	 * @param pageCondition the pageCondition to set
	 */
	public void setPageCondition(PageCondition pageCondition) {
		this.pageCondition = pageCondition;
	}

	/**
	 * @return the dataset
	 */
	public List getDataset() {
		return dataset;
	}

	/**
	 * @param dataset the dataset to set
	 */
	public void setDataset(List dataset) {
		this.dataset = dataset;
	}

  
}
