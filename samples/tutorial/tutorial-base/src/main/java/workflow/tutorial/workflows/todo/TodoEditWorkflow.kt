package workflow.tutorial.workflows.todo

import androidx.annotation.VisibleForTesting
import com.squareup.workflow1.Snapshot
import com.squareup.workflow1.StatefulWorkflow
import com.squareup.workflow1.WorkflowAction
import com.squareup.workflow1.action
import workflow.tutorial.screens.TodoEditScreen
import workflow.tutorial.workflows.todo.TodoEditWorkflow.EditProps
import workflow.tutorial.workflows.todo.TodoEditWorkflow.Output
import workflow.tutorial.workflows.todo.TodoEditWorkflow.Output.Discard
import workflow.tutorial.workflows.todo.TodoEditWorkflow.Output.Save
import workflow.tutorial.workflows.todo.TodoEditWorkflow.State
import workflow.tutorial.workflows.todo.TodoWorkflow.TodoItem

object TodoEditWorkflow : StatefulWorkflow<EditProps, State, Output, TodoEditScreen>() {

  // got it from parent, use this to make a local state copy
  data class EditProps(
    val initialTodo: TodoItem
  )

  // the local state copy
  data class State(
    val todoItem: TodoItem
  )

  // final result
  sealed class Output {
    object Discard : Output()
    data class Save(
      val todoItem: TodoItem
    ) : Output()
  }

  override fun initialState(
    props: EditProps,
    snapshot: Snapshot?
  ): State {
    return State(props.initialTodo)
  }

  override fun render(
    renderProps: EditProps,
    renderState: State,
    context: RenderContext
  ): TodoEditScreen {
    return TodoEditScreen(
      title = renderState.todoItem.title,
      note = renderState.todoItem.note,
      onTitleChanged = {
        context.actionSink.send(onTitleChanged(it))
      },
      onNoteChanged = {
        context.actionSink.send(onNoteChanged(it))
      },
      discardChanges = {
        context.actionSink.send(onDiscard())
      },
      saveChanges = {
        context.actionSink.send(onSave())
      }
    )
  }

  override fun onPropsChanged(
    old: EditProps,
    new: EditProps,
    state: State
  ): State {

    return if (old.initialTodo != new.initialTodo) {
      state.copy(todoItem = new.initialTodo)
    } else {
      state
    }
  }

  override fun snapshotState(state: State): Snapshot? {
    return null
  }

  @VisibleForTesting
  internal fun onTitleChanged(title: String): WorkflowAction<EditProps, State, Output> {
    return action {
      state = state.copy(todoItem = state.todoItem.copy(title = title))
    }
  }

  @VisibleForTesting
  internal fun onNoteChanged(note: String): WorkflowAction<EditProps, State, Output> {
    return action {
      state = state.copy(todoItem = state.todoItem.copy(note = note))
    }
  }

  @VisibleForTesting
  internal fun onSave(): WorkflowAction<EditProps, State, Output> {
    return action {
      setOutput(Save(state.todoItem))
    }
  }

  @VisibleForTesting
  internal fun onDiscard(): WorkflowAction<EditProps, State, Output> {
    return action {
      setOutput(Discard)
    }
  }
}
