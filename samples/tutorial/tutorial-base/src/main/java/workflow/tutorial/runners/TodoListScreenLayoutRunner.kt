package workflow.tutorial.runners

import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.workflow1.ui.LayoutRunner
import com.squareup.workflow1.ui.LayoutRunner.Companion.bind
import com.squareup.workflow1.ui.ViewEnvironment
import com.squareup.workflow1.ui.ViewFactory
import com.squareup.workflow1.ui.WorkflowUiExperimentalApi
import com.squareup.workflow1.ui.backPressedHandler
import workflow.tutorial.R
import workflow.tutorial.screens.TodoListScreen
import workflow.tutorial.views.TodoListAdapter
import workflow.tutorial.views.databinding.TodoListViewBinding

@OptIn(WorkflowUiExperimentalApi::class)
class TodoListScreenLayoutRunner(
  private val binding: TodoListViewBinding
): LayoutRunner<TodoListScreen> {

  private val adapter = TodoListAdapter()

  init {
    binding.todoList.layoutManager = LinearLayoutManager(binding.root.context)
    binding.todoList.adapter = adapter
  }

  override fun showRendering(
    rendering: TodoListScreen,
    viewEnvironment: ViewEnvironment
  ) {
    binding.root.backPressedHandler = rendering.onBack
    with(binding.todoListWelcome) {
      text = resources.getString(R.string.todo_list_welcome, rendering.username)
    }
    adapter.onTodoSelected = rendering.onTodoSelected
    adapter.todoList = rendering.todoTitles
    adapter.notifyDataSetChanged()
  }

  companion object: ViewFactory<TodoListScreen> by bind(
    TodoListViewBinding::inflate, ::TodoListScreenLayoutRunner
  )
}
