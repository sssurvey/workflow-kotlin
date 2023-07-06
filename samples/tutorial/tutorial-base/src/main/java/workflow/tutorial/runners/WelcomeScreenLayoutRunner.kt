package workflow.tutorial.runners

import com.squareup.workflow1.ui.LayoutRunner
import com.squareup.workflow1.ui.LayoutRunner.Companion.bind
import com.squareup.workflow1.ui.ViewEnvironment
import com.squareup.workflow1.ui.ViewFactory
import com.squareup.workflow1.ui.WorkflowUiExperimentalApi
import com.squareup.workflow1.ui.setTextChangedListener
import com.squareup.workflow1.ui.updateText
import workflow.tutorial.screens.WelcomeScreen
import workflow.tutorial.views.databinding.WelcomeViewBinding

@OptIn(WorkflowUiExperimentalApi::class)
class WelcomeScreenLayoutRunner(
  private val binding: WelcomeViewBinding
): LayoutRunner<WelcomeScreen> {

  override fun showRendering(
    rendering: WelcomeScreen,
    viewEnvironment: ViewEnvironment
  ) {

    with(binding) {
      username.updateText(rendering.username)
      username.setTextChangedListener { rendering.onUsernameTextChanged(it.toString()) }
      login.setOnClickListener { rendering.onLoginClick() }
    }

  }

  companion object : ViewFactory<WelcomeScreen> by bind(
    WelcomeViewBinding::inflate, ::WelcomeScreenLayoutRunner
  )
}
