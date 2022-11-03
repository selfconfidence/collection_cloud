package com.manyun.business.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import com.manyun.business.config.PosterUtil;
import com.manyun.business.design.mychain.MyChainService;
import com.manyun.business.domain.dto.*;
import com.manyun.business.domain.entity.CntCollection;
import com.manyun.business.domain.entity.UserCollection;
import com.manyun.business.domain.form.PushMuseumForm;
import com.manyun.business.domain.vo.MediaVo;
import com.manyun.business.domain.vo.MuseumListVo;
import com.manyun.business.domain.vo.UserCollectionVo;
import com.manyun.business.mapper.UserCollectionMapper;
import com.manyun.business.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manyun.comm.api.RemoteBuiUserService;
import com.manyun.common.core.constant.BusinessConstants;
import com.manyun.common.core.constant.SecurityConstants;
import com.manyun.common.core.domain.Builder;
import com.manyun.common.core.domain.R;
import com.manyun.common.redis.service.RedisService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import java.io.File;
import java.math.BigDecimal;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.manyun.common.core.constant.BusinessConstants.CommDict.REAL_COMPANY;
import static com.manyun.common.core.constant.BusinessConstants.LogsTypeConstant.POLL_SOURCE;
import static com.manyun.common.core.constant.BusinessConstants.LogsTypeConstant.PULL_SOURCE;
import static com.manyun.common.core.constant.BusinessConstants.ModelTypeConstant.COLLECTION_MODEL_TYPE;
import static com.manyun.common.core.constant.BusinessConstants.RedisDict.COLLECTION_AUTO_NUM;
import static com.manyun.common.core.enums.CollectionLink.NOT_LINK;
import static com.manyun.common.core.enums.CollectionLink.OK_LINK;
import static com.manyun.common.core.enums.CommAssetStatus.NOT_EXIST;
import static com.manyun.common.core.enums.CommAssetStatus.USE_EXIST;

/**
 * <p>
 * 用户购买藏品中间表 服务实现类
 * </p>
 *
 * @author yanwei
 * @since 2022-06-21
 */
@Service
public class UserCollectionServiceImpl extends ServiceImpl<UserCollectionMapper, UserCollection> implements IUserCollectionService {


    @Autowired
    private ILogsService logsService;

    @Resource
    private UserCollectionMapper userCollectionMapper;

    @Autowired
    private IStepService stepService;

    @Autowired
    private IMsgService msgService;

    @Autowired
    private MyChainService myChainService;

    @Autowired
    private ObjectFactory<ICollectionService> collectionServiceObjectFactory;

    @Autowired
    private RedisService redisService;

    @Autowired
    private ISystemService systemService;

    @Autowired
    private RemoteBuiUserService remoteBuiUserService;

    @Autowired
    private IMediaService mediaService;


    /**
     * 直接上链即可,转让的新增个函数进行处理
     *
     * 绑定用户与藏品的相关联信息
     * @param userId
     * @param buiId
     * @param info
     * @param goodsNum
     */
    @Override
    public String bindOrderCollection(String userId, String buiId, String collectionName,String info, Integer goodsNum) {
        ArrayList<UserCollection> userCollections = Lists.newArrayList();
        for (Integer i = 0; i < goodsNum; i++) {
            UserCollection userCollection = Builder.of(UserCollection::new).build();
            userCollection.setId(IdUtil.getSnowflake().nextIdStr());
            userCollection.setCollectionId(buiId);
            userCollection.setUserId(userId);
            userCollection.setSourceInfo(info);
            userCollection.setLinkAddr(IdUtil.getSnowflake().nextIdStr());
            userCollection.setIsExist(USE_EXIST.getCode());
            userCollection.setCollectionName(collectionName);
            // 初始化 未上链过程
            userCollection.setIsLink(NOT_LINK.getCode());
            userCollection.createD(userId);
            userCollections.add(userCollection);
        }
        saveBatch(userCollections);
        // 增加日志
        logsService.saveLogs(LogInfoDto.builder().jsonTxt(info).buiId(userId).modelType(COLLECTION_MODEL_TYPE).isType(PULL_SOURCE).formInfo(goodsNum.toString()).build());
        // 开始上链 // 组装所有上链所需要数据结构 并且不能报错
        ThreadUtil.execute(()->{
            for (UserCollection userCollection : userCollections) {
                BigDecimal realPrice = collectionServiceObjectFactory.getObject().getById(userCollection.getCollectionId()).getRealPrice();
                myChainService.accountCollectionUp(CallCommitDto.builder()
                        .userCollectionId(userCollection.getId())
                        .artId(userCollection.getLinkAddr())
                        .artName(userCollection.getCollectionName())
                        .artSize("80")
                        .location(userCollection.getLinkAddr())
                        .price(realPrice.toString())
                        .date(userCollection.getCreatedTime().format(DateTimeFormatter.ofPattern("yyyy-MM")))
                        .sellway(userCollection.getSourceInfo())
                        .owner(userCollection.getUserId())
                        .build(), (hash)->{
                    userCollection.setIsLink(OK_LINK.getCode());
                    userCollection.setRealCompany(REAL_COMPANY);
                    // 编号特殊生成 借助 redis 原子性操作
                    userCollection.setCollectionNumber(StrUtil.format("CNT_{}",autoCollectionNum(userCollection.getCollectionId())));
                    //userCollection.setLinkAddr(hash);
                    userCollection.setCollectionHash(hash);
                    userCollection.updateD(userCollection.getUserId());
                    updateById(userCollection);
                });
            }
        });
        return userCollections.get(0).getId();
    }

