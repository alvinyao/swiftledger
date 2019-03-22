[![Build Status](https://travis-ci.org/Aurorasic/swiftledger.svg?branch=master)](https://travis-ci.org/Aurorasic/swiftledger)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=com.higgs.trust%3Aparent&metric=alert_status)](https://sonarcloud.io/dashboard?id=com.higgs.trust%3Aparent)
# 欢迎来到 SWIFTLEDGER

## SWIFTLEDGER是什么？
SWIFTLEDGER为行业用户提供牢固可靠的信任基础，为开展业务协同提供高效一致的共识达成。
其主要模块如下： 
1. common：配置文件读取、数据库访问、自定义路由等；
2. consensus：[协议层](https://github.com/Aurorasic/TRUST/wiki/%E5%8D%8F%E8%AE%AE%E5%B1%82%E6%B5%81%E7%A8%8B)、[共识](https://github.com/Aurorasic/TRUST/wiki/%E5%85%B1%E8%AF%86%E5%B1%82%E6%B5%81%E7%A8%8B)；
3. [contract](https://github.com/Aurorasic/TRUST/wiki/%E6%99%BA%E8%83%BD%E5%90%88%E7%BA%A6)：智能合约；
4. [rs](https://github.com/Aurorasic/TRUST/wiki/RS%E5%A4%84%E7%90%86%E6%B5%81%E7%A8%8B)：业务定制、交易组装和下发；
5. [slave](https://github.com/Aurorasic/TRUST/wiki/slave%E5%A4%84%E7%90%86%E6%B5%81%E7%A8%8B)：package组装和处理、区块生成。
## SWIFTLEDGER提供什么功能？
[^_^]:![](https://github.com/PrimeBlockCAS/TRUST/wiki/images/TP1.png)
* **成员注册和校验** - 成员注册提供新节点加入已有联盟的功能，待已有联盟校验成功并达成一致时方可加入成功。
* **交易管理** - 交易管理用于管理各种业务订单的接入和封装。
* **投票管理** - 投票管理用于管理具体业务的投票参与者与规则和结果的统计。
* **账务管理** - trust提供UTXO和余额两种账户模型，根据不同业务选择合适的模型或两种模型一起使用皆可。
* **CA管理** - CA管理用于统一管理各节点的公钥注册、查询、更新、注销等功能。
* **共识策略管理** - trust提供BFT和CFT两类共识策略的支持，其实现分别为copycat和BFT-SMaRt，两种算法可随意选择其中一种。
* **智能合约** - 智能合约是一段可以执行的计算机程序代码，SWIFTLEDGER的智能合约使用jvm作为执行环境，javascript和java作为合约语言对系统提供支持。
* **区块链浏览器** - 区块链浏览器提供可视化的区块、交易、UTXO、账户、合约等查询功能。  
## 快速开始
[快速开始](https://github.com/Aurorasic/TRUST/wiki/%E5%BF%AB%E9%80%9F%E5%BC%80%E5%A7%8B)
## Wiki
更多详细资料，欢迎参见[Wiki](https://github.com/Aurorasic/TRUST/wiki)
