local stockKey = KEYS[1]
local userKey  = KEYS[2]
local count    = tonumber(ARGV[1])
local ttl      = tonumber(ARGV[2])

local stock = tonumber(redis.call('GET', stockKey) or -1)
if stock < count then
    return -1
end

if redis.call('EXISTS', userKey) == 1 then
    return -2
end

redis.call('DECRBY', stockKey, count)
redis.call('SETEX', userKey, ttl, '1')
return stock - count
