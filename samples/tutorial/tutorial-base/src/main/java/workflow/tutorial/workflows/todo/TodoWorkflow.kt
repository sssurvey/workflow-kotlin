package workflow.tutorial.workflows.todo

import com.squareup.workflow1.Snapshot
import com.squareup.workflow1.StatefulWorkflow
import com.squareup.workflow1.action
import workflow.tutorial.workflows.todo.TodoWorkflow.Back
import workflow.tutorial.workflows.todo.TodoWorkflow.Props
import workflow.tutorial.workflows.todo.TodoWorkflow.State

object TodoWorkflow : StatefulWorkflow<Props, State, Back, List<Any>>() {

  data class TodoItem(
    val title: String,
    val note: String
  )
  data class Props(val username: String)
  data class State(
    val todoList: List<TodoItem>,
    val step: Step
  )

  sealed class Step {
    object List: Step()
    data class Edit(val index: Int): Step()
  }

  object Back

  override fun initialState(
    props: Props,
    snapshot: Snapshot?
  ): State {
    return State(
      todoList = testData(),
      step = Step.List
    )
  }

  override fun render(
    renderProps: Props,
    renderState: State,
    context: RenderContext
  ): List<Any> {

    val backStack = mutableListOf<Any>()
    val todoListScreen = context.renderChild(
      TodoListWorkflow,
      TodoListWorkflow.Props(username = renderProps.username, todoItems = renderState.todoList)
    ) {
      when (it) {
        is TodoListWorkflow.Result.Back -> {
          action {
            setOutput(Back)
          }
        }
        is TodoListWorkflow.Result.Selected -> {
          action {
            state = state.copy(
              step = Step.Edit(index = it.index)
            )
          }
        }
      }
    }

    when (renderState.step) {
      is Step.List -> {
        // ADD LIST
        backStack.add(todoListScreen)
      }
      is Step.Edit -> {
        val todoEditScreen = context.renderChild(
          TodoEditWorkflow,
          TodoEditWorkflow.EditProps(initialTodo = renderState.todoList[renderState.step.index])
        ) {
          when (it) {
            is TodoEditWorkflow.Output.Discard -> {
              action { state = renderState.copy(
                step = Step.List
              ) }
            }
            is TodoEditWorkflow.Output.Save -> {
              action {
                val mutableList = renderState.todoList.toMutableList()
                mutableList[renderState.step.index] = it.todoItem
                state = renderState.copy(
                  step = Step.List,
                  todoList = mutableList
                )
              }
            }
          }
        }

        backStack.add(todoListScreen)
        backStack.add(todoEditScreen)
      }
    }

    return backStack
  }

  override fun snapshotState(state: State): Snapshot? {
    return null
  }

  private fun testData(): List<TodoItem> {
    val list = mutableListOf<TodoItem>()
    for (i in 0 until 10) {
      list.add(TodoItem("Title #$i", "Just for test $i."))
    }
    return list
  }
}