    /**
     * 直接上链即可,转让的新增个函数进行处理
     *
     * 绑定用户与藏品的相关联信息
     * @param userId
     * @param buiId
     * @param info
     */
    @Override
    public void bindCollection(String userId, String buiId, String collectionName,String info) {
            UserCollection userCollection = Builder.of(UserCollection::new).build();
            userCollection.setId(IdUtil.getSnowflake().nextIdStr());
            userCollection.setCollectionId(buiId);
            userCollection.setUserId(userId);
            userCollection.setSourceInfo(info);
            userCollection.setLinkAddr(IdUtil.getSnowflake().nextIdStr());
            userCollection.setIsExist(USE_EXIST.getCode());
            userCollection.setCollectionName(collectionName);
            // 初始化 未上链过程
            userCollection.setIsLink(NOT_LINK.getCode());
            userCollection.createD(userId);
        save(userCollection);
        // 增加日志
        logsService.saveLogs(LogInfoDto.builder().jsonTxt(info).buiId(userId).modelType(COLLECTION_MODEL_TYPE).isType(PULL_SOURCE).formInfo(Integer.valueOf(1).toString()).build());
        // 开始上链 // 组装所有上链所需要数据结构 并且不能报错
       ThreadUtil.execute(()->{
           BigDecimal realPrice = collectionServiceObjectFactory.getObject().getById(userCollection.getCollectionId()).getRealPrice();
           myChainService.accountCollectionUp(CallCommitDto.builder()
                   .userCollectionId(userCollection.getId())
                   .artId(userCollection.getLinkAddr())
                   .artName(userCollection.getCollectionName())
                   .artSize("80")
                   .location(userCollection.getLinkAddr())
                   .price(realPrice.toString())
                   .date(userCollection.getCreatedTime().format(DateTimeFormatter.ofPattern("yyyy-MM")))
                   .sellway(userCollection.getSourceInfo())
                   .owner(userCollection.getUserId())
                   .build(), (hash)->{
               userCollection.setIsLink(OK_LINK.getCode());
               userCollection.setRealCompany(REAL_COMPANY);
               // 编号特殊生成 借助 redis 原子性操作
               userCollection.setCollectionNumber(StrUtil.format("CNT_{}",autoCollectionNum(userCollection.getCollectionId())));
               //userCollection.setLinkAddr(hash);
               userCollection.setCollectionHash(hash);
               userCollection.updateD(userCollection.getUserId());
               updateById(userCollection);
           });
       });

    }

    @Override
    public String autoCollectionNum(String collectionId) {
        //String intAutoNum = redisService.getIntAutoNum(COLLECTION_AUTO_NUM.concat(collectionId)).toString();
        Object randomNum = redisService.getRandomNum(BusinessConstants.RedisDict.COLLECTION_RANDOM_NUM.concat(collectionId));
        String intAutoNum = "";
        if (Objects.isNull(randomNum)) {
            intAutoNum = redisService.getIntAutoNum(COLLECTION_AUTO_NUM.concat(collectionId)).toString();
        } else {
            intAutoNum = randomNum.toString();
        }
        //String poiLent = systemService.getVal(COLLECTION_POT, String.class);
        CntCollection cntCollection = collectionServiceObjectFactory.getObject().getById(collectionId);
        int total = cntCollection.getBalance() + cntCollection.getSelfBalance();
        String poiLent = StringUtils.repeat("0", String.valueOf(total).length());
        return  poiLent.substring(0, poiLent.length() - intAutoNum.length()).concat(intAutoNum).concat("/" + total);
    }


