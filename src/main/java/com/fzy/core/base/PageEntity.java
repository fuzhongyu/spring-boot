package com.fzy.core.base;

import com.fzy.core.util.ParamUtil;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 分页类
 * @author Fucai
 * @date 2018/3/19
 */
@Getter
@Setter
@NoArgsConstructor
public class PageEntity<T> {

    /**
     * 当前页码
     */
    private Integer pageNo;

    /**
     * 每页大小
     */
    private Integer pageSize;

    /**
     * 总条数
     */
    private Long totalCount;

    /**
     * 总页面数
     */
    private Integer totalPage;

    /**
     * 是否是第一页，true-是，false-否
     */
    private Boolean fistPage;

    /**
     * 是否是最后一页,true-是，false-否
     */
    private Boolean lastPage;

    /**
     * 数据列表
     */
    private List<T> dataList;

    public PageEntity(int pageNo,int pageSize,Long totalCount,List<T> dataList){
        this.pageNo=pageNo;
        this.pageSize=pageSize;

        if(totalCount==null){
            this.totalCount=0L;
            this.totalPage=0;
            this.fistPage=true;
            this.lastPage=true;
        }else {
            this.totalCount=totalCount;
            this.totalPage= ParamUtil.IntegerParam((totalCount+pageSize-1)/pageSize);
            this.fistPage=pageNo<2?true:false;
            this.lastPage=pageNo>=totalPage?true:false;
        }
        if(dataList==null){
            this.dataList=new ArrayList<>();
        }else {
            this.dataList=dataList;
        }
    }

}
