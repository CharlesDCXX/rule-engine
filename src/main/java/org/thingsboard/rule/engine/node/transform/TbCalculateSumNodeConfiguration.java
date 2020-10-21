/**
 * Copyright © 2018 The Thingsboard Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.thingsboard.rule.engine.node.transform;

import lombok.Data;
import org.thingsboard.rule.engine.api.NodeConfiguration;


@Data
public class TbCalculateSumNodeConfiguration implements NodeConfiguration<TbCalculateSumNodeConfiguration> {

    private String inputKey;
    private String outputKey;
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
    //角度
    private double angle;
    //角度字节起始位置
    private int angleStart;
    //角度字节长度
    private int angleLength;
    //角度缩放系数
    private double angleScalingFactor;
    //角度偏移量
    private double angleOffset;
    //角度类型
    private int angleType;
    //幅度
    private double range;
    //幅度字节起始位置
    private int rangeStart;
    //幅度字节长度
    private int rangeLength;
    //幅度缩放系数
    private double rangeScalingFactor;
    //幅度偏移量
    private double rangeOffset;
    //幅度类型
    private int rangeType;
    //臂头高度
    private double armHeadH;
    //臂头字节起始位置
    private int armHeadHStart;
    //臂头字节长度
    private int armHeadHLength;
    //臂头缩放系数
    private double armHeadHScalingFactor;
    //臂头偏移量
    private double armHeadHOffset;
    //臂头类型
    private int armHeadHType;
    //额载
    private double load;
    //臂头字节起始位置
    private int loadStart;
    //臂头字节长度
    private int loadLength;
    //臂头缩放系数
    private double loadScalingFactor;
    //臂头偏移量
    private double loadOffset;
    //臂头类型
    private int loadType;
    @Override
    public TbCalculateSumNodeConfiguration defaultConfiguration() {
        TbCalculateSumNodeConfiguration configuration = new TbCalculateSumNodeConfiguration();
        configuration.setInputKey("Can_data");
        configuration.setOutputKey("TemperatureSum");
        configuration.setArmL(0);
        configuration.setArmStart(0);
        configuration.setArmLength(16);
        configuration.setArmScalingFactor(0.01);
        configuration.setArmOffset(0);
        configuration.setArmType(0);
        return configuration;
    }

}


