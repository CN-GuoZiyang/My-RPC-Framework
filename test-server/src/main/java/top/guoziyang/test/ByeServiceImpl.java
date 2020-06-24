package top.guoziyang.test;

import top.guoziyang.rpc.annotation.Service;
import top.guoziyang.rpc.api.ByeService;

/**
 * @author ziyang
 */
@Service
public class ByeServiceImpl implements ByeService {

    @Override
    public String bye(String name) {
        return "bye, " + name;
    }
}
