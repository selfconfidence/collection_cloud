package com.manyun.business.domain.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * @author yanwei
 * @date 2022/7/25
 */
@Data
@Builder
@ToString
public class CallCommitDto {
    /**
     *      *     插入艺术品基本信息
     *      *     artId:艺术品Id  自己拥有得藏品得唯一编号  并不是藏品编号
     *      *     date:艺术品年代
     *      *     owner:艺术品拥有者
     *      *     artName:艺术品名称
     *      *     price:艺术品价格
     *      *     sellWay:艺术品获得方式
     *      *     artSize：艺术品尺寸
     *      *     location：艺术品目前所在地
     */
   private String artId;
   private String date;
   private String artName;
   private String owner;
   private String price;
   private String sellway;
   private String artSize;
   private String location;
   private String userCollectionId;



}