    /**
     *
     * @param tranUserId 转让方用户编号
     * @param toUserId  受让方用户编号
     * @param tranUserCollection  转让方的藏品中间表
     * @param format
     * @param formatTran
     */
    // 转让,需要额外处理了,这个链 属于转增级别的链
   public void tranCollection(String tranUserId,String toUserId,UserCollection tranUserCollection,String format,String formatTran,String oldCollectionHash,String oldCollectionNumber){
       UserCollection userCollection = Builder.of(UserCollection::new).build();
       userCollection.setId(IdUtil.getSnowflake().nextIdStr());
       userCollection.setCollectionId(tranUserCollection.getCollectionId());
       userCollection.setUserId(toUserId);
       userCollection.setSourceInfo(format);
       userCollection.setLinkAddr(tranUserCollection.getLinkAddr());
       userCollection.setIsExist(USE_EXIST.getCode());
       userCollection.setCollectionName(tranUserCollection.getCollectionName());
       userCollection.setCollectionHash(oldCollectionHash);
       userCollection.setCollectionNumber(oldCollectionNumber);
       userCollection.setRealCompany(REAL_COMPANY);
       // 初始化 未上链过程
       userCollection.setIsLink(OK_LINK.getCode());
       userCollection.createD(toUserId);
       save(userCollection);
       //StepDto.builder().buiId(userCollection.getLinkAddr()).userId(tranUserId).modelType(COLLECTION_MODEL_TYPE).reMark("转让方").formHash(oldCollectionHash).formInfo(formatTran).build()
       stepService.saveBatch(StepDto.builder().buiId(userCollection.getLinkAddr()).userId(toUserId).modelType(COLLECTION_MODEL_TYPE).formHash(oldCollectionHash).reMark("受让方").formInfo(format).build());

       // 开始转赠
       /*myChainService.tranForm(CallTranDto.builder()
               .userCollectionId(userCollection.getId())
               .artId(userCollection.getLinkAddr())
               .owner(userCollection.getUserId())
               .date(userCollection.getCreatedTime().format(DateTimeFormatter.ofPattern("yyyy-MM"))).build(), (hash) ->{
           // 得到 hash
           userCollection.setIsLink(OK_LINK.getCode());
           userCollection.setRealCompany("蚂蚁链");
           // 编号特殊生成
           userCollection.setCollectionNumber(oldCollectionNumber);
          // userCollection.setLinkAddr(hash);
           userCollection.setCollectionHash(hash);
           userCollection.updateD(userCollection.getUserId());
           updateById(userCollection);
           // 流转记录
           stepService.saveBatch(StepDto.builder().buiId(userCollection.getLinkAddr()).userId(tranUserId).modelType(COLLECTION_MODEL_TYPE).reMark("转让方").formHash(oldCollectionHash).formInfo(formatTran).build()
                   ,StepDto.builder().buiId(userCollection.getLinkAddr()).userId(toUserId).modelType(COLLECTION_MODEL_TYPE).formHash(hash).reMark("受让方").formInfo(format).build()
           );
       });*/




   }

    /**
     * 查询拥有拥有得藏品
     * @param userId
     * @return
     */

    @Override
    public List<UserCollectionVo> userCollectionPageList(String userId,String commName) {
        return userCollectionMapper.userCollectionPageList(userId,commName);
    }



    /**
     * 根据中间表编号查询 用户得藏品详情
     * @param id
     * @return
     */

    @Override
    public UserCollectionVo userCollectionById(String id) {
        return userCollectionMapper.userCollectionById(id);
    }

    /**
     * 是否存在该藏品?
     * @param userId
     * @param id
     * @return
     */
    @Override
    public Boolean existUserCollection(String userId, String id) {
        // 必须是已上链的
        return Objects.nonNull(getOne(Wrappers.<UserCollection>lambdaQuery().eq(UserCollection::getIsExist,USE_EXIST.getCode()).eq(UserCollection::getId,id).eq(UserCollection::getUserId,userId).eq(UserCollection::getIsLink,OK_LINK.getCode())));
    }

