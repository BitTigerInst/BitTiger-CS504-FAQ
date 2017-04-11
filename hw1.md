# 实现 RunningInformationAnalysisService

功能要求：
每一个RunningInformation都包含
```
runningId,
latitude,
longitude,
runningDistance,
totalRunningTime,
heartRate,
Timestamp,
userInfo
```

其中`userInfo`包含了
```
username,
address
```

`HearRate`初始值为0，要求当数据插入数据库时候生成一个60-200中间的随机数

## 要求
1. `RunningInformation` 存储在`RUNNING_ANALYSIS` 表里

2. 要求设计REST API 能够返回符合如下条件的JSON Response:
```
{
    "runningId": "7c08973d-bed4-4cbd-9c28-9282a02a6032",
    "totalRunningTime": 2000,
    “heartRate”: 75, 
    “userId”: 1,
    "userName": "Ross",
    "userAddress": "504 CS Street, Mountain View, CA 88888",
    "healthWarningLevel": HIGH
}
```

3. 要求返回结果按照`healthWarningLevel`从高到低进行排列，每页显示两个数据

>**提示**
>`HealthWarningLevel`由`heartRate`大小决定
> - 如果heartRate>=60 && <=75 healthWarningLevel = “LOW”
> - 如果heartRate>75 && <=120 healthWarningLevel = “NORMAL”
> - 如果heartRate>120 healthWarningLevel = “HIGH”

4. REST API能支持delete by running ID 操作

5. 数据库要求用MySQL实现

## JSON输入
```
[
  {
    "runningId": "7c08973d-bed4-4cbd-9c28-9282a02a6032",
    "latitude": "38.9093216",
    "longitude": "-77.0036435",
    "runningDistance": "39492",
    "totalRunningTime": "2139.25",
    "heartRate": 0,
    "timestamp": "2017-04-01T18:50:35Z",
    "userInfo": {
      "username": "ross0",
      "address": "504 CS Street, Mountain View, CA 88888"
    }
  },
  {
    "runningId": "07e8db69-99f2-4fe2-b65a-52fbbdf8c32c",
    "latitude": "39.927434",
    "longitude": "-76.635816",
    "runningDistance": "1235",
    "totalRunningTime": "3011.23",
    "heartRate": 0,
    "timestamp": "2017-04-01T18:50:35Z",
    "userInfo": {
      "username": "ross1",
      "address": "504 CS Street, Mountain View, CA 88888"
    }
  },
  {
    "runningId": "2f3c321b-d239-43d6-8fe0-c035ecdff232",
    "latitude": "40.083824",
    "longitude": "-76.098019",
    "runningDistance": "23567",
    "totalRunningTime": "85431.23",
    "heartRate": 0,
    "timestamp": "2017-04-01T18:50:35Z",
    "userInfo": {
      "username": "ross2",
      "address": "504 CS Street, Mountain View, CA 88888"
    }
  },
  {
    "runningId": "28810a26-25e6-4680-8baf-59bb07c4aee0",
    "latitude": "42.957466",
    "longitude": "-76.344201",
    "runningDistance": "11135",
    "totalRunningTime": "98965",
    "heartRate": 0,
    "timestamp": "2017-04-01T18:50:35Z",
    "userInfo": {
      "username": "ross3",
      "address": "504 CS Street, Mountain View, CA 88888"
    }
  },
  {
    "runningId": "fb0b4725-ac25-4812-b425-d43a18c958bb",
    "latitude": "38.5783821",
    "longitude": "-77.3242436",
    "runningDistance": "231",
    "totalRunningTime": "123",
    "heartRate": 0,
    "timestamp": "2017-04-01T18:50:35Z",
    "userInfo": {
      "username": "ross4",
      "address": "504 CS Street, Mountain View, CA 88888"
    }
  },
  {
    "runningId": "35be446c-9ed1-4e3c-a400-ee59bd0b6872",
    "latitude": "42.375786",
    "longitude": "-76.870872",
    "runningDistance": "0",
    "totalRunningTime": "0",
    "heartRate": 0,
    "timestamp": "2017-04-01T18:50:35Z",
    "userInfo": {
      "username": "ross5",
      "address": "504 CS Street, Mountain View, CA 88888"
    }
  },
  {
    "runningId": "15dfe2b9-e097-4899-bcb2-e0e8e72416ad",
    "latitude": "40.2230391039276",
    "longitude": "-76.0626631118454",
    "runningDistance": "0.1",
    "totalRunningTime": "0.1",
    "heartRate": 0,
    "timestamp": "2017-04-01T18:50:35Z",
    "userInfo": {
      "username": "ross6",
      "address": "504 CS Street, Mountain View, CA 88888"
    }
  }
]
```
