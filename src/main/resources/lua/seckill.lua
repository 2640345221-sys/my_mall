local stockKey = KEYS[1]
local userKey = KEYS[2]
local count = tonumber(ARGV[1])
local ttl = tonumber(ARGV[2])

local stock = redis.call('GET', stockKey)
if not stock or tonumber(stock) < count then
    return -1
end

local exists = redis.call('EXISTS', userKey)
if exists == 1 then
    return -2
end

redis.call('DECRBY', stockKey, count)
redis.call('SET', userKey, '1', 'EX', ttl)

return tonumber(redis.call('GET', stockKey))
