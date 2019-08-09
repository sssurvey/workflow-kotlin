/*
 * Copyright 2019 Square Inc.
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
package com.squareup.sample.dungeon

import android.os.Vibrator
import com.squareup.sample.dungeon.GameWorkflow.Output.PlayerWasEaten
import com.squareup.sample.dungeon.GameWorkflow.Output.Vibrate
import com.squareup.workflow.RenderContext
import com.squareup.workflow.Snapshot
import com.squareup.workflow.StatefulWorkflow
import com.squareup.workflow.WorkflowAction.Companion.noAction
import com.squareup.workflow.renderChild

class DungeonAppWorkflow(
  private val gameWorkflow: GameWorkflow,
  private val vibrator: Vibrator
) : StatefulWorkflow<Unit, Unit, Nothing, Any>() {

  override fun initialState(
    input: Unit,
    snapshot: Snapshot?
  ) {
  }

  override fun render(
    input: Unit,
    state: Unit,
    context: RenderContext<Unit, Nothing>
  ): Any {
    return context.renderChild(gameWorkflow) { output ->
      when (output) {
        Vibrate -> vibrate(50)
        PlayerWasEaten -> {
          vibrate(20)
          vibrate(20)
          vibrate(20)
          vibrate(20)
          vibrate(1000)
        }
      }
      noAction()
    }
  }

  override fun snapshotState(state: Unit): Snapshot = Snapshot.EMPTY

  private fun vibrate(durationMs: Long) {
    @Suppress("DEPRECATION")
    vibrator.vibrate(durationMs)
  }
}
