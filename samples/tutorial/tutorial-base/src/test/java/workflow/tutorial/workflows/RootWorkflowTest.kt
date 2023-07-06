package workflow.tutorial.workflows

import com.squareup.workflow1.WorkflowOutput
import com.squareup.workflow1.testing.expectWorkflow
import com.squareup.workflow1.testing.launchForTestingFromStartWith
import com.squareup.workflow1.testing.testRender
import com.squareup.workflow1.ui.WorkflowUiExperimentalApi
import junit.framework.TestCase.assertTrue
import org.junit.Test
import workflow.tutorial.screens.TodoEditScreen
import workflow.tutorial.screens.TodoListScreen
import workflow.tutorial.screens.WelcomeScreen
import workflow.tutorial.workflows.RootWorkflow.State.Todo
import workflow.tutorial.workflows.RootWorkflow.State.Welcome
import workflow.tutorial.workflows.WelcomeWorkflow.LoggedIn
import kotlin.test.assertEquals
import kotlin.test.assertNull

@OptIn(WorkflowUiExperimentalApi::class)
internal class RootWorkflowTest {

  @Test
  fun `welcome rendering`() {
    RootWorkflow
      .testRender(initialState = Welcome, props = Unit)
      .expectWorkflow(
        workflowType = WelcomeWorkflow::class,
        rendering = WelcomeScreen(
          username = "Test",
          onUsernameTextChanged = {},
          onLoginClick = {}
        )
      )
      .render {
        val backstack = it.frames
        assertTrue(backstack.size == 1)
        assertTrue(backstack.last() is WelcomeScreen)
        assertTrue((backstack.last() as WelcomeScreen).username == "Test")
      }
      .verifyActionResult { _, output ->
        assertNull(output)
      }
  }

  @Test
  fun `welcome login event check`() {
    RootWorkflow
      .testRender(
        initialState = Welcome, props = Unit
      )
      .expectWorkflow(
        workflowType = WelcomeWorkflow::class,
        rendering = WelcomeScreen(
          username = "Test",
          onUsernameTextChanged = {},
          onLoginClick = {}
        ),
        output = WorkflowOutput(LoggedIn("Test"))
      )
      .render {
        val backstack = it.frames
        assertTrue(backstack.size == 1)
        assertTrue(backstack.last() is WelcomeScreen)
        assertTrue((backstack.last() as WelcomeScreen).username == "Test")
      }
      .verifyActionResult { state, _ ->
        assertTrue(state == Todo("Test"))
      }
  }

  @Test
  fun `app flow`() {
    RootWorkflow.launchForTestingFromStartWith {

      awaitNextRendering().let { rendering ->
        assertTrue(rendering.frames.size == 1)
        val welcomeScreen = rendering.frames[0] as WelcomeScreen
        welcomeScreen.onUsernameTextChanged.invoke("test username")
      }

      awaitNextRendering().let { rendering ->
        assertEquals(1, rendering.frames.size)
        (rendering.frames[0] as WelcomeScreen).onLoginClick.invoke()
      }

      awaitNextRendering().let { rendering ->
        // stack size == 2 here since in the last step we logged in
        assertEquals(2, rendering.frames.size)
        assertTrue(rendering.frames[0] is WelcomeScreen)
        val todoScreen = rendering.frames[1] as TodoListScreen
        // custom, should be 10 due to my mock data
        assertEquals(10, todoScreen.todoTitles.size)
        todoScreen.onTodoSelected.invoke(0)
      }

      awaitNextRendering().let { rendering ->
        // [0] welcome screen, [1] todoList screen, [2] todoEdit screen
        assertEquals(3, rendering.frames.size)
        assertTrue(rendering.frames[0] is WelcomeScreen)
        assertTrue(rendering.frames[1] is TodoListScreen)
        val todoEditScreen = rendering.frames[2] as TodoEditScreen
        todoEditScreen.onTitleChanged("new title edited")
      }

      awaitNextRendering().let { rendering ->
        assertEquals(3, rendering.frames.size)
        assertTrue(rendering.frames[0] is WelcomeScreen)
        assertTrue(rendering.frames[1] is TodoListScreen)
        val todoEditScreen = rendering.frames[2] as TodoEditScreen
        todoEditScreen.saveChanges.invoke()
      }

      awaitNextRendering().let { rendering ->
        assertEquals(2, rendering.frames.size)
        assertTrue(rendering.frames[0] is WelcomeScreen)
        val todoListScreen = rendering.frames[1] as TodoListScreen
        // custom, should be 10 due to my mock data
        assertTrue(10 == todoListScreen.todoTitles.size)
        assertTrue("new title edited" == todoListScreen.todoTitles[0])
      }

    }

  }

}
