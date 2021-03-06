/*
 * Copyright 2017, OpenSkywalking Organization All rights reserved.
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
 *
 * Project repository: https://github.com/OpenSkywalking/skywalking
 */

package org.skywalking.apm.commons.datacarrier.partition;

import org.skywalking.apm.commons.datacarrier.buffer.BufferStrategy;

/**
 * 数据分配者接口
 *
 * Created by wusheng on 2016/10/25.
 */
public interface IDataPartitioner<T> {

    /**
     * 获得数据被分配的分区位置
     *
     * @param total 分区总数
     * @param data 数据
     * @return 分区位置
     */
    int partition(int total, T data);

    /**
     * 获得最大重试次数
     *
     * @return an integer represents how many times should retry when {@link BufferStrategy#IF_POSSIBLE}.
     *
     * Less or equal 1, means not support retry.
     */
    int maxRetryCount();

}
