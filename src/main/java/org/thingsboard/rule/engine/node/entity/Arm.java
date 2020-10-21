package org.thingsboard.rule.engine.node.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Arm {
    //臂长
    private double armL;
    //臂长字节起始位置
    private int armStart;
    //臂长字节长度
    private int armLength;
    //臂长缩放系数
    private double armScalingFactor;
    //臂长偏移量
    private double armOffset;
    //臂长类型
    private int armType;

}
