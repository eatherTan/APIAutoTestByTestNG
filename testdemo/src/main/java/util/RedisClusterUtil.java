package util;

import org.apache.log4j.Logger;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;

/**
 * redis 集群连接，查询
 */
public class RedisClusterUtil {
    private static Logger logger = Logger.getLogger(RedisClusterUtil.class);
    private static JedisCluster jedisCluster;

    ExecutorService executorService;

    /**
     * @param redisNodes 集群结点
     * @return
     */
    public static JedisCluster getJedis(String redisNodes){
        init(redisNodes);
        return jedisCluster;
    }


    public static void init(String redisNodes){
        if(null == redisNodes||"".equals(redisNodes.trim())) {
            throw new RuntimeException("shardNodes is empty");
        }
        String[] addresses = redisNodes.split(",");
        if(null == addresses||addresses.length==0) {
            throw new RuntimeException("ip or port is empty");
        }
        Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
        for(String address: addresses){
            String[] addr = address.split(":");
            jedisClusterNodes.add(new HostAndPort(addr[0],Integer.valueOf(addr[1])));
        }
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        jedisCluster = new JedisCluster(jedisClusterNodes,poolConfig);
    }

    /**
     * 关闭连接
     */
    public void destory(){
        jedisCluster.close();
    }

    public static void main(String[] args) {
        String redisNodes = "172.22.12.14:6381,172.22.12.14:6382,172.22.12.14:6383";
        String[] addresses = redisNodes.split(",");
        System.out.println(addresses[0]);
        System.out.println(addresses[1]);
        System.out.println(addresses[2]);

    }
    /**
     * 判断key是否存在
     *
     * @param key
     * @return
     */
    public static boolean exists(String key) {
        if (null == key || key.trim().length() == 0) {
            return false;
        }
        JedisCluster getJedis = null;
        return false;
    }

    /**
     * 获取值
     * @param key
     * @return
     */
    public String get(String key){
        if (null == key || key.trim().length() == 0) {
            return null;
        }
        return jedisCluster.get(key);
    }

    public Map<String,String> hgetAll(String key){
        return jedisCluster.hgetAll(key);
    }
}
