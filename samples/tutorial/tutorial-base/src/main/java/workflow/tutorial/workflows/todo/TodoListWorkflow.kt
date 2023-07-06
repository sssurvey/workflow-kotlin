package workflow.tutorial.workflows.todo

import android.util.Log
import com.squareup.workflow1.StatelessWorkflow
import com.squareup.workflow1.WorkflowAction
import com.squareup.workflow1.action
import workflow.tutorial.screens.TodoListScreen
import workflow.tutorial.workflows.todo.TodoListWorkflow.Props
import workflow.tutorial.workflows.todo.TodoListWorkflow.Result
import workflow.tutorial.workflows.todo.TodoListWorkflow.Result.Back
import workflow.tutorial.workflows.todo.TodoListWorkflow.Result.Selected
import workflow.tutorial.workflows.todo.TodoWorkflow.TodoItem

object TodoListWorkflow : StatelessWorkflow<Props, Result, TodoListScreen>() {

  private const val TAG = "TodoListWorkflow"

  data class Props(
    val username: String,
    val todoItems: List<TodoItem>
  )

  sealed class Result {
    object Back : Result()
    // data class TodoItemSelected(
    //   val todoItem: TodoItem
    // ) : Result()
    data class Selected(val index: Int): Result()
  }

  override fun render(
    renderProps: Props,
    context: RenderContext
  ): TodoListScreen {

    val titles = renderProps.todoItems.map { it.title }
    val todoListScreen = TodoListScreen(
      username = renderProps.username,
      todoTitles = titles,
      onTodoSelected = {
        // Log.d(TAG, "onTodoItemSelected: #$it")
        context.actionSink.send(onTodoSelected(todoItemIndex = it))
      },
      onBack = { context.actionSink.send(onBackHandler()) }
    )

    return todoListScreen
  }

  private fun onBackHandler(): WorkflowAction<Props, Nothing, Result> {
    return action {
      setOutput(Back)
    }
  }

  private fun onTodoSelected(todoItemIndex: Int): WorkflowAction<Props, Nothing, Result> {
    return action {
      // state = state.copy(step = Step.Edit(index = todoItemIndex))
      setOutput(Selected(index = todoItemIndex))
    }
  }

}
