--lua中的下标是从1开始的，所以KEY[1]是正确的。在lua中，数组的下标是从1开始的，而不是从0开始的。因此，如果你有一个包含多个元素的数组KEY，并且你想要访问第一个元素，你应该使用KEY[1]。
local key = KEYS[1] --获取注解上的key 也就是被限流方法的key

local limit = tonumber(ARGV[1]) --获取限流的次数
local currentLimit = tonumber(redis.call("get", key) or 0) --获取当前key的访问次数

--超过限流次数的话直接返回 0
if currentLimit + 1 > limit then
    return 0 --拒绝访问
else --否则的话自增1
    redis.call("incrby", key, 1)
    redis.call("expire", key, ARGV[2]) --设置过期时间为注解中指定的时间
    return currentLimit + 1
end