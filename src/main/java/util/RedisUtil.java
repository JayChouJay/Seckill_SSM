package util;

import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {
    @Autowired
    RedisTemplate<String, Object> template;

    public boolean set(String key, Object value) {
        try {
            template.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean setex(String key, Object value, long time) {
        try {
            if (time > 0)
                template.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            else
                template.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Object get(String key) {
        if (StrUtil.isEmpty(key)) {
            return null;
        }
        return template.opsForValue().get(key);
    }


    final static String LUA_SCRIPT;
    final static String LUA_SCRIPT2;

    static {

        // 减库存脚本
        StringBuilder sb = new StringBuilder();
        sb.append("if (redis.call('exists', KEYS[1]) == 1) then");
        sb.append("    local stock = tonumber(redis.call('get', KEYS[1]));");
        sb.append("    if (stock == -1) then");
        sb.append("        return -1");
        sb.append("    end;");
        sb.append("    if (stock > 0) then");
        sb.append("        redis.call('decr', KEYS[1]);");
        sb.append("        return stock - 1;");
        sb.append("    end;");
        sb.append("    return -1;");
        sb.append("end;");
        sb.append("return -2;");

        LUA_SCRIPT = sb.toString();


        // 加库存脚本
        StringBuilder sb2 = new StringBuilder();
        sb2.append("if (redis.call('exists', KEYS[1]) == 1) then");
        sb2.append("    local stock = tonumber(redis.call('get', KEYS[1]));");
        sb2.append("    if (stock == -1) then");
        sb2.append("        redis.call('set', KEYS[1],1)");
        sb2.append("    end;");
        sb2.append("    if (stock == 0) then");
        sb2.append("        redis.call('set', KEYS[1],1)");
        sb2.append("    end;");
        sb2.append("    if (stock > 0) then");
        sb2.append("        redis.call('incr', KEYS[1]);");
        sb2.append("        return stock + 1;");
        sb2.append("    end;");
        sb2.append("end;");
        sb2.append("return -2;");

        LUA_SCRIPT2 = sb2.toString();
    }

    /**
     * lua脚本加库存
     */
    public Integer stockAdd(String key) {
        // 脚本里的KEYS参数
        final List<String> keys = new ArrayList<String>();
        keys.add(key);
        // 脚本里的ARGV参数
        final List<String> args = new ArrayList<String>();

        Integer result = template.execute(new RedisCallback<Integer>() {

            public Integer doInRedis(RedisConnection connection) throws DataAccessException {
                Object nativeConnection = connection.getNativeConnection();
                if (nativeConnection instanceof Jedis) {
                    Object temp = ((Jedis) nativeConnection).eval(LUA_SCRIPT2, keys, args);
                    return Integer.valueOf(String.valueOf(temp));
                }

                return null;
            }
        });
        return result;
    }

    /**
     * lua脚本减库存
     */
    public Integer stockDecr(String key) {

        // 初始化减库存lua脚本
        // -1 库存不足
        // -2 不存在
        // 整数是正常操作，减库存成功

        // 脚本里的KEYS参数
        final List<String> keys = new ArrayList<String>();
        keys.add(key);
        // 脚本里的ARGV参数
        final List<String> args = new ArrayList<String>();

        Integer result = template.execute(new RedisCallback<Integer>() {

            public Integer doInRedis(RedisConnection connection) throws DataAccessException {
                Object nativeConnection = connection.getNativeConnection();
                // 集群模式和单机模式虽然执行脚本的方法一样，但是没有共同的接口，所以只能分开执行
                // redis集群模式，执行脚本
//                if (nativeConnection instanceof JedisCluster) {
//                    return (Integer) ((JedisCluster) nativeConnection).eval(LUA_SCRIPT, keys, args);
//                }

                // redis单机模式，执行脚本
//                else if (nativeConnection instanceof Jedis) {
                if (nativeConnection instanceof Jedis) {
                    Object temp = ((Jedis) nativeConnection).eval(LUA_SCRIPT, keys, args);
                    System.out.println(" =========================================================" + temp);
                    return Integer.valueOf(String.valueOf(temp));
                }

                return null;
            }
        });
        return result;
    }
}
