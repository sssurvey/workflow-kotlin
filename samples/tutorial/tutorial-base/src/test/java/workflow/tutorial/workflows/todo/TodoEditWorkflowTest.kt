package workflow.tutorial.workflows.todo

import com.squareup.workflow1.applyTo
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import workflow.tutorial.workflows.todo.TodoEditWorkflow.Output.Discard
import workflow.tutorial.workflows.todo.TodoEditWorkflow.Output.Save
import workflow.tutorial.workflows.todo.TodoWorkflow.TodoItem
import kotlin.test.assertNull

internal class TodoEditWorkflowTest {

  @Test
  fun `onPropsChanged should change state`() {
    val initialProps = TodoEditWorkflow.EditProps(initialTodo = TodoItem("Title", "Note"))
    var validationState = TodoEditWorkflow.initialState(initialProps, null)

    assertEquals("Title", validationState.todoItem.title)
    assertEquals("Note", validationState.todoItem.note)

    validationState = TodoEditWorkflow.State(TodoItem("Updated Title", "Note"))

    validationState = TodoEditWorkflow.onPropsChanged(
      initialProps, initialProps, validationState
    )

    // NO CHANGE, since newProps == oldProps == initialProps == initialProps
    assertTrue(validationState.todoItem.title == "Updated Title")
    assertTrue(validationState.todoItem.note == "Note")

    val newProps = TodoEditWorkflow.EditProps(initialTodo = TodoItem("NEW Title", "NEW Note"))
    validationState = TodoEditWorkflow.onPropsChanged(
      old = initialProps,
      new = newProps,
      state = validationState
    )
    // CHANGED!! Since oldProps != newProps
    assertTrue(validationState.todoItem.title == newProps.initialTodo.title)
    assertTrue(validationState.todoItem.note == newProps.initialTodo.note)
  }

  @Test
  fun `onTitleChanged should update todo title`() {
    val originalState = TodoEditWorkflow.State(todoItem = TodoItem("1", "2"))
    val originalProps = TodoEditWorkflow.EditProps(TodoItem("1", "2"))
    val (state, output) = TodoEditWorkflow.onTitleChanged("new").applyTo(
      state = originalState,
      props = originalProps
    )

    assertTrue(state.todoItem.title == "new")
    assertNull(output.output)
  }

  @Test
  fun `onNoteChanged should update todo content`() {
    val originalState = TodoEditWorkflow.State(TodoItem("", ""))
    val originalProps = TodoEditWorkflow.EditProps(TodoItem("", ""))
    val (state, output) = TodoEditWorkflow.onNoteChanged("2").applyTo(
      state = originalState,
      props = originalProps
    )

    assertTrue(state.todoItem.note == "2")
    assertNull(output.output)
  }

  @Test
  fun `onSave should output newly created todo item`() {
    val originalState = TodoEditWorkflow.State(TodoItem("title", "note"))
    val originalProps = TodoEditWorkflow.EditProps(TodoItem("", ""))
    val (state, output) = TodoEditWorkflow.onSave().applyTo(
      originalProps,
      originalState
    )

    assertTrue(state.todoItem.note == "note")
    assertTrue(state.todoItem.title == "title")

    assertTrue(
      output.output!!.value == Save(TodoItem("title", "note"))
    )
  }

  @Test
  fun `onDiscard should discard change by output Discard`() {
    val originalState = TodoEditWorkflow.State(TodoItem("title", "note"))
    val originalProps = TodoEditWorkflow.EditProps(TodoItem("", ""))
    val (_, output) = TodoEditWorkflow.onDiscard().applyTo(
      originalProps,
      originalState
    )

    assertTrue(output.output!!.value is Discard)
  }
}
