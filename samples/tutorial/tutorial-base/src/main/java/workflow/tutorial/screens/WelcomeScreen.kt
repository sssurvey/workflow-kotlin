package workflow.tutorial.screens

data class WelcomeScreen(
  val username: String,
  val onLoginClick: () -> Unit,
  val onUsernameTextChanged: (String) -> Unit
)
