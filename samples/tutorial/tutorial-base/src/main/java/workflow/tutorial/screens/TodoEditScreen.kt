package workflow.tutorial.screens

import com.squareup.workflow1.ui.AndroidScreen
import com.squareup.workflow1.ui.ScreenViewFactory
import com.squareup.workflow1.ui.ScreenViewFactory.Companion.fromViewBinding
import com.squareup.workflow1.ui.WorkflowUiExperimentalApi
import com.squareup.workflow1.ui.backPressedHandler
import com.squareup.workflow1.ui.setTextChangedListener
import com.squareup.workflow1.ui.updateText
import workflow.tutorial.views.databinding.TodoEditViewBinding

@OptIn(WorkflowUiExperimentalApi::class)
data class TodoEditScreen(
  val title: String,
  val note: String,
  val onTitleChanged: (String) -> Unit,
  val onNoteChanged: (String) -> Unit,
  val discardChanges: () -> Unit,
  val saveChanges: () -> Unit
): AndroidScreen<TodoEditScreen> {
  override val viewFactory: ScreenViewFactory<TodoEditScreen>
    get() = fromViewBinding(TodoEditViewBinding::inflate) { render, _ ->
      root.backPressedHandler = render.discardChanges
      save.setOnClickListener { render.saveChanges() }
      todoTitle.updateText(render.title)
      todoTitle.setTextChangedListener { render.onTitleChanged(it.toString()) }
      todoNote.updateText(render.note)
      todoNote.setTextChangedListener { render.onNoteChanged(it.toString()) }
    }
}
