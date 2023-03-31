package cn.mirakyux.wx_cp_bot.core.cp.model;

import cn.mirakyux.wx_cp_bot.core.cp.converter.CDataConverter;
import cn.mirakyux.wx_cp_bot.core.cp.converter.EventConverter;
import cn.mirakyux.wx_cp_bot.core.cp.converter.MsgTypeConverter;
import cn.mirakyux.wx_cp_bot.core.cp.enumerate.EventEnum;
import cn.mirakyux.wx_cp_bot.core.cp.enumerate.MsgTypeEnum;
import cn.mirakyux.wx_cp_bot.utils.XStreamTransformer;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * WxCpMessage
 *
 * @author mirakyux
 * @since 2023.03.24
 */
@Data
@Slf4j
@XStreamAlias("xml")
public class WxCpInXmlMessage implements Serializable {
    private static final long serialVersionUID = 10000L;

    /**
     * 企业微信CorpID
     */
    @XStreamAlias("ToUserName")
    @XStreamConverter(value = CDataConverter.class)
    private String toUserName;

    /**
     * 成员UserID
     */
    @XStreamAlias("FromUserName")
    @XStreamConverter(value = CDataConverter.class)
    private String fromUserName;

    /**
     * 消息创建时间（整型）
     */
    @XStreamAlias("CreateTime")
    private Long createTime;

    /**
     * 消息类型, 只解析text和image, 别的不做处理
     * <ul>
     *     <li>text</li>
     *     <li>image</li>
     * </ul>
     */
    @XStreamAlias("MsgType")
    @XStreamConverter(value = MsgTypeConverter.class)
    private MsgTypeEnum msgType;

    /**
     * 文本消息内容
     */
    @XStreamAlias("Content")
    @XStreamConverter(value = CDataConverter.class)
    private String content;

    /**
     * 消息id，64位整型
     */
    @XStreamAlias("MsgId")
    private Long msgId;

    /**
     * 企业应用的id，整型。可在应用的设置页面查看
     */
    @XStreamAlias("AgentID")
    private String agentId;

    /**
     * 图片链接
     */
    @XStreamAlias("PicUrl")
    @XStreamConverter(value = CDataConverter.class)
    private String picUrl;

    /**
     * 事件类型, 只解析subscribe和unsubscribe, 别的先不做处理
     * <ul>
     *     <li>subscribe</li>
     *     <li>unsubscribe</li>
     * </ul>
     */
    @XStreamAlias("Event")
    @XStreamConverter(value = EventConverter.class)
    private EventEnum event;

    public static WxCpInXmlMessage fromXml(String xml) {
        return (WxCpInXmlMessage) XStreamTransformer.of(WxCpInXmlMessage.class).fromXML(xml);
    }
}
