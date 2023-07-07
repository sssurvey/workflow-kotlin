package workflow.tutorial.screens

import com.squareup.workflow1.ui.AndroidScreen
import com.squareup.workflow1.ui.ScreenViewFactory
import com.squareup.workflow1.ui.ScreenViewFactory.Companion.fromViewBinding
import com.squareup.workflow1.ui.WorkflowUiExperimentalApi
import com.squareup.workflow1.ui.setTextChangedListener
import com.squareup.workflow1.ui.updateText
import workflow.tutorial.views.databinding.WelcomeViewBinding

@OptIn(WorkflowUiExperimentalApi::class)
data class WelcomeScreen(
  val username: String,
  val onLoginClick: () -> Unit,
  val onUsernameTextChanged: (String) -> Unit
) : AndroidScreen<WelcomeScreen> {
  override val viewFactory: ScreenViewFactory<WelcomeScreen>
    get() = fromViewBinding(WelcomeViewBinding::inflate) { rendering, _ ->
      username.updateText(rendering.username)
      username.setTextChangedListener { rendering.onUsernameTextChanged(it.toString()) }
      login.setOnClickListener { rendering.onLoginClick() }
    }
}
