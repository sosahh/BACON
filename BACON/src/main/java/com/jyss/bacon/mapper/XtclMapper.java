package com.jyss.bacon.mapper;

import com.jyss.bacon.entity.BaseArea;
import com.jyss.bacon.entity.Xtcl;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XtclMapper {
	/**
	 * 根据标识符取得维护常量
	 * 
	 * @return
	 */
	List<Xtcl> getCls();


	//根据标识符取得标志对应常量值
	List<Xtcl> getClsBy(@Param("bz_type") String bz_type,@Param("bz_id") String bz_id);

	/**
	 * 获取常量下拉
	 *
	 * @param bz_type
	 * @param pid
	 * @return
	 */
	List<Xtcl> getClsCombox(@Param("bz_type") String bz_type,
                            @Param("pid") String pid);


	//根据标识符取得标志对应常量值
	Xtcl getClsValue(@Param("bz_type") String bz_type,@Param("bz_id") String bz_id);



	// /////////////////////系统基础维护表/////////////////////////////////

	/**
	 * 系统基础地域表
	 *
	 * @return
	 */
	List<BaseArea> getBaseAreas(@Param("status") String status,@Param("area") String area);

}