    /**
     * 转让 藏品
     * @param tranUserId
     * @param toUserId
     * @param buiId
     */
    @Override
    public String tranCollection(String tranUserId, String toUserId, String buiId) {
        UserCollection userCollection = getOne(Wrappers.<UserCollection>lambdaQuery().eq(UserCollection::getId, buiId).eq(UserCollection::getUserId, tranUserId).eq(UserCollection::getIsLink, OK_LINK.getCode()));
        String format = StrUtil.format("{}:赠送获得该藏品!", userCollection.getCollectionName());
        String formatTran = StrUtil.format("{}:藏品被赠送!",userCollection.getCollectionName());
        //原始拥有者的绑定关系 解绑
        userCollection.setIsExist(NOT_EXIST.getCode());
        updateById(userCollection);
        // 增加日志 ...................
        logsService.saveLogs(
                LogInfoDto.builder().jsonTxt(format).buiId(toUserId).modelType(COLLECTION_MODEL_TYPE).isType(PULL_SOURCE).formInfo(Integer.valueOf(1).toString()).build()
                ,LogInfoDto.builder().jsonTxt(formatTran).buiId(tranUserId).modelType(COLLECTION_MODEL_TYPE).isType(POLL_SOURCE).formInfo(Integer.valueOf(1).toString()).build()
        );
        String oldCollectionHash = userCollection.getCollectionHash();
        String oldCollectionNumber = userCollection.getCollectionNumber();
        // 此接口更改为转赠链接口
        //bindCollection(toUserId,userCollection.getCollectionId(),userCollection.getCollectionName(),format,Integer.valueOf(1));
        tranCollection(tranUserId,toUserId,userCollection,format,formatTran,oldCollectionHash,oldCollectionNumber);


        msgService.saveMsgThis(MsgThisDto.builder().userId(tranUserId).msgForm(formatTran).msgTitle(formatTran).build());
        msgService.saveMsgThis(MsgThisDto.builder().userId(toUserId).msgForm(format).msgTitle(format).build());
        msgService.saveCommMsg(MsgCommDto.builder().msgTitle(format).msgForm(format).build());

        return userCollection.getCollectionId();
    }

    /**
     * 隐藏对应藏品
     * @param buiId
     * @param userId
     * @param info
     */
    @Override
    public String hideUserCollection(String buiId, String userId, String info) {
        UserCollection userCollection = getOne(Wrappers.<UserCollection>lambdaQuery().eq(UserCollection::getIsExist, USE_EXIST.getCode()).eq(UserCollection::getId, buiId).eq(UserCollection::getUserId, userId).eq(UserCollection::getIsLink, OK_LINK.getCode()));
        Assert.isTrue(Objects.nonNull(userCollection),"藏品有误,检查藏品是否上链!");
        userCollection.setIsExist(NOT_EXIST.getCode());
        userCollection.updateD(userId);
        userCollection.setSourceInfo(StrUtil.join("\n", userCollection.getSourceInfo(),info));
        updateById(userCollection);
        return userCollection.getCollectionId();
    }


    /**
     * 重新上链
     * @param userId
     * @param userCollectionId
     */
    @Override
    public void resetUpLink(String userId, String userCollectionId) {
        UserCollection userCollection = getOne(Wrappers.<UserCollection>lambdaQuery().eq(UserCollection::getUserId,userId).eq(UserCollection::getId,userCollectionId));
        Assert.isTrue(Objects.nonNull(userCollection) && NOT_LINK.getCode().equals(userCollection.getIsLink()),"藏品信息有误,请核实!");
        BigDecimal realPrice = collectionServiceObjectFactory.getObject().getById(userCollection.getCollectionId()).getRealPrice();

        myChainService.accountCollectionUp(CallCommitDto.builder()
                .userCollectionId(userCollection.getId())
                .artId(userCollection.getLinkAddr())
                .artName(userCollection.getCollectionName())
                .artSize("80")
                .location(userCollection.getLinkAddr())
                .price(realPrice.toString())
                .date(userCollection.getCreatedTime().format(DateTimeFormatter.ofPattern("yyyy-MM")))
                .sellway(userCollection.getSourceInfo())
                .owner(userCollection.getUserId())
                        .build(), (hash)->{
                    userCollection.setIsLink(OK_LINK.getCode());
                    userCollection.setRealCompany(REAL_COMPANY);
                    // 编号特殊生成
                    userCollection.setCollectionNumber(StrUtil.format("CNT_{}",autoCollectionNum(userCollection.getCollectionId())));
                    //userCollection.setLinkAddr(hash);
                    userCollection.setCollectionHash(hash);
                    userCollection.updateD(userCollection.getUserId());
                    updateById(userCollection);
                    // TODO 会有个 bug 就是如果是转赠失败的话，那么统一上链处理，流转记录得不到记录。
    });

    }

//    @Override
//    public String showUserCollection(String buiId, String userId, String info) {
//        UserCollection userCollection = getOne(Wrappers.<UserCollection>lambdaQuery().eq(UserCollection::getId, buiId).eq(UserCollection::getUserId, userId));
//       // Assert.isTrue(Objects.nonNull(userCollection),"藏品有误,检查藏品是否上链!");
//        userCollection.setIsExist(USE_EXIST.getCode());
//        userCollection.updateD(userId);
//        userCollection.setSourceInfo(StrUtil.join("\n", userCollection.getSourceInfo(),info));
//        updateById(userCollection);
//        return userCollection.getCollectionId();
//    }


