package top.guoziyang.rpc.exception;

/**
 * 序列化异常
 *
 * @author ziyang
 */
public class SerializeException extends RuntimeException {
    public SerializeException(String msg) {
        super(msg);
    }
}
