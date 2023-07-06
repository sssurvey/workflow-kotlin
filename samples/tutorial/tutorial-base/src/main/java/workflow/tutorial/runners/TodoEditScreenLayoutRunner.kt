package workflow.tutorial.runners

import com.squareup.workflow1.ui.LayoutRunner
import com.squareup.workflow1.ui.LayoutRunner.Companion.bind
import com.squareup.workflow1.ui.ViewEnvironment
import com.squareup.workflow1.ui.ViewFactory
import com.squareup.workflow1.ui.WorkflowUiExperimentalApi
import com.squareup.workflow1.ui.backPressedHandler
import com.squareup.workflow1.ui.setTextChangedListener
import com.squareup.workflow1.ui.updateText
import workflow.tutorial.screens.TodoEditScreen
import workflow.tutorial.views.databinding.TodoEditViewBinding

@OptIn(WorkflowUiExperimentalApi::class)
class TodoEditScreenLayoutRunner(
  private val binding: TodoEditViewBinding
): LayoutRunner<TodoEditScreen> {

  override fun showRendering(
    rendering: TodoEditScreen,
    viewEnvironment: ViewEnvironment
  ) {
    with(binding) {
      root.backPressedHandler = rendering.discardChanges
      save.setOnClickListener { rendering.saveChanges() }
      todoTitle.updateText(rendering.title)
      todoTitle.setTextChangedListener { rendering.onTitleChanged(it.toString()) }
      todoNote.updateText(rendering.note)
      todoNote.setTextChangedListener { rendering.onNoteChanged(it.toString()) }
    }
  }

  companion object: ViewFactory<TodoEditScreen> by bind(
    TodoEditViewBinding::inflate, ::TodoEditScreenLayoutRunner
  )
}
