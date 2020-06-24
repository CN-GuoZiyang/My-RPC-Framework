package top.guoziyang.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.guoziyang.rpc.annotation.Service;
import top.guoziyang.rpc.api.HelloObject;
import top.guoziyang.rpc.api.HelloService;

/**
 * @author ziyang
 */
@Service
public class HelloServiceImpl implements HelloService {

    private static final Logger logger = LoggerFactory.getLogger(HelloServiceImpl.class);

    @Override
    public String hello(HelloObject object) {
        logger.info("接收到消息：{}", object.getMessage());
        return "这是Impl1方法";
    }

}
