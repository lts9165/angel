/*
 * Tencent is pleased to support the open source community by making Angel available.
 *
 * Copyright (C) 2017 THL A29 Limited, a Tencent company. All rights reserved.
 *
 * Licensed under the BSD 3-Clause License (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * https://opensource.org/licenses/BSD-3-Clause
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 *
 */

package com.tencent.angel.ml.matrixfactorization.threads

import java.util.concurrent.{Callable, LinkedBlockingQueue}

import com.tencent.angel.ml.math.vector.DenseFloatVector

class SgdTask(var L: Array[DenseFloatVector], var R: Array[DenseFloatVector], var eta: Double,
              var lambda: Double) extends Callable[Boolean] {
  private var taskQueue: LinkedBlockingQueue[ItemVec] = null

  def setTaskQueue(queue: LinkedBlockingQueue[ItemVec]) {
    taskQueue = queue
  }

  @throws[Exception]
  def call: Boolean = {
    var itemVec: ItemVec = null
    while ( {
      itemVec = taskQueue.poll; itemVec != null
    }) {
      {
        val itemId: Int = itemVec.getItemId
        val Rj = R(itemId)
        Utils.sgdOneItemVec(itemVec, Rj, L, eta, lambda)
      }
    }
    return true
  }
}
