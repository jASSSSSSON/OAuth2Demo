# OAuth2 基础示例工程

## 共包含2个启动器，请使用`auth`后缀配置文件启动签发服务器，`resource`后缀文件启动资源服务器
### auth
    令牌签发服务器，接口为ip:50201/oauth/** 测试示例仅开启password和刷新模式，请使用http协议
    发送数据至对应接口获取令牌，数据类型如下
    {
        grant_type:password #授权方式
        username:admin
        password:admin
        client_id:myapp
        client_secret:test
        scope:all #作用域
    }
    返回类型如下
    {
        "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsicmVzb3VyY2UwMSJdLCJ1c2VyX25hbWUiOiJhZG1pbiIsInNjb3BlIjpbImFsbCJdLCJleHAiOjE2NDczMTcwNjEsImp0aSI6ImI1N2U0NDUyLTc4ZmUtNGI4Mi1iZGUwLWJjNmIwODdiMjg1ZiIsImNsaWVudF9pZCI6Im15YXBwIiwibyI6InRlc3QifQ.SwZWvx7DAB3MI1JP_gSXVLWo-zmXBMz-lBC2f24toKn8sq1UonCLOsxJSi-Of9E60D2IF53dt8AJZtkmjaQZCYnYEO6dWzKGb-B5cuXl8NFe4d3bB6qRX2XXQsFcvSxFK4XvFbcKnXpozytg2AGQwYGYm6n_LXMxMQDo8zGGZ5mFrLnNhHy_DOYQV-gJ329qSoeo8DTDKgNUuUzFfny3t9LzUswiWJ-Uw1-9UjGmu5bS5YEk_9yRb0ucBiSnNQUGEdbtkGY6iOUku58iaxAW4e0yR9_ttl9AdUqLbdney1eT-BjOVC3MK_wShFjXtxFraPpG0o-J_U27MD0gvkMgZw",
        "token_type": "bearer",
        "refresh_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsicmVzb3VyY2UwMSJdLCJ1c2VyX25hbWUiOiJhZG1pbiIsInNjb3BlIjpbImFsbCJdLCJhdGkiOiJiNTdlNDQ1Mi03OGZlLTRiODItYmRlMC1iYzZiMDg3YjI4NWYiLCJleHAiOjE2NDczMTgxNzIsImp0aSI6IjBlMzg2NDlkLWZmZDktNDMxNy05NDUxLWM0NTQzZTM1OThkMSIsImNsaWVudF9pZCI6Im15YXBwIiwibyI6InRlc3QifQ.gmpx1T3XjgROviE0u5bJvnHs1cl28mXj9O2qWgjrGfKW9plnr16NjhnJQGmcb6YcGibaBS7xpcIidi-rjs0ELhG9hDFi4lPHLYdfrDbdOdLOwICC8pcmNRcZR6HUNJaKuhEKADDWi1Rl80uSKlQ_LQOweypS7DgylGtj2StC7MNIm9zIk1IqVEvf0imcyJiS5ANrZfQZhTSIngTZAr4jDU4Ob0NXJhn71CdG8qPrRSwO0hS6tijP6hJrPdjEhU0Hb-ZOYqwHNtk9GtMK_2YK3L-UDJfqPn1lJhVBNLCjzDZU8wLiS_oUw_svixrmAJsQbEAHHbPNo_1fCdxj88hn1g",
        "expires_in": 6665,
        "scope": "all",
        "jti": "b57e4452-78fe-4b82-bde0-bc6b087b285f"
    }
### resource
    请将获取的令牌保留，资源服务器预留两个测试接口，路径分别为ip:50301/testResourceApi/testApi1  (授权限制) 
    ip:50301/testResourceApi/testApi2  (无授权限制)
    请将获取令牌放置于http请求头部Authorization属性中，比如
    {
        "Authorization": "bearer ${access_token}"
    }

#### 注意，测试用`RSA`证书请勿用于生产环境