
package cn.mirakyux.wx_cp_bot.core.openai.model;

import cn.mirakyux.wx_cp_bot.core.openai.enumerate.ErrorCode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Error
 *
 * @author mirakyux
 * @since 2023.03.29
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Error {
    @JsonDeserialize(converter = ErrorCode.ErrorCodeConverter.class)
    private ErrorCode code;

    private String message;

    private String param;

    private String type;
}
