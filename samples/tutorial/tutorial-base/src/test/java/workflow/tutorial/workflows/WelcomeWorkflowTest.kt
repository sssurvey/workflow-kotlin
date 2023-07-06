package workflow.tutorial.workflows

import com.squareup.workflow1.applyTo
import com.squareup.workflow1.testing.testRender
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import org.junit.Test
import workflow.tutorial.workflows.WelcomeWorkflow.LoggedIn

internal class WelcomeWorkflowTest {

  @Test
  fun `onUsernameTextChanged should change state username`() {
    val startState = WelcomeWorkflow.State("")
    val action = WelcomeWorkflow.onUsernameTextChanged("myName")
    val (state, output) = action.applyTo(state = startState, props = Unit)

    assertNull(output.output)
    assertEquals("myName", state.username)
  }

  @Test
  fun `onLoginClicked should output loggedIn`() {
    val startState = WelcomeWorkflow.State("myName")
    val action = WelcomeWorkflow.onLoginClicked()
    val (_, output) = action.applyTo(state = startState, props = Unit)

    assertNotNull(output.output)
    assertEquals(output.output!!.value, LoggedIn("myName"))
  }

  @Test
  fun `onLoginClicked should not do anything if username is empty`() {
    val startState = WelcomeWorkflow.State("")
    val action = WelcomeWorkflow.onLoginClicked()
    val (state, output) = action.applyTo(state = startState, props = Unit)

    assertEquals("", state.username)
    assertNull(output.output)
  }

  @Test
  fun `rendering initial`() {
     WelcomeWorkflow
       .testRender(props = Unit)
       .render {
         assertEquals("", it.username)
         it.onLoginClick.invoke()
       }
       .verifyActionResult { _, output ->
         assertNull(output)
       }
  }

  @Test
  fun `rendering name change`() {
    WelcomeWorkflow
      .testRender(props = Unit)
      .render {
        it.onUsernameTextChanged.invoke("test")
      }
      .verifyActionResult { state, output ->
        assertEquals("test", (state as WelcomeWorkflow.State).username)
        assertNull(output)
      }
  }

  @Test
  fun `rendering login none empty username`() {
    WelcomeWorkflow
      .testRender(props = Unit, initialState = WelcomeWorkflow.State("test"))
      .render {
        it.onLoginClick.invoke()
      }
      .verifyActionResult { _, output ->
        assertEquals(LoggedIn("test"), output?.value)
      }
  }

  @Test
  fun `rendering login empty username`() {
    WelcomeWorkflow
      .testRender(props = Unit, initialState = WelcomeWorkflow.State(""))
      .render {
        it.onLoginClick.invoke()
      }
      .verifyActionResult { _, output ->
        assertNull(output)
      }
  }

}
