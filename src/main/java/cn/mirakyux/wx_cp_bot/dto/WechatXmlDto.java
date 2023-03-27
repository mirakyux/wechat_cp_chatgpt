package cn.mirakyux.wx_cp_bot.dto;

import lombok.Data;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * InvokeController
 *
 * @author mirakyux
 * @since 2023.03.20
 */
@Data
@XmlRootElement(name = "xml")
public class WechatXmlDto implements Serializable {
    private static final long serialVersionUID = 10002L;

    @XmlElement(name = "ToUserName")
    private String ToUserName;

    @XmlElement(name = "AgentID")
    private String AgentID;

    @XmlElement(name = "Encrypt")
    private String Encrypt;
}
