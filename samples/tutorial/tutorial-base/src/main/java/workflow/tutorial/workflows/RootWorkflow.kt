package workflow.tutorial.workflows

import com.squareup.workflow1.Snapshot
import com.squareup.workflow1.StatefulWorkflow
import com.squareup.workflow1.action
import com.squareup.workflow1.renderChild
import com.squareup.workflow1.ui.WorkflowUiExperimentalApi
import com.squareup.workflow1.ui.backstack.BackStackScreen
import com.squareup.workflow1.ui.backstack.toBackStackScreen
import workflow.tutorial.screens.WelcomeScreen
import workflow.tutorial.workflows.RootWorkflow.State
import workflow.tutorial.workflows.RootWorkflow.State.Todo
import workflow.tutorial.workflows.RootWorkflow.State.Welcome
import workflow.tutorial.workflows.todo.TodoWorkflow.Back
import workflow.tutorial.workflows.todo.TodoWorkflow

@OptIn(WorkflowUiExperimentalApi::class)
object RootWorkflow : StatefulWorkflow<Unit, State, Nothing, BackStackScreen<Any>>() {

  private const val TAG = "RootWorkflow"

  sealed class State {
    object Welcome: State()
    data class Todo(val username: String): State()
    // data class TodoEdit(val todoItem: TodoItem): State()
  }

  override fun initialState(
    props: Unit,
    snapshot: Snapshot?
  ): State {
    // default to showing the welcome screen
    return Welcome
  }

  override fun render(
    renderProps: Unit,
    renderState: State,
    context: RenderContext
  ): BackStackScreen<Any> {

    val backStackScreen = mutableListOf<Any>()

    backStackScreen.add(renderWelcomeWorkflow(context))

    when (renderState) {
      is Todo -> {
        backStackScreen.addAll(
          renderTodoWorkflow(context, renderState) // list of screens
        )
      }
      // is TodoEdit -> backStackScreen.add(renderTodoEditWorkflow(context, renderState))
      is Welcome -> {}
    }

    return backStackScreen.toBackStackScreen()
  }

  override fun snapshotState(state: State): Snapshot? {
    return null
  }

  private fun renderWelcomeWorkflow(context: RenderContext): WelcomeScreen {
    return context.renderChild(WelcomeWorkflow) {
      login(it.username)
    }
  }

  private fun  renderTodoWorkflow(context: RenderContext, renderState: Todo): List<Any> {
    return context.renderChild(
      TodoWorkflow,
      TodoWorkflow.Props(username = renderState.username) // This param, is prop for child
    ) {
      when (it) {
        is Back -> logout()
        else -> throw Exception("it can't be !!!")
        // is TodoItemSelected -> todoItemSelected(it)
      }
    }
  }

  // private fun renderTodoEditWorkflow(context: RenderContext, renderState: TodoEdit): TodoEditScreen {
  //   return context.renderChild(TodoEditWorkflow, TodoEditWorkflow.EditProps(initialTodo = renderState.todoItem)) {
  //     when (it) {
  //       is Discard -> {
  //         // return to todo list screen, but not making the edit
  //         action { TODO("back action not done") }
  //       }
  //       is Save -> {
  //         // return to todo list screen, but MAKING the edit
  //         action { TODO("save action not done") }
  //       }
  //     }
  //   }
  // }

  private fun login(username: String) = action {
    state = Todo(username)
  }

  private fun logout() = action {
    state = Welcome
  }

  // private fun todoItemSelected(todoItemSelected: TodoItemSelected): WorkflowAction<Unit, State, Nothing> {
  //   return action {
  //     state = TodoEdit(todoItemSelected.todoItem)
  //   }
  // }

}
