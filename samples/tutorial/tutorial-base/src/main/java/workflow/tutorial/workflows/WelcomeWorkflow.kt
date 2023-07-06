package workflow.tutorial.workflows

import android.util.Log
import androidx.annotation.VisibleForTesting
import com.squareup.workflow1.Snapshot
import com.squareup.workflow1.StatefulWorkflow
import com.squareup.workflow1.WorkflowAction
import com.squareup.workflow1.action
import workflow.tutorial.screens.WelcomeScreen
import workflow.tutorial.workflows.WelcomeWorkflow.LoggedIn
import workflow.tutorial.workflows.WelcomeWorkflow.State

object WelcomeWorkflow : StatefulWorkflow<Unit, State, LoggedIn, WelcomeScreen>() {

  data class State(
    val username: String
  )

  data class LoggedIn(
    val username: String
  )

  override fun initialState(
    props: Unit,
    snapshot: Snapshot?
  ): State {
    return State(
      username = ""
    )
  }

  override fun render(
    renderProps: Unit,
    renderState: State,
    context: RenderContext
  ): WelcomeScreen {
    return WelcomeScreen(
      renderState.username,
      onLoginClick = {
        context.actionSink.send(onLoginClicked())
      },
      onUsernameTextChanged = {
        context.actionSink.send(onUsernameTextChanged(it))
      }
    )
  }

  override fun snapshotState(state: State): Snapshot? {
    return null
  }

  @VisibleForTesting
  internal fun onLoginClicked(): WorkflowAction<Unit, State, LoggedIn> {
    return action {
      if (state.username.isNotEmpty()) setOutput(LoggedIn(username = this.state.username))
    }
  }

  @VisibleForTesting
  internal fun onUsernameTextChanged(username: String): WorkflowAction<Unit, State, LoggedIn> {
    return action {
      // Log.d(TAG, "::onUsernameTextChanged called")
      state = this.state.copy(username = username)
    }
  }

  // private const val TAG = "WelcomeWorkflow"
}
