package cn.mypandora.springboot.core.base;

import cn.mypandora.springboot.core.enums.ResultEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Result
 * 返回结果包装类。
 *
 * @author hankaibo
 * @date 2019/6/19
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> implements Serializable {

    private static final long serialVersionUID = -7870885617136909303L;

    /**
     * 状态码
     */
    private int code;

    /**
     * 成功与否
     */
    private boolean success;

    /**
     * 状态说明
     */
    private String message;

    /**
     * 返回的数据
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    /**
     * 判断是否是成功结果。
     *
     * @return 判断结果
     */
    public boolean isSuccess() {
        return ResultEnum.SUCCESS.getCode() == this.code;
    }
}
