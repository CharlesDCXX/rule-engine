package org.thingsboard.rule.engine.node.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Device {
    //臂长
    private BigDecimal value;
    //臂长字节起始位置
    private int start;
    //臂长字节长度
    private int length;
    //臂长缩放系数
    private BigDecimal scalingFactor;
    //臂长偏移量
    private BigDecimal offset;
    //臂长类型
    private int type;

}
