package cn.mirakyux.wx_cp_bot.core.cp.converter;

import cn.mirakyux.wx_cp_bot.core.cp.enumerate.EventEnum;
import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;

/**
 * MsgTypeConverter
 *
 * @author mirakyux
 * @since 2023.03.24
 */
public class EventConverter extends AbstractSingleValueConverter {
    @Override
    public boolean canConvert(Class type) {
        return type == EventEnum.class;
    }

    public Object fromString(String str) {
        return EventEnum.of(str);
    }

    public String toString(Object obj) {
        return "<![CDATA[" + super.toString(obj) + "]]>";
    }
}
