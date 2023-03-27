package cn.mirakyux.wx_cp_bot.core.cp.converter;

import com.thoughtworks.xstream.converters.basic.StringConverter;

/**
 * CDataConverter
 *
 * @author mirakyux
 * @since 2023.03.24
 */
public class CDataConverter extends StringConverter {
    @Override
    public String toString(Object obj) {
        return "<![CDATA[" + super.toString(obj) + "]]>";
    }
}
