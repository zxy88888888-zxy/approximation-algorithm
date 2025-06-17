package com.order.predict.service;

import com.order.predict.config.PenaltyUtils;
import com.order.predict.domain.OrderData;
import com.order.predict.domain.ResultDTO;
import ilog.concert.*;
import ilog.cplex.IloCplex;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SchedulingService {

    public ResultDTO run158Algorithm(OrderData data) {
        try {
            long start = System.currentTimeMillis();

            int m = data.getM();
            int n = data.getN();
            double[][] p = data.getP();      // p[i][j] = 第j个订单在第i台机器的处理时间
            double[][] d = data.getD();      // d[j][0] = orderId, d[j][1] = 打包时间
            double[][] Pi = data.getPi();    // Pi[j][0] = orderId, Pi[j][1] = 违约惩罚

            // 提取 orderId 映射列表
            Long[] orderIdList = new Long[n];
            for (int i = 0; i < n; i++) {
                orderIdList[i] = (long) d[i][0];  // 假设 d 的第一列是 orderId
            }

            // 打包时间排序（降序），生成排序索引
            Integer[] sortedIndices = new Integer[n];
            for (int i = 0; i < n; i++) sortedIndices[i] = i;
            Arrays.sort(sortedIndices, (a, b) -> Double.compare(d[b][1], d[a][1]));

            // 根据排序后顺序构造新的 d、p、Pi
            double[] newD = new double[n];           // 打包时间
            double[][] newP = new double[m][n];      // p[i][j]
            double[] newPi = new double[n];          // 违约惩罚

            Long[] newOrderIdList = new Long[n];

            for (int newIdx = 0; newIdx < n; newIdx++) {
                int oldIdx = sortedIndices[newIdx];
                newD[newIdx] = d[oldIdx][1];
                newPi[newIdx] = Pi[oldIdx][1];
                newOrderIdList[newIdx] = orderIdList[oldIdx];
                for (int i = 0; i < m; i++) {
                    newP[i][newIdx] = p[oldIdx][i + 1];
                }
            }

            // CPLEX 初始化
            IloCplex cplex = new IloCplex();
            IloNumVar[] xz = cplex.numVarArray(n, 0, 1);
            IloNumVar[] z = cplex.numVarArray(n, 0, 1);
            IloNumVar maxProcessingTime = cplex.numVar(0, Double.MAX_VALUE);

            // 约束1：xz[j] + z[j] >= 1
            for (int j = 0; j < n; j++) {
                cplex.addGe(cplex.sum(xz[j], z[j]), 1);
            }

            // 约束2：最大处理时间 ≥ 每个订单的完成时间（处理 + 打包）
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    IloLinearNumExpr expr = cplex.linearNumExpr();
                    for (int k = 0; k <= j; k++) {
                        expr.addTerm(newP[i][k], xz[k]);
                    }
                    expr.addTerm(newD[j], xz[j]);
                    cplex.addGe(maxProcessingTime, expr);
                }
            }

            // 目标函数初始化
            double[] alpha = new double[n];
            Arrays.fill(alpha, 1.0);
            IloObjective objective = cplex.addMinimize();

            // 迭代更新 alpha
            int maxIterations = 10;
            double[] previousAlpha = new double[n];

            for (int iter = 0; iter < maxIterations; iter++) {
                IloLinearNumExpr objExpr = cplex.linearNumExpr();
                objExpr.addTerm(1.0, maxProcessingTime);
                for (int j = 0; j < n; j++) {
                    objExpr.addTerm(alpha[j], z[j]);
                }
                objective.setExpr(objExpr);

                if (!cplex.solve()) {
                    throw new RuntimeException("CPLEX 无法求解");
                }

                double[] zValues = cplex.getValues(z);
                alpha = PenaltyUtils.updateAlpha(zValues, newPi);

                if (Arrays.equals(previousAlpha, alpha)) break;
                previousAlpha = Arrays.copyOf(alpha, n);
            }

            // 获取解
            double[] xValues = cplex.getValues(xz);
            Integer[] finalSortedIdx = new Integer[n];
            for (int i = 0; i < n; i++) finalSortedIdx[i] = i;
            Arrays.sort(finalSortedIdx, (a, b) -> Double.compare(xValues[b], xValues[a]));

            // 枚举加入A过程
            Set<Integer> A = new HashSet<>();
            Set<Integer> R = new HashSet<>();
            for (int i = 0; i < n; i++) R.add(i);

            Set<Integer> bestA = new HashSet<>();
            Set<Integer> bestR = new HashSet<>(R);
            double minCost = PenaltyUtils.penalty(R, newPi);

            for (int i = 0; i < n; i++) {
                int order = finalSortedIdx[i];
                A.add(order);
                R.remove(order);

                List<Integer> A_sorted = new ArrayList<>(A);
                A_sorted.sort((a, b) -> Double.compare(newD[b], newD[a]));

                double[] compTime = new double[m];
                double maxWT = 0;
                for (int j : A_sorted) {
                    for (int k = 0; k < m; k++) compTime[k] += newP[k][j];
                    double finish = Arrays.stream(compTime).max().orElse(0) + newD[j];
                    maxWT = Math.max(maxWT, finish);
                }

                double penalty = PenaltyUtils.penalty(R, newPi);
                double totalCost = maxWT + penalty;
                if (totalCost < minCost) {
                    minCost = totalCost;
                    bestA = new HashSet<>(A);
                    bestR = new HashSet<>(R);
                }
            }

            // 映射回订单ID
            Set<Long> bestAOrderIds = new HashSet<>();
            Set<Long> bestROrderIds = new HashSet<>();
            for (int idx : bestA) bestAOrderIds.add(newOrderIdList[idx]);
            for (int idx : bestR) bestROrderIds.add(newOrderIdList[idx]);

            long end = System.currentTimeMillis();
            return new ResultDTO(bestAOrderIds, bestROrderIds, minCost, (end - start) / 1000.0);

        } catch (Exception e) {
            throw new RuntimeException("算法执行失败: " + e.getMessage(), e);
        }
    }
}