    /**
     *  查询用户拥有个藏品的数量
     * @param collectionIds
     * @return
     */
    @Override
    public List<UserCollectionCountDto> selectUserCollectionCount(List<String> collectionIds, String userId) {
        return userCollectionMapper.selectUserCollectionCount(collectionIds,userId);
    }

    @Override
    public void deleteMaterial(String userId,String collectionId, Integer deleteNum) {
        userCollectionMapper.deleteMaterial(userId,collectionId,deleteNum);
    }

    @Override
    public void updateMaterial(String userId,String collectionId, Integer deleteNum) {
        List<UserCollection> list = list(Wrappers.<UserCollection>lambdaQuery().eq(UserCollection::getCollectionId, collectionId).eq(UserCollection::getUserId, userId));
        for (int i=0; i<deleteNum; i++ ) {
            list.get(i).setIsExist(2);
        }
        updateBatchById(list);
    }


    @Override
    public String showUserCollection(String userId, String buiId, String info) {
        UserCollection userCollection = getOne(Wrappers.<UserCollection>lambdaQuery().eq(UserCollection::getIsExist, NOT_EXIST.getCode()).eq(UserCollection::getId, buiId).eq(UserCollection::getUserId, userId).eq(UserCollection::getIsLink, OK_LINK.getCode()));
        Assert.isTrue(Objects.nonNull(userCollection),"藏品有误,请检查藏品状态!");
        userCollection.setIsExist(USE_EXIST.getCode());
        userCollection.updateD(userId);
        userCollection.setSourceInfo(info);
        updateById(userCollection);
        return userCollection.getCollectionId();
    }


    @Override
    public R<String> shareCollection(String userId, String myGoodsId) {
        UserCollection userCollection = getById(myGoodsId);
        List<MediaVo> mediaVos = mediaService.initMediaVos(userCollection.getCollectionId(), COLLECTION_MODEL_TYPE);
        //String mediaUrl = userCollectionVo.getMediaVos().get(0).getMediaUrl();

        //背景，海报图
        String background = mediaVos.get(0).getMediaUrl();
        //String background = "https://cnt-images.oss-cn-chengdu.aliyuncs.com/user/images/26d6bca223244eb78a9d8a3fe385ec8d";
        int width = 0;
        int height = 0;
        try {
            URL backgroundUrl = new URL(background);
            BufferedImage read = ImageIO.read(backgroundUrl.openStream());
            width = read.getWidth();
            height = read.getHeight();

            //BufferedImage bufferedImage = PosterUtil.drawInitAndChangeSize(background, backgroundUrl.openConnection().getInputStream(),width, height);

            // 画 二维码 并改变大小
            // 1. 先 获取二维码(二维码携带一个参数)

            String regUrl = systemService.getVal(BusinessConstants.SystemTypeConstant.REG_URL, String.class) + "?pleaseCode=" + remoteBuiUserService.commUni(userId,SecurityConstants.INNER).getData().getPleaseCode();
            // + "?" + cntUser.getPleaseCode()
            // 生成二维码并指定宽高
            BufferedImage qrCode = QrCodeUtil.generate(regUrl, (int)Math.round(height*0.2), (int)Math.round(height*0.2));


            // 2. 初始化并的改变大小
            // 将二维码保存到本地
            // 3. 画二维码
            //PosterUtil.drawImage(bufferedImage, qrCode, 300, 300, (int) Math.round(width*0.37), (int) Math.round(height*0.75));
            PosterUtil.drawImage(read, qrCode, (int)Math.round(height*0.2), (int)Math.round(height*0.2), (int) Math.round(width/2 - height*0.2/2), (int) Math.round(height*0.75));

            // 海报 保存到本地/var/opt/  线上使用 "d:\\upload\\"
            String linuxPath = systemService.getVal(BusinessConstants.SystemTypeConstant.LOCAL_PATH, String.class);
            File file1 = new File(linuxPath);
            if (!file1.exists() && !file1.isDirectory()) {
                file1.mkdirs();
            }
            String localPath = linuxPath + System.currentTimeMillis() + ".png";
            PosterUtil.save(read, "png", localPath);

            HashMap<String, Object> paramMap = new HashMap<>();
            File file = FileUtil.file(localPath);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            paramMap.put("file", file);

            String gatewayUrl = systemService.getVal(BusinessConstants.SystemTypeConstant.GATEWAY_URL, String.class);

            String post = HttpUtil.post(gatewayUrl, paramMap);
            JSONObject responseJson = JSONUtil.toBean(post, JSONObject.class);
            Object data = responseJson.get("data");
            file.delete();
            List<UserCollection> list = list(Wrappers.<UserCollection>lambdaQuery().eq(UserCollection::getUserId, userId).eq(UserCollection::getCollectionId, userCollection.getCollectionId()));
            if (!list.isEmpty()) {
                for (UserCollection collection : list) {
                    collection.setShareUrl(data.toString());
                }
            }
            //cntUser.setInviteUrl(data.toString());
            //updateById(cntUser);
            //inviteUserVo.setInviteUrl(data.toString());
            updateBatchById(list);
            return R.ok(data.toString());


        } catch (Exception e) {
            e.printStackTrace();
            return R.fail(e.getMessage());
        }
    }


