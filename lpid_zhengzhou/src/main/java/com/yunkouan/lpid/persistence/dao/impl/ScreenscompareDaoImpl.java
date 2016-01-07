package com.yunkouan.lpid.persistence.dao.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import static com.yunkou.common.util.DateTimeUtil.*;

import com.yunkou.common.util.Tuple;
import com.yunkou.common.util.TwoTuple;
import com.yunkouan.lpid.biz.screenscompare.bo.ScreenscompareQueryModel;
import com.yunkouan.lpid.persistence.dao.ScreenscompareDao;
import com.yunkouan.lpid.persistence.entity.RunningPackage;

/**
 * <P>Title: ppoiq</P>
 * <P>Description: </P>
 * <P>Copyright: Copyright(c) 2014</P>
 * <P>Company: yunkouan.com</P>
 * <P>Date:2014年12月01日-04时29分21秒</P>
 * @author yangli
 * @version 1.0.0
 */
@Repository("screenscompareDao") 
public class ScreenscompareDaoImpl extends GenericJpaDaoImpl implements ScreenscompareDao {

	@Override
	public List<RunningPackage> findRunningPackages(int firstResult, int pageSize, ScreenscompareQueryModel queryModel) {
		return super.findByNamedQuery(getJpql(queryModel).first, firstResult, pageSize, getJpql(queryModel).second.toArray());
	}
	
	private TwoTuple<String, List<Object>> getJpql(ScreenscompareQueryModel queryModel) {
		//查询jpql语句
        StringBuffer jpql = new StringBuffer(
                  " from RunningPackage runningpackage "
                + " where 1 = 1 ");
        //参数
        List<Object> params = new ArrayList<Object>();
        
		
		//查询时间开始
        if(StringUtils.isNotEmpty(queryModel.getStartDate())) {
            jpql.append(" and runningpackage.createTime >= ? ");
            Date fromDate = parseDate(getDateBeginTime(queryModel.getStartDate()), ISO_DATE_TIME_FORMAT);
            params.add(fromDate);
        }
        //查询时间结束
        if(StringUtils.isNotEmpty(queryModel.getEndDate())) {
            jpql.append(" and runningpackage.createTime <= ? ");
            Date toDate = parseDate(getDateEndTime(queryModel.getEndDate()), ISO_DATE_TIME_FORMAT);
            params.add(toDate);
        }
       
        jpql.append(" order by runningpackage.createTime desc");
        
        return Tuple.tuple(jpql.toString(), params);
	}

	@Override
	public int findRunningPackageCount(ScreenscompareQueryModel queryModel) {
		return super.getCount(getJpql(queryModel).first, getJpql(queryModel).second.toArray());
	}

	@Override
	public RunningPackage getRunningPackageByEPC(String epcNo) {
		String jpql = "from RunningPackage r WHERE r.rfidNo = ?";
		return super.findFirstOrNull(jpql, epcNo);
	}

	@Override
	public RunningPackage getRunningPackageById(int id) {
		String jpql = "from RunningPackage r WHERE r.id = ?";
		return super.findFirstOrNull(jpql, id);
	}
}

