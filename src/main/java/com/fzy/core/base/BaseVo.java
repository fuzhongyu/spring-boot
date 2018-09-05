package com.fzy.core.base;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

/**
 * vo基类
 * @author Fucai
 * @date 2018/3/19
 */
public class BaseVo<VO> extends BaseEntity<VO>{

  private static final Logger logger = LoggerFactory.getLogger(BaseVo.class);


  /**
   * 单个对象转换(entity转vo)
   * @param from  需要转换的对象
   * @param clazz  转成的目标对象类
   * @return
   */
  public static <DO,VO> VO convert2Vo(DO from, Class<VO> clazz) {
    return convert2Vo(from, clazz, null);
  }


  /**
   * 单个对象转换(entity转vo)
   *
   * @param from 需要转换的对象
   * @param clazz 转成的目标对象类
   * @param ignoreProperties 转换对象中，不需要copy的属性
   */
  public static <DO,VO> VO convert2Vo(DO from, Class<VO> clazz, String... ignoreProperties) {
    if (from == null) {
      return null;
    }
    VO to = null;
    try {
      to = clazz.newInstance();
    } catch (ReflectiveOperationException e) {
      e.printStackTrace();
      logger.error("初始化{}对象失败。", clazz, e);
      return null;
    }
    BeanUtils.copyProperties(from, to, ignoreProperties);
    return to;
  }

  /**
   * 批量对象转换(entity转vo)
   * @param fromList  需要转换的对象列表
   * @param clazz  转成的目标对象类
   */
  public static <DO,VO> List<VO> convert2VoList(List<DO> fromList, Class<VO> clazz) {
    return convert2VoList(fromList, clazz, null);
  }

  /**
   * 批量对象转换
   *
   * @param fromList 需要转换的对象列表
   * @param clazz 转成的目标对象类
   * @param ignoreProperties 转换对象中，不需要copy的属性
   */
  public static <DO,VO> List<VO> convert2VoList(List<DO> fromList, Class<VO> clazz, String... ignoreProperties) {
    List<VO> toList = new ArrayList<VO>();
    if (fromList != null) {
      for (DO unit:fromList){
        toList.add(convert2Vo(unit, clazz, ignoreProperties));
      }
    }

    return toList;
  }

  /**
   * 分页对象转换(entity转vo)
   * @param fromPage  需要转换的分页对象
   * @param clazz  转成的目标对象类
   */
  public static <DO,VO> PageEntity<VO> convert2VoPage(PageEntity<DO> fromPage, Class<VO> clazz) {
    return convert2VoPage(fromPage, clazz, null);
  }

  /**
   * 分页对象转换(entity转vo)
   *
   * @param fromPage 需要转换的分页对象
   * @param clazz 转成的目标对象类
   * @param ignoreProperties 转换对象中，不需要copy的属性
   */
  public static <DO,VO> PageEntity<VO> convert2VoPage(PageEntity<DO> fromPage, Class<VO> clazz, String... ignoreProperties) {
    List<VO> voDataList = new ArrayList();

    if (fromPage != null && fromPage.getDataList() != null) {
      voDataList = convert2VoList(fromPage.getDataList(), clazz, ignoreProperties);
    }

    return new PageEntity<VO>(fromPage.getPageNo(),fromPage.getPageSize(),fromPage.getTotalCount(),voDataList);
  }


  /**
   * 单个对象转换(vo转enity)
   * @param from  需要转换的对象
   * @param clazz  转成的目标对象类
   **/
  public static <DO,VO> DO convert2Entity(VO from,Class<DO> clazz){
    return convert2Entity(from,clazz,null);
  }

  /**
   * 单个对象转换(vo转enity)
   * @param from  需要转换的对象
   * @param clazz  转成的目标对象类
   * @param ignoreProperties 转换对象中，不需要copy的属性
   * @return
   */
  public static <DO,VO> DO convert2Entity(VO from,Class<DO> clazz, String... ignoreProperties){
    if (from == null) {
      return null;
    }
    DO to = null;
    try {
      to = clazz.newInstance();
    } catch (ReflectiveOperationException e) {
      e.printStackTrace();
      logger.error("初始化{}对象失败。", clazz, e);
      return null;
    }

    BeanUtils.copyProperties(from, to, ignoreProperties);
    return to;
  }


  /**
   * 批量对象转换(vo转enity)
   * @param fromList  需要转换的对象列表
   * @param clazz  转成的目标对象类
   */
  public static <DO,VO> List<DO> convert2EnitiyList(List<VO> fromList, Class<DO> clazz) {
    return convert2EnitiyList(fromList, clazz, null);
  }

  /**
   * 批量对象转换(vo转enity)
   *
   * @param fromList 需要转换的对象列表
   * @param clazz 转成的目标对象类
   * @param ignoreProperties 转换对象中，不需要copy的属性
   */
  public static <DO,VO> List<DO> convert2EnitiyList(List<VO> fromList, Class<DO> clazz, String... ignoreProperties) {
    List<DO> toList = new ArrayList<>();
    if (fromList != null) {
      for (VO unit:fromList){
        toList.add(convert2Entity(unit, clazz, ignoreProperties));
      }
    }

    return toList;
  }


}