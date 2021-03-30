package cn.jimoos.dao;

import cn.jimoos.model.RProductTag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author :keepcleargas
 * @date :2021-03-29 19:52.
 */
@Mapper
public interface RProductTagMapper {
    int deleteByPrimaryKey(Long id);

    int insert(RProductTag record);

    RProductTag selectByPrimaryKey(Long id);

    int updateByPrimaryKey(RProductTag record);

    int batchInsert(@Param("list") List<RProductTag> list);

    /**
     * 统计 标签下的商品数量
     *
     * @param tagId tag id
     * @return product total num under tagId
     */
    Long countByTagId(@Param("tagId") Long tagId);
}