    @Override
    @Transactional
    public void pushMuseum(PushMuseumForm pushMuseumForm, String userId) {
        List<UserCollection> list = list(Wrappers.<UserCollection>lambdaQuery().eq(UserCollection::getUserId, userId).eq(UserCollection::getIsExist, 1)
                .eq(UserCollection::getIsMuseum, 1));
        if (!list.isEmpty()) {
            list = list.parallelStream().map(item -> {
                item.setIsMuseum(2);
                return item;
            }).collect(Collectors.toList());
            updateBatchById(list);
        }
        Assert.isTrue(pushMuseumForm.getCollections().size() <= 15, "藏品数量已达上限，请核实");
        /*ArrayList<String> idList = new ArrayList<>();
        Collections.addAll(idList, collections);*/
        List<String> idList = pushMuseumForm.getCollections();
        if (idList.isEmpty()) {
            return;
        }
        List<UserCollection> userCollections = listByIds(idList);
        List<UserCollection> collect = userCollections.parallelStream().map(item -> {
            Assert.isTrue(Objects.nonNull(item), "选择藏品有误，请核实");
            Assert.isTrue(userId.equals(item.getUserId()), "藏品有误，请核实");
            item.setIsMuseum(1);
            return item;
        }).collect(Collectors.toList());
        updateBatchById(collect);

    }


    @Override
    public List<MuseumListVo> museumList(String userId) {
        List<UserCollection> list = list(Wrappers.<UserCollection>lambdaQuery().eq(UserCollection::getUserId, userId).eq(UserCollection::getIsExist, 1)
                .eq(UserCollection::getIsMuseum, 1));
        if (list.isEmpty()) {
            return new ArrayList<>();
        }
        List<MuseumListVo> museumList = new ArrayList<>();

        for (UserCollection userCollection : list) {
            MuseumListVo museumListVo = new MuseumListVo();
            museumListVo.setMyGoodsId(userCollection.getId());
            museumListVo.setGoodsId(userCollection.getCollectionId());
            museumListVo.setMediaUrl(mediaService.initMediaVos(userCollection.getCollectionId(), COLLECTION_MODEL_TYPE).get(0).getMediaUrl());
            List<MediaVo> thumbnailImgMediaVos = mediaService.thumbnailImgMediaVos(userCollection.getCollectionId(), COLLECTION_MODEL_TYPE);
            List<MediaVo> threeDimensionalMediaVos = mediaService.threeDimensionalMediaVos(userCollection.getCollectionId(), COLLECTION_MODEL_TYPE);
            museumListVo.setThumbnailImg(thumbnailImgMediaVos.size()>0?thumbnailImgMediaVos.get(0).getMediaUrl():"");
            museumListVo.setThreeDimensionalImg(threeDimensionalMediaVos.size()>0?threeDimensionalMediaVos.get(0).getMediaUrl():"");
            museumList.add(museumListVo);
        }
        return museumList;

    }
}
