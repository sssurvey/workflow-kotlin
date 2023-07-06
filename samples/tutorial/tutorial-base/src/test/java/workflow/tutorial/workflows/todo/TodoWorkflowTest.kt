package workflow.tutorial.workflows.todo

import com.squareup.workflow1.WorkflowOutput
import com.squareup.workflow1.testing.expectWorkflow
import com.squareup.workflow1.testing.testRender
import junit.framework.TestCase.assertTrue
import org.junit.Test
import workflow.tutorial.screens.TodoEditScreen
import workflow.tutorial.screens.TodoListScreen
import workflow.tutorial.workflows.todo.TodoWorkflow.TodoItem
import kotlin.test.assertEquals

internal class TodoWorkflowTest {

  @Test
  fun `selecting todo`() {
    val todos = listOf(TodoItem("title", "note"))
    TodoWorkflow
      .testRender(
        props = TodoWorkflow.Props("test name"),
        initialState = TodoWorkflow.State(
          todoList = todos,
          step = TodoWorkflow.Step.List
        )
      )
      .expectWorkflow(
        workflowType = TodoListWorkflow::class,
        rendering = TodoListScreen(
          username = "",
          todoTitles = listOf("title"),
          onTodoSelected = {},
          onBack = {}
        ),
        output = WorkflowOutput(TodoListWorkflow.Result.Selected(0))
      )
      .render {
        assertTrue(it.size == 1)
        assertTrue(it.last() is TodoListScreen)
      }
      .verifyActionResult { state, _ ->
        assertEquals(
          TodoWorkflow.State(
            todoList = todos,
            step = TodoWorkflow.Step.Edit(0)
          ),
          state
        )
      }
  }

  @Test
  fun `saving todo`() {
    val todos = listOf(TodoItem("title", "note"))
    TodoWorkflow
      .testRender(
        props = TodoWorkflow.Props("test name"),
        initialState = TodoWorkflow.State(
          todoList = todos,
          step = TodoWorkflow.Step.Edit(0)
        )
      )
      .expectWorkflow(
        workflowType = TodoListWorkflow::class,
        rendering = TodoListScreen(
          username = "",
          todoTitles = listOf("title"),
          onTodoSelected = {},
          onBack = {}
        )
      )
      .expectWorkflow(
        workflowType = TodoEditWorkflow::class,
        rendering = TodoEditScreen(
          "",
          "",
          {},
          {},
          {},
          {}
        ),
        output = WorkflowOutput(
          TodoEditWorkflow.Output.Save(
            TodoItem("new title", "new note")
          )
        )
      )
      .render {
        assertTrue(it.size == 2)
      }
      .verifyActionResult { state, _ ->
        assertEquals(
          TodoWorkflow.State(
            todoList = listOf(TodoItem("new title", "new note")),
            step = TodoWorkflow.Step.List
          ),
          state
        )
      }
  }
}
