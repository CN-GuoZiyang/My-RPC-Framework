package top.guoziyang.rpc.entity;

import lombok.Data;
import top.guoziyang.rpc.enumeration.ResponseCode;

import java.io.Serializable;

/**
 * 提供者执行完成或出错后向消费者返回的结果对象
 *
 * @author ziyang
 */
@Data
public class RpcResponse<T> implements Serializable {

    /**
     * 响应状态码
     */
    private Integer statusCode;
    /**
     * 响应状态补充信息
     */
    private String message;
    /**
     * 响应数据
     */
    private T data;

    public RpcResponse() {
    }

    public static <T> RpcResponse<T> success(T data) {
        RpcResponse<T> response = new RpcResponse<>();
        response.setStatusCode(ResponseCode.SUCCESS.getCode());
        response.setData(data);
        return response;
    }

    public static <T> RpcResponse<T> fail(ResponseCode code) {
        RpcResponse<T> response = new RpcResponse<>();
        response.setStatusCode(code.getCode());
        response.setMessage(code.getMessage());
        return response;
    }

}
