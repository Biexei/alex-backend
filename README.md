开源一款接口自动化平台，支持自动生成测试用例、MOCK等功能。平台其实已经写好蛮久了，之前一直在coding托管，最近迁移至github公共仓库
## 1.地址
演示地址：http://42.194.187.183:7778/#/    youke/admini0
开源地址：https://github.com/Biexei
Testerhome：https://testerhome.com/topics/35203

## 2.运行环境
基于vue+elemui+java+mysql
jdk1.8、nodejs12.19.0、mysql8、python3.6.5、redis3.2.100

## 3.功能简介
1. 用例支持har/json/yaml/excel/csv导入
2. 支持根据等价类，边界值，正交法，笛卡尔积自动生成测试用例，且支持动态数据，数据来源支持数据库
3. 测试套件支持并行，大幅提升测试效率，生成的测试报告美观，报表数据丰富
4. 定时任务支持动态启停，配置
5. 支持在线配置代理
6. 动态mock服务，无需启停服务，且支持请求转发
7. 丰富的断言策略，目前已支持jsonpath,xpath,header,code,响应耗时
8. 支持接口间数据依赖，方便测试流程
9. 数据中心支持接口依赖，sql依赖，反射方法。解决数据依赖，数据加密的痛点
10. 稳定性测试

## 4.主要功能
### 4.1 测试数据
目前支持了四种测试数据的来源，第一种是基于jsonpath从接口返回值提取数据，第二种是通过SQL查询语句，第三种是基于内置的反射方法，第四种是基于固定值。采取${xxx}语法表达式提取
* 基于jsonpath从接口返回值提取数据
![](/uploads/photo/2023/a1ec0e88-68a4-48e7-a316-f6f01fffb645.png!large)
* 通过SQL查询语句
![](/uploads/photo/2023/e034a861-4a3c-4390-aaaa-99d96b65907c.png!large)
* 基于内置的反射方法
![](/uploads/photo/2023/c640d9d2-8094-4f0b-902e-b3a97d5a998d.png!large)
* 基于固定值
![](/uploads/photo/2023/4996d59e-c9c3-4628-9d9e-e7c1a40a9d0c.png!large)

### 4.2 自动生成测试用例
自定义了一套生成用例的约束，根据约束填写模板，将会产生标准的测试用例文件，而后再导入至平台即可
* 以自动生成用户注册接口测试用例为例。支持生成正交测试用例，也可以生成全量的笛卡尔积测试用例。同时为了保证用例的可复用性，支持生成动态测试数据（如用户注册生成的用户名具有唯一性，这就需要保证每次生成的用户名都是唯一的）
![](/uploads/photo/2023/04e4c132-f6e0-40d5-8411-d00d41e8a6ba.png!large)
  * 生成的用例如下
![](/uploads/photo/2023/22c3e598-0cb4-4811-9d19-1d92a444dbd3.png!large)

### 4.3 用例执行
支持串行、并行，两种策略可自行保证用例的执行顺序or高效运行。
![](/uploads/photo/2023/0e1d9e29-376c-422f-92b3-ca0f9887b194.png!large)
以及还算丰富的测试报告
![](/uploads/photo/2023/037c0bc1-3245-4b27-b2d6-4b680149b644.png!large)
![](/uploads/photo/2023/45e604a9-da1a-4c50-b8de-0bcb1c6ffe58.png!large)

### 4.4 Mock
mock基于java的mock-server。目前支持多节点（可方便不同人员调试相同接口地址），以及丰富的命中策略
![](/uploads/photo/2023/32b73c3b-f580-4766-b685-8627bf1e6b3d.png!large)
![](/uploads/photo/2023/ae3b0f92-0f72-45f2-a8e7-334601b1bf27.png!large)
![](/uploads/photo/2023/6dc43b1e-4480-4458-a5ae-469db2eceb7e.png!large)
![](/uploads/photo/2023/cbc35491-9588-4900-860a-8a3e0a5fe7bb.png!large)

### 4.4 常见问题
#### 一. 如何保证用例之间的数据关联关系？
---

*目前可采取两种方式解决用例间的数据依赖问题*
**1.数据中心-接口依赖创建**
* 举个栗子，添加用户需要用到登录成功返回的token字段
* 第一步先创建一个登录成功的接口
![](/uploads/photo/2023/ac246fae-5229-4c43-8436-a34a40ae54d7.png!large)
* 第二步在数据中心-接口依赖创建选择该接口，并提取token字段
![](/uploads/photo/2023/81a30b9d-6efe-427b-99ba-b2413b12ea7b.png!large)
* 第三步通过${token}引用
![](/uploads/photo/2023/5b437547-9f41-49af-9999-46f682d70344.png!large)

---
---
**2.配置前置用例**
* 第一步先创建一个登录成功的接口，并为其配置响应数据缓存
![](/uploads/photo/2023/2e794ce0-fb99-49c4-afcb-22079883bf68.png!large)
* 第二步为需要关联的用例选择前置用例
![](/uploads/photo/2023/187bb0d4-9adf-4c67-afbc-a3003b7ca3e2.png!large)
* 第三步通过#{token}引用
![](/uploads/photo/2023/11a926c7-8383-472a-ae7c-a52fc1c5470f.png!large)

---
存在两种方式主要是为了减少重复请求次数，以及确保数据的一致性，如需要前置接口返回的多个数据，通过第一种方式则会在需要数据的时候就请求一次，而通过第二种方式则无论需要前置接口的多少个请求数据，都只请求一次

#### 二. 数据加密如何处理？
---

通过反射方法的形式，可自定义方法处理加密（统一处理类：org.alex.platform.common.InvokeCenter），目前仅提供md5哈希
![](/uploads/photo/2023/2367728d-a1cf-4d08-b4eb-80c43a8a12ae.png!large)
![](/uploads/photo/2023/cefd2764-b20f-481d-8c05-7fcd21814462.png!large)

---
---

#### 三. 如从TEST环境切换到STG环境，是否需要编写多套用例？
不需要。系统在创建项目时已强制为每个环境配置host和domain，目前支持5种环境。即使在用例中引用了数据库也无需担心环境问题
![](/uploads/photo/2023/afbad30f-6d03-4266-a788-f886fff90880.png!large)
![](/uploads/photo/2023/c0f5cfb3-90dd-4913-92ea-7e2e3f3f4f66.png!large)
在执行测试套件时可指定运行环境
![](/uploads/photo/2023/d02bc6e4-d917-4896-98f3-7bbe47d24b72.png!large)

---
---

#### 四. mock是否支持请求转发？
支持。举个例子，第一步：配置请求转发
![](/uploads/photo/2023/eee39bb8-d014-41e6-9a72-f1f4b26b1286.png!large)
第二步：试验。可以看到/user/list接口已经转发至真实接口
![](/uploads/photo/2023/0735c3a9-e208-471d-a17a-fbdf08174841.png!large)

---

