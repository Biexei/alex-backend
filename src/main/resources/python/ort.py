from allpairspy import AllPairs
import yaml
import json
from redis import Redis
import sys

# 根据正交法生成用例
try:
    key = sys.argv[1]
    # with open(r"src\main\resources\application.yml", "r") as f:
    #     content = yaml.load(f, Loader=yaml.FullLoader)
    # redis_info = content["spring"]["redis"]
    # host = redis_info["host"]
    # port = redis_info["port"]
    host = '127.0.0.1'
    port = '6379'
    redis_conn = Redis(host, port)

    field = redis_conn.get(key)
    params = json.loads(field)

    result = []
    for i, pairs in enumerate(AllPairs(params)):
        result.append(pairs)
    redis_conn.delete(key)
    redis_conn.set(key, json.dumps(result, ensure_ascii=False))
    redis_conn.close()
    print("")
except Exception as e:
    print(e)
