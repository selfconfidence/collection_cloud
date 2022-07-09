package com.manyun.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageHelper;
import com.manyun.business.domain.dto.MsgCommDto;
import com.manyun.business.domain.dto.MsgThisDto;
import com.manyun.business.domain.entity.Msg;
import com.manyun.business.domain.vo.MsgVo;
import com.manyun.business.mapper.MsgMapper;
import com.manyun.business.service.IMsgService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.manyun.common.core.domain.Builder;
import com.manyun.common.core.web.page.PageQuery;
import com.manyun.common.core.web.page.TableDataInfo;
import com.manyun.common.core.web.page.TableDataInfoUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.manyun.common.core.enums.MsgTypeEnum.NO_OPEN_MSG;
import static com.manyun.common.core.enums.MsgTypeEnum.OK_OPEN_MSG;

/**
 * <p>
 * 用户消息 服务实现类
 * </p>
 *
 * @author yanwei
 * @since 2022-06-17
 */
@Service
public class MsgServiceImpl extends ServiceImpl<MsgMapper, Msg> implements IMsgService {






    /**
     * 保存自己的消息,仅自己可见
     */
    @Override
    public void saveMsgThis(MsgThisDto msgThisDto){
     saveMsg(msgThisDto.getUserId(), NO_OPEN_MSG.getCode(), msgThisDto.getMsgForm(),msgThisDto.getMsgTitle());
    }

    /**
     * 保存通用得系统消息,全平台可见
     */
    @Override
    public void saveCommMsg(MsgCommDto msgCommDto){
        saveMsg(null, OK_OPEN_MSG.getCode(),msgCommDto.getMsgForm(),msgCommDto.getMsgTitle());

    }


    /**
     * 保存系统消息
     * @param
     * @return
     */
    private void saveMsg(String userId,Integer msgType,String msgForm,String msgTitle){
        String snowflakeNextIdStr = IdUtil.getSnowflakeNextIdStr();
        Msg msg = Builder.of(Msg::new).build();
        msg.setUserId(userId);
        msg.setId(snowflakeNextIdStr);
        msg.setMsgType(msgType);
        msg.setMsgForm(msgForm);
        msg.setMsgTitle(msgTitle);
        msg.createD(StrUtil.isBlank(userId) ? snowflakeNextIdStr : userId );
        save(msg);
    }


    @Override
    public TableDataInfo<MsgVo> pageMsgList(PageQuery pageQuery) {
        PageHelper.startPage(pageQuery.getPageNum(), pageQuery.getPageSize());
        List<Msg> msgList =
                list(Wrappers.<Msg>lambdaQuery().eq(Msg::getMsgType, OK_OPEN_MSG.getCode()).orderByDesc(Msg::getCreatedTime));
        return TableDataInfoUtil.pageTableDataInfo(msgList.parallelStream().map(this::initMsgVo).collect(Collectors.toList()), msgList);
    }

    @Override
    public TableDataInfo<MsgVo> pageMsgThisList(String userId, PageQuery pageQuery) {
        PageHelper.startPage(pageQuery.getPageNum(), pageQuery.getPageSize());
        List<Msg> msgList =
                list(Wrappers.<Msg>lambdaQuery().eq(Msg::getMsgType, OK_OPEN_MSG.getCode()).eq(Msg::getUserId, userId).orderByDesc(Msg::getCreatedTime));
        return TableDataInfoUtil.pageTableDataInfo(msgList.parallelStream().map(this::initMsgVo).collect(Collectors.toList()), msgList);
    }

    private MsgVo initMsgVo(Msg msg) {
        MsgVo msgVo = Builder.of(MsgVo::new).build();
        BeanUtil.copyProperties(msgVo, msgVo);
        return msgVo;
    }
}
