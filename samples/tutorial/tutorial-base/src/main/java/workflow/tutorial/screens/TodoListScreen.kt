package workflow.tutorial.screens

import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.workflow1.ui.AndroidScreen
import com.squareup.workflow1.ui.ScreenViewFactory
import com.squareup.workflow1.ui.ScreenViewFactory.Companion.fromViewBinding
import com.squareup.workflow1.ui.WorkflowUiExperimentalApi
import com.squareup.workflow1.ui.backPressedHandler
import workflow.tutorial.R.string
import workflow.tutorial.views.TodoListAdapter
import workflow.tutorial.views.databinding.TodoListViewBinding

@OptIn(WorkflowUiExperimentalApi::class)
data class TodoListScreen(
  val username: String,
  val todoTitles: List<String>,
  val onTodoSelected: (Int) -> Unit,
  val onBack: () -> Unit
): AndroidScreen<TodoListScreen> {

  override val viewFactory: ScreenViewFactory<TodoListScreen>
    get() = fromViewBinding(TodoListViewBinding::inflate) { render, _ ->
      val adapter = TodoListAdapter()
      todoList.adapter = adapter
      todoList.layoutManager = LinearLayoutManager(root.context)
      root.backPressedHandler = onBack
      with(todoListWelcome) {
        text = resources.getString(string.todo_list_welcome, render.username)
      }
      adapter.onTodoSelected = render.onTodoSelected
      adapter.todoList = render.todoTitles
      adapter.notifyDataSetChanged()
    }
}
