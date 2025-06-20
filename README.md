# approximation-algorithm

#### 介绍

这个代码是带次模惩罚的平行机客户订单调度问题的前后端实现

#### 项目描述

本项目以“带次模惩罚费用的平行机客户订单调度优化”为方向，研究在多台平行机的制造环境下如何有效分配和调度客户订单。次模惩罚费用机制的引入，使得调度模型可以更灵活地反映实际中的分段性惩罚需求——即拒绝订单惩罚不再是固定的，而是随着拒绝订单数量的增加而呈现递减趋势，从而更贴合现实特点。

订单调度主要用于优化生产资源的分配与利用，确保订单按时交付并尽可能减少生产成本。它广泛应用于各类资源调度场景，如流水线生产、机械加工、电子设备装配、航班调度、物流分配等，帮助企业提高生产效率和资源利用率。本项目能够为各行业实现更加灵活和精确的订单调度，有效地支持其提高交付效率、优化生产成本，提升市场竞争力，具有较高的实际应用价值。




#### 项目意义

在学术上，该项目进一步丰富了基于惩罚费用的调度优化问题的研究和分析方法。在应用层面，该项目具备显著的灵活性和适应性，不仅适用于生产调度，还可广泛应用于其他资源调度场景。例如，在数据处理中心，本模型可用于计算资源与数据处理任务的优化分配；在物流配送中，可提升运输工具和配送物品的配置效率；在航班调度中，可支持跑道和空中资源的高效分配。这些场景均可抽象为资源调度问题，因此本项目的模型和方法对广泛领域的资源优化问题都具有参考价值和应用潜力。

#### 目前研究

目前关于这个项目现有研究是设计了一个2近似算法，本方法将提升这个近似比

#### 方法介绍

1. 基于定理：存在一个最优解，其交货时间越大的越先加工，对订单进行排序处理，并构建线性规划

2. 基于Lovasz拓展的定义可以构造线性规划的最优解

3. 引入决策变量并基于该线性规划最优解对订单进行划分接收订单集合和拒绝订单集合

4. 论证得到的解不超过最优解的1.58倍

   主要流程如下：
![Image text](https://gitee.com/zxyldl888888/approximation-algorithm/raw/master/Snipaste_2025-06-17_14-12-36.jpg)


#### 软件如何使用

1.  不同的账号进去所使用的功能不同，对于管理员账号，可以看到下载导入模版进行查看导入模版规整自己要导入的数据，可以新建预测任务进行对订单任务排序，可以配置工厂的各项产能，其订单的各个部分的处理时间就是依据产能和数量得出的。普通账号就只能查看预测好的任务。
![Image text](https://gitee.com/zxyldl888888/approximation-algorithm/raw/master/login-admin.jpg)
![Image text](https://gitee.com/zxyldl888888/approximation-algorithm/raw/master/%E6%9F%A5%E7%9C%8B%E6%9C%BA%E5%99%A8%E4%BA%A7%E8%83%BD.jpg)
![Image text](https://gitee.com/zxyldl888888/approximation-algorithm/raw/master/loginuser.jpg)
2.  新建预测任务，第一步输入描述便于查找和看任务对应的日期和内容
![Image text](https://gitee.com/zxyldl888888/approximation-algorithm/raw/master/%E6%96%B0%E5%BB%BA%E9%A2%84%E6%B5%8B.jpg)
3.  新建完后，将订单输入按照导入模版整理后导入，预览看是否正确，正确则导入，错误则取消，整理excel表格后在进行导入
![Image text](https://gitee.com/zxyldl888888/approximation-algorithm/raw/master/%E5%AF%BC%E5%85%A5%E5%89%8D%E9%A2%84%E8%A7%88.jpg)
4.  导入成功后，点击开始预测，就是使用本项目设计的1.58近似算法开始对订单进行预测排序。
![Image text](https://gitee.com/zxyldl888888/approximation-algorithm/raw/master/%E7%82%B9%E5%87%BB%E5%BC%80%E5%A7%8B%E9%A2%84%E6%B5%8B.jpg)
5.  预测成功后，接受订单排序那可以滑动查看订单Id
![Image text](https://gitee.com/zxyldl888888/approximation-algorithm/raw/master/%E6%BB%91%E5%8A%A8%E5%B1%95%E7%A4%BA%E6%8E%A5%E8%AE%A2%E5%8D%95id.jpg)
6.  在操作那，可以点击详情，查看导入订单的详细记录。
![Image text](https://gitee.com/zxyldl888888/approximation-algorithm/raw/master/%E5%AF%BC%E5%85%A5%E8%AE%A2%E5%8D%95%E8%AF%A6%E6%83%85.jpg)

